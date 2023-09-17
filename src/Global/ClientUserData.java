package Global;

import java.net.Socket;

public class ClientUserData {
    // user data class

    Socket mSocket;

    String mNickName;

    public ClientUserData(Socket _socket, String _nickName) {
        mSocket = _socket;
        mNickName = _nickName;
    }

    public Socket getmSocket() {
        return mSocket;
    }

    public void setmSocket(Socket _mSocket) {
        this.mSocket = _mSocket;
    }

    public String getmNickName() {
        return mNickName;
    }

    public void setmNickName(String _mNickName) {
        this.mNickName = _mNickName;
    }

}
