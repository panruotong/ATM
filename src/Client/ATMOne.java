package Client;

import ui.AdminUI;
import ui.adminLogin;
import ui.modifyPW;

import java.io.IOException;

public class ATMOne {
    private ATMClient chatter;
    public ATMOne(String ip,int port) throws IOException {
        chatter= new ATMClient(ip, port,502);
        //chatter.conn2server();
    }

    public static void main(String[]args) throws IOException{
        ATMOne atm1= new ATMOne("localhost",11111);
        //adminLogin al=new adminLogin();
        //AdminUI adminUI = new AdminUI();
        //new modifyPW(new ATMClient("localhost",11111,302));
    }
}
