

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class NealButton {
    StackPane trueButton;
    Text name;
    Image icon;
    Rectangle fakeButton;
    EventHandler<MouseEvent> handler;
    public NealButton(Scene scene, double w, double h, double arc, Color buttonColor, Color actionColor , String buttonText, Font font,Color textColor, double x, double y, EventHandler<MouseEvent> e){
        fakeButton =new Rectangle(w,h);
        name=new Text(buttonText);
        trueButton =new StackPane(fakeButton,name);
        name.setFont(font);
        name.setFill(textColor);
        fakeButton.setArcHeight(arc);
        fakeButton.setArcWidth(arc);

        fakeButton.setFill(buttonColor);
        trueButton.setLayoutX(x);
        trueButton.setLayoutY(y);
        KeyValue oldValue=new KeyValue(fakeButton.fillProperty(),actionColor);
        KeyFrame oldFrame=new KeyFrame(Duration.millis(200),oldValue);
        Timeline line0=new Timeline(oldFrame);
        KeyValue newValue=new KeyValue(fakeButton.fillProperty(),buttonColor);
        KeyFrame newFrame=new KeyFrame(Duration.millis(200),newValue);
        Timeline line1=new Timeline(newFrame);
        trueButton.setOnMouseEntered(event -> {
            scene.setCursor(Cursor.HAND);
            line1.stop();
            line0.play();
        });
        trueButton.setOnMouseExited(event -> {
            scene.setCursor(Cursor.DEFAULT);
            line0.stop();
            line1.play();
        });
        trueButton.setOnMouseClicked(e);
    }
    public NealButton(Scene scene,double w,double h,double arc,Color buttonColor,Color actionColor,Image icon,double iconW,double x,double y,EventHandler<MouseEvent> e){
        fakeButton =new Rectangle(w,h);
        this.icon=icon;
        ImageView imageView=new ImageView(icon);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(iconW);
        trueButton =new StackPane(fakeButton,imageView);
        fakeButton.setArcHeight(arc);
        fakeButton.setArcWidth(arc);
        fakeButton.setFill(buttonColor);
        trueButton.setLayoutX(x);
        trueButton.setLayoutY(y);
        KeyValue oldValue=new KeyValue(fakeButton.fillProperty(),actionColor);
        KeyFrame oldFrame=new KeyFrame(Duration.millis(200),oldValue);
        Timeline line0=new Timeline(oldFrame);
        KeyValue newValue=new KeyValue(fakeButton.fillProperty(),buttonColor);
        KeyFrame newFrame=new KeyFrame(Duration.millis(200),newValue);
        Timeline line1=new Timeline(newFrame);
        trueButton.setOnMouseEntered(event -> {
            scene.setCursor(Cursor.HAND);
            line1.stop();
            line0.play();
        });
        trueButton.setOnMouseExited(event -> {
            scene.setCursor(Cursor.DEFAULT);
            line0.stop();
            line1.play();
        });
        this.handler=e;
        trueButton.setOnMouseClicked(e);
    }
    public StackPane getTrueButton(){
        return trueButton;
    }
    public Text getName(){
        return name;
    }
    public void setDisable(boolean disable){
        if(disable)trueButton.setOnMouseClicked(e->{});
        else trueButton.setOnMouseClicked(handler);
    }
}

