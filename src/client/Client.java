package client;

import java.io.*;
import java.net.Socket;

/**
 * 1 대 1 소켓 통신 클라이언트 예제
 */
public class Client {

    private Socket mSocket;

    String ip;
    int port;

    public Client(String ip, int port) {
        try {
            this.ip = ip;
            this.port = port;
            // 서버에 요청 보내기
            mSocket = new Socket(ip,port);
            ReceiveDataThread receiveDataThread = new ReceiveDataThread(mSocket); // 서버로부터 메세지 받는 Thread
            SendDataThread sendDataThread = new SendDataThread(mSocket); // 서버에 메세지 보내는 Thread
            Thread t1 = new Thread(receiveDataThread);
            Thread t2 = new Thread(sendDataThread);
            t1.start();
            t2.start();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

//    void Lobby() throws IOException {
//        // 로비 구현
//        System.out.println("로비에 오신것을 환영합니다. 원하시는 기능을 숫자로 입력해주세요.");
//        System.out.println("1. 닉네임 설정");
//        System.out.println("2. 접속자 리스트 출력");
//        System.out.println("3. 채팅 프로그램 종료");
//
//        Scanner scanner = new Scanner(System.in);
//        while(true){
//            input = scanner.nextLine();
//            if(input.equals("1")){
//                System.out.println("닉네임을 입력해 주세요 : ");
//                bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//                nickName = bufferedReader.readLine();
//                System.out.println("닉네임 설정 완료");
//                mSocket = new Socket(ip, port);
//                PrintWriter writer = new PrintWriter(mSocket.getOutputStream(), true);
//                writer.println("setNickname/" + nickName);
//                return ;
//            } else if (input.equals("2")) {
//                // 현재 클라이언트 접속자 확인
//                mSocket = new Socket(ip, port);
//                PrintWriter writer = new PrintWriter(mSocket.getOutputStream(), true);
//                writer.println("currentUserCount");
//                return ;
//            }else if (input.equals("3")){
//                // 채팅 프로그램 종료
//                if(mSocket !=null )mSocket.close();
//                mSocket = null;
//                return;
//            }else{
//                System.out.println("1,2,3 중 하나만 선택하세요.");
//            }
//        }
//    }


    public static void main(String[] args) {
        // 서버 ip, port 가져와 보내기.
        String ip = "192.168.21.144";
        int port = 5556;
        new Client(ip, port);
    }
}