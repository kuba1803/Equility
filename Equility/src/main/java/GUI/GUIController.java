/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Controller.MainControler;
import equility.MainApp;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
    TextArea Output;
    @FXML
    BorderPane borderPane;

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

    @FXML
    private void handleButtonCompute(ActionEvent event) {
        contr.getEquity();
        Output.setText("Obliczam");
    }

    public void display(String text) {
        Output.appendText(text);
    }

    public String intToRank(int r) {
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
        return ranks[r];
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
                    Input.appendText(intToRank(row) + intToRank(col) + (row > col ? "s" : (row == col ? "" : "o")) + " ");
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
