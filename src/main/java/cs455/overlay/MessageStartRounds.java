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
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(byteArrayInputStream));

        this.numRounds = din.readInt();
        this.numMessages = din.readInt();
        this.numConnectedNodes = din.readInt();

        this.hostnames = new ArrayList<>(0);
        this.ports = new ArrayList<>(0);
        for (int i = 0; i < this.numConnectedNodes; i++) {
            int hostNameLength = din.readInt();
            byte[] hostNameBytes = new byte[hostNameLength];
            din.readFully(hostNameBytes);
            String hn = new String(hostNameBytes);
            int p = din.readInt();
            this.hostnames.add(hn);
            this.ports.add(p);
        }

        byteArrayInputStream.close();
        din.close();
    }

    public byte[] getBytes() throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream((byteArrayOutputStream)));

        dout.writeInt(MessageStartRounds.TYPE);
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
        System.out.printf("Rounds: %d, Messages: %d, Nodes: %d\n", this.numRounds, this.numMessages, this.numConnectedNodes);
        this.hostnames.forEach(System.out::println);
        this.ports.forEach(System.out::println);
    }

}
