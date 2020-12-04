package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class Client{
    final int COMMADNPORT = 6000;
    final int DATAPORT = 6060;
    private final int SCANBIT = 1;
    private final int SUCCSSESSBIT = 1;
    private final int DOWNLOADBIT = 2;
    private final int FAILBIT = 0;
    String downloadFolder = System.getProperty("user.home")+"/FileTogetherDownload";
    static boolean contains(String pattern, String text, Integer fromIndex){
        if(fromIndex != null && fromIndex < text.length())
            return Pattern.compile(pattern).matcher(text).find();
        return Pattern.compile(pattern).matcher(text).find();
    }
//    System.out.println(downloadFolder);
    private InterfaceData myInterface = null;
    public String[] scan() {
        InterfaceData ifd = null;
        if ( this.getMyInterface() == null ){
            System.out.println("no interface setted");
            return null; // not setted the interface
        } else {
            ifd = getMyInterface();
        }

        ArrayList<String> serverList = new ArrayList<String>();

        SubnetUtils utils = new SubnetUtils(ifd.getIpAddress()+"/"+ifd.getPrefix());
        String[] allIps = utils.getInfo().getAllAddresses();
        final ExecutorService es = Executors.newFixedThreadPool(255);
        final int timeout = 2000;
        final List<Future<String>> futures = new ArrayList<>();

        for (String ip: allIps
        ) {
            futures.add(Ping(es, ip, COMMADNPORT, timeout,SCANBIT,SUCCSSESSBIT,FAILBIT));
        }
        es.shutdown();
        int openPorts = 0;

        for (final Future<String> scannedIp : futures) {
            try{
                String tmp = scannedIp.get();
                if(tmp != null){
                    System.out.println(tmp);
                    serverList.add(tmp);
                }
            }catch (Exception e){
                System.out.println("error");
            }
        }
        System.out.println("scan end");
        return serverList.toArray(new String[serverList .size()]);

    }
    public ArrayList<InterfaceData> getInterfaces(){

        ArrayList<InterfaceData> interfaceList = new ArrayList<InterfaceData>();
        String myip = null;
        try {


            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();

            while(en.hasMoreElements()){
                NetworkInterface ni = en.nextElement();
                InterfaceData data = new InterfaceData();
                data.setNicName(ni.getName());
                List<InterfaceAddress> list = ni.getInterfaceAddresses();
                Iterator<InterfaceAddress> it = list.iterator();

                while(it.hasNext()){
                    InterfaceAddress ia = it.next();
                    if(ia.getAddress().toString().contains(":")) {
                        continue;
                    }

                    data.setIpAddress(ia.getAddress().toString().replace("/",""));
                    data.setPrefix(ia.getNetworkPrefixLength());
                }

                interfaceList.add(data);
            }
        } catch (Exception e) {
            System.out.println("cannot get interfaces");
        }

        return interfaceList;
    }
    public Boolean download(String ip){
        try{
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, COMMADNPORT),1000);

            socket.getOutputStream().write(DOWNLOADBIT);
            int res = socket.getInputStream().read();
            if(res == SUCCSSESSBIT){
                System.out.println("File Transfer start");

                InputStream ins = socket.getInputStream(); //inputstream 과 연결되었
                DataInputStream dins = new DataInputStream(ins);

                String filename= dins.readUTF();
                dins.close();
                
                makeDataTranserfer(filename);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            System.out.println("server blocked");
            return false;
        }
    }
    // Getter Setter

    public class InterfaceData {
        private String nicName = "";
        private String ipAddress = "";
        private short prefix = 0;
        public String getNicName(){
            return this.nicName;
        }
        public void setNicName(String nicName){
            this.nicName = nicName;
        }
        public String getIpAddress(){
            return this.ipAddress;
        }
        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }
        public short getPrefix() {
            return prefix;
        }
        public void setPrefix(short prefix) {
            this.prefix = prefix;
        }

    }
    public static Future<String> Ping(final ExecutorService es, final String ip, final int port, final int timeout,int SCANBIT,int SUCCSSESSBIT,int FAILBIT) {
        return es.submit(new Callable<String>() {
            @Override public String call() {
                try {
                    System.out.println(ip);
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(ip, port), timeout);

                    System.out.println("scanning");
                    DataOutputStream out;
                    out = new DataOutputStream(socket.getOutputStream());
                    out.write(SCANBIT);

                    try {
                        int data = socket.getInputStream().read();
                        if (data == SUCCSSESSBIT) {
                            socket.close();
                            return ip;
                        } else if (data == FAILBIT) {
                            return null;
                        } else {
                            return null;
                        }
                    } catch (IOException e) {
                        return null;
                    }
                } catch (Exception ex) {
                    return null;
                }
            }
        });
    }
    private void makeDataTranserfer(String filename) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(DATAPORT);
        } catch (IOException ex) {
            System.out.println("Can't setup server on this port number. ");
        }

        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;

        try {
            socket = serverSocket.accept();
            System.out.println("transfer socket accepted");
        } catch (IOException ex) {
            System.out.println("Can't accept client connection. ");
        }

        try {
            in = socket.getInputStream();
        } catch (IOException ex) {
            System.out.println("Can't get socket input stream. ");
        }

        try {
            out = new FileOutputStream(downloadFolder+"/"+filename);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. ");
        }

        byte[] bytes = new byte[16*1024];

        int count;
        while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
        }
        try {
            out.close();
            in.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("error on closing streams");
        }
    }



    public InterfaceData getMyInterface(){
        return this.myInterface;
    }
    public void setMyInterface(InterfaceData data) {
        this.myInterface = data;
    }


}