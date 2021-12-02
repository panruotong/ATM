package ui;

import service.adminService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AdminUI extends JFrame implements ActionListener {
    JPanel p;
    JButton tR;
    JButton aI;
    adminService service = new adminService();
    int pow;
    public AdminUI(int power){
        pow=power;
        setTitle("管理员主界面");
        setBounds(100, 100, 400, 348);
        p=new JPanel();
        p.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(p);
        p.setLayout(null);

        JLabel label = new JLabel("欢迎使用ATM管理系统",JLabel.CENTER);
        label.setFont(new Font("宋体", Font.PLAIN, 24));
        label.setBounds(50,60,300,30);
        p.add(label);

        tR = new JButton("查询内部交易记录");
        aI = new JButton("查询账户信息");
        tR.setBounds(125, 129, 150, 28);
        p.add(tR);
        aI.setBounds(125, 181, 150, 28);
        p.add(aI);
        tR.addActionListener(this);
        aI.addActionListener(this);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==tR){
            if (pow>1){
                JOptionPane.showMessageDialog(tR,"您的权限不够，暂无法查询交易记录","消息提示框",JOptionPane.WARNING_MESSAGE);
            }else{
                try {
                    new transRecord(service.getTransRecord(),pow);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                this.dispose();
            }
        }else if (e.getSource()==aI){
            if (pow!=0){
                JOptionPane.showMessageDialog(tR,"您的权限不够，暂无法查询用户账户信息","消息提示框",JOptionPane.WARNING_MESSAGE);
            }else {
                try {
                    new AccountInfo(service.getAccountInfo());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                this.dispose();
            }
        }
    }
}
