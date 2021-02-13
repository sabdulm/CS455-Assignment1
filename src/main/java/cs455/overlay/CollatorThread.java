package cs455.overlay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CollatorThread extends Thread{
    private DataInputStream clientDIS;
    private DataOutputStream clientDOS;
    private Socket clientSocket;
    private Collator collator;

    public CollatorThread(Socket s, DataInputStream dis, DataOutputStream dos, Collator c){
        this.clientSocket = s; this.clientDIS = dis; this.clientDOS = dos;
        this.collator = c;
    }

    @Override
    public void run() {
        int type = 0;
        try {
            type = clientDIS.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (type == 1){ // nodes come and register their ip and port
            try {
                MessageRegister regMsg = MessageRegister.getInstance(clientDIS.readAllBytes());

            } catch (IOException e) {
                e.printStackTrace();
                try {
                    clientDIS.close();
                    clientDOS.close();
                    clientSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        } else if (type == 2) { // collator sends start signal to nodes

        } else if (type == 3) {  // message received from other node with number

        } else if(type == 4) { // received summary of messages from node
            collator.stop();
        }
    }
}
