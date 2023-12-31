package server;

import Global.CommandMsgStrings;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import static Global.CommandMsgStrings.*;
import static Global.Global.clients;
import static Global.Global.serverChatRooms;
import static Global.InformationMsgStrings.*;

public class ChatServerThread extends Thread {
    // 채팅 Thread

    String mNickName = CommandMsgStrings.initNickName; // 클라이언트 이름 설정용

    private Socket mSocket; // 소켓 멤버변수

    boolean isJoinChatRoom; // 방에 참가했는지 여부 확인


    public ChatServerThread(Socket mSocket) {
        this.mSocket = mSocket; // 유저 socket을 할당
        clients.put(mSocket, mNickName); // 유저를 맵에 삽입
        isJoinChatRoom = false; // 아직 참가하지 않았음을 표시
    }

    boolean isExistNickName(String _nickName) {
        for (Map.Entry<Socket, String> entry : clients.entrySet()) {
            if (entry.getValue().equals(_nickName)) {
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
            InputStream input = mSocket.getInputStream(); // InputStream - 클라이언트에서 보낸 메세지 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream out = mSocket.getOutputStream();  // OutputStream - 서버에서 클라이언트로 메세지 보내기
            PrintWriter writer = new PrintWriter(out, true);


            writer.print(cmtWelcome);  // 클라이언트에게 연결되었다는 메세지 보내기
            writer.flush();
            String readValue; // Client에서 보낸 값 저장

            boolean identify = false; // 연결 후 리스트를 한번만 노출하려고 check하는  변수

            while ((readValue = reader.readLine()) != null) {

                if (!identify && !readValue.isBlank()) { // 연결 후 한번만 노출
                    identify = true;

                    if (readValue.equals(cmdInitialMenuNumberSetNickName)) { // 1번 : 닉네임 입력
                        writer.println(cmtInputNickName);
                        writer.flush();
                        // InputStream - 클라이언트에서 보낸 메세지 읽기
                        InputStream nameInput = mSocket.getInputStream();

                        BufferedReader nameReader = new BufferedReader(new InputStreamReader(nameInput));
                        String wantNickName = nameReader.readLine();

                        while (isExistNickName(wantNickName)) {
                            // 이름을 입력받은 후 while문에서 유일한 이름인지 검사한다.
                            // 이름이 중복되지 않으면 while문을 종료하고 이름을 지정한다.

                            writer.println(cmtAlreadyNicknameExist);
                            wantNickName = nameReader.readLine();
                        }

                        mNickName = wantNickName;
                        writer.println("닉네임이 " + mNickName + "으로 설정되었습니다.");
                        clients.put(mSocket, mNickName);

                    } else if (readValue.equals(cmdInitialMenuNumberUserList)) {
                        for (Map.Entry<Socket, String> entry : clients.entrySet()) {
                            Socket socket = entry.getKey();
                            String clientName = entry.getValue();
                            // 각 키-값 쌍에 접근하여 원하는 작업 수행
                            writer.println("클라이언트 소켓: " + socket);
                            writer.println("클라이언트 이름: " + clientName);
                        }
                        writer.println(cmtWelcome);
                        writer.flush();
                    } else if (readValue.equals(cmdInitialMenuNumberExit)) {
                        System.out.println(cmtExitChat);
                        System.out.println("remove : " + mSocket.toString());
                        clients.remove(mSocket);
                    } else {
                        writer.print(cmtWrongInputCmtAndWelcome);
                    }
                }
                if(readValue.equals(cmdCheckUserList)){
                    writer.println("연결된 유저 목록");
                    for (Map.Entry<Socket, String> entry : clients.entrySet()) {
                        //계정 관리
                        writer.println(clients.get(entry.getKey()));
                    }
                }
                if (!isJoinChatRoom) {
                    // 아직 chatRoom에 들어가지 않았을 경우
                    if (readValue.equals(cmdShowChatRoomList)) {
                        // 채팅방 리스트 목록 요청
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("채팅방 목록\n");
                        stringBuilder.append("------------------------\n");
                        for (ChatRoomData chatRoom : serverChatRooms) {
                            stringBuilder.append(chatRoom.getmHostName() + "님의 채팅방 ||  " + chatRoom.isCanJoinChatRoomCmt() + "\n");
                        }
                        writer.println(stringBuilder);
                    }
                    if (readValue.startsWith(cmdWantJoinChatRoom)) {
                        // 채팅방 진입을 원하는 커맨드
                        String wantHostID = readValue.split(cmdWantJoinChatRoom)[1]; // 명령어 : join/상대방닉네임
                        for (ChatRoomData chatRoom : serverChatRooms) {
                            if (chatRoom.getmHostName().equals(wantHostID)) {
                                //원하는 호스트 닉네임을 찾기위함.
                                // 자신의 정보를 넣고 참가.
                                String joinResult = chatRoom.joinChatRoom(mSocket, mNickName);
                                // 객체 안에 이미 있는 채팅방 연계 삽입.
                                writer.println(joinResult);
                                if (!joinResult.equals(cmtFullRoom) && !joinResult.equals(cmtNullRoom)) {
                                    // 만약 없어진 방 또는 벙애 모두 차있을 경우가 아니라면
                                    isJoinChatRoom = true;
                                }
                                //상대방에게 채팅방에 들어온것을 인지
                                PrintWriter printWriter = new PrintWriter(chatRoom.getmHostSocket().getOutputStream(), true);
                                printWriter.println(mNickName + "님이 채팅방에 들어오셨습니다.");
                                break;
                            }
                        }
                    }


                    if (readValue.equals(cmdCreateChatRoom)) {
                        // 채팅방 만들기 요청
                        ChatRoomData chatRoom = new ChatRoomData(mSocket, mNickName);
                        // 채팅방 목록에 저장.
                        serverChatRooms.add(chatRoom);
                        isJoinChatRoom = true;
                        writer.println(cmtEnterChatRoomSuccess);
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
                    if (readValue.equals(cmdExitTargetChatRoom)) {
                        //상대방이 방에 나갔다는 알림일 경우
                        isJoinChatRoom = false;
                        writer.println(cmtExitChatRoom);
                        writer.println(cmtWelcome);
                        continue;
                    }
                    Socket _targetSocket = null;
                    ChatRoomData _targetChatRoom = null;
                    for (ChatRoomData chatRoom : serverChatRooms) {
                        if (chatRoom.getmHostName().equals(mNickName)) {
                            //호스트 이름이 나와 같은 경우, 게스트 소켓을 가져옴.
                            _targetSocket = chatRoom.getmGuestSocket();
                            _targetChatRoom = chatRoom;
                            break;
                        } else if (chatRoom.getmGuestName().equals(mNickName)) {
                            //게스트 이름이 나와 같은 경우, 호스트 소켓을 가져옴.
                            _targetSocket = chatRoom.getmHostSocket();
                            _targetChatRoom = chatRoom;
                            break;
                        }
                    }
                    if (readValue.startsWith(cmdExitChatRoom)) {
                        //만약 메세지가 방을 나가고싶은 커맨드일 경우,
                        isJoinChatRoom = false;
                        serverChatRooms.remove(_targetChatRoom);
                        writer.println(cmtExitChatRoom);
                        writer.println(cmtWelcome);
                        // 상대가 있을 경우, 메세지 전달.
                        assert _targetSocket != null;
                        PrintWriter printWriter = new PrintWriter(_targetSocket.getOutputStream(), true);
                        printWriter.println(readValue);
                    } else if (_targetSocket == null) {
                        //만약 아직 상대가 없을 경우
                        writer.println(cmtNotEnterPartner);
                    } else {
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