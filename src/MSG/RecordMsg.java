package MSG;

public class RecordMsg extends MSGHead{

    String CardID;
    char operation ;
    double num;
    public RecordMsg(int len, byte type, int dest, int src,String CardID,char oper,double num) {
        super(len, type, dest, src);
        this.CardID=CardID;
        this.operation=oper;
        this.num=num;
    }
    public char getOperation() {
        return operation;
    }

    public double getNum() {
        return num;
    }

    public String getCardID() {
        return CardID;
    }
}
