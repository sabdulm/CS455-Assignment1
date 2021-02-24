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
    private final ArrayList<String> messageSummaries;
    private int numConnectedNodes = 0;
    private final int numNodes;
    private final int numRounds;
    private final int numMessages;
    private boolean running = true;
    private int nodeDoneSending = 0;
    private final Object lock = new Object();

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
        for (int i = 0; i < this.numConnectedNodes; i++) {
            Socket socket = new Socket(this.nodeHosts.get(i), this.nodePorts.get(i));
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            MessageClose closeMsg;
            if (i == this.numConnectedNodes-1) {
                closeMsg = new MessageClose(true);
            } else {
               closeMsg = new MessageClose();
            }
            byte[] marshalledMsg = closeMsg.getBytes();
            outputStream.write(marshalledMsg);
            outputStream.flush();
            outputStream.close();
            socket.close();
        }
    }

    public void addSummary(String summary) throws IOException {
        synchronized(this.lock){
            this.messageSummaries.add(summary);
        }

        if(this.messageSummaries.size() == this.numConnectedNodes) {
            System.out.println("Collator: received all summaries, sending signal to shutdown.");
            this.stop();
            this.sendStopToNodes();
        }
    }

    public void addNode(String hostname, int port) throws IOException {
        System.out.printf("Collator: Node %s %d has registered\n", hostname, port);
        synchronized(this.lock) {
            this.nodeHosts.add(hostname);
            this.nodePorts.add(port);
            this.numConnectedNodes++;
        }

        if(this.numConnectedNodes == this.numNodes){
            //send start messages to all nodes

            System.out.println("Collator: all nodes joined, sending start signal");

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

    public void updateDoneSending() throws IOException, InterruptedException {
        synchronized(this.lock){
            this.nodeDoneSending++;
        }
        
        
        if(this.nodeDoneSending == this.numConnectedNodes){
            System.out.println("Collator: all nodes completed their rounds, now asking for summaries");
            Thread.sleep(2000);
            for (int i = 0; i < this.numConnectedNodes; i++) {
                Socket socket = new Socket(this.nodeHosts.get(i), this.nodePorts.get(i));
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                MessageSendSummary summaryMsg = new MessageSendSummary();
                byte[] marshalledMsg = summaryMsg.getBytes();
                outputStream.write(marshalledMsg);
                outputStream.flush();
                outputStream.close();
                socket.close();
            }
        }
    }

    private void printSummary() {
        long totalSentMsgs = 0, totalRecvMsgs = 0;
        long totalSentSum = 0, totalRecvSum = 0;

        for (String msgSummary : this.messageSummaries) {
            String[] splitSummary = msgSummary.split(",");
            totalSentMsgs += Long.parseLong(splitSummary[2]);
            totalRecvMsgs += Long.parseLong(splitSummary[3]);
            totalSentSum += Long.parseLong(splitSummary[4]);
            totalRecvSum += Long.parseLong(splitSummary[5]);
        }

        System.out.println("Collator: Summary of Messages");
        System.out.println("Hostname, port, Total Sent Msgs, Total Recvd Msgs, Total Sent Summation, Total Recvd Summation");
        this.messageSummaries.forEach(System.out::println);
        System.out.printf("Total of summaries: %d, %d, %d, %d\n", totalSentMsgs, totalRecvMsgs, totalSentSum, totalRecvSum);

    }

    public void runCollator() throws IOException {
        //start collator server

        ServerSocket serverSocket = new ServerSocket(this.port,100);
        System.out.println("Collator: started server, waiting for connections");

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

        this.printSummary();

        System.out.println("Collator: shutting down now");
    }

}
