package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 1 대 1 소켓 통신 서버 예제
 */
public class Server {
    private ServerSocket mServerSocket;
    private Socket mSocket;

    Scanner scanner;


    public Server() {
        try {
            int socketPort = 5556;
            mServerSocket = new ServerSocket(socketPort);
            System.out.println("서버 시작!!!");
            mSocket = null;
            System.out.println("socket : " + socketPort + "으로 서버가 열렸습니다.");
            System.out.println("클라이언트의 접속을 기다립니다.");
            ServerNotice notice = new ServerNotice();
            notice.start();
            while (true) {
                mSocket = mServerSocket.accept();
                Thread t1 = new ChatServerThread(mSocket);
                t1.start(); // Thread 시작
                // 서버에서 글 작성 가능 - 서버 공지
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
