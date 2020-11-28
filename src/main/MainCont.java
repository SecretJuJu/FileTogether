package main;

import java.util.Scanner;

import client.Client;
import server.Server;

public class MainCont {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("input : ");
		String str = sc.next();
		
		if (str.equals("s")) {
			Server s = new Server();
			s.server();
		}else {
			Client c = new Client();
			c.client();
		}
	}
}
