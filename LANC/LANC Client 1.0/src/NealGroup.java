import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.LinkedList;
import java.util.List;

public class NealGroup{
    public Color backColor,vanishColor;
    public double width,height,arc;
    public double initX,initY,gap;
    public LinkedList<StackPane> group=new LinkedList<StackPane>();
    Scene scene;
    public NealGroup(Color backColor, Color vanishColor, double width, double height, double arc, double initX, double initY, double gap, Scene mainScene){
        this.backColor=backColor;
        this.width=width;
        this.height=height;
        this.arc=arc;
        this.vanishColor=vanishColor;
        this.initY=initY;
        this.initX=initX;
        this.gap=gap;
        this.scene=mainScene;
    }
    public void add(Node...nodes){
        for(Node w:nodes){
            Rectangle back=new Rectangle(width,height,vanishColor);
            back.setArcHeight(arc);
            back.setArcWidth(arc);
            StackPane button=new StackPane(back,w);
            button.setPrefSize(width,height);
            button.setOnMouseEntered(e->scene.setCursor(Cursor.HAND));
            button.setOnMouseExited(e->scene.setCursor(Cursor.DEFAULT));
            group.add(button);
        }
        setHArrangement();
    }
    public <U extends Node> void add(List<U> nodes){//这里使用泛型方法，可以应对不同类型的列表。
//        if(clear)group.clear();
        for(U w:nodes){
            Rectangle back=new Rectangle(width,height,vanishColor);
            back.setArcHeight(arc);
            back.setArcWidth(arc);
            StackPane button=new StackPane(back,w);
            button.setPrefSize(width,height);
            button.setOnMouseEntered(e->scene.setCursor(Cursor.HAND));
            button.setOnMouseExited(e->scene.setCursor(Cursor.DEFAULT));
            group.add(button);
        }
        setHArrangement();
    }
    private void setHArrangement(){
        int i=0;
        for(StackPane w:group){
            w.setLayoutX(initX+(w.getPrefWidth()+gap)*i);
            w.setLayoutY(initY);
            i++;
        }
    }
    public void addTo(Pane aimPane){
        aimPane.getChildren().addAll(group);
    }
    public void updateTo(Pane aimPane){
        for(StackPane w:group){
            if(!aimPane.getChildren().contains(w)){
                aimPane.getChildren().add(w);
            }
        }
    }
    public void setPressHandler(Runnable...handler){
        int i=0;
        for(StackPane w:group){
            int temporaryI = i;//通过在循环中重新新建一个final的临时变量可以解决内部类无法使用非final局部变量的问题。（一次循环之后该循环体内新建的临时常量即消失）
            //BASIC:如何处理按钮颜色互斥？
            w.setOnMousePressed(e->{
                for(StackPane w2:group){
                    if(w2!=w)((Rectangle)(w2.getChildren().get(0))).setFill(vanishColor);
                }
                ((Rectangle)(w.getChildren().get(0))).setFill(backColor);
                new Thread(handler[temporaryI]).start();
            });
            i++;
        }
    }
    public void setPressHandler(List<Runnable> handler){
        int i=0;
        for(StackPane w:group){
            int temporaryI = i;//通过在循环中重新新建一个final的临时变量可以解决内部类无法使用非final局部变量的问题。（一次循环之后该循环体内新建的临时常量即消失）
            //BASIC:如何处理按钮颜色互斥？
            w.setOnMousePressed(e->{
                for(StackPane w2:group){
                    if(w2!=w)((Rectangle)(w2.getChildren().get(0))).setFill(vanishColor);
                }
                ((Rectangle)(w.getChildren().get(0))).setFill(backColor);
                new Thread(handler.get(temporaryI)).start();
            });
            i++;
        }
    }
}