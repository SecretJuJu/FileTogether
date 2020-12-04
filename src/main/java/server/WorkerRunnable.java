package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**

 */
public class WorkerRunnable implements Runnable{
    private final int SCANBIT = 1;
    private final int SUCCSSESSBIT = 1;
    private final int FAILBIT = 0;
    private final int DOWNLOADBIT = 2;
    private final int COMMANDPORT = 6000;
    private final int DATAPORT = 6060;
    private String filePath = null;
    private String filename = null;
    protected Socket clientSocket = null;
    protected String serverText   = null;

    public WorkerRunnable(Socket clientSocket, String filePath) {
        this.clientSocket = clientSocket;
        this.filePath   = filePath;
        this.filePath = this.filePath.replace("\\","/");
        String[] bits = filePath.split("/");
        this. filename = bits[bits.length-1];
    }

    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            System.out.println("[ " + clientSocket.getInetAddress() + " ] client connected");
            in = clientSocket.getInputStream();
            int data = in.read();
            System.out.println(data);
            if (data == SCANBIT) {
                // send True data
                System.out.println("SCANBIT");
                clientSocket.getOutputStream().write(SUCCSSESSBIT);
            } else if (data == DOWNLOADBIT) {
                if (this.filePath != null){
                    System.out.println("DOWNLOAD BIT");
                    clientSocket.getOutputStream().write(SUCCSSESSBIT);

                    this.filePath   = filePath;
                    String[] bits = filePath.split("/");
                    this. filename = bits[bits.length-1];
                    OutputStream ous = clientSocket.getOutputStream();
                    DataOutputStream dous = new DataOutputStream(ous);
                    dous.writeUTF(filename);
                    dous.flush();
                    dous.close();


                    System.out.println("FILE TRANSFERING");
                    dataTransfer(clientSocket.getInetAddress().toString().replace("/",""));
                } else {
                    System.out.println("DOWNLOAD FAILED");
                    clientSocket.getOutputStream().write(FAILBIT);
                }
            }else {
                clientSocket.getOutputStream().write(FAILBIT);
            }
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }


    private void dataTransfer(String ip) throws IOException {
        try {
            Socket socket = null;

            socket = new Socket(ip, DATAPORT);

            File file = new File(this.filePath);
            // Get the size of the file
            long length = file.length();
            byte[] bytes = new byte[16 * 1024];
            InputStream in = new FileInputStream(file);
            OutputStream out = socket.getOutputStream();

            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }

            out.close();
            in.close();
            socket.close();
        }catch (Exception e) {
            System.out.println(e);
        }
    }
}