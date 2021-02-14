package cs455.overlay;

import java.io.*;
import java.util.ArrayList;

public class MessageStartRounds {
    public static final int TYPE = 2;
    public ArrayList<String> hostnames;
    public ArrayList<Integer> ports;
    public int numRounds, numMessages, numConnectedNodes;

    public MessageStartRounds(ArrayList<String> hns, ArrayList<Integer> ps, int nr, int nm, int cn){
        this.hostnames = hns; this.ports = ps;
        this.numRounds = nr; this.numMessages = nm; this.numConnectedNodes = cn;
    }

    public MessageStartRounds(byte[] marshalledBytes) throws IOException{
    }

    public byte[] getBytes() throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream((byteArrayOutputStream)));

        dout.writeInt(this.TYPE);
        dout.writeInt(this.numRounds);
        dout.writeInt(this.numMessages);
        dout.writeInt(this.numConnectedNodes);

        for (int i = 0; i < this.numConnectedNodes; i++) {
            byte[] hostnameBytes = this.hostnames.get(i).getBytes();
            int hostnameLength = hostnameBytes.length;
            dout.writeInt(hostnameLength);
            dout.write(hostnameBytes);
            dout.writeInt(this.ports.get(i));
        }
        dout.flush();

        byte[] marshalledBytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        dout.close();

        return marshalledBytes;
    }

    public void printContents(){
        System.out.println("printing start rounds message");
    }

}
