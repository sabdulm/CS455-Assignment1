package cs455.overlay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CollatorThread extends Thread{
    private final DataInputStream clientDIS;
    private final DataOutputStream clientDOS;
    private final Socket clientSocket;
    private final Collator collator;

    public CollatorThread(Socket s, DataInputStream dis, DataOutputStream dos, Collator c){
        this.clientSocket = s; this.clientDIS = dis; this.clientDOS = dos;
        this.collator = c;
    }

    @Override
    public void run() {
        int type;
        try {
            type = clientDIS.readInt();
            if (type == 1){ // nodes come and register their ip and port
                try {
                    MessageRegister regMsg = new MessageRegister(clientDIS.readAllBytes());
//                    regMsg.printContents();
                    collator.addNode(regMsg.hostName, regMsg.portNumber);
                } catch (IOException e) { e.printStackTrace();}
            } else if (type == 2) { // collator sends start signal to nodes

            } else if (type == 3) {  // message received from other node with number

            } else if(type == 4) { // received summary of messages from node
                collator.stop();
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
