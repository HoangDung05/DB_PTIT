package db_ptit;

import java.io.ByteArrayOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class TCP_GZIPStream2 {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("36.50.135.242", 2210);
        socket.setSoTimeout(5000);

        GZIPInputStream gis = new GZIPInputStream(socket.getInputStream());
        GZIPOutputStream gos = new GZIPOutputStream(socket.getOutputStream(), true);

        // a.
        String request = "B23DCCN199;N5N11Y5w\n";
        gos.write(request.getBytes(StandardCharsets.UTF_8));
        gos.flush();

        // b. 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c;
        while ((c = gis.read()) != -1) {
            if (c == '\n')
                break;
            baos.write(c);
        }
        String s = baos.toString(StandardCharsets.UTF_8);

        // c.
        char[] charArr = s.toCharArray();
        Arrays.sort(charArr);
        String result = new String(charArr) + "\n";
        gos.write(result.getBytes(StandardCharsets.UTF_8));
        gos.flush();

        // d.
        socket.close();
    }
    
}
