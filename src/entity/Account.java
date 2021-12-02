package entity;

public class Account {
    private String username;
    private String password;
    private String CardID;
    private double balance;
    private String address;//通信地址

    public Account(String username, String password, String cardID, double balance) {
        this.username = username;
        this.password = password;
        CardID = cardID;
        this.balance = balance;
    }
    public  Account(){}

    public Account(String password, String cardID) {
        this.password = password;
        CardID = cardID;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
