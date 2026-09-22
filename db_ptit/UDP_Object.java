package db_ptit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import UDP.Student;

public class UDP_Object {
    public static void main(String[] args) throws Exception{
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(5000);
        InetAddress serverAddress = InetAddress.getByName("36.50.135.242");
        int port = 2209;

        // a.
        String request = ";B23DCCN199;6EtQP6ZV";
        socket.send(new DatagramPacket(request.getBytes(), request.length(), serverAddress, port));

        // b.
        byte[] buffer = new byte[4096];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(receivePacket);

        // Tach 8 byte dau de lay requestId
        String requestId = new String(buffer, 0, 8);

        // Tach cac byte con lai de lay Object Student
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer, 8, receivePacket.getLength() - 8);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Student student = (Student) ois.readObject();

        // c.
        String[] words = student.getName().trim().toLowerCase().split("\\s+");
        StringBuilder normalizedName = new StringBuilder();
        StringBuilder email = new StringBuilder(words[words.length - 1]);

        for (int i = 0; i < words.length; i++) {
            // Chuan hoa ten: In hoa chu cai dau moi tu
            String word = words[i];
            normalizedName.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");

            // Xay dung email: noi them chu cai dau cua ho va ten dem
            if (i < words.length - 1) {
                email.append(word.charAt(0));
            }
        }

        // Cập nhật lại vào object
        student.setName(normalizedName.toString().trim()); // Nguyen Van Tuan Nam
        student.setEmail(email.toString() + "@ptit.edu.vn"); // namnvt@ptit.edu.vn

        // Đóng gói dữ liệu gửi lên: 8 byte requestId + Object Student
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(requestId.getBytes()); // Viết 8 byte chuỗi vào trước
        
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(student); // Viết tiếp Object vào sau
        oos.flush();

        // Lấy mảng byte tổng hợp và gửi đi
        byte[] sendData = baos.toByteArray();
        socket.send(new DatagramPacket(sendData, sendData.length, serverAddress, port));

        // d. Kết thúc
        socket.close();
    }   
    
}
