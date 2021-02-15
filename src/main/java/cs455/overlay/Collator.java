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
    private final ArrayList<String> nodeHosts;
    private final ArrayList<Integer> nodePorts;
    private ArrayList<String> messageSummaries;
    private int numConnectedNodes = 0;
    private boolean startedMessaging = false;
    private final int numNodes;
    private final int numRounds;
    private final int numMessages;
    private boolean running = true;
    private int nodeDoneSending = 0;

    Collator(String hn, int p, int nn, int nr, int nm){
        this.hostname = hn;
        this.port = p;
        this.nodeHosts = new ArrayList<>(0);
        this.nodePorts = new ArrayList<>(0);
        this.messageSummaries = new ArrayList<>(0);
        this.numNodes = nn;
        this.numRounds = nr;
        this.numMessages = nm;
    }

    private void stop (){
        this.running = false;
    }

    private void sendStartMessageToNode(String hostname, Integer port, MessageStartRounds message) throws IOException {
        Socket socket = new Socket(hostname, port);
        DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
        byte[] marshalledMsg = message.getBytes();
        outStream.write(marshalledMsg);
        outStream.flush();
        outStream.close();
        socket.close();
    }

    private void sendStopToNodes() throws IOException {

    }

    public void addSummary(String summary) throws IOException {
        this.messageSummaries.add(summary);

        if(this.messageSummaries.size() == this.numConnectedNodes) {
            this.stop();
            this.sendStopToNodes();
        }
    }

    public void addNode(String hostname, int port) throws IOException {
        this.nodeHosts.add(hostname);
        this.nodePorts.add(port);
        this.numConnectedNodes++;

        if(this.numConnectedNodes == this.numNodes){
            //send start messages to all nodes
            this.startedMessaging = true;
            for (int i = 0; i < this.numNodes; i++) {
                ArrayList<String> tempNodes = new ArrayList<>(0);
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

                this.sendStartMessageToNode(selectedNodeHN, selectedNodeP, startMsg);
            }
        }

    }

    public void updateDoneSending() throws IOException {
        this.nodeDoneSending++;
        if(this.nodeDoneSending == this.numConnectedNodes){
            System.out.println("all nodes completed sending, now asking for summaries");

            for (int i = 0; i < this.numConnectedNodes; i++) {
                Socket socket = new Socket(this.nodeHosts.get(i), this.nodePorts.get(i));
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                MessageSendSummary summaryMsg = new MessageSendSummary();
                byte[] marshalledMsg = summaryMsg.getBytes();
                outputStream.flush();
                outputStream.close();
                socket.close();
            }
        }
    }

    public void runCollator() throws IOException {
        //start collator server

        ServerSocket serverSocket = new ServerSocket(this.port,100);
        System.out.println("created serverSocket");

        while (this.running) {
            Socket clientSocket;

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
