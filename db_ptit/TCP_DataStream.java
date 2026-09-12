package db_ptit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class TCP_DataStream {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("36.50.135.242", 2207);

        socket.setSoTimeout(5000);

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF("B23DCCN199;VU9Ing5Z");
        dos.flush();

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        int a = dis.readInt();
        int b = dis.readInt();

        dos.writeInt(a + b);
        dos.writeInt(a * b);


        socket.close();
    }
}
