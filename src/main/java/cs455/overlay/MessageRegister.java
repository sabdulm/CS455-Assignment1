package cs455.overlay;

import java.io.*;

public class MessageRegister {

	public static final int TYPE = 1;
	public String hostName;
	public int portNumber;

	public MessageRegister(String hostName, int portNumber){
		this.hostName = hostName; this.portNumber = portNumber;
	}

	public MessageRegister(byte[] marshalledBytes) throws IOException{
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(marshalledBytes);
		DataInputStream din = new DataInputStream(new BufferedInputStream(byteArrayInputStream));
		int hostNameLength = din.readInt();
		byte[] hostNameBytes = new byte[hostNameLength];
		din.readFully(hostNameBytes);
		String hn = new String(hostNameBytes);
		int p = din.readInt();
		byteArrayInputStream.close();
		din.close();
		this.hostName = hn; this.portNumber = p;
	}

	public byte[] getBytes() throws IOException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream((byteArrayOutputStream)));

		dout.writeInt(MessageRegister.TYPE);
		byte[] hostnameBytes = this.hostName.getBytes();
		int hostnameLength = hostnameBytes.length;
		dout.writeInt(hostnameLength);
		dout.write(hostnameBytes);
		dout.writeInt(this.portNumber);
		dout.flush();

		byte[] marshalledBytes = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();
		dout.close();

		return marshalledBytes;
	}

	public void printContents(){
		System.out.printf("Register %s with port %d\n", this.hostName, this.portNumber);
	}

}
