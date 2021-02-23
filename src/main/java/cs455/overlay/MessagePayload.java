package cs455.overlay;

import java.io.*;

public class MessagePayload {
    public static final int TYPE = 3;
    public int numRound;
    public int toPort, fromPort;
    public int numMessage;
    public String toHostname, fromHostname;
    public long payload;

    public MessagePayload(int numRound, int numMessage, long payload, int fp, String fh, int tp, String th){
        this.fromHostname = fh; this.fromPort = fp;
        this.toHostname = th; this.toPort = tp;
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
        this.fromHostname = new String(hostNameBytes);
        this.fromPort = din.readInt();

        hostNameLength = din.readInt();
        hostNameBytes = new byte[hostNameLength];
        din.readFully(hostNameBytes);
        this.toHostname = new String(hostNameBytes);
        this.toPort = din.readInt();

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

        byte[] hostnameBytes = this.fromHostname.getBytes();
        int hostnameLength = hostnameBytes.length;
        dout.writeInt(hostnameLength);
        dout.write(hostnameBytes);
        dout.writeInt(this.fromPort);

        hostnameBytes = this.toHostname.getBytes();
        hostnameLength = hostnameBytes.length;
        dout.writeInt(hostnameLength);
        dout.write(hostnameBytes);
        dout.writeInt(this.toPort);
        

        dout.flush();

        byte[] marshalledBytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        dout.close();

        return marshalledBytes;
    }

    public void printContents(){
        System.out.printf("From %s %d to %s %d\nRound: %d, Message: %d, payload: %d\n", this.fromHostname, this.fromPort, this.toHostname, this.toPort, this.numRound, this.numMessage, this.payload);
    }
}
