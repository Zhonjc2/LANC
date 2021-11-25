import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

enum Position {
    LEFT(true),RIGHT(false);
    private boolean pos;
    /*private */Position(boolean pos){//枚举构造方法默认私有
        this.pos=pos;
    }
    public boolean getPos(){
        return pos;
    }
}
public class ChatInterface{
    private double lastLayoutY;
    private final double gap;
    private final double bubbleHeight;
    private final Font messageFont;
    private final ScrollPane chatPane;

    public ChatInterface(double width,double visionHeight,Color backColor,double gap,double initPosY,double bubbleHeight,Font messageFont,double x,double y){
        Pane back=new Pane();
        back.setPrefSize(width,visionHeight);
        chatPane=new ScrollPane(back);
        chatPane.setPrefSize(width,visionHeight);
        chatPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatPane.setFitToWidth(true);
        chatPane.setStyle("-fx-background-color: rgb("+backColor.getRed()*255+","+backColor.getGreen()*255+","+backColor.getBlue()*255+");");
        back.setStyle("-fx-background-color: rgb("+backColor.getRed()*255+","+backColor.getGreen()*255+","+backColor.getBlue()*255+");");
        this.gap=gap;
        lastLayoutY=initPosY;
        this.bubbleHeight=bubbleHeight;
        this.messageFont=messageFont;
        chatPane.setLayoutX(x);
        chatPane.setLayoutY(y);
    }

    public void addMessage(String message, Color backColor, Paint textColor, double arc, Position pos){
//        System.out.println(chatPane.getVvalue());
        Text messageText=new Text("   "+message+"   ");
        messageText.setFont(messageFont);
        messageText.setFill(textColor);
        StackPane bubble=new StackPane(messageText);
//        bubble.setPrefSize(messageText.getText().length()*20,bubbleHeight);
        bubble.setPrefHeight(bubbleHeight);
        //无法获取StackPane的自动长度，只能通过文字个数来计算位置。
        //后来通过getBoundsInParent().getWidth()解决，它可以获取真实宽度。
        bubble.setStyle("-fx-background-color: rgb("+backColor.getRed()*255+","+backColor.getGreen()*255+","+backColor.getBlue()*255+");" +
                "-fx-background-radius: "+arc+";");
        Pane newPane=(Pane)(chatPane.getContent());
        if(pos.getPos()) {
            bubble.setLayoutX(0);
        }else{
                bubble.setLayoutX(newPane.getPrefWidth()-bubble.getBoundsInParent().getWidth()-5);
            //bubble.getWidth()获取无用，返回的是0。
        }
        lastLayoutY+=(gap+bubbleHeight);
        bubble.setLayoutY(lastLayoutY);
        if(lastLayoutY+bubbleHeight+gap>=newPane.getPrefHeight()){
            newPane.setPrefHeight(newPane.getPrefHeight()+bubbleHeight+gap+20);
        }
        newPane.getChildren().add(bubble);
        chatPane.setVvalue(1);
    }

    public void addMessage(String message, String stop1,String stop2, Paint textColor, double arc, Position pos){
        Text messageText=new Text("   "+message+"   ");
        messageText.setFont(messageFont);
        messageText.setFill(textColor);
        StackPane bubble=new StackPane(messageText);
        bubble.setPrefHeight(bubbleHeight);
        bubble.setStyle("-fx-background-color: linear-gradient(to right,"+stop1+","+stop2+");" +
                "-fx-background-radius: "+arc+";");
        Pane newPane=(Pane)(chatPane.getContent());
        if(pos.getPos()) {
            bubble.setLayoutX(0);
        }else{
            bubble.setLayoutX(newPane.getPrefWidth()-bubble.getBoundsInParent().getWidth()-5);
        }
        lastLayoutY+=(gap+bubbleHeight);
        bubble.setLayoutY(lastLayoutY);
        if(lastLayoutY+bubbleHeight+gap>=newPane.getPrefHeight()){
            newPane.setPrefHeight(newPane.getPrefHeight()+bubbleHeight+gap+20);
        }
        newPane.getChildren().add(bubble);
        chatPane.setVvalue(1);
    }

    public ScrollPane getChatPane(){
        return chatPane;
    }
}

