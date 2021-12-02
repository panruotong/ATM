package ui;

import Client.ATMClient;
import service.atmService;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Deposit extends JFrame implements ActionListener{
    ATMClient chatter;
    JPanel jp = new JPanel();
    JLabel lable = new JLabel("            请输入存款金额");
    JTextField textf = new JTextField();//文本框
    JButton button = new JButton("返回     ");
    JButton button2 = new JButton("100元   ");
    JButton button3 = new JButton("500元  ");
    JButton button4 = new JButton("1000元");
    JButton button5 = new JButton("3000元");
    JButton button6 = new JButton("5000元");
    JButton button7 = new JButton("更正     ");
    JButton button8 = new JButton("确认     ");
    public Deposit(ATMClient chatter) {
        this.chatter=chatter;
        //构造方法
        setTitle("存款界面");
        //******************************************************
        //为页面布局
        jp_one();
        add(jp);
        //******************************************************
        //设置windowListener,监听用户点击x的动作，若点击x，则关闭窗口
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //设置最佳大小，pack方法
        pack();
        setVisible(true);//设置可见
    }
    public void jp_one() {
        // TODO Auto-generated method stub
        jp.setLayout(new BorderLayout());
        lable.setFont(new Font("宋体",Font.BOLD,20));
        textf.setBounds(150, 50, 110, 40);
        add(textf);
        //以下是按钮
        Box BoxV = Box.createVerticalBox();

        Box BoxH = Box.createHorizontalBox();
        BoxH.add(Box.createVerticalStrut(50));
        Box BoxH2 = Box.createHorizontalBox();
        BoxH2.add(Box.createVerticalStrut(50));
        Box BoxH3 = Box.createHorizontalBox();
        BoxH3.add(Box.createVerticalStrut(50));
        Box BoxH4 = Box.createHorizontalBox();
        BoxH4.add(Box.createVerticalStrut(50));

        BoxH.add(button);
        BoxH.add(Box.createHorizontalStrut(260));
        BoxH.add(button5);

        BoxH2.add(button2);
        BoxH2.add(Box.createHorizontalStrut(260));
        BoxH2.add(button6);

        BoxH3.add(button3);
        BoxH3.add(Box.createHorizontalStrut(260));
        BoxH3.add(button7);

        BoxH4.add(button4);
        BoxH4.add(Box.createHorizontalStrut(260));
        BoxH4.add(button8);

        BoxV.add(BoxH);
        BoxV.add(BoxH2);
        BoxV.add(BoxH3);
        BoxV.add(BoxH4);
        jp.add(lable,BorderLayout.NORTH);
        jp.add(BoxV);

        //注册监听
        button.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        button5.addActionListener(this);
        button6.addActionListener(this);
        button7.addActionListener(this);
        button8.addActionListener(this);

        textf.setEditable(false);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        String number = textf.getText();
        if(e.getSource()==button) {
            //new UserUI();
            chatter.backUserViewer();
            dispose();
        }
        if(e.getSource()==button2) {
            textf.setText("100");
            //basi2.Withdrawal(basi2.cardID, "100");
        }
        if(e.getSource()==button3) {
            textf.setText("500");
            //basi2.Withdrawal(basi2.cardID, "100");
        }
        if(e.getSource()==button4) {
            textf.setText("1000");
            //basi2.Withdrawal(basi2.cardID, "100");
        }
        if(e.getSource()==button5) {
            textf.setText("3000");
            //basi2.Withdrawal(basi2.cardID, "100");
        }
        if(e.getSource()==button6) {
            textf.setText("5000");
            //basi2.Withdrawal(basi2.cardID, "100");
        }
        if(e.getSource()==button7) {
            //更正
            textf.setText("");
        }
        if(e.getSource()==button8) {
            String s = null;
            try {
                chatter.OperDeposit(Double.parseDouble(number));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //JOptionPane.showMessageDialog(button8,s,"消息提示框",JOptionPane.WARNING_MESSAGE);
        }
    }
}
