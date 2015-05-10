/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Controller.MainControler;
import equility.MainApp;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.Iterator;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author kuba1_000
 */
public class GUIController implements Initializable {

    private MainControler contr;
    Label actual;
    @FXML
    TextField Input;
    @FXML
    Pane Panel1;
    @FXML
    Pane Panel2;
    @FXML
    Pane Panel3;
    @FXML
    TextArea Output;
    @FXML
    BorderPane borderPane;

    @FXML
    ImageView flop1;
    @FXML
    ImageView flop2;
    @FXML
    ImageView flop3;
    @FXML
    ImageView turn;
    @FXML
    ImageView river;
    @FXML
    ImageView test;
    @FXML
    GridPane cardTable;
    
    @FXML
    Text eqMP3;
     @FXML
    Text eqCO;
      @FXML
    Text eqBU;
       @FXML
    Text eqSB;
        @FXML
    Text eqBB;
         @FXML
    Text eqMP2;
       
    
    
    @FXML
    GridPane handChart;

    @FXML
    private void handleButtonClear(ActionEvent event) {
        ObservableList list = Panel1.getChildren();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Object o = iter.next();
            System.out.println(o.getClass().getName());
            if (o.getClass().getName().equals("javafx.scene.control.Label")) {
                Label l = (Label) o;
                l.setStyle("-fx-border-width: 5; -fx-border-color: #000000; -fx-background-color: #8d8d8d");
            }
        }
        contr.Clear();
    }
    
    
    public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
}
    String[] positionList = {   "eqMP3", "eqCO","eqBU","eqSB","eqBB","eqMP2"};

    @FXML
    private void handleButtonCompute(ActionEvent event) {
        contr.getEquity();
        Random rn = new Random();
        for(Node n: Panel1.getChildren())
            if(n.getClass().equals(Text.class)){
                Text t = (Text)n;
                double res = rn.nextDouble();
                t.setText(String.valueOf(round(res*100, 2))+"%");
                t.setFill(Color.rgb((int)(res*255), 255-(int)(res*255), 30));
                
            }
        
        
        Output.setText("Obliczam");
    }

    public void display(String text) {
        Output.appendText(text);
    }
    
 String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
 String[] suits = {"c", "d", "h", "s"};
    public String intToRank(int r) {
       
        return ranks[r];
    }
    
  
    @FXML
    private void handleTableChoice(final MouseEvent clickTableCard) {
        Panel3.setVisible(true);
        Panel3.setDisable(false);
        Panel1.setDisable(true);
        
       for(int i=0; i<4; i++)
           for(int j=0; j<13; j++){
               Image image = new Image(getClass().getResourceAsStream("/fxml/"+ranks[j]+suits[i]+".png"));
               ImageView v = new ImageView(image);
               v.addEventHandler(MouseEvent.MOUSE_CLICKED,new  javafx.event.EventHandler<MouseEvent>(){

                   @Override
                   public void handle(MouseEvent event) {
                       System.out.println("cardClicked");
                       ((ImageView)clickTableCard.getSource()).setImage(((ImageView)event.getSource()).getImage());
                       Panel3.setVisible(false);
                       Panel3.setDisable(true);
                       Panel1.setDisable(false);
                      
                   }
               });
               
               cardTable.add(v, j  ,i);
           }
        
    
        //((ImageView) event.getSource()).setImage(image);
    }
    
    @FXML
    private void handleLabelPosition(MouseEvent event) {
        System.out.print(event.getSource());
        actual = (Label) event.getSource();
        Input.setText(contr.getRange(actual.getText()));
        Panel1.setDisable(true);
        Panel2.setVisible(true);
        Panel2.setDisable(false);
        handChart = new GridPane();

        class MarkHand implements javafx.event.EventHandler<MouseEvent> {

            Rectangle which;

            MarkHand(Rectangle which) {
                this.which = which;
            }

            @Override
            public void handle(MouseEvent t) {
                int row = GridPane.getRowIndex(which);
                int col = GridPane.getColumnIndex(which);
                String h = intToRank(row) + intToRank(col) + (row > col ? "s" : (row == col ? "" : "o"))+ " ";
                if(which.getFill().equals(Color.RED)){
                   String oldText = Input.getText();
                    Input.setText(oldText.replaceAll(h, ""));
                   which.setFill(Color.STEELBLUE);
                }else{
                    Input.appendText(h);
                    which.setFill(Color.RED);
                }
            }
        }
        

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                final Rectangle rectangle = new Rectangle(30, 15);
                
                String h = intToRank(j) + intToRank(i) + (j > i ? "s" : (j == i ? "" : "o"))+ " ";
                final Text handName = new Text(h);
                if(Input.getText().contains(h)){
                   
                    rectangle.setFill(Paint.valueOf("Red"));
                }else
                    rectangle.setFill(Paint.valueOf("steelblue"));
                rectangle.setStroke(Paint.valueOf("orange"));
                
                
               
                rectangle.setOnMouseClicked(new MarkHand(rectangle));
                handName.setOnMouseClicked(new MarkHand(rectangle));
                handChart.add(rectangle, j, i);
                handChart.add(handName, j, i);
            }
        }
        borderPane.setCenter(handChart);
        BorderPane.setAlignment(handChart, Pos.CENTER);

    }

    @FXML
    private void handlerConfirm(ActionEvent event) {
        String str = Input.getText();

        if (str.isEmpty()) {
            actual.setStyle("-fx-border-width: 5; -fx-border-color: #000000; -fx-background-color: #8d8d8d");
        } else {
            String pos = actual.getText();
            if (contr.checkRange(str)) {
                contr.setRange(pos, str);
                switch (pos) {
                    case "BB": {
                        actual.setStyle("-fx-border-width: 5; -fx-border-color: #000000; -fx-background-color: #ff0000");
                        break;
                    }
                    case "SB": {
                        actual.setStyle("-fx-border-width: 5; -fx-border-color: #000000; -fx-background-color: #ff0000");
                        break;
                    }
                    case "BU": {
                        actual.setStyle("-fx-border-width: 5; -fx-border-color: #000000; -fx-background-color: #5fff3b");
                        break;
                    }
                    case "CO": {
                        actual.setStyle("-fx-border-width: 5; -fx-border-color: #000000; -fx-background-color: #5fff3b");
                        break;
                    }
                    case "MP3": {
                        actual.setStyle("-fx-border-width: 5; -fx-border-color: #000000; -fx-background-color: #e1a81f");
                        break;
                    }
                    case "MP2": {
                        actual.setStyle("-fx-border-width: 5; -fx-border-color: #000000; -fx-background-color: #e1a81f");
                        break;
                    }
                }
            }
        }
        Panel2.setDisable(true);
        Panel2.setVisible(false);
        Panel1.setDisable(false);
        Input.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contr = MainApp.contr;

    }

}
