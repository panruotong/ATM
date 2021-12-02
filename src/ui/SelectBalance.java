package ui;

import Client.ATMClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectBalance extends JFrame implements ActionListener {
    ATMClient chatter;
    double balance;
    JLabel label;
    JPanel p = new JPanel();
    JButton button = new JButton("返回");

    public SelectBalance(ATMClient chatter,double balance) throws HeadlessException {
        this.chatter = chatter;
        this.balance=balance;
        String balanceStr;
        if(balance != 0.00){
            java.text.DecimalFormat df = new java.text.DecimalFormat("################.00");
            balanceStr = df.format(balance);
        }else{
            balanceStr =  "0.00";
        }


        setTitle("查询余额");
        setBounds(100, 100, 420, 348);
        p=new JPanel();
        p.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(p);
        p.setLayout(null);

        label = new JLabel("您的余额是："+balanceStr,JLabel.CENTER);
        label.setFont(new Font("宋体",Font.BOLD,20));
        label.setBounds(50,100,320,60);

        Box box,space1,space2;//水平
        box = Box.createHorizontalBox();
        space1=Box.createHorizontalBox();
        space2=Box.createHorizontalBox();
        space1.add(Box.createHorizontalStrut(320));
        space2.add(Box.createHorizontalStrut(320));
        box.add(label);

        p.add(label);
        p.add(box);
        p.add(space2);
        button.addActionListener(this);
        button.setBounds(170,250,80,30);
        p.add(button);

        //设置windowListener,监听用户点击x的动作，若点击x，则关闭窗口
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //设置最佳大小，pack方法
        setVisible(true);//设置可见
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
        chatter.backUserViewer();
    }
}
