package server;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import static Global.global.*;

public class ChatServerThread extends Thread {
    String mNickName = "unknown"; // 클라이언트 이름 설정용
    private Socket mSocket;

    //
    boolean isJoinChatRoom;

    boolean isChatHost;

    public ChatServerThread(Socket mSocket) {
        this.mSocket = mSocket; // 유저 socket을 할당
        // 유저를 맵에 삽입
        clients.put(mSocket, mNickName);
        isJoinChatRoom = false;
        isChatHost = false;
    }

    boolean isExistNickName(String nickName) {
        for (Map.Entry<Socket, String> entry : clients.entrySet()) {
            if (entry.getValue().equals(nickName)) {
                // 만약 같은 닉네임이 있을 경우
                return true;
            }
        }
        return false;
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
            writer.print(cmtWelcome);
            writer.flush();
            String readValue; // Client에서 보낸 값 저장

            boolean isFirstConnect = false;

            while ((readValue = reader.readLine()) != null) {
//                System.out.println(readValue);
                if (!isFirstConnect && !readValue.isBlank()) { // 연결 후 한번만 노출
                    if (readValue.equals("1")) { // 1번 : 닉네임 입력
                        writer.println(cmtInputNickName);
                        writer.flush();
                        // InputStream - 클라이언트에서 보낸 메세지 읽기
                        InputStream nameInput = mSocket.getInputStream();

                        BufferedReader nameReader = new BufferedReader(new InputStreamReader(nameInput));
                        String wantNickName = nameReader.readLine();
                        if (isExistNickName(wantNickName)) {
                            writer.println("닉네임이 이미 존재 합니다. 다시 입력해주세요.");
                        } else {
                            mNickName = wantNickName;
                            writer.println("닉네임이 " + mNickName + "으로 설정되었습니다.");
                            clients.put(mSocket, mNickName);
                            isFirstConnect = true;
                        }

                    } else if (readValue.equals("2")) {
                        for (Map.Entry<Socket, String> entry : clients.entrySet()) {
                            Socket socket = entry.getKey();
                            String clientName = entry.getValue();
                            // 각 키-값 쌍에 접근하여 원하는 작업 수행
                            writer.println("클라이언트 소켓: " + socket);
                            writer.println("클라이언트 이름: " + clientName);
                        }
                        writer.println(cmtWelcome);
                        writer.flush();
                    } else if (readValue.equals("3")) {
                        System.out.println(cmtExitChat);
                        System.out.println("remove : " + mSocket.toString());
                        clients.remove(mSocket);
                    } else {
                        if (!isFirstConnect) {
                            writer.print(cmtWrongInputCmtAndWelcome);
                        }
                    }
                }
                if (!isJoinChatRoom) {
                    // 아직 chatRoom에 들어가지 않았을 경우
                    if (readValue.equals("!chatRoomList")) {
                        // 채팅방 리스트 목록 요청
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("채팅방 목록\n");
                        stringBuilder.append("------------------------\n");
                        for (ChatRoom chatRoom : serverChatRooms) {
                            stringBuilder.append(chatRoom.getmHostName() + "님의 채팅방 ||  " + chatRoom.isCanJoinChatRoomCmt() + "\n");
                        }
                        writer.println(stringBuilder);
                    }
                    if (readValue.startsWith(cmdWantJoinChatRoom)) {
                        // 채팅방 진입을 원하는 커맨드
                        String wantHostID = readValue.split(cmdWantJoinChatRoom)[1];
                        for (ChatRoom chatRoom : serverChatRooms) {
                            if (chatRoom.getmHostName().equals(wantHostID)) {
                                //원하는 호스트 닉네임을 찾기위함.
                                // 자신의 정보를 넣고 참가.
                                String joinResult = chatRoom.joinChatRoom(mSocket, mNickName);
                                // 객체 안에 이미 있는 채팅방 연계 삽입.
                                writer.println(joinResult);
                                if(!joinResult.equals(cmtFullRoom) && !joinResult.equals(cmtNullRoom)){
                                    // 만약 없어진 방 또는 벙애 모두 차있을 경우가 아니라면
                                    isJoinChatRoom = true;
                                    isChatHost = false;
                                }
                                //상대방에게 채팅방에 들어온것을 인지
                                PrintWriter printWriter = new PrintWriter(chatRoom.getmHostSocket().getOutputStream(), true);
                                printWriter.println(mNickName+"님이 채팅방에 들어오셨습니다.");
                                break;
                            }
                        }
                    }


                    if (readValue.equals(cmdCreateChatRoom)) {
                        // 채팅방 만들기 요청
                        ChatRoom chatRoom = new ChatRoom(mSocket, mNickName);
                        // 채팅방 목록에 저장.
                        serverChatRooms.add(chatRoom);
                        isJoinChatRoom = true;
                        isChatHost = true;
                        writer.println("채팅방에 성공적으로 입장하였습니다.");
                    }

                    // 로비 이후, 클라이언트가 보내는 메세지 분기.
                    else if (readValue.startsWith("send:")) {
                        // 클라이언트 간 1대1 메세지 송수신은 send:로 시작하며 /로 닉네임을 분리한다. ex) send:incava/안녕! -> 안녕! 메세지를 incava에 전송
                        int startIndex = readValue.indexOf("send:") + "send:".length();
                        int endIndex = readValue.indexOf("/");
                        String extractedString = "";
                        if (endIndex != -1) {
                            // 닉네임 찾기 위함. :와 /사이를 찾아 가져온다.
                            extractedString = readValue.substring(startIndex, endIndex);
                            System.out.println(extractedString);
                        } else {
                            System.out.println(cmtWrongSend);
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
                } else {
                    Socket _targetSocket = null;
//
                    for (ChatRoom chatRoom : serverChatRooms) {
                        if (chatRoom.getmHostName().equals(mNickName)) {
                            //호스트 이름이 나와 같은 경우, 게스트 소켓을 가져옴.
                            _targetSocket = chatRoom.getmGuestSocket();
                            break;
                        } else if (chatRoom.getmGuestName().equals(mNickName)) {
                            //게스트 이름이 나와 같은 경우, 호스트 소켓을 가져옴.
                            _targetSocket = chatRoom.getmHostSocket();
                        }
                    }
                    if(_targetSocket == null){
                        //만약 아직 상대가 없을 경우
                        writer.println("아직 상대방이 들어오지 않았습니다.");
                    }else{
                        // 상대가 있을 경우, 메세지 전달.
                        PrintWriter printWriter = new PrintWriter(_targetSocket.getOutputStream(), true);
                        printWriter.println(readValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외처리
        }
    }
}