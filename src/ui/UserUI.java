package ui;

//这个页面是综合页面
import Client.ATMClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class UserUI extends JFrame implements ActionListener{
    JFrame jframe = new JFrame();
    JPanel p,p2;
    //****************功能页面*************************
    JLabel jn_labal;
    JLabel jn_labal2;
    JButton jn_balance = new JButton("查询余额");
    JButton jn_deposit = new JButton("存   款    ");//存款
    JButton jn_cash = new JButton("取   款    ");//取款
    JButton jn_pin = new JButton("修改密码");//改密
    JButton jn_exit = new JButton("取   卡    ");//退出

    ATMClient chatter;

    public UserUI(ATMClient chatter) {
        this.chatter=chatter;
        jUserUI();
    }

    public void jUserUI(){
        //p是功能页面
        p = new JPanel();
        jnjt();
        //p2是查询界面
        p2 = new JPanel();
        jnjt2();

        Box BoxV = Box.createVerticalBox();
        BoxV.add(p);
        BoxV.add(p2);

        add(BoxV);

        //注册监听
        jn_balance.addActionListener(this);
        jn_deposit.addActionListener(this);
        jn_cash.addActionListener(this);
        jn_pin.addActionListener(this);
        jn_exit.addActionListener(this);

        //设置windowListener,监听用户点击x的动作，若点击x，则关闭窗口
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //设置最佳大小，pack方法
        pack();
        setVisible(true);//设置可见
    }

    public void jnjt() {
        p.setLayout(new GridLayout(2,1));//2行1列
        setTitle("功能选择界面");
        setLayout(new FlowLayout());//设置流式布局
        //p.setBackground(new Color(57,138,204));
        jn_labal = new JLabel("请选择服务项目",JLabel.CENTER);
        //jn_labal.setForeground(new Color(30, 144, 255));
        jn_labal.setFont(new Font("宋体",Font.BOLD,20));
        jn_labal.setForeground(new Color(0,99,177));
        //jn_labal2.setForeground(new Color(0, 255,255));
        p.add(jn_labal);

    }
    private void jnjt2() {
        // TODO Auto-generated method stub
        p2.setLayout(new GridLayout(8,3));//4行3列
        //p2.setBackground(new Color(57,138,204));
        //布局
        Box boxH,boxH2,boxH3,boxH4,space1,space2,space3;//水平
        boxH = Box.createHorizontalBox();
        boxH2 = Box.createHorizontalBox();
        boxH3 = Box.createHorizontalBox();
        boxH4 = Box.createHorizontalBox();
        space1=Box.createHorizontalBox();
        space2=Box.createHorizontalBox();
        space3=Box.createHorizontalBox();

        boxH.add(jn_balance);
        boxH.add(Box.createHorizontalStrut(260));
        boxH.add(jn_exit);

        boxH2.add(jn_cash);
        boxH3.add(jn_deposit);
        boxH4.add(jn_pin);


        //jn_exit.setBackground(new Color(227, 23, 13));//设置按钮的背景颜色为红色

        p2.add(boxH);
        p2.add(space1);
        p2.add(boxH2);
        p2.add(space2);
        p2.add(boxH3);
        p2.add(space3);
        p2.add(boxH4);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==jn_balance){
            try {
                System.out.println("qqqq");
                chatter.getBalance();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if(e.getSource()==jn_cash) {
            new Cash(chatter);//打开取款页面
            dispose();
        }
        if(e.getSource()==jn_deposit) {
            new Deposit(chatter);//打开存款页面
            dispose();
        }
        if(e.getSource()==jn_pin) {
            //new Pin();//打开改密类
            new modifyPW(chatter);
            dispose();
        }
        if(e.getSource()==jn_exit) {
            chatter.closeMe("退卡");
            dispose();
        }
    }
}
