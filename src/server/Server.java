package server;




import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;


public class Server {
    private final int SCANBIT = 1;
    private final int SUCCSSESSBIT = 1;
    private final int FAILBIT = 0;

    public void makeServer() {
        final int PORT = 6000;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // get request, & response
            System.out.println("Server is listening on PORT " + PORT);
//            JSONParser jsonParser = new JSONParser();
            InputStream in = null;

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("[ " + socket.getInetAddress() + " ] client connected");

//                InputStream inputStream = socket.getInputStream();
//                DataInputStream dataInputStream = new DataInputStream(inputStream);
//                String data = dataInputStream.readUTF();

                try{
                    in = socket.getInputStream();
                    int data = in.read();
                    System.out.println(data);
                    if (data == SCANBIT) {
                        // send True data
                        socket.getOutputStream().write(SUCCSSESSBIT);
                    }else {
                        socket.getOutputStream().write(FAILBIT);
                    }

                } catch (IOException e) {
                    System.out.println("IO exception");
                }


            }

        } catch(IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }

    }
}
