package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

import static Global.global.clients;

public class ServerNoticeThread extends Thread {
    // 서버 공지를 위한 글 전체 공지 메세지 전송
    // 네이밍은 쓰레드를 상속 받을 시, 클래스에 Thread를 적어주는 것이 좋을듯.

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
                    System.out.println("연결된 유저 목록");
                    for (Map.Entry<Socket, String> entry : clients.entrySet()) {
                        //계정 관리
                        System.out.println(clients.get(entry.getKey()));
                    }
                } else if (message.startsWith("notice/")) {
                    // 공지 문자열 제외하고 알림.
                    String msg = message.substring("notice/".length());
                    for (Map.Entry<Socket, String> entry : clients.entrySet()) {
                        //계정 관리
                        PrintWriter writer = new PrintWriter(entry.getKey().getOutputStream(), true);
                        writer.println(msg);
                    }
                }
                else{

                }
            }
        } catch (Exception e) {//예외처리 발생시 실행
            e.printStackTrace();  //예외처리시 출력
        }
    }
}
