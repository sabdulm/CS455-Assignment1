package cs455.overlay;

import java.io.*;

public class MessagePayload {
    public static final int TYPE = 3;
    public int numRound;
    public int port;
    public int numMessage;
    public String hostname;
    public long payload;

    public MessagePayload(int numRound, int numMessage, long payload, int port, String hostname){
        this.hostname = hostname; this.port = port;
        this.numRound = numRound; this.numMessage = numMessage; this.payload = payload;
    }

    public MessagePayload(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(byteArrayInputStream));

        this.numRound = din.readInt();
        this.numMessage = din.readInt();
        this.payload = din.readLong();
        int hostNameLength = din.readInt();
        byte[] hostNameBytes = new byte[hostNameLength];
        din.readFully(hostNameBytes);
        this.hostname = new String(hostNameBytes);
        this.port = din.readInt();

        byteArrayInputStream.close();
        din.close();
    }

    public byte[] getBytes() throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream((byteArrayOutputStream)));

        dout.writeInt(MessagePayload.TYPE);
        dout.writeInt(this.numRound);
        dout.writeInt(this.numMessage);
        dout.writeLong(this.payload);

        byte[] hostnameBytes = this.hostname.getBytes();
        int hostnameLength = hostnameBytes.length;
        dout.writeInt(hostnameLength);
        dout.write(hostnameBytes);
        dout.writeInt(this.port);

        dout.flush();

        byte[] marshalledBytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        dout.close();

        return marshalledBytes;
    }

    public void printContents(){
        System.out.println("printing payload message");
        System.out.printf("received msg from %s %d\n", this.hostname, this.port);
        System.out.printf("Round: %d, Message: %d, payload: %d\n", this.numRound, this.numMessage, this.payload);
    }
}
