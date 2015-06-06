/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Controller.MainControler;
import equility.MainApp;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
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


    /*
        handleButtonClear czysci zarowno range, jak i boarda
     */
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
        for (String key : clickedBoard.keySet()) {
            ImageView needChange = clickedBoard.get(key);
            needChange.setImage(new Image(getClass().getResourceAsStream("/fxml/revers.png")));
        }
        clickedBoard.clear();
        contr.Clear();
        Output.clear();

        for (Node n : equityDisplays.getChildren()) {
            if (n.getClass().equals(Text.class)) {
                Text t = (Text) n;
                t.setText("zero range");
                t.setVisible(false);
            }
        }
    }

    @FXML
    Pane displayPane;

    /*
    obsuguje najechanie myszka na goscia
     */
    @FXML
    private void handleMouseEntered(MouseEvent event) {

        displayPane.setStyle("-fx-background-color: transparent;");
        double scale = 2.5;
        displayPane.setPrefSize(20 / scale, 20 / scale);

        actual = (Label) event.getSource();
        String tempRange = contr.getRange(actual.getText());
        System.out.println(tempRange);

        for (int row = 0; row < 13; row++) {
            for (int col = 0; col < 13; col++) {
                final RangeRect rectangle = new RangeRect(25 / scale, 25 / scale);

                String h = null;
                if (row > col)
                    h = intToRank(col) + intToRank(row) + "s";
                else if (row < col)
                    h = intToRank(row) + intToRank(col) + "o";
                else
                    h = intToRank(row) + intToRank(row);

                final Text handName = new Text(h);
                if (tempRange.contains(h)) {
                    rectangle.setFill(Paint.valueOf("Red"));
                } else {
                    rectangle.setFill(Paint.valueOf("steelblue"));
                }
                rectangle.setStroke(Paint.valueOf("steelblue"));


                rectangle.col = col;
                rectangle.row = row;
                rectangle.setX((160) + (col * 25) / scale);

                rectangle.setY((50) + (row * 25) / scale);
                displayPane.getChildren().add(rectangle);


            }
        }


        displayPane.setMouseTransparent(true);
        displayPane.toFront();
        displayPane.setLayoutX(Math.min(((Label) event.getSource()).getLayoutX(), Panel1.getWidth() - (13 * 25 + 50) / scale));
        displayPane.setLayoutY(Math.min(((Label) event.getSource()).getLayoutY(), Panel1.getHeight() - (13 * 25 + 50) / scale));
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
            stage.setTitle("My New Training");
            stage.setScene(new Scene(root));
            stage.show();

            //hide this current window (if this is whant you want
            //((Node)(event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /*
    do obliczania oddsow
     */
    @FXML
    private void handleButtonCompute(ActionEvent event) {

        HashMap<String, String> ranges = new HashMap<String, String>();

        for (Node n : equityDisplays.getChildren()) {
            if (n.getClass().equals(Text.class)) {
                String id = n.getId().substring(2);
                String rejndz = contr.getRange(id);
                if (rejndz != null && rejndz.length() > 0) {
                    ranges.put(id, rejndz);
                }
            }
        }

        if (ranges.size() < 2) {
            Output.appendText("inefficient number of players\n");
            return;
        }

        HashMap<String, Double> odds = contr.getEquity(ranges, new HashSet<String>(clickedBoard.keySet()));

        for (Node n : equityDisplays.getChildren()) {
            if (n.getClass().equals(Text.class)) {
                String id = n.getId().substring(2);
                if (odds.keySet().contains(id)) {
                    Text t = (Text) n;
                    t.setText(round(odds.get(id), 2) + "%");
                    t.setFill(Color.valueOf("black"));
                    t.setVisible(true);
                } else {
                    Text t = (Text) n;
                    t.setText("zero range");
                    t.setVisible(false);
                }
            }
        }
    }

    public void display(String text) {
        Output.appendText(text);
    }

    String[] ranks = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
    String[] suits = {"s", "h", "d", "c"};

    public String intToRank(int r) {
        return ranks[r];
    }


    Map<String, ImageView> clickedBoard = new HashMap<String, ImageView>();

    /*
    obsluga boardu
    wszystkie karty z boardu sa w haszmapie clickedBoard
     */
    @FXML
    private void handleTableChoice(final MouseEvent clickTableCard) {
        Panel3.setVisible(true);
        Panel3.setDisable(false);
        Panel1.setDisable(true);

        for (String key : clickedBoard.keySet()) {
            if (clickedBoard.get(key).equals(((ImageView) clickTableCard.getSource()))) {
                clickedBoard.remove(key);
                ((ImageView) clickTableCard.getSource()).setImage(new Image(getClass().getResourceAsStream("/fxml/revers.png")));
                break;
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                Image image;

                final String key = ranks[j] + suits[i];
                final String cardSource = "/fxml/" + ranks[j] + suits[i] + ".png";

                if (clickedBoard.get(key) == null) {
                    image = new Image(getClass().getResourceAsStream(cardSource));
                } else {
                    image = new Image(getClass().getResourceAsStream("/fxml/revers.png"));
                }

                ImageView v = new ImageView(image);
                v.addEventHandler(MouseEvent.MOUSE_CLICKED, new javafx.event.EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        //System.out.println("cardClicked");

                        if (clickedBoard.get(key) == null) {
                            ((ImageView) clickTableCard.getSource()).setImage(((ImageView) event.getSource()).getImage());
                            clickedBoard.put(key, ((ImageView) clickTableCard.getSource()));
                            System.out.println(((ImageView) clickTableCard.getSource()));
                        } else {
                            ImageView needChange = clickedBoard.get(key);
                            clickedBoard.remove(key);
                            if (needChange.equals((ImageView) clickTableCard.getSource())) {
                                ((ImageView) clickTableCard.getSource()).setImage(new Image(getClass().getResourceAsStream("/fxml/revers.png")));
                            } else {
                                needChange.setImage(new Image(getClass().getResourceAsStream("/fxml/revers.png")));
                                ((ImageView) clickTableCard.getSource()).setImage(new Image(getClass().getResourceAsStream(cardSource)));
                                clickedBoard.put(key, ((ImageView) clickTableCard.getSource()));
                            }
                        }

                        Panel3.setVisible(false);
                        Panel3.setDisable(true);
                        Panel1.setDisable(false);
                    }
                });

                cardTable.add(v, 12 - j, i);
            }
        }
    }

    class RangeRect extends Rectangle {
        int row;
        int col;

        public RangeRect(double width, double height) {
            super(width, height);
        }

    }

    Map<String, String> ranges = new HashMap<String, String>();

    @FXML
    private void handleLabelPosition(MouseEvent event) {
        System.out.println(event.getSource());
        actual = (Label) event.getSource();
        Input.setText(contr.getRange(actual.getText()));

        Panel1.setDisable(true);
        Panel2.setVisible(true);
        Panel2.setDisable(false);
        System.out.println(actual.getText());

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
                    h = intToRank(col) + intToRank(row) + "s ";
                } else if (row < col) {
                    h = intToRank(row) + intToRank(col) + "o ";
                } else {
                    h = intToRank(row) + intToRank(row) + " ";
                }
                if (which.getFill().equals(Color.RED)) {
                    System.out.println("hello " + h);
                    String oldText = Input.getText();
                    Input.setText(oldText.replaceAll(h, ""));
                    which.setFill(Color.STEELBLUE);
                } else {
                    Input.appendText(h);
                    which.setFill(Color.RED);
                }
            }
        }

        for (int row = 0; row < 13; row++) {
            for (int col = 0; col < 13; col++) {
                final RangeRect rectangle = new RangeRect(25, 25);

                String h;
                if (row > col) {
                    h = intToRank(col) + intToRank(row) + "s ";
                } else if (row < col) {
                    h = intToRank(row) + intToRank(col) + "o ";
                } else {
                    h = intToRank(row) + intToRank(row) + " ";
                }

                final Text handName = new Text(h.substring(0, 2));

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
                rectangle.setX(col * 25);
                rectangle.setY(25 + row * 25);
                handName.setX(5 + col * 25);
                handName.setDisable(true);

                handName.setY(45 + row * 25);
                Panel2.getChildren().add(handName);

                Panel2.getChildren().add(rectangle);
                handName.toFront();

            }
        }

        ranges.remove(actual.getId());
        //ranges.put(actual.getId(), )
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
