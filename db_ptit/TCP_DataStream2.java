package db_ptit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class TCP_DataStream2 {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("36.50.135.242", 2207);
        socket.setSoTimeout(5000);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        // a. gửi chuỗi "B23DCCN199;ZfSpbgOZ" đến server
        dos.writeUTF("B23DCCN199;ZfSpbgOZ");

        // b. Nhận lần lượt chuỗi đã bị mã hoá caesar và giá trị dịch chuyển s nguyên
        String str = dis.readUTF();
        int s = dis.readInt();

        // c. Giải mã chuỗi và gửi lên server
        StringBuilder decodedStr = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                decodedStr.append((char) ((c - 'A' - s + 26) % 26 + 'A'));
            } else {
                decodedStr.append(c);
            }
        }

        dos.writeUTF(decodedStr.toString());
        
        socket.close();
    }
}
