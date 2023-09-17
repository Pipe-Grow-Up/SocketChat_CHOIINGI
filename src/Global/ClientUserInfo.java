package Global;

import java.net.Socket;

public class ClientUserInfo {
    Socket mSocket;

    String mNickName;
    public ClientUserInfo(Socket socket, String nickName) {
        mSocket = socket;
        mNickName = nickName;
    }

    public Socket getmSocket() {
        return mSocket;
    }

    public void setmSocket(Socket mSocket) {
        this.mSocket = mSocket;
    }

    public String getmNickName() {
        return mNickName;
    }

    public void setmNickName(String mNickName) {
        this.mNickName = mNickName;
    }

}
