package server;

import MSG.*;
import entity.Account;
import service.atmService;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ServerThread extends Thread {
    private Socket client;//用户连接套接字
    private Account user;//该线程处理的用户对象
    private InputStream ins;//聊天的输入输出流
    private OutputStream ous;
    private DataInputStream din;//包装后的输入输出流
    private DataOutputStream dou;
    private atmService userService = new atmService();

    /**
     * 构造函数(获得连接对象和输入输出流)
     * @param client
     * @throws IOException
     */
    public ServerThread(Socket client) throws IOException {
        this.client = client;
        ins = client.getInputStream();
        ous = client.getOutputStream();
        din = new DataInputStream(ins);
        dou = new DataOutputStream(ous);
    }

    /**
     * 重写run方法
     */
    public void run() {
        //处理该用户
        try {
            processClient();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理该用户首次连接（注册或者登录请求）
     * @throws IOException
     */
    public void processClient() throws IOException, SQLException {
        while(true) {
            //读消息头
            MSGHead msg = MSGDao.readMsgHead(din);
            int msgType = msg.getType();
            switch (msgType) {
                case 0x01: {//登陆消息
                    //登录，得到登录的用户名，密码，核对是否正确并响应是否成功
                    LoginMsg lm = (LoginMsg) msg;
                    String CardID = lm.getAccNum();//账号
                    String pwd = lm.getPwd();//密码
                    int atmnum = lm.getAtmNum();
                    byte type = 0x02, state;
                    boolean res = userService.login(CardID, pwd,atmnum);
                    if (!res) {
                        //登陆失败，结束函数;
                        state = 1;
                        //发送登陆失败应答消息
                        LoginResMsg lrm = new LoginResMsg(13 + 1, type, 0, 0, state);
                        byte[] data = MSGDao.packMsg(lrm);
                        dou.write(data);
                        dou.flush();
                        break;
                    } else {
                        //开始通信
                        user = new Account(CardID, pwd);
                        user.setAddress(client.getRemoteSocketAddress().toString());
                        //在线用户列表中加入该用户的线程对象
                        //UserDao.addOnlineUser(this);
                        //登陆成功，发送登录成功消息，与用户通信
                        state = 0;
                        LoginResMsg lrm = new LoginResMsg(13 + 1, type, 0, 0, state);
                        byte[] data = MSGDao.packMsg(lrm);
                        dou.write(data);
                        dou.flush();
                        beginChat();
                        break;
                    }
                }
                case 0x20:{//客户端主动连接关闭消息
                    System.out.println("客户" + client.getRemoteSocketAddress() + "连接断开");
                    //回馈给客户端
                    byte[] data = MSGDao.packMsg(msg);
                    dou.write(data);
                    dou.flush();
                    //关闭socket
                    client.close();
                    System.out.println("关闭线程,关闭Socket");
                    //停止处理该客户对象
                    return;
                }
            }
        }
    }

    /**
     *     与用户正式通信
     */
    public void beginChat() throws IOException, SQLException {
        while(true){
            MSGHead msg=MSGDao.readMsgHead(din);
            int type = msg.getType();
            switch (type) {
                case 0x11: {//查询余额消息
                    System.out.println("查询余额消息");
                    SelectMsg sel = (SelectMsg) msg;
                    double balance = userService.select(sel.getCardID());
                    System.out.println("Server:"+balance);
                    byte restype = 0x12;
                    SelectResMsg selectRes = new SelectResMsg(13+8,restype,0,0,balance);
                    byte[] data = MSGDao.packMsg(selectRes);
                    dou.write(data);
                    dou.flush();
                    break;
                }
                case 0x06: {//存取款记录消息
                    RecordMsg rec = (RecordMsg) msg;
                    if(rec.getOperation()=='a'){
                        String s = userService.deposit(rec.getCardID(), rec.getNum());
                        System.out.println(s);
                    }else{
                        String s = userService.Withdrawal(rec.getCardID(), rec.getNum());
                        if (s.equals("余额不足,请重新输入")){
                            byte state = 1;
                            byte restype = 0x17;
                            CashResMsg cashRes = new CashResMsg(13+1,restype,0,0,state);
                            byte[] data = MSGDao.packMsg(cashRes);
                            dou.write(data);
                            dou.flush();
                        }
                    }
                    break;
                }
                case 0x07:{//修改密码
                    ChangepwMsg change = (ChangepwMsg)msg;
                    String s = userService.changePassword(change.getCardID(),change.getnPW());
                    System.out.println(s);
                }
                case 0x20: {//关闭消息
                    OffServiceMsg osm = (OffServiceMsg) msg;
                    System.out.println("收到来自用户" + osm.getSender() + "的关闭连接消息" + osm.getNotice());
                    if (osm.getSender() == 0) {//客户端主动断开连接，回馈断开消息给客户端
                        byte[] data = MSGDao.packMsg(osm);
                        dou.write(data);
                        dou.flush();
                    }
                    //关闭socket
                    client.close();
                    userService.close();
                    System.out.println("关闭线程,关闭Socket");
                    //通知UserDao下线，移除在线用户
                    //UserDao.removeOfflineUser(this);
                    //停止读取消息
                    return;
                }
                default: {
                    //未知数据包
                    System.out.println("接收到未知数据包！");
                }
            }
        }
    }

    /**
     * 发送打包后的消息（字节数组）给对应的用户对象
     * @param data 字节数组
     */
    public void sendData(byte[] data){
        try {
            dou.write(data);
            dou.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *    服务器端主动关闭该用户
     */
    public void closeMe(String notice, int problem) throws IOException{
        byte type=0x20;
        int len= 13+notice.getBytes().length;
        OffServiceMsg osm= new OffServiceMsg(len,type,problem,1,notice);
        byte []data=MSGDao.packMsg(osm);
        dou.write(data);
        dou.flush();
    }

    public void setUser(Account user){
        this.user = user;
    }

    public Account getUser(){
        return user;
    }

    public Socket getClient() {
        return client;
    }

}