package server;

import Global.ClientUserData;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static Global.global.*;

public class ChatRoomData {
    // 채팅방 데이터 클래스


    // 방 생성자 이름
    public List<ClientUserData> serverChatRoomInfoList;

    private String mHostName;


    private Socket mHostSocket;


    // 손님 이름
    private String mGuestName;

    private Socket mGuestSocket;


    public ChatRoomData(Socket _socket, String _hostName){
        // 생성 시, 호스트 이름과 정보 안에 이름 삽입.

        serverChatRoomInfoList = new ArrayList<>();
        mHostName = _hostName;
        mHostSocket = _socket;
        serverChatRoomInfoList.add(new ClientUserData(_socket,_hostName));
    }

    public String getmHostName() {
        return mHostName;
    }

    public void setmHostName(String _mHostName) {
        this.mHostName = _mHostName;
    }

    public String getmGuestName() {
        return mGuestName;
    }

    public void setmGuestName(String _mGuestName) {
        this.mGuestName = _mGuestName;
    }

    public List<ClientUserData> getServerChatRoomInfoList() {
        return serverChatRoomInfoList;
    }

    public Socket getmHostSocket() {
        return mHostSocket;
    }

    public Socket getmGuestSocket() {
        return mGuestSocket;
    }

    String joinChatRoom(Socket _socket,String _guestName){
        // 게스트가 들어가기 위한 함수
        if(mHostName == null){
            return cmtNullRoom;
        } else if (serverChatRoomInfoList.size() >= 2) {
            return cmtFullRoom;
        }
        // 게스트 입장.
        serverChatRoomInfoList.add(new ClientUserData(_socket,_guestName));
        mGuestName = _guestName;
        mGuestSocket = _socket;
        return  mHostName+"님의 채팅방에 입장하였습니다.";
    }

    boolean isCanJoinChatRoom(){
        // 혼자일 경우 와 아닐 경우로 구분.
        return serverChatRoomInfoList.size() == 1;
    }

    String isCanJoinChatRoomCmt(){
        // 혼자일 경우와 아닐 때를 코멘트로 반환
        return isCanJoinChatRoom()? cmtCanJoinChatRoom : cmtCanNotJoinChatRoom;
    }

    Socket getSocketInfo(String _nickName){
        // 이름을 통해 소켓 정보를 가져옴.
        for(ClientUserData clientUserInfo :serverChatRoomInfoList){
            if(clientUserInfo.getmNickName().equals(_nickName)){
                return clientUserInfo.getmSocket();
            }
        }
        return null;
    }







}
