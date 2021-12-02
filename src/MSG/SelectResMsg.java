package MSG;

public class SelectResMsg extends MSGHead{
    private double balance;
    public SelectResMsg(int len, byte type, int dest, int src,double balance) {
        super(len, type, dest, src);
        this.balance=balance;
    }

    public double getBalance() {
        return balance;
    }
}
