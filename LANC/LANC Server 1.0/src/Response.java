import java.io.Serializable;
import java.util.Vector;

public class Response implements Serializable {
    private int condition;
    private String message;
//    private Vector<UserInfo> userList;由于UserInfo内部含有Socket对象，它不是串行化的，因此无法通过流传输
    private Vector<String> userList;
    private String userHost;
    private int userPort;

    public int getUserPort() {
        return userPort;
    }

    public void setUserPort(int userPort) {
        this.userPort = userPort;
    }

    public Response(int condition, String message){
        this.condition=condition;
        this.message=message;
    }

    public Vector<String> getUserList() {
        return userList;
    }

    public void setUserList(Vector<String> userList) {
        this.userList = userList;
    }

    public String getUserHost() {
        return userHost;
    }

    public void setUserHost(String userHost) {
        this.userHost = userHost;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
