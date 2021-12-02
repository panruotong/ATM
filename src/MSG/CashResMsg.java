package MSG;

public class CashResMsg extends MSGHead{
    private byte state;
    public CashResMsg(int len, byte type, int dest, int src,byte state) {
        super(len, type, dest, src);
        this.state=state;
    }

    public byte getState() {
        return state;
    }
}
