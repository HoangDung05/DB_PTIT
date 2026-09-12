package db_ptit;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP_String {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(5000);

        InetAddress serverAddress = InetAddress.getByName("36.50.135.242");
        int serverPort = 2208;

        // a. Gửi gói tin chứa chuỗi "B23DCCN199;fF8AF4t8" đến server
        String message = ";B23DCCN199;fF8AF4t8";
        byte[] sendData = message.getBytes();
        // Đóng gói bưu kiện gửi đi (Phải dán địa chỉ và port của server vào gói tin)
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
        socket.send(sendPacket);

        // b. Nhận gói tin về
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(receivePacket);
        // Lấy dữ liệu từ gói tin nhận được
        String receivedStr = new String(receivePacket.getData(), 0, receivePacket.getLength());

        // c. Tim kiem ky tu xuat hien nhieu nhat theo dinh dang "requestId;kyTuXuatHienNhieuNhat:cacvitriCuaKyTu"
        String[] parts = receivedStr.split(";");
        String requestId = parts[0];
        String str = parts[1];
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(requestId).append(";");
        
        // Tìm ký tự xuất hiện nhiều nhất
        int[] charCount = new int[256]; // Mảng đếm số lần xuất hiện của các ký tự
        for (char c : str.toCharArray()) {
            charCount[c]++;
        }
        char maxChar = 0;
        int maxCount = 0;
        for (int i = 0; i < str.length(); i++) {
            if (charCount[str.charAt(i)] > maxCount) {
                maxCount = charCount[str.charAt(i)];
                maxChar = str.charAt(i); 
            }
        }
        resultBuilder.append(maxChar).append(":");
        // Tìm các vị trí xuất hiện của ký tự nhiều nhất
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == maxChar) {
                resultBuilder.append(i + 1).append(",");
            }
        }
        
        // Gửi kết quả về server
        byte[] resultData = resultBuilder.toString().getBytes();
        DatagramPacket resultPacket = new DatagramPacket(resultData, resultData.length, serverAddress, serverPort);
        socket.send(resultPacket);
        socket.close();
    }
    
}
