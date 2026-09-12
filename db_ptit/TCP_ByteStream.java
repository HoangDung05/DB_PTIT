package db_ptit;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class TCP_ByteStream {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("36.50.135.242",2206);
        socket.setSoTimeout(5000);

        // 1. Sử dụng InputStream và OutputStream gốc (dạng Raw Byte)
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        // 2. Gửi chuỗi: Phải chuyển String thành mảng byte bằng hàm getBytes()
        String request = "B23DCCN199;8jXtCHMl";
        os.write(request.getBytes());
        os.flush();

        // 3. Đọc dữ liệu: Dùng một mảng byte trống để hứng dữ liệu
        byte[] data = new byte[1024];
        int n = is.read(data); // n = số byte thực tế nhận được
        String str = new String(data, 0, n); // Chuyển mảng byte thành String

        // 4. Xử lý dữ liệu: Tách chuỗi thành mảng số nguyên, sắp xếp và tìm khoảng cách nhỏ nhất
        String[] strArr = str.split(",");
        int[] intArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            intArr[i] = Integer.parseInt(strArr[i]);
        }

        Arrays.sort(intArr);
        int[] dis1 = new int[intArr.length - 1];
        for (int i = 0; i < intArr.length - 1; i++) {
            dis1[i] = intArr[i+1] - intArr[i];
            
        }
        int minDis = dis1[0];
        int ind = 0;
        for (int i = 1; i < dis1.length; i++) {
            if (dis1[i] <= minDis) {
                minDis = dis1[i];
                ind = i;
            }
        }
        String result = dis1[ind] + "," + intArr[ind] + "," + intArr[ind+1];
        os.write(result.getBytes());
        os.flush();

        socket.close();
    }
}
