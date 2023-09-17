package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientSendDataToServerThread implements Runnable{
    // 서버에서 받은 데이터 처리.
    Socket mSocket; // socket 맴버변수

    Scanner scanner;
    BufferedReader mReader; // BufferReader 변수 reader 선언




    public ClientSendDataToServerThread(Socket _socket) throws IOException {
        this.mSocket = _socket;
    }

    public void run(){
        try{
          //  scanner = new Scanner(System.in);
//            InputStream input = mSocket.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//

            mReader = new BufferedReader(new BufferedReader(new InputStreamReader(System.in))); // 사용자의 입력을 받으
              PrintWriter writer = new PrintWriter(mSocket.getOutputStream(), true);  // 소켓에 대한 송신 준비

            while(true) {
                // 전할 메세지 입력
                //String message = scanner.nextLine();
                // 메세지 발송
                String message = mReader.readLine();
                writer.println(message);
                //writer.flush();
                System.out.println("송신 성공!");
                if(message.equals("3")){
                    System.out.println("채팅 프로그램 종료.");
                    break;
                } else if (message.startsWith("!")) {
                    System.out.println("서버에서 보낸겁니다.");
                }
            }
            System.exit(0);
        }catch(Exception e){//예외처리 발생시 실행
            e.printStackTrace();  //예외처리시 출력
        }
    }
}