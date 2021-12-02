package MSG;

import java.io.*;

public class MSGDao {

    /**
     *     构造方法私有，不允许构建该类的对象
     */
    private MSGDao(){}

    /**
     * 封装读消息头的方法
     * @param din 输入流
     * @return 返回读取到的消息对象
     * @throws IOException
     */
    public static MSGHead readMsgHead(DataInputStream din) throws IOException{
        int len=din.readInt();//读总长
        byte []data= new byte[len-4];//记住务必要-4！已经读取了一个整数了！
        din.readFully(data);
        MSGHead msg= parseMsg(data);//消息解包
        //ObjectInputStream objectInputStream = new ObjectInputStream(din);
        return msg;
    }

    /**
     *   消息解包，返回MSGHead对象
     * @param data 待解包的字节数据
     * @return 返回解包后的消息对象
     * @throws IOException
     */
    public static MSGHead parseMsg(byte[]data) throws IOException{
        ByteArrayInputStream bin= new ByteArrayInputStream(data);//字节数组输入流
        DataInputStream din = new DataInputStream(bin);//包装输入流
        int len= 4+data.length;
        byte type=din.readByte();
        int dest= din.readInt();
        int src= din.readInt();
        if(type==0x01){//登录请求消息
            byte[]data1= new byte[8];
            din.readFully(data1);
            String CardID= new String(data1).trim();
            byte[]data2= new byte[6];
            din.readFully(data2);//密码
            String pwd= new String(data2).trim();
            int atmNum = din.readInt();
            System.out.println("MSGDao正在解包登录请求消息消息包,账号："+CardID+",密码:"+pwd);
            return new LoginMsg(len,type,dest,src,CardID,pwd,atmNum);
        }
        else if(type==0x02){//登录应答消息
            byte state= din.readByte();
            System.out.println("MSGDao正在解包登录应答消息包,state:"+state);
            return new LoginResMsg(len,type,dest,src,state);
        }
        else if(type==0x11){//查询余额消息
            byte[]data1= new byte[8];
            din.readFully(data1);
            String CardID= new String(data1).trim();
            return new SelectMsg(len,type,dest,src,CardID);
        }
        else if(type==0x12){//余额应答消息
            double balance = din.readDouble();
            System.out.println("MSGDao正在解包余额应答消息包");
            System.out.println("balance:"+balance);
            return new SelectResMsg(len,type,dest,src,balance);
        }
        else if(type==0x06){//存取款记录消息
            byte[]data1= new byte[8];
            din.readFully(data1);
            String CardID= new String(data1).trim();
            System.out.println(CardID+"   ");
            char oper = din.readChar();
            System.out.println(oper);
            double num = din.readDouble();
            System.out.println("MSGDao正在解包记录消息包");
            return new RecordMsg(len,type,dest,src,CardID,oper,num);
        }
        else if(type==0x07){//修改密码
            byte[]data1= new byte[8];
            din.readFully(data1);
            String CardID= new String(data1).trim();
            byte[]data2= new byte[6];
            din.readFully(data2);//密码
            String pwd= new String(data2).trim();
            return new ChangepwMsg(len,type,dest,src,CardID,pwd);
        }else if(type==0x17){
            byte state= din.readByte();
            System.out.println("MSGDao正在解包取款应答消息包,state:"+state);
            return new CashResMsg(len,type,dest,src,state);
        }
        else if(type==0x20) {//断开连接消息
            int l = len - 13;
            byte[] notice = new byte[l];
            din.readFully(notice);
            String snotice = new String(notice);
            System.out.println("MSGTools正在解包断开连接消息包，notice:" + snotice);
            return new OffServiceMsg(len, type, dest, src, snotice);
        }
        else return null;//(待完善)
    }

    /**
     * 消息打包，返回字节数组
     * @param msg 待打包的消息对象
     * @return 返回打包好的字节数组
     * @throws IOException
     */
    public static byte[] packMsg(MSGHead msg) throws IOException{
        ByteArrayOutputStream bou= new ByteArrayOutputStream();//字节数组输出流
        DataOutputStream dou= new DataOutputStream(bou);//包装输出流
        writeHead(msg,dou);//写入消息头
        byte type= msg.getType();
        if(type==0x01){
            //登录请求消息
            LoginMsg lm=(LoginMsg)msg;
            writeString(lm.getAccNum(),8,dou);//账号
            writeString(lm.getPwd(),6,dou);//密码
            dou.writeInt(lm.getAtmNum());
            System.out.println("MSGDao正在包装登录请求消息消息包,JKnum:"+lm.getAccNum()+",密码"+lm.getPwd());
            return bou.toByteArray();
        }
        else if(type==0x02){
            //登录应答消息
            LoginResMsg lrm=(LoginResMsg)msg;
            dou.writeByte(lrm.getState());
            System.out.println("MSGDao正在包装登录应答消息包,state:"+lrm.getState());
            return bou.toByteArray();
        }
        else if(type==0x11){//查询余额消息
            SelectMsg sel = (SelectMsg) msg;
            writeString(sel.getCardID(),8,dou);//账号
            System.out.println("MSGDao正在包装查询余额消息包");
            return bou.toByteArray();
        }
        else if(type==0x12){//余额应答消息
            SelectResMsg selRes = (SelectResMsg) msg;
            dou.writeDouble(selRes.getBalance());
            System.out.println("MSGDao正在包装余额应答消息包"+selRes.getBalance());
            return bou.toByteArray();
        }
        else if(type==0x06){//存取款记录消息
            RecordMsg rec = (RecordMsg)msg;
            writeString(rec.getCardID(),8,dou);
            dou.writeChar(rec.getOperation());
            dou.writeDouble(rec.getNum());
            System.out.println("MSGDao正在包装记录消息,msg:"+rec.getCardID()+" "+rec.getNum());
            return bou.toByteArray();
        }
        else if(type==0x07){//修改密码
            ChangepwMsg changepw = (ChangepwMsg) msg;
            writeString(changepw.getCardID(),8,dou);//账号
            writeString(changepw.getnPW(),6,dou);//密码
            return bou.toByteArray();

        }else if(type==0x20){//连接断开消息
            OffServiceMsg osm=(OffServiceMsg)msg;
            byte[]data=osm.getNotice().getBytes();
            dou.write(data);
            dou.flush();
            System.out.println("MSGTools正在断开连接消息包,notice:"+osm.getNotice());
            return bou.toByteArray();
        }else if (type==0x17){
            CashResMsg cashRes = (CashResMsg)msg;
            byte state = cashRes.getState();
            dou.writeByte(state);
            dou.flush();
            return bou.toByteArray();
        }
        else return null;//(待完善)
    }

    /**
     * 发送消息对象
     * @param msg
     * @param dou
     * @throws IOException
     */
    private static void writeHead(MSGHead msg,DataOutputStream dou) throws IOException{
        dou.writeInt(msg.getLen());//总长
        dou.writeByte(msg.getType());//类型
        dou.writeInt(msg.getDest());//目标
        dou.writeInt(msg.getSender());//来源
    }

    /**
     * 发送定长的字符串
     * @param msg 待发送的字符串
     * @param len 字符串长度
     * @param dou 输出流
     * @throws IOException
     */
    public static void writeString(String msg, int len, DataOutputStream dou) throws IOException{
        byte []data= msg.getBytes();
        dou.write(data);
        while(len>data.length){
            dou.write('\0');//这里千万不要用writeChar()方法，每调用一次会多输出一个空字符
            len--;
        }
    }
}
