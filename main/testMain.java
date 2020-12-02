package main;
import java.util.ArrayList;
import java.util.Scanner;
import client.Client;
import server.Server;
import org.apache.commons.net.util.*;

public class testMain {

    public static void main(String[] args) {
        ArrayList<String> serverList = new ArrayList<String>();

        Scanner sc = new Scanner(System.in);
        System.out.print("input : ");
        String str = sc.next();

        SubnetUtils utils = new SubnetUtils("192.168.1.0/24");
        String[] allIps = utils.getInfo().getAllAddresses();

        if (str.equals("s")) {
            Server s = new Server();
            s.makeServer();
        }else {
            Client c = new Client();
//          serverList = c.scan();
            ArrayList<Client.InterfaceData> interfaces = c.getInterfaces();
            interfaces.forEach(item -> {
                System.out.println("============");
                System.out.println(item.getNicName());
                System.out.println(item.getIpAddress());
                System.out.println(item.getPrefix());
            });

//            System.out.println(serverList);
        }
    }
}
