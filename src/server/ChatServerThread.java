package server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static Global.global.clients;

public class ChatServerThread extends Thread {
    String name = "unknown"; // 클라이언트 이름 설정용
    private Socket socket;

    Scanner scanner;

    public ChatServerThread(Socket socket) {
        this.socket = socket; // 유저 socket을 할당
        // 유저를 맵에 삽입
        clients.put(socket,name);
    }



    public void run() {

        try {
            System.out.println("서버 : " + socket.getInetAddress()
                    + " IP의 클라이언트와 연결되었습니다");
            // InputStream - 클라이언트에서 보낸 메세지 읽기
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // OutputStream - 서버에서 클라이언트로 메세지 보내기
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);

            // 클라이언트에게 연결되었다는 메세지 보내기
            writer.print("로비에 오신것을 환영합니다. 원하시는 기능을 숫자로 입력해주세요!\n" + "1. 닉네임 설정\n" +
                    "2. 접속자 리스트 출력\n" +
                    "3. 채팅 프로그램 종료\n"
                   );
            writer.flush();
            String readValue; // Client에서 보낸 값 저장

            boolean isFirstConnect = false;

            while ((readValue = reader.readLine()) != null) {
                System.out.println(readValue);
                if (!isFirstConnect && !readValue.isBlank()) { // 연결 후 한번만 노출
                    isFirstConnect = true;
                    if (readValue.equals("1")) { // 1번 : 닉네임 입력
                        out = socket.getOutputStream();
                        writer = new PrintWriter(out, true);
                        writer.println("닉네임을 입력해주세요: ");
                        writer.flush();
                        // InputStream - 클라이언트에서 보낸 메세지 읽기
                        InputStream nameInput = socket.getInputStream();

                        BufferedReader nameReader = new BufferedReader(new InputStreamReader(nameInput));
                        name = nameReader.readLine();
                        writer.println("닉네임이 " + name + "으로 설정되었습니다.");
                        clients.put(socket,name);
                    } else if (Integer.parseInt(readValue) == 2) {
                        for (Map.Entry<Socket, String> entry : clients.entrySet()) {
                            Socket socket = entry.getKey();
                            String clientName = entry.getValue();
                            // 각 키-값 쌍에 접근하여 원하는 작업 수행
                            writer.println("클라이언트 소켓: " + socket);
                            writer.println("클라이언트 이름: " + clientName);
                        }
                    } else if (Integer.parseInt(readValue) == 3 || readValue.contains("/exit")) {
                        System.out.println("채팅 프로그램을 종료합니다.");
                        System.out.println("remove : " + socket.toString());
                        clients.remove(socket);
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외처리
        }
    }
}