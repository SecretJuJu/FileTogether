package server;




import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;


public class Server implements Runnable{
    private final int SCANBIT = 1;
    private final int SUCCSSESSBIT = 1;
    private final int FAILBIT = 0;
    private final int DOWNLOADBIT = 2;
    private final int COMMANDPORT = 6000;
    private final int DATAPORT = 6060;
    private String filePath = null;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = true;
    protected Thread       runningThread= null;


    @Override
    public void run() {
        this.isStopped = false;
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            new Thread(
                    new WorkerRunnable(
                            clientSocket, this.filePath)
            ).start();
        }
        System.out.println("Server Stopped.") ;
    }
    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.COMMANDPORT);
        } catch (IOException e) {
            System.out.println("error");
        }
    }


    public boolean getIsStppped() {return isStopped;}
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }
    public String getFilePath(){
        return filePath;
    }
}
