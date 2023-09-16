package client;

public class Main {

    public static void main(String[] args) {
        // 서버 ip, port 가져와 보내기.
        String ip = "127.0.0.1";
        int port = 5556;
        new SocketClient(ip, port);
    }
}
