package main;
import java.util.ArrayList;
import java.util.Scanner;
import client.Client;
import server.Server;
import javax.swing.*;
import java.awt.*;


public class testMain {

    public static void main(String[] args) {

        Client c = new Client();
        ArrayList<Client.InterfaceData> interfaces = c.getInterfaces();
        String myIp = null;
        interfaces.forEach(item -> {
            if(item.getNicName().equals("en0")){
                System.out.println("set my interface");
                c.setMyInterface(item);
            }
        });

        if(c.download("192.168.43.165")){
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAIL");
        }
    }
}
