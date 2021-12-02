package MSG;

public class ChangepwMsg extends MSGHead{
    private String CardID;
    private String nPW;
    public ChangepwMsg(int len, byte type, int dest, int src,String CardID,String nPW) {
        super(len, type, dest, src);
        this.CardID=CardID;
        this.nPW=nPW;
    }

    public String getCardID() {
        return CardID;
    }

    public String getnPW() {
        return nPW;
    }
}
