package MSG;

public class SelectMsg extends MSGHead{
    String CardID;
    public SelectMsg(int len, byte type, int dest, int src,String id) {
        super(len, type, dest, src);
        this.CardID=id;
    }

    public String getCardID() {
        return CardID;
    }
}
