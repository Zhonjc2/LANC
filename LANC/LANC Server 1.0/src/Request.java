import java.io.Serializable;

public class Request implements Serializable {
    private String user;
    private String pass;
    private String userMessage;
    private String aimUser;
    private int operator;
    //0为登录操作，1为注册操作，2为获取在线用户操作，3为发送消息操作
    public Request(String user,String pass,int operator){
        this.user=user;
        this.pass=pass;
        this.operator=operator;
    }
    public String getUserMessage() {
        return userMessage;
    }
    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
    public String getAimUser() {
        return aimUser;
    }
    public void setAimUser(String aimUser) {
        this.aimUser = aimUser;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public int getOperator() {
        return operator;
    }
    public void setOperator(int operator) {
        this.operator = operator;
    }
}
