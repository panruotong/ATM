package Client;

import MSG.*;
import entity.Account;
import ui.Login;
import ui.SelectBalance;
import ui.UserUI;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ATMClient extends Thread {
    private Login login;//登录视图对象
    private UserUI userView;//客户端聊天视图对象
    private SelectBalance selectView;
    private String serverIp;//连接的服务器的ip和端口
    private int serverPort;
    private Socket client;//与服务器连接的对象
    private InputStream ins;//聊天的输入输出流
    private OutputStream ous;
    private DataInputStream din;//包装后的输入输出流
    private DataOutputStream dou;
    String CardID;
    String pw;
    int ATMnum;
    int login_count = 0;

    /**
     * 通过给定的ip和端口建立客户对象
     * @param ip 服务器ip地址
     * @param port 服务器端口号
     */
    public ATMClient(String ip, int port,int atm){
        System.out.println("创建一次");
        login = new Login(this);//初始化视图层对象
        this.serverIp=ip;
        this.serverPort=port;
        this.ATMnum=atm;
        conn2server();
    }

    /**
     *  重写Run函数,读取服务器消息
     */
    public void run(){
        try {
            readFromServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *  连接服务器
     */
    public boolean conn2server(){
        try {
            client= new Socket(this.serverIp,this.serverPort);
            ins=client.getInputStream();
            ous=client.getOutputStream();
            din=new DataInputStream(ins);
            dou= new DataOutputStream(ous);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 登陆服务器
     * @param CardID 账号
     * @param pwd 密码
     * @throws IOException
     */
    public void loginServer(String CardID, String pwd){
        //1.设置用户对象的部分消息
        login_count++;
        this.CardID=CardID;
        this.pw=pwd;
        try {
            //2，发送登录请求消息给服务器端
            int len= 13+14+4;
            byte type=0x01;
            LoginMsg lg = new LoginMsg(len,type,0,0,CardID,pwd,ATMnum);
            byte []data= MSGDao.packMsg(lg);
            dou.write(data);
            dou.flush();
            //3.等待服务器端的登录请求响应消息
            LoginResMsg lrm=(LoginResMsg)MSGDao.readMsgHead(din);
            byte state=lrm.getState();
            if(state==0){
                this.start();//开启线程
                login.dispose();//登录窗口消失
                System.out.println("loginServer: "+this.CardID);
                userView = new UserUI(this);//显示聊天界面
            }
            else{
                login.FailLogin();
                if (login_count == 3){
                    login.FailThird();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 读取服务器消息
     * @throws IOException
     */
    public void readFromServer() throws IOException {
        //不停从服务器读取消息
        while (true) {
            MSGHead msg = MSGDao.readMsgHead(din);//读取消息头
            byte type = msg.getType();//消息类型
            switch (type) {
                case 0x12: {//余额应答消息
                    userView.dispose();
                    SelectResMsg selRes = (SelectResMsg) msg;
                    selectView = new SelectBalance(this,selRes.getBalance());
                    break;
                }
                case 0x20: {//断开连接消息
                    OffServiceMsg osm = (OffServiceMsg) msg;
                    System.out.println(osm.getNotice());
                    if (osm.getSender() == 1) {//服务端主动断开,回馈给服务器
                        byte[] data = MSGDao.packMsg(osm);
                        dou.write(data);
                        dou.flush();
                    }
                    //关闭Socket
                    client.close();
                    //结束消息读取
                    return;
                }
                case 0x17:{
                    CashResMsg cashRes = (CashResMsg) msg;
                    if(cashRes.getState()==1){
                        JOptionPane.showMessageDialog(null,"余额不足，请重新输入！","消息提示框",JOptionPane.ERROR_MESSAGE);
                    }else {
                        JOptionPane.showInternalMessageDialog(null, "取款成功","消息提示框", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }
    }
    public void OperCash(double num) throws IOException {
        System.out.println("OperCash:"+CardID);
        byte type=0x06;
        char oper = 'd';
        //账号8+操作2+数值double 8
        RecordMsg rec = new RecordMsg(13+18,type,0,0,CardID,oper,num);
        byte[] text = MSGDao.packMsg(rec);
        System.out.println(text.length);
        dou.write(text);
        dou.flush();
    }
    public void OperDeposit(double num) throws IOException {
        byte type=0x06;
        char oper = 'a';

        RecordMsg rec = new RecordMsg(13+18,type,0,0,CardID,oper,num);
        byte[] text = MSGDao.packMsg(rec);
        dou.write(text);
        dou.flush();
    }
    public void getBalance() throws IOException {
        byte type=0x11;
        SelectMsg sel = new SelectMsg(14+8,type,0,0,CardID);
        System.out.println("CardID:"+CardID);
        byte[] text = MSGDao.packMsg(sel);
        dou.write(text);
        dou.flush();
    }
    public String changePW(String o_pw,String n_pw){
        if(o_pw.equals(this.pw)){
            byte type = 0x07;
            ChangepwMsg cpw = new ChangepwMsg(14+14,type,0,0,CardID,n_pw);
            byte[] text;
            try {
                text = MSGDao.packMsg(cpw);
                dou.write(text);
                dou.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            closeMe("修改密码，结束对话");
            return "已成功修改！";
        }
        return "原密码不正确！";
    }
    public void backUserViewer(){
        userView.show();
    }
    /**
     * 发送意外或者手动退出消息
     */
    public void closeMe(String notice) {
        byte[]data=notice.getBytes();
        byte type=0x20;
        int len=13+data.length;
        OffServiceMsg osm= new OffServiceMsg(len,type,0,0,notice);
        try {
            byte []msg=MSGDao.packMsg(osm);//打包
            dou.write(msg);
            dou.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getATMnum() {
        return ATMnum;
    }
}
