package service;

import java.sql.Connection;
import java.sql.DriverManager;

public class GetDBConnection {
    //DBName:数据库名 ，id : 用户名（root）password:数据库密码
    public static Connection ConnectDB(String DBName,String id,String password) {
        Connection con = null;
        String uri="jdbc:mysql://localhost:3306/"+DBName+"?useSSL=false&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//加载驱动
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            con = DriverManager.getConnection(uri,id,password);//连接
            if (!con.isClosed()) {
                System.out.println("数据库连接成功");
            }
        } catch (Exception e) {e.printStackTrace();}

        return con;
    }
}
