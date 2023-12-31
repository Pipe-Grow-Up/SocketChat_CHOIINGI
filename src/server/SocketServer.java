package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 소켓통신 서버 클래스
 */

public class SocketServer {
    private ServerSocket mServerSocket;
    private Socket mSocket;


    public SocketServer() {
        try {
            int socketPort = 5550;
            mServerSocket = new ServerSocket(socketPort);
            System.out.println("서버 시작!!!");
            mSocket = null;
            System.out.println("socket : " + socketPort + "으로 서버가 열렸습니다.");
            System.out.println("클라이언트의 접속을 기다립니다.");
            ServerNoticeThread notice = new ServerNoticeThread();
            notice.start();
            while (true) {
                // 서버 소켓에서
                mSocket = mServerSocket.accept();
                Thread t1 = new ChatServerThread(mSocket);
                t1.start(); // Thread 시작

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
