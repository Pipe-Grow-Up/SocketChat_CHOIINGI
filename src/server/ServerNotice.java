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
                if (message.equals("!userList")) {
                    // 접속 사용자 리스트 체크
                    System.out.println("연결된 유저 목록 \n");
                    for (Map.Entry<Socket, String> entry : clients.entrySet()) {
                        //계정 관리
                        System.out.println(clients.get(entry.getKey()));
                    }
                }
                // 메세지 발송
                else{
//                    PrintWriter writer = new PrintWriter(entry.getKey().getOutputStream(), true);
//                    writer.println(message);
                }
            }
        } catch (Exception e) {//예외처리 발생시 실행
            e.printStackTrace();  //예외처리시 출력
        }
    }
}
