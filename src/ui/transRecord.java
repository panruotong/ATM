package ui;

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

public class transRecord extends JFrame implements ActionListener {
    private JPanel p;
    private JButton back;
    JTable jtable;
    JScrollPane jscrollpane = new JScrollPane();

    Vector columnNames;
    int pow;
    public transRecord(Vector rowData,int pow) throws SQLException {
        setTitle("内部交易记录");
        setBounds(100, 100, 500, 348);
        p=new JPanel();
        p.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(p);
        p.setLayout(null);

        JLabel label = new JLabel("银行内部交易记录",JLabel.CENTER);
        //label.setFont(new Font("宋体", Font.PLAIN, 24));
        //label.setBounds(50,30,300,30);
        label.setBounds(100,0,300,30);

        columnNames = new Vector();
        columnNames.add("ATM机编号");
        columnNames.add("卡号");
        columnNames.add("持卡人姓名");
        columnNames.add("类型");
        columnNames.add("交易金额");
        columnNames.add("交易时间");

        jtable = new JTable(rowData, columnNames);
        jscrollpane = new JScrollPane(jtable);
        jscrollpane.setBounds(20,40,450,220);

        back = new JButton("返回");
        back.setBounds(190,260,100,30);
        back.addActionListener(this);

        p.add(label);
        p.add(jscrollpane);
        p.add(back);

        setVisible(true);
        this.pow=pow;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new AdminUI(pow);
        dispose();
    }
}
