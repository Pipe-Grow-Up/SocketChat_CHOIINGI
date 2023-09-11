package server;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import static Global.global.clients;

public class ChatServerThread extends Thread {
    String mName = "unknown"; // 클라이언트 이름 설정용
    private Socket mSocket;

    public ChatServerThread(Socket mSocket) {
        this.mSocket = mSocket; // 유저 socket을 할당
        // 유저를 맵에 삽입
        clients.put(mSocket, mName);
    }

    public void run() {

        try {
            System.out.println("서버 : " + mSocket.getInetAddress()
                    + " IP의 클라이언트와 연결되었습니다");
            // InputStream - 클라이언트에서 보낸 메세지 읽기
            InputStream input = mSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // OutputStream - 서버에서 클라이언트로 메세지 보내기
            OutputStream out = mSocket.getOutputStream();
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
                    if (readValue.equals("1")) { // 1번 : 닉네임 입력
                        out = mSocket.getOutputStream();
                        writer = new PrintWriter(out, true);
                        writer.println("닉네임을 입력해주세요: ");
                        writer.flush();
                        // InputStream - 클라이언트에서 보낸 메세지 읽기
                        InputStream nameInput = mSocket.getInputStream();

                        BufferedReader nameReader = new BufferedReader(new InputStreamReader(nameInput));
                        mName = nameReader.readLine();
                        writer.println("닉네임이 " + mName + "으로 설정되었습니다.");
                        clients.put(mSocket, mName);
                        isFirstConnect = true;
                    } else if (readValue.equals("2")) {
                        for (Map.Entry<Socket, String> entry : clients.entrySet()) {
                            Socket socket = entry.getKey();
                            String clientName = entry.getValue();
                            // 각 키-값 쌍에 접근하여 원하는 작업 수행
                            writer.println("클라이언트 소켓: " + socket);
                            writer.println("클라이언트 이름: " + clientName);
                        }
                        writer.println("로비에 오신것을 환영합니다. 원하시는 기능을 숫자로 입력해주세요!\n" + "1. 닉네임 설정\n" +
                                "2. 접속자 리스트 출력\n" +
                                "3. 채팅 프로그램 종료\n"
                        );
                        writer.flush();
                    } else if (readValue.equals("3") || readValue.contains("/exit")) {
                        System.out.println("채팅 프로그램을 종료합니다.");
                        System.out.println("remove : " + mSocket.toString());
                        clients.remove(mSocket);
                    } else {
                        if (!isFirstConnect) {
                            writer.print("잘못된 숫자를 입력하셨습니다.\n 원하시는 기능을 숫자로 입력해주세요!\n" + "1. 닉네임 설정\n" +
                                    "2. 접속자 리스트 출력\n" +
                                    "3. 채팅 프로그램 종료\n"
                            );
                            writer.flush();
                        }
                    }
                }
                if (readValue.startsWith("send:")) {
                    // 클라이언트 간 1대1 메세지 송수신은 send:로 시작하며 /로 닉네임을 분리한다. ex) send:incava/안녕! -> 안녕! 메세지를 incava에 전송
                    int startIndex = readValue.indexOf("send:") + "send:".length();
                    int endIndex = readValue.indexOf("/");
                    String extractedString = "";
                    if (endIndex != -1) {
                        // 닉네임 찾기 위함. :와 /사이를 찾아 가져온다.
                        extractedString = readValue.substring(startIndex, endIndex);
                        System.out.println(extractedString);
                    } else {
                        System.out.println("보내는 형식이 잘못 되었습니다.");
                    }
                    for (Map.Entry<Socket, String> entry : clients.entrySet()) {
                        Socket socket = entry.getKey();
                        String clientName = entry.getValue();
                        if (clientName.equals(extractedString)) {
                            // 닉네임이 일치하는 경우, 메세지 전송
                            PrintWriter targetWriter = new PrintWriter(socket.getOutputStream(), true);
                            targetWriter.println(clients.get(this.mSocket) + " : " + readValue.substring(endIndex + 1));
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외처리
        }
    }
}