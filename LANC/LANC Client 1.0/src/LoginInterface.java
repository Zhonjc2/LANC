import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Vector;

public class LoginInterface extends Application {
    public static final String FONT_LIB_B="file:///Users/zhonjc/Downloads/Font-OPPOSans/OPPOSans-B.ttf";
    public static final String FONT_LIB_H="file:///Users/zhonjc/Downloads/Font-OPPOSans/OPPOSans-H.ttf";
    public static final String FONT_LIB_R="file:///Users/zhonjc/Downloads/Font-OPPOSans/OPPOSans-R.ttf";

    static LoginClient service=new LoginClient();
    public static boolean online =false;
    static String connectedUser=null;
    static String connectedPass=null;
    static Vector<String> allUsers=new Vector<String>();
    Vector<Integer> offlineOrder=new Vector<Integer>();
    String paneStyle="-fx-border-width:4;-fx-border-radius: 48;-fx-background-radius: 49;-fx-background-color: WHITE;";

    static Pane mainPane=new Pane();
    Pane loginPane=new Pane();
    static Scene mainScene=new Scene(mainPane);
    Color mainColor0=Color.web("#929292");
    Color mainColor1=Color.web("#E5E5E5");
    Color mainColor2=Color.web("#C4C4C4");
    Color mainColor3=Color.web("#878787");

    Image logo=new Image("LANC.png");
    ImageView logo0=new ImageView(logo);
    Text[] tips;
    Thread tipThread;
    Text loginSign;
    Text userText;
    NealTextField userField=new NealTextField("??????????????????",312.81,40.11,25,15,3,mainColor0,Color.WHITE, Font.loadFont(FONT_LIB_B,20),mainColor0);
    Text passText;
    NealTextField passField=new NealTextField("???????????????",312.81,40.11,50,15,3,mainColor0,Color.WHITE, Font.loadFont(FONT_LIB_B,20),mainColor0);
    NealButton nextButton=new NealButton(mainScene,110.63,41.07,13,mainColor1,mainColor2,"?????????",Font.loadFont(FONT_LIB_B,20),Color.BLACK,71.11,407.82,e->nextStep());
    NealButton registerButton=new NealButton(mainScene,110.63,41.07,20,mainColor1,mainColor2,"??????",Font.loadFont(FONT_LIB_B,20),Color.BLACK,231.17,407.82,e->register());
    Text response;
    StackPane responsePane=new StackPane(new Rectangle(412.9,45,Color.TRANSPARENT));


    Pane userListPane=new Pane();
    static NealGroup userList=new NealGroup(Color.rgb(0,0,0,0.07),Color.TRANSPARENT,70,70,30,12.58,13.16,18.54,mainScene);
    boolean loginHide=false;

    public NealTextField chatField=new NealTextField("????????????...",802.61,66.34,19,48,0.5,mainColor3,Color.WHITE,Font.loadFont(FONT_LIB_R,28),mainColor3);
    public static LinkedList<ChatInterface> mainFace=new LinkedList<ChatInterface>();
    public LinkedList<Runnable> handlers=new LinkedList<Runnable>();
    public static String currentMessage=null;
    public static String currentUserHost=null;
    public static int currentUserPort;
    public NealButton sendButton=new NealButton(mainScene,52.78,52.78,34,Color.web("#929292"),Color.BLACK,new Image("arrow.png"),17.82,745,7.5, e->{
        currentMessage=chatField.getText();
        sendMessage();
        chatField.trueField.clear();
        currentMessage="";
    });
    public NealButton closeButton=new NealButton(mainScene,18,18,18,Color.web("#E9E9E9"),Color.web("#E9E9E9"),new Image("close.png"),11,421.81,583.5,e->close());
    public Pane chatPane=new Pane();
    public static String receiveUser;
    public DatagramPacket sender;
    DatagramSocket gram;
    public static int order=-1;


    public void close(){
        if(online) {
            System.out.println(service.close(connectedUser, connectedPass).getMessage());
            online = false;
            try {
                service.client.close();
            } catch (IOException ex) {
                System.out.println("????????????");
            }
        }else if(service.client!=null){
            System.out.println(service.close(connectedUser, connectedPass).getMessage());
            online=false;
        }
        Platform.exit();
        System.exit(0);
    }

    public static void setLayoutPos(Node node, double x, double y){
        node.setLayoutX(x);
        node.setLayoutY(y);
    }

    public static Text createText(String text, Color color, Font font, double x, double y, boolean add, Pane aimPane){
        Text textSign=new Text(text);
        textSign.setFill(color);
        textSign.setFont(font);
        setLayoutPos(textSign,x,y);
        if(add)aimPane.getChildren().add(textSign);
        return textSign;
    }

    public void prepareTips(int textNum,String...tip){
        Color initColor=new Color(mainColor3.getRed(),mainColor3.getGreen(),mainColor3.getBlue(),0);
        int i=0;
        tips=new Text[textNum];
        for(String w:tip){
            tips[i]=createText(w,initColor,Font.loadFont(FONT_LIB_B,16),49.04,125.09,true,loginPane);
            i++;
        }
        tipThread=new Thread(()->{
//            for(int p=0;p<textNum;p++) {
            int P=0,r;
            while(!loginHide){
                r=P;
                P=(int)(Math.random()*(textNum-1));
                while(r==P){
                    P=(int)(Math.random()*(textNum-1));
                }
//                System.out.println("?????????????????????"+P);
                final int finalP = P;
                new Thread(()->{
                    KeyValue slv = new KeyValue(tips[finalP].layoutYProperty(), 115.09, new Interpolator() {
                        protected double curve(double t) {
                            return (1.0 - Math.pow((1.0 - t), 4));
                        }
                    });
                    KeyValue scv = new KeyValue(tips[finalP].fillProperty(), mainColor3, new Interpolator() {
                        protected double curve(double t) {
                            return (1.0 - Math.pow((1.0 - t), 4));
                        }
                    });
                    KeyValue elv = new KeyValue(tips[finalP].layoutYProperty(), 105.09, new Interpolator() {
                        protected double curve(double t) {
                            return (1.0 - Math.pow((1.0 - t), 4));
                        }
                    });
                    KeyValue ecv = new KeyValue(tips[finalP].fillProperty(), initColor, new Interpolator() {
                        protected double curve(double t) {
                            return (1.0 - Math.pow((1.0 - t), 4));
                        }
                    });
                    KeyFrame ef = new KeyFrame(Duration.millis(600), elv, ecv);
                    KeyFrame sf = new KeyFrame(Duration.millis(1000), slv, scv);
                    Timeline el = new Timeline(ef);
                    Timeline sl = new Timeline(sf);
                    el.setDelay(Duration.millis(2000));//4000
                    sl.setDelay(Duration.millis(1000));
//                    sl.setOnFinished(e -> el.play());
//                    el.setOnFinished(e -> {
//                        tips[P].setLayoutY(125.09);
//                        sl.play();
//                    });
                    sl.setOnFinished(e->el.play());
                    el.setOnFinished(e -> tips[finalP].setLayoutY(125.09));
                    sl.play();
                }).start();
                try {
                    Thread.sleep(3200);//6500
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                    this.tipThread.interrupt();
                }
//                if(p==textNum-1)p=-1;
            }
        });
        tipThread.start();
//        loginPane.getChildren().add(tips[0]);
    }

    public void prepareLogin(){
        loginPane.setPrefSize(412.9,495.02);
        setLayoutPos(loginPane,222.55,629);
        loginPane.setStyle("-fx-border-width: 1;-fx-border-color: #A8A8A8;-fx-background-radius: 32;-fx-border-radius: 32;-fx-background-color: linear-gradient(to right,WHITE,#F0F0F0);");
        KeyValue upv=new KeyValue(loginPane.layoutYProperty(), 56.99, new Interpolator() {
            protected double curve(double t) {
                return Math.pow(2, -20 * t) * Math.sin((t - 0.4 / 4) * (2 * Math.PI) / 0.4) + 1;
            }
        });
        KeyFrame upf=new KeyFrame(Duration.millis(1200),upv);
        Timeline upl=new Timeline(upf);
        upl.setDelay(Duration.millis(1000));
        upl.play();
        logo0.setPreserveRatio(true);
        logo0.setFitHeight(30);
        logo0.setLayoutX(50.04);
        logo0.setLayoutY(60.82);
        loginPane.getChildren().add(logo0);
        loginSign=createText("??????",mainColor3,Font.loadFont(FONT_LIB_H,36),296.95,96.44,true,loginPane);
        userText=createText("?????????",mainColor3,Font.loadFont(FONT_LIB_B,20),178.52,183.73,true,loginPane);
        setLayoutPos(userField.getField(),50.04,197.46);
        loginPane.getChildren().add(userField.getField());
        passText=createText("??????",mainColor3,Font.loadFont(FONT_LIB_B,20),186.02,272.09,true,loginPane);
        setLayoutPos(passField.getField(),50.04,287.96);
        loginPane.getChildren().add(passField.getField());
        loginPane.getChildren().addAll(nextButton.getTrueButton(),registerButton.getTrueButton());
        prepareTips(7,"????????????????????????","????????????????????????","?????????????????????????????????","????????????LANC","LANC?????????????????????","Local Area Net Chat","??????????????????????????????");
    }

    public void nextStep(){
        Response re;
        if(loginPane.getChildren().contains(responsePane))loginPane.getChildren().remove(responsePane);
        if(responsePane.getChildren().contains(response))responsePane.getChildren().remove(response);
        try {
            re = service.startARequest(userField.getText(), passField.getText(), 0);
        }catch(NullPointerException e){
            response=createText("???????????????????????????????????????",mainColor3,Font.loadFont(FONT_LIB_B,15),0,0,true,responsePane);
            setLayoutPos(responsePane,0,362.06);
            loginPane.getChildren().add(responsePane);
            mainPane.setStyle(paneStyle + "-fx-border-color: #ff7373");
            online =false;
            return;
        }
        switch (re.getCondition()) {
            case -1 : {
                response = createText("???????????????????????????", mainColor3, Font.loadFont(FONT_LIB_B, 15), 0, 0, true, responsePane);
                setLayoutPos(responsePane, 0, 362.06);
                loginPane.getChildren().add(responsePane);
                mainPane.setStyle(paneStyle+"-fx-border-color: #ff7373");
                online = false;
                break;
            }
            case 1 : {
                response = createText("????????????", mainColor3, Font.loadFont(FONT_LIB_B, 15), 0, 0, true, responsePane);
                setLayoutPos(responsePane, 0, 362.06);
                loginPane.getChildren().add(responsePane);
                online = true;
                connectedUser = userField.getText();
                connectedPass = passField.getText();
                mainPane.setStyle(paneStyle+"-fx-border-color: #68bf7b");
                downLogin();
                break;
            }
            case 0 : {
                response = createText("???????????????????????????????????????????????????", mainColor3, Font.loadFont(FONT_LIB_B, 15), 0, 0, true, responsePane);
                setLayoutPos(responsePane, 0, 362.06);
                loginPane.getChildren().add(responsePane);
                mainPane.setStyle(paneStyle+"-fx-border-color: GOLD");
                online = false;
                break;
            }
            default : {
                response = createText("???????????????-100", mainColor3, Font.loadFont(FONT_LIB_B, 15), 0, 0, true, responsePane);
                setLayoutPos(responsePane, 0, 362.06);
                loginPane.getChildren().add(responsePane);
                online = false;
                break;
            }
        }
    }

    public void register(){
        Response re;
        if(loginPane.getChildren().contains(responsePane))loginPane.getChildren().remove(responsePane);
        if(responsePane.getChildren().contains(response))responsePane.getChildren().remove(response);
        try{
            re=service.startARequest(userField.getText(),passField.getText(),1);
        }catch(NullPointerException e){
            response=createText("???????????????????????????????????????",mainColor3,Font.loadFont(FONT_LIB_B,15),0,0,true,responsePane);
            setLayoutPos(responsePane,0,362.06);
            loginPane.getChildren().add(responsePane);
            mainPane.setStyle(paneStyle+"-fx-border-color: #ff7373");
            online =false;
            return;
        }
        switch (re.getCondition()) {
            case -3: {
                response = createText("????????????????????????", mainColor3, Font.loadFont(FONT_LIB_B, 15), 0, 0, true, responsePane);
                setLayoutPos(responsePane, 0, 362.06);
                loginPane.getChildren().add(responsePane);
                mainPane.setStyle(paneStyle+"-fx-border-color: #f58c23");
                online = false;
                break;
            }
            case 2 : {
                response = createText("????????????", mainColor3, Font.loadFont(FONT_LIB_B, 15), 0, 0, true, responsePane);
                setLayoutPos(responsePane, 0, 362.06);
                loginPane.getChildren().add(responsePane);
                mainPane.setStyle(paneStyle+"-fx-border-color: #68bf7b");
                online = true;
                connectedUser = userField.getText();
                connectedPass = passField.getText();
                downLogin();
                break;
            }
            default : {
                response = createText("???????????????-100", mainColor3, Font.loadFont(FONT_LIB_B, 15), 0, 0, true, responsePane);
                setLayoutPos(responsePane, 0, 362.06);
                loginPane.getChildren().add(responsePane);
                mainPane.setStyle(paneStyle+"-fx-border-color: #ff7373");
                online = false;
                break;
            }
        }
    }

    public void downLogin(){
        KeyValue downV=new KeyValue(loginPane.layoutYProperty(), 629, new Interpolator() {
            protected double curve(double x) {
                return x * x * ((1.5 + 1) * x - 1.5);
            }
        });
        KeyFrame downF=new KeyFrame(Duration.millis(800),e->{
            mainPane.getChildren().remove(loginPane);
            loginHide=true;
            prepareChat();
        },downV);
        Timeline downL=new Timeline(downF);
        downL.play();
    }

    public void prepareChat(){
        userListPane.setStyle("-fx-border-width: 0.5;" +
                "-fx-border-color: #C4C4C4;" +
                "-fx-background-radius: 25;" +
                "-fx-border-radius: 25;"+
                "-fx-background-color: linear-gradient(to right, #EFEFEF 0.72%, #F9F9F9 100%)");
        userListPane.setPrefSize(802.61,95.88);
        chatPane.getChildren().addAll(chatField.getField(),sendButton.getTrueButton());
        setLayoutPos(chatPane,27.18,638);
        setLayoutPos(userListPane, 27.18, -100);
        Platform.runLater(()->mainPane.getChildren().addAll(userListPane,chatPane));
        mainScene.setOnKeyPressed(e->{
            if(e.getCode()== KeyCode.ENTER){
                currentMessage=chatField.getText();
                sendMessage();
                chatField.trueField.clear();
                currentMessage="";
            }
        });
        KeyValue vc = new KeyValue(userListPane.layoutYProperty(), 25.17, new Interpolator() {
            protected double curve(double x) {
                return (1.0 - Math.pow((1.0 - x), 4));
            }
        });
        KeyValue vu=new KeyValue(chatPane.layoutYProperty(),513.57,new Interpolator() {
            protected double curve(double x) {
                return (1.0 - Math.pow((1.0 - x), 4));
            }
        });
        KeyFrame f = new KeyFrame(Duration.millis(600),e->startChat(),vc,vu);
        Timeline l = new Timeline(f);
        l.setDelay(Duration.millis(300));
        l.play();
    }

    public void monitor(){
        new Thread(()->{
            while(online){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(()->{
                    if(offlineOrder.contains(order)){
                        if(!chatField.trueField.getText().equals("???????????????")) {
                            chatField.trueField.setText("???????????????");
                            chatField.setEditable(false);
                            sendButton.setDisable(true);
                        }
                    }else if(order==allUsers.indexOf(connectedUser)){
                        if(!chatField.trueField.getText().equals("????????????????????????")) {
                            chatField.trueField.setText("????????????????????????");
                            chatField.setEditable(false);
                            sendButton.setDisable(true);
                        }
                    }else{
                        if(chatField.trueField.getText().equals("???????????????") || chatField.trueField.getText().equals("????????????????????????")) {
                            chatField.trueField.setText("");
                            chatField.setEditable(true);
                            initAimUser(allUsers.get(order));
                            sendButton.setDisable(false);
                        }
                    }
                });
            }
        }).start();
    }

    public void startChat(){
        new Thread(()->{
            Response re;
            try {
                System.out.println("????????????UDP?????????"+service.client.getLocalPort());
                gram =new DatagramSocket(service.client.getLocalPort());
                System.out.println("?????????UDP??????");
            } catch (SocketException e) {
                e.printStackTrace();
            }
            monitor();
            while(online){
                try {
                    ObjectInputStream obi= new ObjectInputStream(service.client.getInputStream());
                    //?????????????????????????service???obi?????????????????????AC???
                    re=(Response) obi.readObject();
//                    re=(Response) service.obi.readObject();
                    System.out.println("?????????Response:"+re.getMessage());
                } catch (IOException e) {
                    System.out.println("??????????????????: "+e+" Response 4 6 7????????????");
                    return;
                } catch (ClassNotFoundException e) {
                    System.out.println("??????????????????");
                    return;
                }
                switch(re.getCondition()){
                    case 4:{
                        for(String w:re.getUserList()) {
                            if(!allUsers.contains(w))allUsers.add(w);//String????????????????????????????????????????????????????????????String??????equals?????????contains??????
                            int i=allUsers.indexOf(w);
                            if(offlineOrder.contains(i))offlineOrder.remove((Object)i);
                        }
                        for(String w: allUsers)System.out.print(w+" ");
                        updateUsers();
                        break;
                    }
                    case 6:{
//                        offlineUser.addAll(re.getUserList());
                        offlineOrder.add(allUsers.indexOf(re.getUserList().lastElement()));
                        break;
                    }
                    case 5:{
                        currentUserHost = re.getUserHost();
                        currentUserPort = re.getUserPort();
                        System.out.println("?????????????????????"+currentUserHost+":"+currentUserPort);
                        break;
                    }
                    case 3:{
                        System.out.println("??????");
                        break;
                    }
                    case 7:{
                        receiveUser=re.getMessage();
                        System.out.println("??????????????????"+receiveUser);
                        break;
                    }
                    case -5:{
                        System.out.println("??????????????????");
                    }
                }
            }
        }).start();

    }

    public void sendMessage(){
        try {
//            currentMessage=currentMessage+"\0";

//            sender=new DatagramPacket(currentMessage.getBytes(StandardCharsets.UTF_8),currentMessage.length()*2,InetAddress.getByName(currentUserHost),currentUserPort);
            //????????????

//            messageSocket=new DatagramSocket(service.client.getLocalPort());
//            messageSocket.send(sender);
            //??????????????????
            ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
            DataOutputStream dataStream = new DataOutputStream(baoStream);
            dataStream.writeUTF(currentMessage);
            byte[] data = baoStream.toByteArray();
            System.out.println("?????????"+data.length);
            sender=new DatagramPacket(data,data.length,InetAddress.getByName(currentUserHost),currentUserPort);

            gram.send(sender);
            dataStream.close();
            baoStream.close();
//            messageSocket.close();
            System.out.println("????????????\""+new String(sender.getData(),StandardCharsets.UTF_8)+"\"??????"+sender.getAddress().getHostAddress()+":"+sender.getPort());
            //?????????????????????byte[]????????????????????????????????????????????????????????????????????????
            //?????????????????????????????????????????????????????????????????????
            mainFace.get(order).addMessage(currentMessage,Color.web("#68bf7b"),Color.WHITE,10,Position.RIGHT);
            currentMessage=null;
        } catch (SocketException e) {
            System.out.println("SocketException"+e);
        } catch (IOException ee) {
            System.out.println("IOException"+ee);
        }
    }
    //??????????????????????????????????????????1.????????????????????????????????????IP?????????2.??????????????????????????????????????????????????????????????????IP?????????

//    public void receiveMessage(){
//        new Thread(()->{
//            while (online) {
//                try {
//                    byte[] rMessage = new byte[1000];
//                    gram = new DatagramPacket(rMessage,1000);
//                    messageSocket = new DatagramSocket(service.client.getPort(),service.client.getInetAddress());
//                    messageSocket.receive(gram);
//                    mainFace.get(order).addMessage(new String(rMessage), new Color(Math.random(), 1, 1, 1), Color.WHITE, 20, Position.RIGHT);
//                } catch (SocketException e) {
//                    System.out.println("SocketException" + e);
//                    return;
//                } catch (IOException ee) {
//                    System.out.println("IOException" + ee);
//                    return;
//                }
//            }
//        }).start();
//    }

//    public ScrollPane prepareMainFace(){
//        ScrollPane s=new ScrollPane();
//        Pane p=new Pane();
//        p.setPrefSize(802.61,492.52);
//        p.getChildren().add(new Rectangle(100,Math.random()*500));
//        p.setStyle("-fx-background-color: WHITE");
//        s.setStyle("-fx-background-color: WHITE");
//        s.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        s.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        s.setContent(p);
//        s.setPrefSize(802.61,392.52);
//        setLayoutPos(s,27.18,121.05);
//        return s;
//    }

    public void initAimUser(String aimUser){
        new Thread(()->{
//            service.startARequest(aimUser,"",3);
            try {
                ObjectOutputStream obi = new ObjectOutputStream(service.client.getOutputStream());
                obi.writeObject(new Request(aimUser,"",3));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void switchAimUser() {
        Platform.runLater(() -> {
            System.out.println(mainFace.size());
            if(mainPane.getChildren().get(mainPane.getChildren().size()-1) instanceof ScrollPane)
                mainPane.getChildren().remove(mainPane.getChildren().size()-1);
            mainPane.getChildren().add(mainFace.get(order).getChatPane());
//            mainFace.get(order).addMessage(Math.random()+"",Color.BLACK,Color.WHITE,10,Position.LEFT);
            initAimUser(allUsers.get(order));
            StackPane temp0=(StackPane)(userList.group.get(order));
            StackPane temp1=(StackPane)(temp0.getChildren().get(temp0.getChildren().size()-1));
            Circle temp2=(Circle)temp1.getChildren().get(0);
            temp2.setStrokeWidth(0);
            System.out.println("?????????"+order+allUsers.get(order));
        });
//        currentUserHost=
    }

    public static int getAimOrder(InetAddress address,int port){
        try {
            ObjectOutputStream obi = new ObjectOutputStream(service.client.getOutputStream());
            obi.writeObject(new Request(address.getHostAddress(),port+"",4));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("??????Order...");
        int i=-1;
        boolean over=false;
        while(!over) {
            System.out.println("HIHI");
            while (receiveUser != null) {//receiverUser?????????????????????????????????while???????????????receiveUser=null???while????????????????????????????????????-1???????????????????????????while????????????????????????????????????????????????while????????????receiveUser????????????
                for (String w : allUsers) {
                    i++;
                    if (receiveUser.equals(w)) {
                        receiveUser = null;
                        over = true;
                        System.out.println("?????????Order???"+i);
                        break;
                    }
                }
            }
        }
        System.out.println("???ReceiveThread??????????????????Order???"+i);
        //??????????????????
        if(order!=i) {
            StackPane temp0 = (StackPane) (userList.group.get(i));
            StackPane temp1 = (StackPane) (temp0.getChildren().get(temp0.getChildren().size() - 1));
            Circle temp2 = (Circle) temp1.getChildren().get(0);
            temp2.setStroke(Color.GOLD);
            temp2.setStrokeType(StrokeType.OUTSIDE);
            temp2.setStrokeWidth(3);
            temp2.setStroke(Color.web("#f58c23"));
            KeyValue v0=new KeyValue(temp2.strokeProperty(), new Color(((Color)(temp2.getStroke())).getRed(),((Color)(temp2.getStroke())).getGreen(),((Color)(temp2.getStroke())).getBlue(),0));
            KeyFrame f0=new KeyFrame(Duration.millis(700),v0);
            Timeline l=new Timeline(f0);
            l.setAutoReverse(true);
            l.setCycleCount(6);
            l.play();
//        temp2.setStrokeLineCap(StrokeLineCap.ROUND);
//        temp2.getStrokeDashArray().addAll(15.0,20.0);
        }
        return i;
    }

    public void updateUsers() {
        int i = userList.group.size();
        System.out.println(i);
        for (String w : allUsers) {
            if (i <= 0) {
                mainFace.add(new ChatInterface(802.61,380.52,Color.WHITE,20,20,48.21,Font.loadFont(FONT_LIB_B,24),28.18,125.05));
                Text t;
                if(w.length()>=2)t=createText(w.substring(0, 2), Color.WHITE, Font.loadFont(FONT_LIB_H, 15), 0, 0, false, null);
                else t=createText(w, Color.WHITE, Font.loadFont(FONT_LIB_H, 15), 0, 0, false, null);
                Circle c = new Circle(24);
                if(!w.equals(connectedUser)) {
                    c.setFill(mainColor2);
                }else c.setFill(Color.web("#ff7373"));
                StackPane s = new StackPane(c, t);
                userList.add(s);
                StackPane temp=userList.group.getLast();//????????????????????????????????????
                handlers.add(new Runnable() {
                    public void run() {
                        order=userList.group.indexOf(temp);
                        switchAimUser();
//                        switchAimUser(p);
//                        p++;
                    }
                });//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                //?????????userList.group.size()-1???????????????????????????????????????????????????userList.group.size()-1??????????????????
                //?????????????????????????????????????????????????????????????????????.
                new Thread(new ReceiveThread(gram)).start();
                System.out.println("?????????order:"+(userList.group.size()-1)+" handlers ?????????"+handlers.size());
//                userIcon.add(s);
            }
            i--;
        }
        System.out.println(userList.group.size());
//        Platform.runLater(()->{
//            userList.<StackPane>add(userIcon,true);
        Platform.runLater(() -> userList.updateTo(userListPane));
        userList.setPressHandler(handlers);
    }
//    }

    public double sPointerX=0, sPointerY=0;
    public void dragWindow(Stage primaryStage){
        mainPane.setOnMousePressed(e -> {
            sPointerX = e.getSceneX();
            sPointerY = e.getSceneY();
        });
        mainPane.setOnMouseDragged(e -> {
            primaryStage.setX(e.getScreenX() - sPointerX);
            primaryStage.setY(e.getScreenY() - sPointerY);
        });
    }
    public void start(Stage primaryStage){
        mainPane.setPrefSize(858,609);
        mainPane.setStyle(paneStyle+"-fx-border-color: #C9C9C9");
//        mainPane.setEffect(new DropShadow(12,0,0,new Color(0,0,0,0.25)));
        prepareLogin();
        mainPane.getChildren().addAll(loginPane,closeButton.trueButton);
        mainScene.setFill(Color.TRANSPARENT);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        dragWindow(primaryStage);
        //?????????????????????????????????????????????????????????????????????
        primaryStage.setOnCloseRequest(e-> close());
    }
}