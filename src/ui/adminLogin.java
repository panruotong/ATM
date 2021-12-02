package ui;

import service.adminService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class adminLogin extends JFrame implements ActionListener {
    JPanel p,p2,p3;//三个画板
    //panel面板中内容
    JTextField jtext = new JTextField(20);//文本框
    JPasswordField jpass = new JPasswordField(20);//密码框
    JButton b=new JButton("登陆");
    JButton b2=new JButton("退出");
    int login_count=0;
    private adminService service = new adminService();
    public adminLogin(){
        setTitle("管理员界面");
        //1.创建一个JPanel对象，里面存放一个JTextField和JPasswordField组件
        p = new JPanel(new GridLayout(5,3,0,15));
        add(p);
        setLocation(100,100);
        init();

        setSize(300,250);
        //设置windowListener,监听用户点击x的动作，若点击x，则关闭窗口
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //设置最佳大小，pack方法
        //pack()使setBounds(x, y, width, height);失灵
        //pack();
        setVisible(true);//设置可见
    }

    void init() {
        b.addActionListener(this);//登陆按钮
        b2.addActionListener(this);//退出按钮


        Box boxH,boxH2,boxH3,boxH4;//水平
        boxH = Box.createHorizontalBox();
        boxH2 = Box.createHorizontalBox();
        boxH3=Box.createHorizontalBox();
        boxH4=Box.createHorizontalBox();

        boxH.add(new JLabel("账号:"));
        boxH.add(jtext);
        boxH2.add(new JLabel("密码:"));
        boxH2.add(jpass);

        boxH3.add(Box.createHorizontalStrut(100));
        boxH3.add(b);
        boxH3.add(Box.createHorizontalStrut(75));
        //boxH3.add(s2);
        boxH4.add(Box.createHorizontalStrut(100));
        boxH4.add(b2);
        boxH4.add(Box.createHorizontalStrut(100));

        p.setBorder(BorderFactory.createTitledBorder("登陆区域:"));
        p.add(boxH);
        p.add(boxH2);
        p.add(boxH3);
        p.add(boxH4);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s="";
        String s2="";
        String mi="";
        String com="";
        com = e.getActionCommand();

        if(jtext.getText().length()!=8) {
            for(int i=0;i<=9;i++) {
                String num = String.valueOf(i);
                if(com.equals(num)) {
                    s2=jtext.getText()+com;
                    jtext.setText(s2);
                }
            }
            jtext.requestFocus();//使光标一直保持在文本框中
        }else{
            for(int i=0;i<=9;i++) {
                String num = String.valueOf(i);
                if(com.equals(num)) {
                    mi=jpass.getText()+com;
                    jpass.setText(mi);
                }
            }
            jpass.requestFocus();//使光标一直保持在密码框之中
        }
        if(com.equals("登陆")) {
            login_count++;
            if(service.login(jtext.getText(),jpass.getText())){
                dispose();
            }else{
                JOptionPane.showMessageDialog(b,"密码错误！","消息提示框",JOptionPane.WARNING_MESSAGE);
                if (login_count == 3){
                    JOptionPane.showMessageDialog(b,"连续三次密码错误，请15分钟后再登陆！","消息提示框",JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        if(e.getSource()==b2){
            //点击退出按钮，则关闭页面
            dispose();
        }
    }

    public static void main(String[] args) {
        new adminLogin();
    }
}
