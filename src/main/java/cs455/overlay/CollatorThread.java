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

            if (type == 1){
                // nodes come and register their ip and port
                MessageRegister regMsg = new MessageRegister(clientDIS.readAllBytes());
                collator.addNode(regMsg.hostName, regMsg.portNumber);
            } else if(type == 4) {
                // node sent signal it has completed its rounds.
                MessageDoneSending doneMsg = new MessageDoneSending(clientDIS.readAllBytes());
                this.collator.updateDoneSending();
            } else if(type == 6) {
                // node sent summary to collator
                MessageSummary summaryMsg = new MessageSummary(clientDIS.readAllBytes());
                this.collator.addSummary(summaryMsg.summary);
            } else if (type == 7) {
                // collator receives close signal.
                this.clientDIS.readAllBytes();
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
