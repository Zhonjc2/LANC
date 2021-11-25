import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ReceiveThread implements Runnable{
    //基于UDP来达到消息接收目的。
    DatagramSocket receiver;
    public ReceiveThread(DatagramSocket receiver){
        this.receiver=receiver;
//        try {
//            gram = new DatagramSocket(port);
//        } catch (SocketException e) {
//            System.out.println("无法创建接收器" + e);
//        }
    }
    public void run(){
        while(LoginInterface.online) {
            try {
                DatagramPacket messagePack = new DatagramPacket(new byte[100], 100);
                //注意接收前要清空原byte[]。
                byte[] temp=messagePack.getData();
                int i=0;
                for(byte w:temp){
                    temp[i]=(byte)0;
                    i++;
                }
                System.out.println("准备下一次接收...");
                receiver.receive(messagePack);//receive()是一个阻塞函数，会等待数据报传来。
                DataInputStream isStream=new DataInputStream(new ByteArrayInputStream(messagePack.getData(),messagePack.getOffset(),messagePack.getLength()));
                String receivedMessage=new String(messagePack.getData(),2,messagePack.getLength(), StandardCharsets.UTF_8);
                //DatagramPacket可以获取接收到的byte数组长度
                System.out.println("从"+messagePack.getAddress()+":"+messagePack.getPort()+"接收到信息："+receivedMessage);
                int order=LoginInterface.getAimOrder(messagePack.getAddress(),messagePack.getPort());
//                int end=receivedMessage.indexOf("\0");
                System.out.println("确认向Order："+order+"加入接收的信息");
                if(order!=-1)
//                    Platform.runLater(()->LoginInterface.mainFace.get(order).addMessage(receivedMessage.substring(0,end), new Color(Math.random(),Math.random(),Math.random(),1), Color.WHITE, 10, Position.LEFT));
                    Platform.runLater(()->LoginInterface.mainFace.get(order).addMessage(receivedMessage,Color.DARKGRAY, Color.WHITE, 10, Position.LEFT));
            } catch (SocketException e) {
                System.out.println("无法创建接收器" + e);
                return;
            } catch (IOException ee) {
                System.out.println("无法接收" + ee);
                return;
            }
        }
    }


}
