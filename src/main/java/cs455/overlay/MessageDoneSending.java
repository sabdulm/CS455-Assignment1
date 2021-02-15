package cs455.overlay;

import java.io.*;

public class MessageDoneSending {
    public static final int TYPE = 4;
    public String hostname;
    public int port;

    public MessageDoneSending(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    public MessageDoneSending(byte[] marshalledMsg) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(marshalledMsg);
        DataInputStream din = new DataInputStream(new BufferedInputStream(byteArrayInputStream));

        int hostNameLength = din.readInt();
        byte[] hostNameBytes = new byte[hostNameLength];
        din.readFully(hostNameBytes);
        String hn = new String(hostNameBytes);
        int p = din.readInt();
        byteArrayInputStream.close();
        din.close();
        this.hostname = hn; this.port = p;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(byteArrayOutputStream));

        dout.writeInt(MessageDoneSending.TYPE);
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

    public void printContents() {
        System.out.printf("Node %s %d has completed sending\n", this.hostname, this.port);
    }
}
