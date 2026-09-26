package db_ptit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import UDP.Customer;

public class UDP_Object2 {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(5000);
        InetAddress serverAddress = InetAddress.getByName("36.50.135.242");
        int port = 2209;

        // gui den server
        String request = ";B23DCCN199;feYNSot3";
        socket.send(new DatagramPacket(request.getBytes(), request.length(), serverAddress, port));

        // nhan du lieu tu server
        byte[] buffer = new byte[4096];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(receivePacket);

        // Tach 8 byte dau de lay requestId
        String requestId = new String(receivePacket.getData(), 0, 8);

        // Tach cac byte con lai de lay object Customer
        ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData(), 8, receivePacket.getLength() - 8);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Customer customer = (Customer) ois.readObject();

        // a + c
        String[] word = customer.getName().trim().split("\\s+");
        StringBuilder newName = new StringBuilder(word[word.length - 1].toUpperCase()).append(", ");
        StringBuilder newUserName = new StringBuilder();
        for (int i = 0; i < word.length - 1; i++) {
            newName.append(Character.toUpperCase(word[i].charAt(0))).append(word[i].substring(1).toLowerCase());
            newUserName.append(Character.toLowerCase(word[i].charAt(0)));

            if (i < word.length - 2)
                newName.append(" ");
        }
        newUserName.append(word[word.length - 1].toLowerCase());
        customer.setName(newName.toString());
        customer.setUserName(newUserName.toString());

        // b
        String[] part = customer.getDayOfBirth().split("-");
        StringBuilder newDay = new StringBuilder(part[1] + "/" + part[0] + "/" + part[2]);
        customer.setDayOfBirth(newDay.toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(requestId.getBytes());

        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(customer);
        oos.flush();

        byte[] sendData = baos.toByteArray();
        socket.send(new DatagramPacket(sendData, sendData.length, serverAddress, port));
        // d. 
        socket.close();
    }
}
