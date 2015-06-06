/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Controller.MainControler;
import equility.MainApp;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;
import java.util.Iterator;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


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
    Group equityDisplays;

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
    Pane displayPane;

    @FXML
    private void handleMouseEntered(MouseEvent event) {
        System.out.println("mma re");
        displayPane.setStyle("-fx-background-color: transparent;");
        displayPane.setPrefSize(200, 200);

        actual = (Label) event.getSource();
        String tempRange = contr.getRange(actual.getText());

        for (int row = 0; row < 13; row++) {
            for (int col = 0; col < 13; col++) {
                final RangeRect rectangle = new RangeRect(25, 25);

                String h = null;
                if (row > col) {
                    h = intToRank(row) + intToRank(col) + "s ";
                } else if (row < col) {
                    h = intToRank(col) + intToRank(row) + "o ";
                } else {
                    h = intToRank(row) + intToRank(row) + " ";
                }

                final Text handName = new Text(h);
                if (tempRange.contains(h)) {

                    rectangle.setFill(Paint.valueOf("Red"));
                } else {
                    rectangle.setFill(Paint.valueOf("steelblue"));
                }
                rectangle.setStroke(Paint.valueOf("orange"));

                rectangle.col = col;
                rectangle.row = row;
                rectangle.setX(100 + col * 25);

                rectangle.setY(100 + row * 25);
                displayPane.getChildren().add(rectangle);

            }
        }

        displayPane.setMouseTransparent(true);
        displayPane.toFront();
        displayPane.setLayoutX(Math.min(((Label) event.getSource()).getLayoutX(), Panel1.getWidth() - 13 * 25 + 50));
        displayPane.setLayoutY(Math.min(((Label) event.getSource()).getLayoutY(), Panel1.getHeight() - 13 * 25 + 50));
        displayPane.setVisible(true);

    }

    @FXML
    private void handleMouseExited(MouseEvent event) {
        displayPane.setVisible(false);
    }

    @FXML
    public void handleTrainingStart(ActionEvent event) {

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/TrainingView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setResizable(false);
                //hide this current window (if this is whant you want
            //((Node)(event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleO_prog(ActionEvent event) {

    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    String[] positionList = {"eqMP3", "eqCO", "eqBU", "eqSB", "eqBB", "eqMP2"};

    @FXML
    private void handleButtonCompute(ActionEvent event) {
        Output.setText("");
        StringBuilder build = new StringBuilder();
        contr.getEquity();
        Random rn = new Random();
        for (Node n : equityDisplays.getChildren()) {
            if (n.getClass().equals(Text.class)) {
                
                Text t = (Text) n;
                
                double res = rn.nextDouble();
                t.setText(String.valueOf(round(res * 100, 2)) + "%");
                t.setFill(Color.rgb((int) (res * 255), 255 - (int) (res * 255), 30));
                build.append(t.getId().substring(2)+": " + t.getText()+" {"+contr.getRange(t.getId().substring(2))+"}"+"\n");
            }
        }
        Output.setText(build.toString());
        
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

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                Image image = new Image(getClass().getResourceAsStream("/fxml/" + ranks[j] + suits[i] + ".png"));
                ImageView v = new ImageView(image);
                v.addEventHandler(MouseEvent.MOUSE_CLICKED, new javafx.event.EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("cardClicked");
                        ((ImageView) clickTableCard.getSource()).setImage(((ImageView) event.getSource()).getImage());
                        Panel3.setVisible(false);
                        Panel3.setDisable(true);
                        Panel1.setDisable(false);

                    }
                });

                cardTable.add(v, j, i);
            }
        }

        //((ImageView) event.getSource()).setImage(image);
    }

    class RangeRect extends Rectangle {

        int row;
        int col;

        public RangeRect(double width, double height) {
            super(width, height);
        }

    }

    @FXML
    private void handleLabelPosition(MouseEvent event) {
        System.out.print(event.getSource());
        actual = (Label) event.getSource();
        Input.setText(contr.getRange(actual.getText()));
        Panel1.setDisable(true);
        Panel2.setVisible(true);
        Panel2.setDisable(false);

        class MarkHand implements javafx.event.EventHandler<MouseEvent> {

            RangeRect which;

            MarkHand(RangeRect which) {
                this.which = which;
            }

            @Override
            public void handle(MouseEvent t) {
                int row = which.row;
                int col = which.col;

                String h;
                if (row > col) {
                    h = intToRank(row) + intToRank(col) + "s ";
                } else if (row < col) {
                    h = intToRank(col) + intToRank(row) + "o ";
                } else {
                    h = intToRank(row) + intToRank(row) + " ";
                }
                if (which.getFill().equals(Color.RED)) {
                    System.out.println("removing " + h);
                    String oldText = Input.getText();
                    System.out.println(oldText);
                    Input.setText(oldText.replaceAll(h, ""));
                    System.out.println(Input.getText());
                    which.setFill(Color.STEELBLUE);
                } else {
                    Input.appendText(h);
                    which.setFill(Color.RED);
                }
            }
        }

//        for (int row = 0; row < 13; row++)
        int rectSize = 25;

        for (int row = 0; row < 13; row++) {
            for (int col = 0; col < 13; col++) {
                final RangeRect rectangle = new RangeRect(rectSize, rectSize);

                String h = null;
                if (row > col) {
                    h = intToRank(row) + intToRank(col) + "s ";
                } else if (row < col) {
                    h = intToRank(col) + intToRank(row) + "o ";
                } else {
                    h = intToRank(row) + intToRank(row) + " ";
                }

                //final Text handName = new Text(h.substring(0, 2));
                final Text handName = new Text(h);
                if (Input.getText().contains(h)) {

                    rectangle.setFill(Paint.valueOf("Red"));
                } else {
                    rectangle.setFill(Paint.valueOf("steelblue"));
                }
                rectangle.setStroke(Paint.valueOf("orange"));

                rectangle.setOnMouseClicked(new MarkHand(rectangle));
                rectangle.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        ((RangeRect) event.getSource()).startFullDrag();
                    }
                });
                rectangle.setOnMouseDragEntered(new MarkHand(rectangle));
                handName.setOnMouseClicked(new MarkHand(rectangle));
                rectangle.col = col;
                rectangle.row = row;
                rectangle.setX(col * rectSize);
                rectangle.setY(rectSize + row * rectSize);
                handName.setX(5 + col * rectSize);
                handName.setDisable(true);

                handName.setY(45 + row * rectSize);
                Panel2.getChildren().add(handName);

                Panel2.getChildren().add(rectangle);
                handName.toFront();

            }
        }

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
        Input.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.endsWith(" ")||newValue.isEmpty())
                {
                    RangeRect prev;
                    javafx.scene.text.Text o;
                    Iterator iter = Panel2.getChildren().iterator();
                    iter.next();
                    iter.next();
                    while(iter.hasNext())
                    {
                        prev= (RangeRect) iter.next();
                        o = (javafx.scene.text.Text)iter.next();
                        if(newValue.contains(o.getText()))
                        {
                            System.out.println(o.getText());
                            prev.setFill(Paint.valueOf("Red"));
                        }
                        else
                        {
                            prev.setFill(Paint.valueOf("steelblue"));
                        }
                        
                    }
                }
            }

           
           
        });

    }

}
