package client;

import java.io.*;
import java.net.Socket;

/**
 * 소켓통신 클라이언트 클래스
 */
public class SocketClient {

    private Socket mSocket;

    String ip;
    int port;

    public SocketClient(String _ip, int _port) {
        try {
            this.ip = _ip;
            this.port = _port;

            mSocket = new Socket(_ip, _port);
            ClientReceiveDataFromServerThread clientReceiveDataThread = new ClientReceiveDataFromServerThread(mSocket); // 서버로부터 메세지 받는 Thread
            ClientSendDataToServerThread sendDataThread = new ClientSendDataToServerThread(mSocket); // 서버에 메세지 보내는 Thread
            Thread t1 = new Thread(clientReceiveDataThread);
            Thread t2 = new Thread(sendDataThread);

            t1.start();
            t2.start();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}