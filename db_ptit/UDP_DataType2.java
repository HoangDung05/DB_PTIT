package db_ptit;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP_DataType2 {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(5000);

        InetAddress serverAddress = InetAddress.getByName("36.50.135.242");
        int serverPort = 2207;

        // a. Gửi gói tin chứa chuỗi ";B23DCCN199;aE4WPe4n" đến server
        String message = ";B23DCCN199;aE4WPe4n";
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
        String[] numberStrings = parts[1].split(",");
        int[] numbers = new int[numberStrings.length];
        for (int i = 0; i < numberStrings.length; i++) {
            numbers[i] = Integer.parseInt(numberStrings[i]);
        }

        // c. Tìm max và min 
        int max = numbers[0];
        int min = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
            if (numbers[i] < min) {
                min = numbers[i];
            }
        }

        // Gửi kết quả về server
        String result = requestId + ";" + max + "," + min;
        byte[] resultData = result.getBytes();
        DatagramPacket resultPacket = new DatagramPacket(resultData, resultData.length, serverAddress, serverPort);
        socket.send(resultPacket);

        // d. Đóng socket
        socket.close();
    }
    
}
