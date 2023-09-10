package client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class ReceiveDataThread implements Runnable {
    // 받은 데이터 처리.
    Socket mSocket; // socket 맴버변수
    BufferedReader mReader; // BufferReader 변수 reader 선언

    InputStream inputStream;

    public ReceiveDataThread(Socket socket) { // 생성자
        this.mSocket = socket; // 현재 소켓을 맴버에 할당.
        try {
            // 서버 메세지를 읽어오기
            inputStream = mSocket.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            mReader = new BufferedReader(new InputStreamReader(inputStream));
            System.out.print("서버 공지 : ");
            while( inputStream != null) { // 무한반복
                System.out.println(mReader.readLine());
            }
        } catch (Exception e) {//예외처리 발생시 실행
            e.printStackTrace();  //예외처리시 출력
        }
    }
}