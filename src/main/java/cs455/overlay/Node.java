package cs455.overlay;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Node {
    private String hostname, collatorHostname;
    private int port, collatorPort;
    private ArrayList<String> nodeHosts;
    private ArrayList<Integer> nodePorts;

    Node(String hn, int p, String chn, int cp){
        this.hostname = hn; this.port = p;
        this.collatorHostname = chn; this.collatorPort = cp;
    }

    public void runNode() throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.port,100);

        Socket collatorSocket = new Socket(collatorHostname, collatorPort);
        DataOutputStream outCollator = new DataOutputStream(collatorSocket.getOutputStream());
        MessageRegister regMsg = new MessageRegister(this.hostname, this.port);
        byte[] marshalledMsg = regMsg.getBytes();
        outCollator.write(marshalledMsg);
        outCollator.flush();
        

    }
}
