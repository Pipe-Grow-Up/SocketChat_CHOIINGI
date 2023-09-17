package client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static Global.global.cmdExitChatRoom;
import static Global.global.cmdExitTargetChatRoom;

class ClientReceiveDataFromServerThread implements Runnable {
    // 서버에서 받은 데이터 처리.
    Socket mSocket; // socket 맴버변수
    BufferedReader mReader; // BufferReader 변수 reader 선언

    InputStream inputStream;

    public ClientReceiveDataFromServerThread(Socket socket) { // 생성자
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
            while( inputStream != null) { // 무한반복
                String receiveMsg = mReader.readLine();
                if(receiveMsg.equals(cmdExitChatRoom)){
                    //만약 상대방이 나가고싶다는 코멘트를 할 경우,
                    PrintWriter writer = new PrintWriter(mSocket.getOutputStream(), true);
                    writer.println(cmdExitTargetChatRoom);
                    //타겟(상대방)이 방에 나감을 서버에 알림.
                }else{
                    System.out.println(receiveMsg);
                }

            }
        } catch (Exception e) {//예외처리 발생시 실행
            e.printStackTrace();  //예외처리시 출력
        }
    }
}