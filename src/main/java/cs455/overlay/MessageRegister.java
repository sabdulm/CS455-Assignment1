package cs455.overlay;

import java.io.*;

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
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(marshalledBytes);
		DataInputStream din = new DataInputStream(new BufferedInputStream(byteArrayInputStream));
		int hostNameLength = din.readInt();
		byte[] hostNameBytes = new byte[hostNameLength];
		din.readFully(hostNameBytes);
		String hostname = new String(hostNameBytes);
		int port = din.readInt();
		byteArrayInputStream.close();
		din.close();
		return new MessageRegister(hostname, port);
	}

}
