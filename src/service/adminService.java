package service;

import entity.Account;
import entity.Record;
import ui.AdminUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class adminService {
    private PreparedStatement ps;
    public static Connection con;
    private int power;
    public adminService(){
        con = GetDBConnection.ConnectDB("atmbankdatabase", "root", "root");
        if(con == null) {
            System.out.println("数据库连接失败");
            return;
        }
    }
    public boolean login(String account,String password){
        String newCardID="0";
        String newPassword="0";
        String sql="select id,password,pow from adminlist where id=?;";
        try {
            ps=con.prepareStatement(sql);
            ps.setString(1, account);
            ResultSet rs =ps.executeQuery();//向数据库发送数据查询语句
            while(rs.next()) {
                newCardID= rs.getString("id");
                newPassword=rs.getString("password");
                power=rs.getInt("pow");
                System.out.println("power: "+power);
            }
            if(account.equals(newCardID) && password.equals(newPassword)) {
                System.out.println("登陆成功");
                new AdminUI(power);
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    public Vector getTransRecord() throws SQLException {
        List<Record> list = new ArrayList<>();
        String sql="select * from record;";
        ps=con.prepareStatement(sql);
        ResultSet rs =ps.executeQuery();//向数据库发送数据查询语句
        Vector rowData = new Vector();
        while(rs.next()) {
            Vector row = new Vector();
            row.add(rs.getInt("ATMnum"));
            row.add(rs.getInt("CardID"));
            row.add(rs.getString("Cardholder"));
            row.add(rs.getString("oper"));
            row.add(rs.getDouble("Tradmoney"));
            row.add(rs.getDate("time"));

            rowData.add(row);
        }
        return rowData;
    }
    public Vector getAccountInfo(){
        List<Account> accountList = new ArrayList<>();
        String sql="select * from account;";
        Vector rowData = new Vector();
        try {
            ps=con.prepareStatement(sql);
            ResultSet rs =ps.executeQuery();//向数据库发送数据查询语句
            while (rs.next()){
                Vector row = new Vector();
                row.add(rs.getString("id"));
                row.add(rs.getString("username"));
                double balance = rs.getDouble("balance");
                String balanceStr;
                java.text.DecimalFormat df = new java.text.DecimalFormat("################.00");
                balanceStr = df.format(balance);
                row.add(balanceStr);
                row.add(rs.getString("username"));

                rowData.add(row);
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowData;
    }
    public int getPower(){
        return power;
    }
}
