package db_ptit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedHashMap;

public class TCP_CharacterStream2 {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("36.50.135.242", 2208);
        socket.setSoTimeout(5000);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // a. Gửi chuỗi "B23DCCN199;fBQgWAVp" đến server
        bw.write("B23DCCN199;fBQgWAVp");
        bw.newLine();
        bw.flush();

        // b. Nhận chuỗi từ server
        String str = br.readLine().trim();

        // c. Liệt kê các ký tự xuất hiện nhiều hơn 1 lần trong chuỗi và số lần xuất hiện của chúng và gửi lên server
        LinkedHashMap<Character, Integer> charCountMap = new LinkedHashMap<>();
        for (char c : str.toCharArray()) {
            charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
        }
        for (Character c : charCountMap.keySet()) {
            if (charCountMap.get(c) > 1) {
                bw.write(c + ":" + charCountMap.get(c) + ",");
            }
        }
        bw.newLine();
        bw.flush();
        socket.close();
    }
    
}
