package cs455.overlay;

import java.io.*;

public class MessageClose {
    public static int TYPE = 7;
    public boolean toClose = false;

    public MessageClose(){}

    public MessageClose(boolean toClose) {
        this.toClose = toClose;
    }

    public MessageClose(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(byteArrayInputStream));

        this.toClose = din.readBoolean();

        byteArrayInputStream.close();
        din.close();
    }


    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(byteArrayOutputStream));

        dout.writeInt(MessageClose.TYPE);
        dout.writeBoolean(this.toClose);
        dout.flush();

        byte[] marshalledBytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        dout.close();

        return marshalledBytes;
    }
}
