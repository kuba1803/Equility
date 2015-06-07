package GUI;

import Model.Card;
import Model.CountingOdds;
import Model.Deck;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.Math.abs;

/**
 * Created by nibylev on 06.06.15.
 */


public class TrainingController implements Initializable{
    @FXML
    ImageView hero1, hero2, flop1, flop2, flop3;
    @FXML
    Slider equitySlider;
    @FXML
    TextField input;
    @FXML
    Button checkButton;
    @FXML
    AnchorPane anchor;

    private ArrayList<Card> heroCards, flopCards;
    private ArrayList<Double> absoluteDifferencesInSession = new ArrayList<>();
    private ArrayList<Double> relativeDifferencesInSession = new ArrayList<>();

    final static String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
    final static String[] suits = {"c", "d", "h", "s"};

    void getNewCardsToTrainOn() {
        heroCards = new ArrayList<>();
        flopCards = new ArrayList<>();
        Deck deck = new Deck();
        deck.shuffle();
        Card card;
        Image image;
        boolean isSet;

        isSet = false;

        while(!isSet) {
            try {
                card = deck.drawACard();
                image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
                hero1.setImage(image);
                isSet = true;
                heroCards.add(card);
            } catch (Exception e) {
            }
        }
        isSet = false;
        while(!isSet) {
            try {
                card = deck.drawACard();
                image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
                hero2.setImage(image);
                isSet = true;
                heroCards.add(card);
            } catch (Exception e) {
            }
        }
        isSet = false;
        while(!isSet) {
            try {
                card = deck.drawACard();
                image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
                flop1.setImage(image);
                isSet = true;
                flopCards.add(card);
            } catch (Exception e) {
            }
        }
        isSet = false;
        while(!isSet) {
            try {
                card = deck.drawACard();
                image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
                flop2.setImage(image);
                isSet = true;
                flopCards.add(card);
            } catch (Exception e) {
            }
        }
        isSet = false;
        while(!isSet) {
            try {
                card = deck.drawACard();
                image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
                flop3.setImage(image);
                isSet = true;
                flopCards.add(card);
            } catch (Exception e) {
            }
        }

        for (Node n : anchor.getChildren()) {
            if (n.getId() != null && n.getId().equals("odds")) {
                javafx.scene.text.Text t = (javafx.scene.text.Text) n;
                t.setText("");
            }
            if (n.getId() != null && n.getId().equals("absdiff")) {
                javafx.scene.text.Text t = (javafx.scene.text.Text) n;
                t.setText("");
            }
            if (n.getId() != null && n.getId().equals("reldiff")) {
                javafx.scene.text.Text t = (javafx.scene.text.Text) n;
                t.setText("");
            }
        }

    }

    @FXML
    private void handleNext(ActionEvent event) {
        getNewCardsToTrainOn();
    }


    //(A[23456789TJQKA]|K[23456789TJQK]|Q[23456789TJQ]|J[23456789TJ]|T[23456789T]|9[23456789]|8[2345678]|7[234567]|6[[23456]|5[2345]|4[234]|3[23]|2)

    boolean isPlusRange (String hand){
        return Pattern.matches("((A[23456789TJQK]|K[23456789TJQA]|Q[23456789TJKA]|J[23456789TQKA]|T[23456789JQKA]|9[2345678TJQKA]|8[2345679TJQKA]|7[2345689TJQKA]|6[2345789TJQKA]|5[2346789TJQKA]|4[2356789TJQKA]|3[2456789TJQKA]|2[3456789TJQKA])([so]\\+))" , hand);
    }

    boolean isPlainHand(String hand){
        return Pattern.matches("((A[23456789TJQK]|K[23456789TJQA]|Q[23456789TJKA]|J[23456789TQKA]|T[23456789JQKA]|9[2345678TJQKA]|8[2345679TJQKA]|7[2345689TJQKA]|6[2345789TJQKA]|5[2346789TJQKA]|4[2356789TJQKA]|3[2456789TJQKA]|2[3456789TJQKA])[so])|(22|33|44|55|66|77|88|99|TT|JJ|QQ|KK|AA)", hand);
    }

    int toInt(String r){
        return Arrays.asList(ranks).indexOf(r);
    }
    int toInt(char r){
        return Arrays.asList(ranks).indexOf(String.valueOf(r));
    }

    boolean isPairRange(String hand){
        boolean pre = Pattern.matches("(22|33|44|55|66|77|88|99|TT|JJ|QQ|KK|AA)-(22|33|44|55|66|77|88|99|TT|JJ|QQ|KK|AA)", hand);
        if(!pre)
            return false;
        if(toInt(hand.charAt(0))>=toInt(hand.charAt(3)))
            return false;
        return true;

    }
    boolean isNormalRange(String hand){
        boolean pre = Pattern.matches("(((A[23456789TJQK]|K[23456789TJQA]|Q[23456789TJKA]|J[23456789TQKA]|T[23456789JQKA]|9[2345678TJQKA]|8[2345679TJQKA]|7[2345689TJQKA]|6[2345789TJQKA]|5[2346789TJQKA]|4[2356789TJQKA]|3[2456789TJQKA]|2[3456789TJQKA])[so])-((A[23456789TJQK]|K[23456789TJQA]|Q[23456789TJKA]|J[23456789TQKA]|T[23456789JQKA]|9[2345678TJQKA]|8[2345679TJQKA]|7[2345689TJQKA]|6[2345789TJQKA]|5[2346789TJQKA]|4[2356789TJQKA]|3[2456789TJQKA]|2[3456789TJQKA])[so]))", hand);

        if(!pre)
            return false;

        System.out.println("isNormalRange: " + hand);

        if(hand.charAt(0)!=hand.charAt(4))
            return false;
        if(hand.charAt(2)!=hand.charAt(6))
            return false;
        if(toInt(hand.charAt(1))>=toInt(hand.charAt(5)))
            return false;
        return true;
    }

    boolean isPairPlus(String hand){
        return Pattern.matches("(22|33|44|55|66|77|88|99|TT|JJ|QQ|KK)\\+", hand);

    }

    boolean isProperRange(String hand){
        if(isPlusRange(hand))
            return true;
        if(isPairRange(hand))
            return true;
        if(isPlainHand(hand))
            return true;
        if(isNormalRange(hand))
            return true;
        if(isPairPlus(hand))
            return true;
        return false;
    }

    List<String> rangeToHands(String range){
        ArrayList<String> result = new ArrayList<String>();
        if(isNormalRange(range)){
            for(int i = toInt(range.charAt(1)); i<=toInt(range.charAt(5)); i++)
                result.add(range.charAt(0)+ranks[i]+range.charAt(2));
        }
        if(isPlusRange(range)){
            for(int i=toInt(range.charAt(1)); i<toInt(range.charAt(0)); i++)
                result.add(range.charAt(0)+ranks[i]+range.charAt(2));
        }

        if(isPlainHand(range)){
            result.add(range);
        }

        if(isPairRange(range)){
            for(int i=toInt(range.charAt(1)); i<=toInt(range.charAt(3)); i++)
                result.add(ranks[i]+ranks[i]);
        }
        if(isPairPlus(range)){
            for(int i=toInt(range.charAt(1)); i<=12; i++ )
                result.add(ranks[i]+ranks[i]);
        }
        return result;
    }


    @FXML
    public void handleRangeInput(KeyEvent event){
        String delims = "[, ]+";
        String[] tokens = input.getText().split(delims);
        for(String e: tokens){
            if(!isProperRange(e)) {
                checkButton.setDisable(true);
                return;
            }
        }
        checkButton.setDisable(false);
    }


    @FXML
    public void handleCheck(ActionEvent event){

        System.out.println(flopCards);
        System.out.println(heroCards);

        ArrayList<String> hands = new ArrayList<>();
        String delims = "[, ]+";
        String[] tokens = input.getText().split(delims);
        for(String e: tokens){
            if(!isProperRange(e)){
                checkButton.setDisable(true);
                return;
            }
            System.out.print(e + " : ");
            hands.addAll(rangeToHands(e));
        }

        HashSet<String> uniqueHands = new HashSet<String>(hands);
        HashMap<String, Double> calculatedOdds = CountingOdds.countTraining(heroCards, uniqueHands, flopCards);

        double myEstimation = equitySlider.getValue();
        double realEstimation = calculatedOdds.get("Hero");
        double absoluteDifference = abs(myEstimation - realEstimation);
        double relativeDifference = 0.0;
        double averageAbsoluteDifference = 0.0;
        double averageRelativeDifference = 0.0;

        relativeDifferencesInSession.add(relativeDifference);
        absoluteDifferencesInSession.add(absoluteDifference);

        for(Double d : absoluteDifferencesInSession) {
            averageAbsoluteDifference += d;
        }
        for(Double d : relativeDifferencesInSession) {
            averageRelativeDifference += d;
        }


        averageAbsoluteDifference /= ((double)absoluteDifferencesInSession.size());

        averageRelativeDifference /= ((double)relativeDifferencesInSession.size());


        for (Node n : anchor.getChildren()) {
            if (n.getId() != null && n.getId().equals("odds")) {
                javafx.scene.text.Text t = (javafx.scene.text.Text) n;
                t.setText(String.format("%.1f", realEstimation) + "%");
            }
            if (n.getId() != null && n.getId().equals("absdiff")) {
                javafx.scene.text.Text t = (javafx.scene.text.Text) n;
                t.setText(String.format("%.1f", absoluteDifference) + "%");
            }
            if (n.getId() != null && n.getId().equals("reldiff")) {
                javafx.scene.text.Text t = (javafx.scene.text.Text) n;
                t.setText(String.format("%.1f", relativeDifference) + "%");
            }
            if (n.getId() != null && n.getId().equals("saad")) {
                javafx.scene.text.Text t = (javafx.scene.text.Text) n;
                t.setText(String.format("%.1f", averageAbsoluteDifference) + "%");
            }
            if (n.getId() != null && n.getId().equals("sard")) {
                javafx.scene.text.Text t = (javafx.scene.text.Text) n;
                t.setText(String.format("%.1f", averageRelativeDifference) + "%");
            }
        }

        System.out.println();
        System.out.println(input.getText());
        System.out.println(hands);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        equitySlider.setMin(0);
        equitySlider.setMax(100);
        equitySlider.setValue(0);
        equitySlider.setShowTickLabels(true);
        equitySlider.setShowTickMarks(true);
        equitySlider.setMajorTickUnit(50);
        equitySlider.setMinorTickCount(5);
        equitySlider.setBlockIncrement(10);
        equitySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                for (Node n : anchor.getChildren()) {
                    if (n.getId() != null && n.getId().equals("lel")) {
                        javafx.scene.text.Text t = (javafx.scene.text.Text) n;
                        t.setText(String.format("%.1f", newValue) + "%");
                    }
                }
            }
        });

        checkButton.setDisable(true);

        getNewCardsToTrainOn();
    }

    @FXML
    private void closeWindowButtonAction(ActionEvent event) {
        ((Stage)hero1.getScene().getWindow()).close();
    }

    @FXML
    private void closePlatformButtonAction(ActionEvent event) {
        Platform.exit();
    }
}
