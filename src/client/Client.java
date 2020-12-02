package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;



public class Client {
    final int COMMADNPORT = 6000;
    final int DATAPORT = 6060;
    private final int SCANBIT = 1;
    private final int SUCCSSESSBIT = 1;
    private final int FAILBIT = 0;

    private InterfaceData myInterface = null;

    public ArrayList<String> scan() {
        if (!(this.myInterface == null)){

        }
        ArrayList<String> serverList = new ArrayList<String>();
        for (int i = 1; i < 255; i++) {
            try {
                String ip = "192.168.0." + i;
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ip, COMMADNPORT), 10);

                System.out.println("scanning");
                DataOutputStream out;
                out = new DataOutputStream(socket.getOutputStream());
                out.write(SCANBIT);

                try {
                    int data = socket.getInputStream().read();
                    if (data == SUCCSSESSBIT) {
                        serverList.add(ip);
                    } else if (data == FAILBIT) {
                        continue;
                    } else {
                        System.out.println(ip + "\t");
                        System.out.println("no fileTogether program");
                    }
                } catch (IOException e) {
                    System.out.println("server filtered");
                }
                socket.close();
            } catch (UnknownHostException ex) {
                System.out.println("Server not found: " + ex.getMessage());
            } catch (IOException ex) {
                continue;
            }
        }
        return serverList;
    }



    public ArrayList<InterfaceData> getInterfaces(){

        ArrayList<InterfaceData> interfaceList = new ArrayList<InterfaceData>();
        String myip = null;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    short prefix = 0;
                    InterfaceData data = new InterfaceData();
                    InetAddress addr = addresses.nextElement();
                    System.out.println();
                    if( addr.getHostAddress().contains(".") ) {
                        prefix = iface.getInterfaceAddresses().get(1).getNetworkPrefixLength(); // only gathering IPV4
                    } else {
                        continue;
                    }


                    data.setNicName(iface.getDisplayName());
                    data.setIpAddress(addr.getHostAddress());
                    data.setPrefix(prefix);
                    interfaceList.add(data);
                }
            }
        } catch (Exception e) {
            System.out.println("cannot get interfaces");
        }

        return interfaceList;
    }

    // Getter Setter
    public InterfaceData getMyInterface(){
        return this.myInterface;
    }
    public void setMyInterface(InterfaceData data) {
        this.myInterface = data;
    }

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
}
