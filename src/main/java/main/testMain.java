package main;
import java.util.ArrayList;
import java.util.Scanner;
import client.Client;
import server.Server;
import javax.swing.*;
import java.awt.*;


public class testMain {

    public static void main(String[] args) {
        ArrayList<String> serverList = new ArrayList<String>();
        JFrame f = new JFrame("FileTogether");
        Container cont = f.getContentPane();
        cont.setLayout(new BorderLayout());

        Scanner sc = new Scanner(System.in);
        System.out.print("input : ");
        String str = sc.next();



        if (str.equals("s")) {
            Server s = new Server();
            System.out.println("--- server started ---");
            s.setFilePath("/Users/secret/test2");
            System.out.println(s.getFilePath());
            new Thread(s).start();
        }else {
            Client c = new Client();
            serverList = c.scan();
            ArrayList<Client.InterfaceData> interfaces = c.getInterfaces();
            String myIp = null;
            interfaces.forEach(item -> {
                if(item.getNicName().equals("en0")){
                    System.out.println("set my interface");
                    c.setMyInterface(item);
                }
            });
            if(c.Download("192.168.43.56")){
                System.out.println("SUCCESS");
            } else {
                System.out.println("FAIL");
            }

//            System.out.println(c.getMyInterface().getIpAddress());

//            c.scan().forEach(item -> {
//                System.out.println(item);
//            });
        }

        f.setSize(600, 400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }
}
