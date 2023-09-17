package Global;

import server.ChatRoomData;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Global {
    // 전역으로 사용하는 데이터모음

    public static HashMap<Socket, String> clients = new HashMap<>();
    // 서버에 있는 채팅방 관리
    public static List<ChatRoomData> serverChatRooms = new ArrayList<>();
}
