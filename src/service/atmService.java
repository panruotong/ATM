package service;

import entity.Record;

import java.sql.*;
import java.util.Date;

public class atmService {
    private PreparedStatement ps;
    public static Connection con;
    public static String cardID="";
    private String balanceFixed = "0";
    private int ATMnum;
    String name="";
    public atmService(){
        con = GetDBConnection.ConnectDB("atmbankdatabase", "root", "root");
        if(con == null) {
            System.out.println("数据库连接失败");
            return;
        }
    }
    public double select(String cardID) throws SQLException{
        String sql="select balance from account where id=?;";
        ps=con.prepareStatement(sql);
        ps.setString(1,cardID);
        ResultSet rs=ps.executeQuery();
        while(rs.next()) {
            balanceFixed = rs.getString("balance");
        }
        double balance_number = Double.parseDouble(balanceFixed);
        balanceFixed = String.valueOf(balance_number);
        System.out.println("当前账户余额："+balanceFixed);
        return balance_number;
    }
    public String Withdrawal(String cardID,double take_money) throws SQLException {
        select(cardID);
        System.out.println(take_money);
        System.out.println();
        //take_money要取的钱数
        String s =null;
        try {
            //balance_number:数字格式的余额，用于和将要取的金额做比较
            double balance_number = Double.parseDouble(balanceFixed);
            if(take_money<=balance_number) {
                String sql="update account set balance=?-? where id=?;";
                ps=con.prepareStatement(sql);
                ps.setDouble(1, balance_number);//余额
                ps.setDouble(2, take_money);//要取的金额
                ps.setString(3, cardID);
                ps.executeUpdate();//注意这个executeUpdate,和查询的不一样
                Date now = new Date();
                balance_number=balance_number-take_money;
                balanceFixed=String.valueOf(balance_number);
                System.out.println("取款成功,当前余额为："+balanceFixed);
                s="取款成功,当前余额为："+balanceFixed;
                Record r = new Record(cardID,'d',take_money,ATMnum,now);
                String addSQl ="insert into record(CardID,Cardholder,oper,Tradmoney,ATMnum,time) values(?,?,?,?,?,?);";
                ps=con.prepareStatement(addSQl);
                ps.setString(1,cardID);
                ps.setString(2,name);
                ps.setString(3,"取款");
                ps.setDouble(4,take_money);
                ps.setInt(5,ATMnum);
                ps.setTimestamp(6, new Timestamp(now.getTime()));
                ps.executeUpdate();
            }else {
                System.out.println("余额不足,请重新输入");
                s="余额不足,请重新输入";
                //Withdrawal(cardID,take_money);
            }
        } catch (SQLException e) {
            System.out.println("取款出错");
            s="取款出错";
        }
        return s;
    }
    public String deposit(String cardID,double m) throws SQLException {
        select(cardID);
        double balance_number = Double.parseDouble(balanceFixed);

        String sql="update account set balance=?+? where id=?;";
        ps=con.prepareStatement(sql);
        ps.setDouble(1, balance_number);//余额
        ps.setDouble(2, m);//要存的金额
        ps.setString(3, cardID);
        ps.executeUpdate();
        Date now = new Date();
        System.out.println(now);
        balance_number=balance_number+m;
        balanceFixed=String.valueOf(balance_number);
        String s="存款成功 当前余额为:"+balanceFixed;
        //用正则表达式来限制输入，确保输入是字母时不会运行异常
        //boolean isNum = m.matches("[0-9]+");
        String addSQl ="insert into record(CardID,Cardholder,oper,Tradmoney,ATMnum,time) values(?,?,?,?,?,?);";
        ps=con.prepareStatement(addSQl);
        ps.setString(1,cardID);
        ps.setString(2,name);
        ps.setString(3,"存款");
        ps.setDouble(4,m);
        ps.setInt(5,ATMnum);
        Timestamp t = new Timestamp(now.getTime());
        ps.setTimestamp(6, t);
        ps.executeUpdate();
        return s;
    }
    public boolean login(String cardID,String password,int atmnum) {
        String newCardID="0";
        String newPassword="0";
        this.ATMnum=atmnum;
        String sql="select id,password,username from account where id=?;";
        String s=null;
        try {
            ps=con.prepareStatement(sql);
            ps.setString(1, cardID);
            ResultSet rs =ps.executeQuery();//向数据库发送数据查询语句
            while(rs.next()) {
                newCardID= rs.getString("id");
                newPassword=rs.getString("password");
                this.name = rs.getString("username");
            }
            if(cardID.equals(newCardID) && password.equals(newPassword)) {
                System.out.println("登陆成功");
                s = "登陆成功";
                this.cardID=cardID;//使另一个类可以调用这个cardID
                return true;
            }else {
                System.out.println("用户名或密码不正确,请重新输入");
                s="用户名或密码不正确,请重新输入";
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public String changePassword(String cardID,String password) {
        String sql="update account set password=? where id=?;";
        String s = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, password);
            ps.setString(2, cardID);
            ps.executeUpdate();
            s="密码修改成功";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }
    public void close(){
        try {
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
