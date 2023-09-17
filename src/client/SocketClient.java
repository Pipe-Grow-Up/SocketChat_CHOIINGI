package client;

import java.io.*;
import java.net.Socket;

/**
 * 1 대 1 소켓 통신 클라이언트 예제
 */
public class SocketClient {

    private Socket mSocket;

    String ip;
    int port;

    public SocketClient(String ip, int port) {
        try {
            this.ip = ip;
            this.port = port;
            // 서버에 요청 보내기
            mSocket = new Socket(ip,port);
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