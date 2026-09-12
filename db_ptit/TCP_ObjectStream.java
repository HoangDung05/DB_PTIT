package db_ptit;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import TCP.Laptop;

public class TCP_ObjectStream {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("36.50.135.242", 2209);
        socket.setSoTimeout(5000);        
        
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        // 1.
        oos.writeObject("B23DCCN199;2lzKgGNt");
        oos.flush();

        // 2.
        Laptop laptop = (Laptop) ois.readObject();

        // 3.
        // sua te
        String[] words = laptop.getName().split(" ");

        String temp = words[0];
        words[0] = words[words.length - 1];
        words[words.length - 1] = temp;

        String newName = String.join(" ", words);
        laptop.setName(newName);

        // sua so luong
        String quantityStr = new StringBuilder(String.valueOf(laptop.getQuantity())).reverse().toString();
        laptop.setQuantity(Integer.parseInt(quantityStr));

        // gui object sau khi sua
        oos.writeObject(laptop);
        oos.flush();

        // 4.
        socket.close();
    }
}
