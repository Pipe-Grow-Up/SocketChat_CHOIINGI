package client;

import Global.Config;

public class Main {

    public static void main(String[] args) {
        // 서버 ip, port 가져와 보내기.
        String ip = Config.ip;      // 접속 IP
        int port = Config.port;     // 접속 port
        new SocketClient(ip, port);
    }
}
