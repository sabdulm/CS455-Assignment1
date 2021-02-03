package cs455.overlay;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Collator {
    final private String hostname;
    final private int port;
    ArrayList<String> nodeHosts;
    ArrayList<Integer> nodePorts;

    Collator(String hn, int p){
        this.hostname = hn; this.port = p;
        this.nodeHosts = new ArrayList<String>(0);
        this.nodePorts = new ArrayList<Integer>(0);
    }

    public void runCollator() throws IOException {
        //start collator server

        ServerSocket serverSocket = new ServerSocket(this.port,100);
        System.out.println("created serverSocket");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("connection accepted");
            String clientHostname = clientSocket.getInetAddress().getHostName();
            int clientPort = clientSocket.getPort();

            System.out.printf("Client connected from %s : %o%n", clientHostname, clientPort);

            clientSocket.close();
            break;
        }
        serverSocket.close();
    }

}
