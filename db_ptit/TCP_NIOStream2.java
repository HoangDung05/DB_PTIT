package db_ptit;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class TCP_NIOStream2 {
    public static void main(String[] args) throws Exception {
        SocketChannel channel = SocketChannel.open(
                new InetSocketAddress("36.50.135.242", 2211)
        );

        // a. Gửi MSSV + mã câu hỏi
        sendFrame(channel, "B23DCCN199;HJJZtDeB");

        // b. Nhận 2 frame
        String json = receiveFrame(channel) + receiveFrame(channel);

        // c. Lấy dữ liệu từ JSON
        String event = json.split("\"event\"\\s*:\\s*\"")[1].split("\"")[0];
        String user = json.split("\"user\"\\s*:\\s*\"")[1].split("\"")[0];

        String okValue = json.split("\"ok\"\\s*:\\s*")[1]
                             .split("[,}]")[0]
                             .trim();

        String ok = okValue.equals("true") ? "1" : "0";

        sendFrame(channel,
                "event=" + event + ";user=" + user + ";ok=" + ok);

        channel.close();
    }

    static void readFully(SocketChannel channel, ByteBuffer buffer)
            throws Exception {

        while (buffer.hasRemaining()) {
            if (channel.read(buffer) == -1) {
                throw new Exception();
            }
        }
    }

    static String receiveFrame(SocketChannel channel) throws Exception {
        ByteBuffer lenBuffer = ByteBuffer.allocate(4);
        readFully(channel, lenBuffer);

        lenBuffer.flip();
        int length = lenBuffer.getInt();

        ByteBuffer dataBuffer = ByteBuffer.allocate(length);
        readFully(channel, dataBuffer);

        return new String(dataBuffer.array(), StandardCharsets.UTF_8);
    }

    static void sendFrame(SocketChannel channel, String data)
            throws Exception {

        byte[] payload = data.getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer = ByteBuffer.allocate(4 + payload.length);

        buffer.putInt(payload.length);
        buffer.put(payload);
        buffer.flip();

        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }
}