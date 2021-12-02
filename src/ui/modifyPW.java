package ui;


import Client.ATMClient;
import service.atmService;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

//修改密码页面
public class modifyPW extends JFrame implements ActionListener{
    JPanel p = new JPanel();
    JPanel p2 = new JPanel();
    //panel2面板中内容
    JButton bt[] =new JButton[14];
    JLabel title = new JLabel("请在确定周围环境安全的前提下操作:",JLabel.CENTER);
    JLabel label = new JLabel("  请输入原密码    ");
    JPasswordField text = new JPasswordField();
    JLabel label2 = new JLabel("  请输入新密码    ");
    JPasswordField text2 = new JPasswordField();
    JLabel label3 = new JLabel(" 再次输入新密码 ");
    JPasswordField text3 = new JPasswordField();
    JButton button = new JButton("返回");
    JButton button2 = new JButton("确认");
    ATMClient chatter;
    public modifyPW(ATMClient chatter) {
        this.chatter=chatter;
        setTitle("修改密码页面");
        //调用方法
        p_in();
        Box h1,h2;
        h2=Box.createHorizontalBox();
        h1=Box.createHorizontalBox();

        h1.add(p);
        h1.add(p2);
        /**
        h2.add(Box.createHorizontalStrut(80));
        h2.add(button);
        h2.add(Box.createHorizontalStrut(100));
        h2.add(button2);
        h2.add(Box.createHorizontalStrut(80));**/

        Box v;
        v=Box.createVerticalBox();
        v.add(h1);
        v.add(h2);

        add(h1);
        //设置windowListener,监听用户点击x的动作，若点击x，则关闭窗口
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //设置最佳大小，pack方法
        pack();
        setVisible(true);//设置可见
    }

    private void p_in() {
        // TODO Auto-generated method stub
        p.setLayout(new GridLayout(6, 1, 0, 15));
        title.setFont(new Font("宋体",Font.BOLD,15));
        Box boxH0,boxH,boxH2,boxH3,boxH4,boxH5;//水平
        boxH0 = Box.createHorizontalBox();
        boxH = Box.createHorizontalBox();
        boxH2 = Box.createHorizontalBox();
        boxH3 = Box.createHorizontalBox();
        boxH4 = Box.createHorizontalBox();
        //boxH5 = Box.createHorizontalBox();
        boxH0.add(title);
        boxH.add(label);
        boxH.add(text);
        boxH2.add(label2);
        boxH2.add(text2);
        boxH3.add(label3);
        boxH3.add(text3);

        //boxH5.add(Box.createVerticalStrut(30));
        p.add(boxH0);
        p.add(boxH);
        p.add(boxH2);
        p.add(boxH3);
        p.add(button);
        p.add(button2);
        //p.add(boxH5);
        //添加监听
        button.addActionListener(this);
        button2.addActionListener(this);

        p2 = new JPanel();
        init2();
        //getContentPane().add(p2);
    }
    void init2() {
        p2.setBorder(BorderFactory.createTitledBorder("数字键盘:"));
        p2.setLayout(new GridLayout(4,3,7,7));//4行3列
        //4.往panel2中添加内容
        for(int i=1;i<13;i++) {
            String jbutton=String.valueOf(i);
            bt[i]=new JButton(jbutton);
            if(i==10||i==11||i==12) {
                if(i==10) {
                    p2.add(bt[i]);
                    bt[i].setText("更正");
                    bt[i].setBackground(Color.ORANGE);//设置按钮的背景颜色为橙色
                }
                if(i==11) {
                    p2.add(bt[i]);
                    bt[i].setText("0");
                }
                if(i==12) {
                    p2.add(bt[i]);
                    bt[i].setText("退格");
                    bt[i].setForeground(Color.red);
                }
            }else{
                p2.add(bt[i]);
            }
        }
        for(int i=1;i<13;i++) {
            bt[i].addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String com="";
        String s2="";
        String s1="";
        String s3="";
        com = e.getActionCommand();
        if(text.getText().length()!=6) {
            for(int i=0;i<=9;i++) {
                String num = String.valueOf(i);
                if(com.equals(num)) {
                    s2=text.getText()+com;
                    text.setText(s2);
                }
            }
            if(com.equals("退格")) {
                s2=text.getText();
                if(s2.length()>0)
                    text.setText(s2.substring(0,s2.length()-1));
            }
            if(com.equals("更正")) {
                s2="";
                text.setText(s2);
            }
            text.requestFocus();//使光标一直保持在文本框中
        }else if (text2.getText().length()!=6){
            for(int i=0;i<=9;i++) {
                String num = String.valueOf(i);
                if(com.equals(num)) {
                    s1=text2.getText()+com;
                    text2.setText(s1);
                }
            }
            if(com.equals("退格")) {
                s1=text2.getText();
                if(s1.length()>0)
                    text2.setText(s1.substring(0,s1.length()-1));
            }
            if(com.equals("更正")) {
                s1="";
                text2.setText(s1);
            }
            text2.requestFocus();
        }else if (text3.getText().length()!=6){
            for(int i=0;i<=9;i++) {
                String num = String.valueOf(i);
                if(com.equals(num)) {
                    s1=text3.getText()+com;
                    text3.setText(s1);
                }
            }
            if(com.equals("退格")) {
                s1=text3.getText();
                if(s1.length()>0)
                    text3.setText(s1.substring(0,s1.length()-1));
            }
            if(com.equals("更正")) {
                s1="";
                text3.setText(s1);
            }
            text3.requestFocus();
        }
        if(e.getSource()==button) {
            //new UserUI();
            dispose();
            new Login(new ATMClient(chatter.getServerIp(),chatter.getServerPort(),chatter.getATMnum()));
        }
        if(e.getSource()==button2) {
            //String s = basi2.changePassword(basi2.cardID, text.getText(), text2.getText(), text3.getText());
            //JOptionPane.showMessageDialog(button2,s,"消息提示框",JOptionPane.WARNING_MESSAGE);
            String origin = text.getText();
            String newpw1 = text2.getText();
            String newpw2 = text3.getText();
            if(newpw1.equals(newpw2)){
                if(newpw1.length()<6)
                {
                    JOptionPane.showMessageDialog(button2,"密码位数不合法","消息提示框",JOptionPane.WARNING_MESSAGE);
                }
                else{
                    String s = chatter.changePW(origin,newpw1);
                    if (s!=null){
                        JOptionPane.showMessageDialog(button2,s,"消息提示框",JOptionPane.WARNING_MESSAGE);
                    }
                }
            }else {
                String s = "新密码不一致！";
                JOptionPane.showMessageDialog(button2,s,"消息提示框",JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //modifyPW a = new modifyPW();
    }

}