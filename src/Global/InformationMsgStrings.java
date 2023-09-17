package Global;

public class InformationMsgStrings {
    // 유저에게 보내는 지시 comment (cmt)
    // 프로그램 이용하는데 필요한 Information Message 모음 클래스

    public static String cmtWelcome = "로비에 오신것을 환영합니다. 원하시는 기능을 숫자로 입력해주세요!\n" + "1. 닉네임 설정\n" +
            "2. 접속자 리스트 출력\n" +
            "3. 채팅 프로그램 종료\n";

    public static String cmtInputNickName = "닉네임을 입력해주세요: ";

    public static String cmtWrongInputCmtAndWelcome = "잘못된 숫자를 입력하셨습니다.\n 원하시는 기능을 숫자로 입력해주세요!\n" + "1. 닉네임 설정\n" +
            "2. 접속자 리스트 출력\n" +
            "3. 채팅 프로그램 종료\n";

    public static String cmtExitChat = "채팅 프로그램을 종료합니다.";

    public static String cmtWrongSend = "보내는 형식이 잘못 되었습니다.";

    public static String cmtNullRoom = "현재 없어진 방입니다.";

    public static String cmtFullRoom = "이미 모두 가득 찬 방입니다.";
    public static String cmtIsNotJoinGuest = "아직 채팅방에 사람이 들어오지 않았습니다.";
    public static String cmtExitChatRoom = "채팅방이 종료되었습니다.";
    public static String cmtCanJoinChatRoom = "입장 가능";
    public static String cmtCanNotJoinChatRoom = "입장 불가능";
    public static String cmtAlreadyNicknameExist = "닉네임이 이미 존재 합니다. 다시 입력해주세요.";
    public static String cmtEnterChatRoomSuccess = "채팅방에 성공적으로 입장하였습니다.";
    public static String cmtNotEnterPartner = "아직 상대방이 들어오지 않았습니다.";
}
