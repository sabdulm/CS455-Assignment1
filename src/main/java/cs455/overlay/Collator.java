package cs455.overlay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Collator {
    final private String hostname;
    final private int port;
    private ArrayList<String> nodeHosts;
    private ArrayList<Integer> nodePorts;
    private int countConnectedNodes = 0;
    private boolean startedMessaging = false;
    private int numNodes;
    private int numRounds;
    private int numMessages;
    private boolean running = true;

    Collator(String hn, int p, int nn, int nr, int nm){
        this.hostname = hn; this.port = p;
        this.nodeHosts = new ArrayList<String>(0);
        this.nodePorts = new ArrayList<Integer>(0);
        this.numNodes = nn;
        this.numRounds = nr;
        this.numMessages = nm;

    }

    public void runCollator() throws IOException {
        //start collator server

        ServerSocket serverSocket = new ServerSocket(this.port,100);
        System.out.println("created serverSocket");

        while (this.running) {
            Socket clientSocket = null;

            try {
                clientSocket = serverSocket.accept();
                DataInputStream clientDIS = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream clientDOS = new DataOutputStream(clientSocket.getOutputStream());
                Thread clientHandler = new CollatorThread(clientSocket, clientDIS, clientDOS, this);
                clientHandler.start();

            } catch (Exception e){
                clientSocket.close();
            }

        }
        serverSocket.close();
    }

}
