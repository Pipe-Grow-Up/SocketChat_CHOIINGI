package Global;

import java.net.Socket;

public class ClientUserData {
    Socket mSocket;

    String mNickName;
    public ClientUserData(Socket socket, String nickName) {
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
