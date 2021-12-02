package entity;

import java.util.Date;

public class Record {
    private String Cardholder;
    private char oper;
    private double Tradmoney;
    private int ATMnum;
    private Date time;
    private String oper_view;

    public String getCardholder() {
        return Cardholder;
    }

    public char getOper() {
        return oper;
    }

    public double getTradmoney() {
        return Tradmoney;
    }

    public int getATMnum() {
        return ATMnum;
    }

    public Date getTime() {
        return time;
    }

    public Record(String cardholder, char oper, double tradmoney, int ATMnum, Date time) {
        Cardholder = cardholder;
        this.oper = oper;
        Tradmoney = tradmoney;
        this.ATMnum = ATMnum;
        this.time = time;
    }
    public Record(){
    }
    public void setCardholder(String cardholder) {
        Cardholder = cardholder;
    }

    public void setOper(char oper) {
        this.oper = oper;
    }

    public void setTradmoney(double tradmoney) {
        Tradmoney = tradmoney;
    }

    public void setATMnum(int ATMnum) {
        this.ATMnum = ATMnum;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getOper_view() {
        return oper_view;
    }

    public void setOper_view(String oper_view) {
        this.oper_view = oper_view;
    }
}
