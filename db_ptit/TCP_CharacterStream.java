package db_ptit;

import java.io.*;
import java.net.*;

public class TCP_CharacterStream {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("36.50.135.242", 2208);
        socket.setSoTimeout(5000);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // a.
        bw.write("B23DCCN199;5XsOWc7J");
        bw.newLine();
        bw.flush();

        // b.
        String str = br.readLine();
        String[] arr = str.split(",");
        StringBuffer result = new  StringBuffer();

        // c.
        for (String s: arr) {
            s.trim();
            if (s.endsWith(".edu")) {
                if (!result.isEmpty()) {
                    result.append(",");
                }
                result.append(s);
            }
        }
        bw.write(result.toString());
        bw.newLine();
        bw.flush();

        // d. 
        socket.close();
    }
}