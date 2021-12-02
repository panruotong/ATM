package ui;

import Client.ATMClient;
import service.atmService;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//登陆界面设置三个画板，一个用来放置账户和密码文本框，另一个用来放置数字键盘，另一个用来放置文本域
public class Login extends JFrame implements ActionListener{
    JFrame frame;
    JPanel p,p2,p3;//三个画板
    //panel面板中内容
    JTextField jtext = new JTextField(20);//文本框
    JPasswordField jpass = new JPasswordField(20);//密码框
    JButton b=new JButton("登陆");
    JButton b2=new JButton("退出");
    //panel2面板中内容
    JButton bt[] =new JButton[14];
    //panel3面板中内容
    JTextArea jtarea = new JTextArea("           欢迎来到建设银行！           ",6,40);//文本域
    ATMClient chatter;
    public Login(ATMClient chatter) {
        open_login();
        this.chatter=chatter;
    }

    public void open_login() {
        frame = new JFrame();
        setTitle("ATM登陆界面");

        //盒式布局
        Box boxHP = Box.createHorizontalBox();
        Box boxHP2 = Box.createHorizontalBox();
        Box boxVP = Box.createVerticalBox();
        //1.创建一个JPanel对象，里面存放一个JTextField和JPasswordField组件
        p = new JPanel(new GridLayout(5,3,0,15));
        init();
        //2.创建一个panel对象p2，并且设置它的布局管理器为GridLayout
        p2 = new JPanel();
        init2();
        //向panel3面板中添加文本域
        //p3 = new JPanel(new GridLayout(1,1,10,10));
        //init3();
        //3.把panel和panel2添加到frame中
        setMyCommandListener();
        boxHP.add(p);
        boxHP.add(p2);
        //boxHP2.add(p3);
        boxVP.add(boxHP);
        boxVP.add(boxHP2);
        add(boxVP);

        //设置windowListener,监听用户点击x的动作，若点击x，则关闭窗口
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //设置最佳大小，pack方法
        //pack()使setBounds(x, y, width, height);失灵
        pack();
        setVisible(true);//设置可见
    }

    void init() {
        Box boxH,boxH2;//水平
        boxH = Box.createHorizontalBox();
        boxH2 = Box.createHorizontalBox();

        boxH.add(new JLabel("账号:"));
        boxH.add(jtext);
        boxH2.add(new JLabel("密码:"));
        boxH2.add(jpass);

        p.setBorder(BorderFactory.createTitledBorder("登陆区域:"));
        p.add(new JLabel("请在确定周围环境安全的前提下操作:"));
        p.add(boxH);
        p.add(boxH2);
        p.add(b);
        p.add(b2);
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
    }
    void init3() {
        p3.setBorder(BorderFactory.createTitledBorder("提示信息:"));
        jtarea.setForeground(Color.blue);
        jtarea.setFont(new Font("宋体",Font.BOLD,20));
        //jtarea.setAlignmentX(CENTER_ALIGNMENT);
        jtarea.setEditable(false);//使文本域不可被操作
        jtarea.setLineWrap(true);//一行的内容大于文本框的长度就自动换行
        p3.add(new JScrollPane(jtarea));//添加带滚动条的文本框

        //调用监听的方法
        setMyCommandListener();
    }

    public void setMyCommandListener() {
        //注册监听
        b.addActionListener(this);//登陆按钮
        b2.addActionListener(this);//退出按钮
        for(int i=1;i<13;i++) {
            bt[i].addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s="";
        String s2="";
        String mi="";
        String com="";
        com = e.getActionCommand();
        //日期
        Date date=new Date();
        String ktime=String.format("%tY年%<tm月%<td日%<ta%<tI:%<tM:%<tS:",date);

        if(jtext.getText().length()!=8) {
            for(int i=0;i<=9;i++) {
                String num = String.valueOf(i);
                if(com.equals(num)) {
                    s2=jtext.getText()+com;
                    jtext.setText(s2);
                }
            }
            if(com.equals("退格")) {
                s2=jtext.getText();
                if(s2.length()>0)
                    jtext.setText(s2.substring(0,s2.length()-1));
            }
            if(com.equals("重输")) {
                s2="";
                jtext.setText(s2);
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
            if(com.equals("退格")) {
                mi=jpass.getText();
                if(mi.length()>0)
                    jpass.setText(mi.substring(0,mi.length()-1));
                else {
                    s=jtext.getText();
                    if(s.length()>0)
                        jtext.setText(s.substring(0,s.length()-1));
                }
            }
            if(com.equals("更正")) {
                mi="";
                jpass.setText(mi);
            }
            jpass.requestFocus();//使光标一直保持在密码框之中
        }
        if(com.equals("登陆")) {
            if(jtext.getText().length()!=8){
                JOptionPane.showMessageDialog(b,"无效银行卡号","消息提示框",JOptionPane.WARNING_MESSAGE);
            }else if (jpass.getText().length()!=6){
                JOptionPane.showMessageDialog(b,"无效密码位数","消息提示框",JOptionPane.WARNING_MESSAGE);
            }else{
                chatter.loginServer(jtext.getText(),jpass.getText());
            }
        }
        if(e.getSource()==b2){
            //点击退出按钮，则关闭页面
            dispose();
        }
    }
    public void FailLogin(){
        //jtarea.append("\n"+ktime+"登陆失败");
        JOptionPane.showMessageDialog(b,"用户名或密码不正确,请重新输入","消息提示框",JOptionPane.WARNING_MESSAGE);
    }
    public void FailThird(){
        //jtarea.append("\n"+ktime+"登陆失败");
        JOptionPane.showMessageDialog(b,"连续三次登陆失败,已退卡！","消息提示框",JOptionPane.WARNING_MESSAGE);
        jtext.setText("");
        jpass.setText("");
    }
    public static void main(String[] args) {

    }
}
