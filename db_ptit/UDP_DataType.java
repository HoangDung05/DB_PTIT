package db_ptit;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP_DataType {
    public static void main(String[] args) throws Exception {
        // Khởi tạo socket UDP
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(5000);

        // Định vị địa chỉ máy chủ và cổng của server
        InetAddress serverAddress = InetAddress.getByName("36.50.135.242");
        int serverPort = 2207;

        // a. Gửi gói tin chứa chuỗi "B23DCCN199;eNh8Q9A5" đến server
        String message = ";B23DCCN199;eNh8Q9A5";
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

        // c. Xử lý tìm số còn thiếu
        String[] parts = receivedStr.split(";");
        String requestId = parts[0];
        int n = Integer.parseInt(parts[1]);
        boolean[] isPresent = new boolean[n + 1];
        if (parts.length > 2 && !parts[2].isEmpty()) {
            String[] numbers = parts[2].split(",");
            for (String num : numbers) {
                isPresent[Integer.parseInt(num)] = true;
            }
        }

        // Xây dựng chuỗi kết quả
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(requestId).append(";");
        for (int i = 1; i <= n; i++) {
            if (!isPresent[i]) {
                resultBuilder.append(i).append(",");
            }
        }
        resultBuilder.setLength(resultBuilder.length() - 1); // Xóa dấu phẩy cuối cùng
        // In kết quả
        System.out.println(resultBuilder.toString());

        byte[] resultData = resultBuilder.toString().getBytes();
        DatagramPacket resultPacket = new DatagramPacket(resultData, resultData.length, serverAddress, serverPort);
        socket.send(resultPacket);
        socket.close();
    }
}
