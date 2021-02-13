package cs455.overlay;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessageRegister {

    public static final int TYPE = 1;
    private String hostName;
    private int portNumber;

    public MessageRegister(String hostName, int portNumber){
        this.hostName = hostName; this.portNumber = portNumber;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream((byteArrayOutputStream)));

        dout.writeInt(this.TYPE);
        byte[] hostnameBytes = this.hostName.getBytes();
        int hostNamelength = hostnameBytes.length;
        dout.writeInt(hostNamelength);
        dout.write(hostnameBytes);
        dout.writeInt(this.portNumber);
        dout.flush();

        byte[] marshalledBytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        dout.close();

        return marshalledBytes;
    }

    public static MessageRegister getInstance(byte[] marshalledBytes) throws IOException {
        return new MessageRegister("", 0);
    }

}
