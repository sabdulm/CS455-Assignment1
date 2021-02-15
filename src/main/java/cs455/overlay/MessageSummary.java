package cs455.overlay;

import java.io.*;

public class MessageSummary {
    public static int TYPE = 6;
    public String summary;

    MessageSummary(String summary){
        this.summary = summary;
    }

    MessageSummary(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(byteArrayInputStream));
        int summaryLength = din.readInt();
        byte[] summaryBytes = new byte[summaryLength];
        din.readFully(summaryBytes);
        this.summary = new String(summaryBytes);
        byteArrayInputStream.close();
        din.close();
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(byteArrayOutputStream));

        dout.writeInt(MessageSummary.TYPE);
        byte[] summaryBytes = this.summary.getBytes();
        int summaryLength = summaryBytes.length;
        dout.writeInt(summaryLength);
        dout.write(summaryBytes);

        dout.flush();

        byte[] marshalledBytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        dout.close();

        return marshalledBytes;
    }
}
