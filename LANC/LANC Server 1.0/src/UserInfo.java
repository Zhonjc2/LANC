import java.io.Serializable;
import java.net.Socket;

public class UserInfo implements Serializable {
    private String userName;
    private Socket toUserSocket;
    public UserInfo(String userName,Socket toUserSocket){
        this.toUserSocket=toUserSocket;
        this.userName=userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Socket getToUserSocket() {
        return toUserSocket;
    }

    public void setToUserSocket(Socket toUserSocket) {
        this.toUserSocket = toUserSocket;
    }
}
