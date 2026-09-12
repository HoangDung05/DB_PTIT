package db_ptit;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.TreeSet;

public class TCP_ByteStream2 {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("36.50.135.242", 2206);
        socket.setSoTimeout(5000);

        // 1. Sử dụng InputStream và OutputStream gốc (dạng Raw Byte)
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        // 2. Gửi chuỗi: Phải chuyển String thành mảng byte bằng hàm getBytes()
        String request = "B23DCCN199;ja7vUmbF";
        os.write(request.getBytes());
        os.flush();

        // 3. Đọc dữ liệu: Dùng một mảng byte trống để hứng dữ liệu
        byte[] data = new byte[1024];
        int n = is.read(data);
        String str = new String(data, 0, n);

        // 4. Xử lý dữ liệu: Tách chuỗi thành mảng số nguyên, sắp xếp và tìm gtri lớn thứ 2
        String[] strArr = str.split(",");
        int[] intArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            intArr[i] = Integer.parseInt(strArr[i]);
        }

        TreeSet<Integer> set = new TreeSet<>();
        for (int i : intArr) {
            set.add(i);
        }
        int secondLargest = set.lower(set.last());
        int ind = 0;
        for (int i = 0; i < intArr.length; i++) {
            if (intArr[i] == secondLargest) {
                ind = i;
                break;
            }
        }
        String result = secondLargest + "," + ind;
        os.write(result.getBytes());
        os.flush();

        socket.close();
    }
}
