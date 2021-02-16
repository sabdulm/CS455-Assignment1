package cs455.overlay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NodeThread extends Thread{
    private final DataInputStream clientDIS;
    private final DataOutputStream clientDOS;
    private final Socket clientSocket;
    private final Node node;

    public NodeThread(Socket s, DataInputStream dis, DataOutputStream dos, Node n){
        this.clientSocket = s; this.clientDIS = dis; this.clientDOS = dos;
        this.node = n;
    }

    @Override
    public void run() {
        int type;
        try {
            type = clientDIS.readInt();

            if (type == 2) {
                // collator sends start signal to nodes
                MessageStartRounds startMsg = new MessageStartRounds(clientDIS.readAllBytes());
                this.node.startSendingMessages(startMsg);
            } else if (type == 3) {
                // message received from other node with payload
                MessagePayload payloadMsg = new MessagePayload(clientDIS.readAllBytes());
                this.node.addReceivedSum(payloadMsg.payload);
            } else if(type == 5) {
                // collator asks for summary
                this.node.sendSummary();
            } else if (type == 7) {
                //collator sends close signal;
                MessageClose closeMsg = new MessageClose(clientDIS.readAllBytes());
                if(closeMsg.toClose){
                    this.node.resendCollatorClose(closeMsg);
                }
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
