import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Snowy extends Application {
    Pane mainPane=new Pane();
    Rectangle2D screen=Screen.getPrimary().getBounds();
    double screenWidth=screen.getWidth()+50;
    double screenHeight=screen.getHeight()+50;
    public static final int QUANTITY=3000;
    public void start(Stage primaryStage){
        Circle[] snowFlower=new Circle[QUANTITY];
//        Rectangle[] snowFlower=new Rectangle[2000];
//        Text[] snowFlower=new Text[2000];
        PathTransition[] path=new PathTransition[QUANTITY];
        FadeTransition[] fade=new FadeTransition[QUANTITY];
        int i=0;
        for(PathTransition ignored :path){
            double x=Math.random()*screenWidth;
            Line a=new Line(x,-2000*Math.random(),x-Math.random()*500,screenHeight);
            snowFlower[i]=new Circle(25,new Color(Math.random(),Math.random(),Math.random(),1));
            mainPane.getChildren().add(snowFlower[i]);
            fade[i]=new FadeTransition();
            fade[i].setNode(snowFlower[i]);
            fade[i].setFromValue(0.2);
            fade[i].setToValue(1);
            fade[i].setAutoReverse(true);
            fade[i].setCycleCount(Animation.INDEFINITE);
            fade[i].setDuration(Duration.millis(1000));
            fade[i].setDelay(Duration.millis(Math.random()*5000));
            fade[i].play();
            path[i]=new PathTransition();
            path[i].setNode(snowFlower[i]);
            path[i].setPath(a);
            path[i].setDelay(Duration.millis(Math.random()*200));
            path[i].setDuration(Duration.millis((1+Math.random())*10000));
            path[i].setCycleCount(Animation.INDEFINITE);
            path[i].play();
        }
        mainPane.setStyle("-fx-background-color: TRANSPARENT");
        mainPane.setPrefSize(screenWidth,screenHeight);
        Scene mainScene=new Scene(mainPane);
        mainScene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(mainScene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
}
