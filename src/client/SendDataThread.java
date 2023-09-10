package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SendDataThread implements Runnable{
    // 받은 데이터 처리.
    Socket mSocket; // socket 맴버변수

    Scanner scanner;

    public SendDataThread(Socket socket){
        mSocket = socket;
    }

    public void run(){
        try{
            scanner = new Scanner(System.in);
            // 소켓에 대한 송신 준비
            PrintWriter writer = new PrintWriter(mSocket.getOutputStream(), true);
            while(true) {
                // 전할 메세지 입력
                String message = scanner.nextLine();
                // 메세지 발송
                writer.println(message);
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