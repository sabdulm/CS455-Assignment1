package cs455.overlay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Node {
    private final String hostname;
    private final String collatorHostname;
    private final int port;
    private final int collatorPort;
    private boolean running = true;
    private long totalSentMessages, totalReceivedMessages, totalSentSum, totalReceivedSum;


    Node(String hn, int p, String chn, int cp){
        this.hostname = hn; this.port = p;
        this.collatorHostname = chn; this.collatorPort = cp;
    }

    public void addReceivedSum(long payload) {
        this.totalReceivedSum += payload;
        this.totalReceivedMessages++;
    }

    private void sendMessageToNode(String hostname, int port, MessagePayload message) throws IOException {
        Socket socket = new Socket(hostname, port);
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        byte[] marshalledMsg = message.getBytes();
        outputStream.write(marshalledMsg);
        outputStream.flush();
        outputStream.close();
        socket.close();
    }

    private void sendCollatorDone() throws IOException {
        Socket collatorSocket = new Socket(this.collatorHostname, this.collatorPort);
        DataOutputStream collatorOutput = new DataOutputStream(collatorSocket.getOutputStream());
        MessageDoneSending message = new MessageDoneSending(this.hostname, this.port);
        byte[] marshalledMsg = message.getBytes();
        collatorOutput.write(marshalledMsg);
        collatorOutput.flush();
        collatorOutput.close();
        collatorSocket.close();
    }

    public void startSendingMessages(MessageStartRounds msg) throws IOException {
        ArrayList<String> nodeHosts = msg.hostnames;
        ArrayList<Integer> nodePorts = msg.ports;
        for (int i = 0; i < msg.numRounds; i++) {
            Random randomizer = new Random();
            int index = randomizer.nextInt(msg.numConnectedNodes);
            String hostname = nodeHosts.get(index);
            int port = nodePorts.get(index);
            for (int j = 0; j < msg.numMessages; j++) {
                // send msgs to selected node
                MessagePayload message = new MessagePayload(i, j, randomizer.nextLong(), this.port, this.hostname);
                this.sendMessageToNode(hostname, port, message);
                this.totalSentMessages++;
                this.totalSentSum += message.payload;
            }
        }

        this.sendCollatorDone();

    }

    public void sendSummary() throws IOException {
        String summary = String.format("%s,%d,%d,%d,%d,%d", this.hostname, this.port, this.totalSentMessages, this.totalReceivedMessages, this.totalSentSum, this.totalReceivedSum);

        Socket collatorSocket = new Socket(this.collatorHostname, this.collatorPort);
        DataOutputStream collatorOutput = new DataOutputStream(collatorSocket.getOutputStream());
        MessageSummary message = new MessageSummary(summary);
        byte[] marshalledMsg = message.getBytes();
        collatorOutput.write(marshalledMsg);
        collatorOutput.flush();
        collatorOutput.close();
        collatorSocket.close();

        this.stop();
    }

    public void resendCollatorClose(MessageClose msg) throws IOException {
        Socket collatorSocket = new Socket(this.collatorHostname, this.collatorPort);
        DataOutputStream collatorOutput = new DataOutputStream(collatorSocket.getOutputStream());
        byte[] marshalledMsg = msg.getBytes();
        collatorOutput.write(marshalledMsg);
        collatorOutput.flush();
        collatorOutput.close();
        collatorSocket.close();
    }

    private void stop(){
        this.running = false;
    }

    public void runNode() throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.port,100);

        Socket collatorSocket = new Socket(this.collatorHostname, this.collatorPort);
        DataOutputStream outCollator = new DataOutputStream(collatorSocket.getOutputStream());
        MessageRegister regMsg = new MessageRegister(this.hostname, this.port);
        byte[] marshalledMsg = regMsg.getBytes();
        outCollator.write(marshalledMsg);
        outCollator.flush();
        outCollator.close();
        collatorSocket.close();

        while(this.running){
            Socket clientSocket;

            try {
                clientSocket = serverSocket.accept();
                DataInputStream clientDIS = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream clientDOS = new DataOutputStream(clientSocket.getOutputStream());
                Thread clientHandler = new NodeThread(clientSocket, clientDIS, clientDOS, this);
                clientHandler.start();

            } catch (Exception e){
                e.printStackTrace();
            }
        }


        serverSocket.close();
        System.out.println("Node has finished processing. Shutting down.");
    }
}
