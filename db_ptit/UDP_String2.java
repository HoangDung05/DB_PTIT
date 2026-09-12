package db_ptit;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP_String2 {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(5000);

        InetAddress serverAddress = InetAddress.getByName("36.50.135.242");
        int serverPort = 2208;
        
        // a. Gửi gói tin chứa chuỗi ";B23DCCN199;BgjfL9Ew" đến server
        String message = ";B23DCCN199;BgjfL9Ew";
        byte[] sendData = message.getBytes();
        // Đóng gói bưu kiện gửi đi (Phải dán địa chỉ và port của server vào gói tin)
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
        socket.send(sendPacket);

        // b. Nhận gói tin gửi về
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(receivePacket);
        // Lấy dữ liệu từ gói tin nhận được
        String receivedStr = new String(receivePacket.getData(), 0, receivePacket.getLength());
        String[] parts = receivedStr.split(";");
        String requestId = parts[0];
        String str = parts[1].trim();

        // c. Xử lý chuẩn hoá chuỗi đã nhận theo nguyên tắc ký tự đầu tiên của chuỗi là ký tự in hoa, các ký tự còn lại là ký tự thường
        StringBuilder normalizedStr= new StringBuilder();
        String[] words = str.split("\\s+");
        for (String word : words) {
            if (!word.isEmpty()) {
                normalizedStr.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    normalizedStr.append(word.substring(1).toLowerCase());
                }
                normalizedStr.append(" ");
            }
        }
        // Xóa khoảng trắng thừa ở cuối chuỗi
        if (normalizedStr.length() > 0) {
            normalizedStr.setLength(normalizedStr.length() - 1);
        }

        // Gửi kết quả về server
        String result = requestId + ";" + normalizedStr.toString();
        byte[] resultData = result.getBytes();
        DatagramPacket resultPacket = new DatagramPacket(resultData, resultData.length, serverAddress, serverPort);
        socket.send(resultPacket);

        // d. Đóng socket
        socket.close();
    }
    
}
