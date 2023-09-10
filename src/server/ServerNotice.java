package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

import static Global.global.clients;

public class ServerNotice extends Thread {
    // 서버 공지를 위한 글 전체 공지 메세지 전송

    Scanner scanner;


    public void run() {

        scanner = new Scanner(System.in);
        // 소켓에 대한 송신 준비
        try {
            while (true) {
                // 전할 메세지 입력
                String message = scanner.nextLine();
                // 메세지 발송
                for (Map.Entry<Socket, String> entry : clients.entrySet()) {
                    // 연결된 소켓에 메세지를 전송
                    PrintWriter writer = new PrintWriter(entry.getKey().getOutputStream(), true);
                    writer.println(message);
                    System.out.println("서버에서 보낸겁니다.");
                    if (message.startsWith("!chatList")) {
                        //계정 관리
                        System.out.println(clients.get(entry.getKey()));
                    }
                }
                System.out.println(clients.size());
            }
        } catch (Exception e) {//예외처리 발생시 실행
            e.printStackTrace();  //예외처리시 출력
        }
    }
}
