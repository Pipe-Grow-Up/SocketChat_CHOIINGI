package client;

import java.io.*;
import java.net.Socket;

public class ClientSendDataToServerThread implements Runnable {
    // 서버로 메시지 보내는 Thread

    Socket mSocket; // socket 맴버변수
    BufferedReader mReader; // BufferReader 변수 reader 선언


    public ClientSendDataToServerThread(Socket _socket) throws IOException {
        this.mSocket = _socket;
    }

    public void run() {
        try {

            mReader = new BufferedReader(new BufferedReader(new InputStreamReader(System.in))); // 사용자의 입력을 받을준비
            PrintWriter writer = new PrintWriter(mSocket.getOutputStream(), true);    // 소켓에 대한 송신 준비

            while (true) {
                String message = mReader.readLine();                                            // 전할 메세지 입력
                writer.println(message);                                                        // 메세지 발송
                System.out.println("송신 성공!");
                if (message.equals("3")) {
                    System.out.println("채팅 프로그램 종료.");
                    break;
                } else if (message.startsWith("!")) {
                    System.out.println("서버에서 보낸겁니다.");
                }
            }
            System.exit(0);
        } catch (Exception e) {//예외처리 발생시 실행
            e.printStackTrace();  //예외처리시 출력
        }
    }
}