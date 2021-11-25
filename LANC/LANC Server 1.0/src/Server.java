import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket server;
    Socket client;

    public static void main(String[] args) {
        new Server().start();
    }
    public void start(){
        try {
            server=new ServerSocket(8000,6);
            while(true) {
                System.out.println("已就绪");
                client = server.accept();
                System.out.println(client.getPort()+"连接成功");
                new Thread(new LoginServerThread(client)).start();
            }
        } catch (IOException e) {
            System.out.println("无法连接客户端");
        }
    }
}
