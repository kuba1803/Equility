package GUI;

import Model.Card;
import Model.Deck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Created by nibylev on 06.06.15.
 */


public class TrainingController implements Initializable{
    @FXML
    ImageView hero1, hero2, flop1, flop2, flop3;

    String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
    String[] suits = {"c", "d", "h", "s"};

    @FXML
    private void handleNext(ActionEvent event) {
        Deck deck = new Deck();
        deck.shuffle();
        Card card = deck.drawACard();
        Image image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
        hero1.setImage(image);
        card = deck.drawACard();
        image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
        hero2.setImage(image);
        card = deck.drawACard();
        image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
        flop1.setImage(image);
        card = deck.drawACard();
        image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
        flop2.setImage(image);
        card = deck.drawACard();
        image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
        flop3.setImage(image);
    }


    //(A[23456789TJQKA]|K[23456789TJQK]|Q[23456789TJQ]|J[23456789TJ]|T[23456789T]|9[23456789]|8[2345678]|7[234567]|6[[23456]|5[2345]|4[234]|3[23]|2)

    boolean isPlusRange (String hand){
        return Pattern.matches("((A[23456789TJQK]|K[23456789TJQ]|Q[23456789TJ]|J[23456789T]|T[23456789]|9[2345678]|8[234567]|7[23456]|6[2345]|5[234]|4[23]|3[2])([so]\\+?))" , hand);
    }

    boolean isPlainHand(String hand){
        return Pattern.matches("((A[23456789TJQK]|K[23456789TJQ]|Q[23456789TJ]|J[23456789T]|T[23456789]|9[2345678]|8[234567]|7[23456]|6[2345]|5[234]|4[23]|3[2])[so])|(22|33|44|55|66|77|88|99|TT|JJ|QQ|KK|AA)", hand);
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
        boolean pre = Pattern.matches("((A[23456789TJQK]|K[23456789TJQ]|Q[23456789TJ]|J[23456789T]|T[23456789]|9[2345678]|8[234567]|7[23456]|6[2345]|5[234]|4[23]|3[2])[so])-((A[23456789TJQK]|K[23456789TJQ]|Q[23456789TJ]|J[23456789T]|T[23456789]|9[2345678]|8[234567]|7[23456]|6[2345]|5[234]|4[23]|3[2])[so])", hand);
        if(!pre)
            return false;
        if(hand.charAt(0)!=hand.charAt(4))
            return false;
        if(hand.charAt(2)!=hand.charAt(6))
            return false;
        if(toInt(hand.charAt(1))>=toInt(hand.charAt(5)))
            return false;
        return true;
    }

    boolean isPairPlus(String hand){
        return Pattern.matches("(22|33|44|55|66|77|88|99|TT|JJ|QQ|KK)\\+?", hand);

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
            for(int i=toInt(range.charAt(1)); i<=toInt(range.charAt(0)); i++)
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
    TextField input;
    // 22-AA, 22  , 53s,63o-65o   A4s+

    @FXML
    Button checkButton;

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
        ArrayList<String> hands = new ArrayList<>();
        String delims = "[, ]+";
        String[] tokens = input.getText().split(delims);
        for(String e: tokens){
            if(!isProperRange(e)) {
                checkButton.setDisable(true);
                return;
            }
            hands.addAll(rangeToHands(e));
        }
        System.out.println(hands);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkButton.setDisable(true);
        System.out.printf("initializing");
        Deck deck = new Deck();
        deck.shuffle();
        Card card = deck.drawACard();
        Image image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
        hero1.setImage(image);
        card = deck.drawACard();
        image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
        hero2.setImage(image);
        card = deck.drawACard();
        image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
        flop1.setImage(image);
        card = deck.drawACard();
        image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
        flop2.setImage(image);
        card = deck.drawACard();
        image = new Image(getClass().getResourceAsStream("/fxml/" + card.rank + card.suit + ".png"));
        flop3.setImage(image);

    }
}
