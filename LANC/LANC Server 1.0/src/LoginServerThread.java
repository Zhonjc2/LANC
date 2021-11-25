import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Locale;
import java.util.Objects;
import java.util.Vector;

public class LoginServerThread implements Runnable{
    Request request;
    Response response;
    Socket client;
    public static Vector<UserInfo> users=new Vector<UserInfo>();
    private boolean online;
    ObjectInputStream obi;
    ObjectOutputStream obo;
    public LoginServerThread(Socket client){
        this.client=client;
//        try {
//            obi=new ObjectInputStream(client.getInputStream());
//            obo=new ObjectOutputStream(client.getOutputStream());
//            尽量只创建一次对象流，否则可能会因为流头混乱导致出错 <-瞎扯。
//        } catch (IOException e) {
//            System.out.println("获取流异常");
//        }
    }
    public void run(){
            online=true;
            new Thread(()->{
                while(online) {
                    System.out.println("准备发送下一响应");
                    request = receiveRequest();
                    if(request!=null)response = parseRequest();
                    if(!online)break;
                    if(response!=null)sendResponse();
                    request=null;
                    response=null;
                }
                try {
                    obi.close();
                    obo.close();
                    client.close();
                    System.out.println(client.getPort()+"退出成功");
                } catch (IOException e) {
                    System.out.println("关闭流异常");
                } catch (NullPointerException ee){
                    System.out.println("有空流");
                }
            }).start();
    }
    public Request receiveRequest(){
        try {
            obi=new ObjectInputStream(client.getInputStream());
            return (Request) obi.readObject();
        } catch (IOException e) {
            System.out.println(client.getPort()+"无法获取输入流"+e);
            return null;
        } catch(ClassNotFoundException e2){
            System.out.println("找不到请求类");
            return null;
        }
    }
    public Response parseRequest(){
        String user=request.getUser();
        String pass=request.getPass();
        UserInfo info=new UserInfo(user,client);
        switch(request.getOperator()){
            case 0:{//登录
                if(judgeContain(user)!=null)return new Response(0,"该用户已登录");
                String judge=Service.login(user,pass).toLowerCase();
                System.out.println(judge);
                if(judge.equals(user.toLowerCase())){
                    users.add(info);
                    new Thread(()->{
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Vector<String> usersName=new Vector<String>();
                        for(UserInfo w:users)usersName.add(w.getUserName());
                        Response userResponse=new Response(4,"返回在线用户列表");
                        userResponse.setUserList(usersName);
                        broadcast(userResponse,null);
                    }).start();
                    return new Response(1,"登录成功");
                }
                else if(judge.equals("-1"))return new Response(-1,"用户名或密码错误");
                else return new Response(-2,"数据库连接错误");
            }
            case 1:{//注册
                String judge=Service.signup(request.getUser(),request.getPass());
                if(judge.equals(user)){
                    users.add(info);
                    new Thread(()->{
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Vector<String> usersName=new Vector<String>();
                        for(UserInfo w:users)usersName.add(w.getUserName());
                        Response userResponse=new Response(4,"返回在线用户列表");
                        userResponse.setUserList(usersName);
                        broadcast(userResponse,null);
                    }).start();
                    return new Response(2,"注册成功");
                }
                else return new Response(-3,"用户已存在");
            }
            case 2:{//获取用户列表
                Response usersResponse=new Response(4,"获取用户成功");
                Vector<String> usersName=new Vector<String>();
                for(UserInfo w:users)usersName.add(w.getUserName());
                usersResponse.setUserList(usersName);
                return usersResponse;
            }
            case 3:{//请求获取用户IP
                Response backHost;
                UserInfo aim=judgeContain(user);
                if(aim!=null){
                    backHost=new Response(5,"该用户在线，返回主机名");
                    backHost.setUserHost(aim.getToUserSocket().getInetAddress().getHostAddress());
                    backHost.setUserPort(aim.getToUserSocket().getPort());
                    return backHost;
                }else return new Response(-5,"该用户已下线");
            }
            case 4:{//请求用户名
                Response userResponse;
                for(UserInfo w:users){
                    if(w.getToUserSocket().getPort()==Integer.parseInt(pass) && user.equals(w.getToUserSocket().getInetAddress().getHostAddress())){
                        userResponse=new Response(7,"返回 "+user+":"+pass+" 用户名");
                        userResponse.setMessage(w.getUserName());
                        return userResponse;
                    }
                }
                userResponse=new Response(-7,"没有该用户");
                return userResponse;
            }
            case -1:{//退出
//                for(UserInfo w:allUsers){
//                    if(w.getUserName().equals(user))allUsers.remove(w);
//                }注意在for-each循环中改变集合的元素数量会导致ConcurrentModificationException
                for(UserInfo w:users){
                    if(w.getUserName().equals(user)){
                        users.remove(w);
                        break;
                    }
                }
                online=false;
                System.out.println(client.getPort()+"已退出");
                Response offlineBroadcastResponse=new Response(6,"有用户离线，返回离线用户列表");
                Vector<String> offline=new Vector<String>();
                offline.add(user);
                offlineBroadcastResponse.setUserList(offline);
                broadcast(offlineBroadcastResponse,null);
                return new Response(3,"已退出登录");
            }
            default:return new Response(-100,"参数错误");
        }
    }
    public void sendResponse(){
        try {
            obo=new ObjectOutputStream(client.getOutputStream());
            obo.writeObject(response);
            obo.flush();
            System.out.println("发送Response:"+response.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void broadcast(Response re,UserInfo exclude){
        System.out.println("尝试广播："+re.getMessage());
        for(UserInfo w:users){
            if(w!=exclude){
//                LoginServerThread temp=new LoginServerThread(w.getToUserSocket());
//                temp.response=re;
//                temp.sendResponse();
                try {
                    ObjectOutputStream o=new ObjectOutputStream(w.getToUserSocket().getOutputStream());
                    o.writeObject(re);
                    System.out.println("广播确认："+re.getMessage()+" TO: "+""+w.getToUserSocket().getPort()+" "+w.getUserName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
//    public int sendMessage(){
//        return 0;
//    }
    public UserInfo judgeContain(String user){
        for(UserInfo w:users){
            if (w.getUserName().toLowerCase().equals(user.toLowerCase())) {
                return w;
            }
        }
        return null;
    }
}
