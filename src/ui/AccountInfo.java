package ui;

import entity.Account;
import entity.Record;
import service.adminService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class AccountInfo extends JFrame implements ActionListener {
    private JPanel p;
    private JButton back;
    Vector columnNames = null;
    JTable jtable;
    JScrollPane jscrollpane = new JScrollPane();

    public AccountInfo(Vector rowData) throws SQLException {
        setTitle("账户信息");
        setBounds(100, 100, 500, 348);
        p=new JPanel();
        p.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(p);
        p.setLayout(null);
        //getContentPane().setLayout(new GridLayout(3, 5));

        Box h1,h2,h3;
        h1=Box.createHorizontalBox();
        h2=Box.createHorizontalBox();
        h3=Box.createHorizontalBox();


        JLabel label = new JLabel("银行账户信息",JLabel.CENTER);
        //label.setFont(new Font("宋体", Font.PLAIN, 24));
        label.setBounds(100,0,300,30);
        //h1.add(label);

        columnNames = new Vector();
        columnNames.add("卡号");
        columnNames.add("持卡人姓名");
        columnNames.add("账户余额");

        jtable = new JTable(rowData, columnNames);
        jscrollpane = new JScrollPane(jtable);
        jscrollpane.setBounds(20,40,450,220);

        back = new JButton("返回");
        back.setBounds(200,270,100,30);
        back.addActionListener(this);

        //p.add(h1);
        p.add(label);
        //h2.add(jscrollpane);
        p.add(jscrollpane);
        //getContentPane().add(jscrollpane);
        //getContentPane().setLayout(new GridLayout(2, 5));
        //h3.add(back);
        p.add(back);

        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
        new AdminUI(0);
    }
}
