package server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

import static server.ChatServerThread.clients;
import static server.ChatServerThread.socket;

/**
 * 1 대 1 소켓 통신 서버 예제
 */
public class Server {
    private ServerSocket mServerSocket;
    private Socket mSocket;

    Scanner scanner;

    public void notice(Socket socket) throws IOException {
        // 서버 공지를 위한 글 전체 공지 메세지 전송
        scanner = new Scanner(System.in);
        // 소켓에 대한 송신 준비
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        try{
            while(true) {
                // 전할 메세지 입력
                String message = scanner.nextLine();
                // 메세지 발송
                writer.println(message);
                System.out.println("서버에서 보낸겁니다.");
                if (message.startsWith("!chatList")) {
                    //계정 관리
                    System.out.println(clients.get(socket));
                }
            }
        }catch(Exception e){//예외처리 발생시 실행
            e.printStackTrace();  //예외처리시 출력
        }
    }

    public Server() {
        try {
            int socketPort = 5552;
            mServerSocket = new ServerSocket(socketPort);
            System.out.println("서버 시작!!!");
            mSocket = null;
            System.out.println("socket : " + socketPort + "으로 서버가 열렸습니다.");
            System.out.println("클라이언트의 접속을 기다립니다.");
            while (true) {
                mSocket = mServerSocket.accept();
                Thread t1 = new ChatServerThread(mSocket);
                t1.start(); // Thread 시작
                // 서버에서 글 작성 가능 - 서버 공지
                for (Map.Entry<Socket, String> entry : clients.entrySet()) {
                    notice(entry.getKey());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
