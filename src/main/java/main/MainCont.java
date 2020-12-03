package main;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class UI extends JFrame{
    UI(){
        initUI();
    }

    public final void initUI(){
        JFrame f = new JFrame("File Together");
        Container c = f.getContentPane();

        JPanel mainPanel = new JPanel();
        JPanel serverPanel = new JPanel();
        JPanel clientPanel = new JPanel();

        mainPanel.setLayout(new GridLayout(1,2));
        JButton setModeServer = new JButton("set Server");
        setModeServer.addActionListener(new ActionListener(){ //익명클래스로 리스너 작성
            public void actionPerformed(ActionEvent e){
                System.out.println("clicked");
            }
        });;

        JButton setModeClient = new JButton("set Client");



        mainPanel.add(setModeServer);
        mainPanel.add(setModeClient);

        f.add(mainPanel);
        f.setSize(600,300);
        f.setVisible(true);
        f.setDefaultCloseOperation(3);
    }
}


public class MainCont {
    public static void main(String[] args) {
        new UI();

    }

}
