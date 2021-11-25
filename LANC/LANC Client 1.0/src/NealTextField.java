import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class NealTextField {
    TextField trueField;
    StackPane nealTextField;
    public NealTextField(String promptText, double w, double h, int column,double arc,double strokeSize,Color strokeColor,Color fieldColor,Font textFont,Color highlightColor){
        trueField=new TextField();
        trueField.setPrefSize(w+5,h);
        trueField.setPromptText(promptText);
        trueField.setFont(textFont);
        trueField.setPrefColumnCount(column);
        trueField.lengthProperty().addListener(observable -> {
            String old=trueField.getText();
            if(trueField.getText().length()>column)trueField.setText(old.substring(0,column));
        });
//        trueField.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        trueField.setStyle("-fx-background-color: rgba(1,1,1,0);"+
                "-fx-text-fill: rgb("+strokeColor.getRed()*255+","+strokeColor.getGreen()*255+","+strokeColor.getBlue()*255+");"+
                "-fx-highlight-fill:  rgb("+highlightColor.getRed()*255+","+highlightColor.getGreen()*255+","+highlightColor.getBlue()*255+");");
        Rectangle fakeField=new Rectangle(w,h,fieldColor);
        //注意stroke的用法！
        fakeField.setStyle("-fx-stroke-width: "+strokeSize+";" +
                "-fx-stroke: rgb("+strokeColor.getRed()*255+","+strokeColor.getGreen()*255+","+strokeColor.getBlue()*255+");");
        fakeField.setArcHeight(arc);
        fakeField.setArcWidth(arc);
        nealTextField=new StackPane(fakeField,trueField);
    }
    public void setPromptText(String promptText){
        trueField.setPromptText(promptText);
    }
    public void setEditable(boolean boo){
        if(boo)trueField.setEditable(true);
        else trueField.setEditable(false);
    }
    public String getText(){
        return trueField.getText();
    }
    public StackPane getField(){
        return nealTextField;
    }
}
