import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginClient {
    Request request;
    Response response;
    Socket client;
    ObjectOutputStream obo;
    ObjectInputStream obi;
//    boolean online;
    public LoginClient(){
        try {
            client=new Socket("localhost",8000);
            response=new Response(100,"连接服务器成功");
//            obo=new ObjectOutputStream(client.getOutputStream());
//            obi=new ObjectInputStream(client.getInputStream());
            //这里可能会出现死锁问题。在Server（Client）端生成一个ObjectInputStream流时必须确保在Client（Server）端生成一个ObjectOutputStream流。
//            online=true;
        } catch (IOException e) {
            System.out.println("连接服务器失败");
            response=new Response(-100,"连接服务器失败");
//            online=false;
        }
    }
    public Response startARequest(String user, String pass, int operator) throws NullPointerException{
        request=new Request(user,pass,operator);
        sendRequest();
        receiveResponse();
        System.out.println("目前Response:"+response.getMessage());
        return response;
    }

    public Response close(String user,String pass){
        request=new Request(user,pass,-1);
        sendRequest();
        receiveResponse();
        System.out.println("目前Response:"+response.getMessage());
        return response;
    }

    public void sendRequest(){
        try {
            obo=new ObjectOutputStream(client.getOutputStream());
            obo.writeObject(request);
            obo.flush();
        } catch (IOException e) {
            System.out.println("无法连接服务器");
        }
    }
    public void receiveResponse(){
        try {
            obi=new ObjectInputStream(client.getInputStream());
            this.response=(Response) obi.readObject();
        } catch (IOException e) {
            System.out.println("无法获取流:"+e);
        } catch (ClassNotFoundException e2){
            System.out.println("找不到响应类");
        }
    }
}
