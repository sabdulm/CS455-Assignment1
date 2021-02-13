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
        if (type == 1){
            try {
                MessageRegister regMsg = MessageRegister.getInstance(clientDIS.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
