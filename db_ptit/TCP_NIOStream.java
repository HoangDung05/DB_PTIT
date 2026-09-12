package db_ptit;

import java.net.Socket;

public class TCP_NIOStream {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("36.50.135.242", 2211);
        socket.setSoTimeout(5000);

        
        
    }
}
