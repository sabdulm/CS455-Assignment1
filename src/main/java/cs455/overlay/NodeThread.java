package cs455.overlay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NodeThread extends Thread{
    private DataInputStream clientDIS;
    private DataOutputStream clientDOS;
    private Socket clientSocket;
    private Node node;

    public NodeThread(Socket s, DataInputStream dis, DataOutputStream dos, Node n){
        this.clientSocket = s; this.clientDIS = dis; this.clientDOS = dos;
        this.node = n;
    }

    @Override
    public void run() {
        int type;
        try {
            type = clientDIS.readInt();
            System.out.println(type);
            if (type == 1){ // nodes come and register their ip and port
//                try {
//                    MessageRegister regMsg = MessageRegister.getInstance(clientDIS.readAllBytes());
//                    node.addNode(regMsg.hostName, regMsg.portNumber);
//                } catch (IOException e) { e.printStackTrace();}
            } else if (type == 2) { // collator sends start signal to nodes

            } else if (type == 3) {  // message received from other node with number

            } else if(type == 4) { // received summary of messages from node
                node.stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientDIS.close();
                clientDOS.close();
                clientSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }
}