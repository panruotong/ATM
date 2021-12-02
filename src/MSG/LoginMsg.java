package MSG;

public class LoginMsg extends MSGHead{

    private String CardID;//登录账号
    private String pwd;
    private int atmNum;

    public LoginMsg(int len, byte type, int dest, int src,String CardID, String pwd,int num) {
        super(len, type, dest, src);
        this.CardID=CardID;
        this.pwd=pwd;
        this.atmNum=num;
    }
    public String getAccNum(){
        return CardID;
    }
    public String getPwd(){
        return pwd;
    }
    public int getAtmNum(){return atmNum;}
}
