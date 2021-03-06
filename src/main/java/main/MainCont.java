package main;
import client.Client;
import server.Server;

import javax.swing.*;
import java.awt.*;
import java.io.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class initUI extends JFrame{
    initUI(){
        initUI();
    }

    public final void initUI(){
        JFrame f = new JFrame("File Together");

        Container c = f.getContentPane();
        JFileChooser chooser = new JFileChooser();
        JPanel mainPanel = new JPanel();
        JPanel serverPanel = new JPanel();
        JPanel clientPanel = new JPanel();
        JButton selectFile = new JButton("Select File");
        JButton serverSwitch = new JButton("SHARE");
        serverSwitch.setVisible(false);
        mainPanel.setLayout(new GridLayout(1,2));
        JButton selectInterface = new JButton("Select");
        JLabel myIntShow = new JLabel();
        JButton refreshBtn = new JButton("Search Server");
        JButton downloadBtn = new JButton("Download");

        JPanel serverSearch = new JPanel();
        JPanel downloadPanel = new JPanel();





        refreshBtn.setVisible(false);

        serverSearch.setVisible(false);
        downloadPanel.setVisible(true);

        // mainPanel
        JButton setModeServer = new JButton("SET SERVER");
        setModeServer.addActionListener(new ActionListener(){ //익명클래스로 리스너 작성
            public void actionPerformed(ActionEvent e){
                serverPanel.setVisible(true);
                clientPanel.setVisible(false);

                f.remove(mainPanel);
                f.remove(clientPanel);

                f.add(mainPanel, BorderLayout.NORTH);
                f.add(serverPanel, BorderLayout.CENTER);
                f.setSize(600,150);
                serverPanel.add(selectFile);
                System.out.println("server");
            }
        });;

        JButton setModeClient = new JButton("SET CLIENT");
        setModeClient.addActionListener(new ActionListener(){ //익명클래스로 리스너 작성
            public void actionPerformed(ActionEvent e){
                serverPanel.setVisible(false);
                clientPanel.setVisible(true);


                f.remove(mainPanel);
                f.remove(serverPanel);
                f.add(mainPanel, BorderLayout.NORTH);
                f.add(clientPanel, BorderLayout.CENTER);
                f.setSize(600,150);
                System.out.println("client");
            }
        });;

        mainPanel.add(setModeServer);
        mainPanel.add(setModeClient);
        // mainPanel

        // serverPanel
        serverPanel.setLayout(new FlowLayout());
        selectFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int ret = chooser.showOpenDialog(null);
                if(ret == JFileChooser.APPROVE_OPTION) {
                    MainCont.s.setFilePath(chooser.getSelectedFile().getPath());
                    System.out.println(MainCont.s.getFilePath());
                    serverPanel.add(serverSwitch);
                    serverSwitch.setVisible(true);
                }
            }
        });

        serverSwitch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(MainCont.s.getIsStppped()==true) {
                    new Thread(MainCont.s).start();
                }
            }
        });
        serverPanel.add(selectFile);
        // serverPanel

        // clientPanel
        clientPanel.setLayout(new GridLayout(2,1));
        serverSearch.setLayout(new FlowLayout());
        ArrayList<Client.InterfaceData> interfaces = MainCont.c.getInterfaces();
        ArrayList<String> interfaceToString = new ArrayList<String>();
        for (Client.InterfaceData i: interfaces) {
            interfaceToString.add(i.getNicName()+" "+i.getIpAddress());
        }

        JComboBox<String> interfaceCombo = new JComboBox<String>(interfaceToString .toArray(new String[interfaceToString .size()]));
        clientPanel.add(interfaceCombo);
        clientPanel.add(selectInterface);
        selectInterface.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String intData = interfaceCombo.getSelectedItem().toString();
                String nicName = intData.split(" ")[0];
                String ipAddr = intData.split(" ")[1];

                for (Client.InterfaceData i: interfaces) {
                    if (i.getIpAddress().equals(ipAddr) && i.getNicName().equals(nicName)){
                        MainCont.c.setMyInterface(i);
                        refreshBtn.setVisible(true);
                    }
                }

                serverSearch.setVisible(true);
            }
        });
        serverSearch.add(refreshBtn);
        serverSearch.add(myIntShow);

        JComboBox selectIP = new JComboBox();
        selectIP.setVisible(false);
        clientPanel.add(selectIP);
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String allIp[] = MainCont.c.scan();
                selectIP.setVisible(true);
                for (String ip : allIp) {
                    downloadBtn.setVisible(true);
                    selectIP.addItem(new String(ip));
                }
                serverSearch.setVisible(true);
            }
        });


        downloadPanel.add(downloadBtn);
        downloadBtn.setVisible(false);
        downloadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = selectIP.getSelectedItem().toString();
                System.out.println(ip);
                if(MainCont.c.download(ip)){
                    System.out.println("Success");
                }else {
                    System.out.println("FAILED");
                }
            }
        });


        clientPanel.add(serverSearch);
        clientPanel.add(downloadPanel);
        f.add(mainPanel);
        f.setSize(600,300);
        f.setVisible(true);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}



public class MainCont {
    static Server s = new Server();
    static Client c = new Client();



    public static void main(String[] args) {
        String downloadFolder = System.getProperty("user.home")+"/FileTogetherDownload";
        new File(downloadFolder).mkdir();
        new initUI();


    }

}