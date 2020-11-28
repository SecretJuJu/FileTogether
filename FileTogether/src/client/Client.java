package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public void client() {
		String hostname = "localhost";
		final int PORT = 6000;
		for (int i = 0; i < 10; i++) {
			try (Socket socket = new Socket(hostname, PORT)) {
				OutputStream out = socket.getOutputStream();
				String realStr = "This is woolbro dev Test";
				out.write(realStr.getBytes());
				InputStream input = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				String time = reader.readLine();
				System.out.println(time);
			} catch(UnknownHostException ex) {
				System.out.println("Server not found: " + ex.getMessage());
			} catch(IOException ex) {
				System.out.println("I/O error: " + ex.getMessage());
			}
		}
	}
}
