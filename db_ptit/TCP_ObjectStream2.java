package db_ptit;

import java.net.*;
import java.io.*;

import TCP.Customer;;

public class TCP_ObjectStream2 {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("36.50.135.242", 2209);
        socket.setSoTimeout(5000);

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        // 1.
        oos.writeObject("B23DCCN199;gpjA7xsf");

        // 2.
        Customer customer = (Customer) ois.readObject();

        // 3.
        // a.
        String[] arr = customer.getName().split(" ");
        StringBuilder newName = new StringBuilder();
        newName.append(arr[arr.length - 1].toUpperCase()).append(",");

        for (int i = 0; i < arr.length - 1; i++) {
            newName.append(" ").append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1).toLowerCase());
        }
        customer.setName(newName.toString());

        // b.
        String[] arrDay = customer.getDayOfBirth().split("-");
        StringBuilder newDay = new StringBuilder();
        newDay.append(arrDay[1]).append("/").append(arrDay[0]).append("/").append(arrDay[2]);
        customer.setDayOfBirth(newDay.toString());

        // c.
        StringBuilder newUserName = new StringBuilder();
        for (int i = 0; i < arr.length - 1; i++) {
            newUserName.append(Character.toLowerCase(arr[i].charAt(0)));
        }
        newUserName.append(arr[arr.length - 1].toLowerCase());
        customer.setUserName(newUserName.toString());

        oos.writeObject(customer);
        oos.flush();

        // 4. 
        socket.close();
    }
}