package db_ptit;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class TCP_GZIPStream {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("36.50.135.242", 2210);
        socket.setSoTimeout(5000);

        // 1. Tạo luồng GZIP (Nhớ giữ tham số true)
        GZIPOutputStream gos = new GZIPOutputStream(socket.getOutputStream(), true);
        GZIPInputStream gis = new GZIPInputStream(socket.getInputStream());

        // a. Gửi mã sinh viên
        String request = "B23DCCN199;3TVlL3jq\n"; // Bắt buộc phải có \n
        gos.write(request.getBytes(StandardCharsets.UTF_8));
        gos.flush();

        // b. Đọc từng byte một để TRÁNH LỖI "Unexpected end of ZLIB"
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c;
        while ((c = gis.read()) != -1) {
            if (c == '\n') {
                break; // Dừng lại ngay lập tức khi thấy dấu xuống dòng
            }
            baos.write(c);
        }
        
        String s = baos.toString(StandardCharsets.UTF_8);

        // c. Xử lý thuật toán
        String reversed = new StringBuilder(s).reverse().toString();
        String base64 = Base64.getEncoder().encodeToString(reversed.getBytes(StandardCharsets.UTF_8));

        // Gửi kết quả
        String result = reversed + "|" + base64 + "\n"; // Bắt buộc phải có \n
        gos.write(result.getBytes(StandardCharsets.UTF_8));
        gos.flush();

        // d. Đóng kết nối
        socket.close();
    }
}