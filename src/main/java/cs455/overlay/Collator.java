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
    private ArrayList<String> messageSummaries;
    private int numConnectedNodes = 0;
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

    public void stop (){
        this.running = false;
    }

    public void addNode(String hostname, int port){
        this.nodeHosts.add(hostname);
        this.nodePorts.add(port);
        this.numConnectedNodes++;

        if(this.numConnectedNodes == this.numNodes){
            //send start messages to all nodes
            for (int i = 0; i < this.numNodes; i++) {
                ArrayList<String> tempNodes = new ArrayList<String>(0);
                ArrayList<Integer> tempPorts = new ArrayList<>(0);
                String selectedNodeHN = this.nodeHosts.get(i);
                Integer selectedNodeP = this.nodePorts.get(i);
                for (int j = 0; j < this.numNodes; j++) {
                    String tnhn = this.nodeHosts.get(j);
                    Integer tnp = this.nodePorts.get(j);
                    if(tnhn != selectedNodeHN && tnp != selectedNodeP){
                        tempNodes.add(tnhn);
                        tempPorts.add(tnp);
                    }
                }
                MessageStartRounds startMsg = new MessageStartRounds(tempNodes, tempPorts, this.numRounds, this.numMessages, this.numConnectedNodes-1);

            }
        }

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
                e.printStackTrace();
            }

        }
        serverSocket.close();
        messageSummaries.forEach(System.out::println);
        System.out.println("Program ended successfully");
    }

}
