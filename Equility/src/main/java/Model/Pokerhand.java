package Model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Created by kaszubki on 30.05.15.
 */
public class Pokerhand {

    static class InvalidPokerhandException extends RuntimeException {}

    private final static String[] pokerhandNames = {"straightFlush", "quads", "fullHouse", "flush", "straight", "trips", "twoPairs", "pair", "highCard"};

    ArrayList<Card> myCards;
    int whichPokerhand;

    Pokerhand(ArrayList<Card> ple) {
        myCards = new ArrayList<Card>(ple);
        this.evaluate();
    }

    void evaluate() {
        for (int i = 0; i < pokerhandNames.length; i++) {
            boolean b = false;
            try {
                if ((boolean) this.getClass().getDeclaredMethod("is_" + pokerhandNames[i]).invoke(this)) {
                    for(int j = myCards.size() - 1; j >= 5; j--) {
                        myCards.remove(j);
                    }
                    whichPokerhand = i;
                    return;
                }
            } catch (Throwable t) {}
        }

        throw new InvalidPokerhandException();
    }

    boolean is_straightFlush() {
        Collections.sort(myCards, Card.CompareBySuit);

        for(int i = 0; i < 3; i++) {
            if(myCards.get(i).suit.equals(myCards.get(i + 4).suit)) {
                if(myCards.get(i).prank == myCards.get(i + 4).prank - 4) {
                    for (int j = 0; j < 5; j++) {
                        Collections.swap(myCards, j, i + j);
                    }
                    return true;
                }
            }
        }

        for(int i = 1; i < myCards.size() - 3; i++) {
            if(myCards.get(i).rank.equals("5") &&
                    myCards.get(i).prank == myCards.get(i + 3).prank - 3) {
                if(myCards.contains(new Card("A", myCards.get(i).suit))) {
                    for (int j = 0; j < 4; j++) {
                        Collections.swap(myCards, j, i + j);
                    }
                }
                myCards.set(4, new Card("A", myCards.get(i).suit));
                return true;
            }
        }

        return false;
    }
    boolean is_quads() { //done
        Collections.sort(myCards, Card.CompareByRankDecreasing);

        for(int i = 0; i < 4; i++) {
            if(myCards.get(i).rank.equals(myCards.get(i + 3).rank)) {
                for(int j = 0; j < 4; j++) {
                    Collections.swap(myCards, j, i + j);
                }
                Collections.sort(myCards.subList(4, 7), Card.CompareByRankDecreasing);
                return true;
            }
        }
        return false;
    }
    boolean is_fullHouse() { //done
        Collections.sort(myCards, Card.CompareByRankDecreasing);

        for (int i = 0; i < 5; i++) {
            if (myCards.get(i).rank.equals(myCards.get(i + 2).rank)) {
                for (int j = 0; j < 3; j++) {
                    Collections.swap(myCards, j, i + j);
                }
                for(int k = i + 3; k < 6; k++) {
                    if (myCards.get(k).rank.equals(myCards.get(k + 1).rank)) {
                        for (int l = 0; l < 2; l++) {
                            Collections.swap(myCards, 3 + l, k + l);
                        }
                        return true;
                    }
                }

                return false;
            }
        }
        return false;
    }
    boolean is_flush() { //done
        Collections.sort(myCards, Card.CompareBySuit);

        for(int i = 0; i < 3; i++) {
            if(myCards.get(i).suit.equals(myCards.get(i + 4).suit)) {
                for(int j = 0; j < 5; j++) {
                    Collections.swap(myCards, j, i + j);
                }
                return true;
            }
        }
        return false;
    }
    boolean is_straight() {

        ArrayList<Card> myCardsTmp = new ArrayList<Card>(myCards);
        Collections.sort(myCardsTmp, Card.CompareByRankDecreasing);

        for(int i = 6; i > 0; i--) {
            if(myCardsTmp.get(i).rank.equals(myCardsTmp.get(i - 1).rank)) {
                myCardsTmp.remove(i);
            }
        }

        for(int i = 0; i < myCardsTmp.size() - 4; i++) {
            if(myCardsTmp.get(i).prank == myCardsTmp.get(i + 4).prank - 4) {
                for(int j = 0; j < 5; j++) {
                    Collections.swap(myCardsTmp, j, i + j);
                }
                myCards = myCardsTmp;
                return true;
            }
        }

        for(int i = 1; i < myCardsTmp.size() - 3; i++) {
            if(myCardsTmp.get(0).rank.equals("A") &&
                    myCardsTmp.get(i).rank.equals("5") &&
                    myCardsTmp.get(i).prank == myCardsTmp.get(i + 3).prank - 3) {
                Card c = myCardsTmp.get(0);
                for(int j = 0; j < 4; j++) {
                    Collections.swap(myCardsTmp, j, i + j);
                }
                myCardsTmp.set(4, c);
                myCards = myCardsTmp;
                return true;
            }
        }

        return false;
    }
    boolean is_trips() { //done
        Collections.sort(myCards, Card.CompareByRankDecreasing);

        for(int i = 0; i < 5; i++) {
            if(myCards.get(i).rank.equals(myCards.get(i + 2).rank)) {
                for(int j = 0; j < 3; j++) {
                    Collections.swap(myCards, j, i + j);
                }
                Collections.sort(myCards.subList(3, 7), Card.CompareByRankDecreasing);
                return true;
            }
        }
        return false;
    }
    boolean is_twoPairs() {
        Collections.sort(myCards, Card.CompareByRankDecreasing);

        for (int i = 0; i < 6; i++) {
            if (myCards.get(i).rank.equals(myCards.get(i + 1).rank)) {
                for (int j = 0; j < 2; j++) {
                    Collections.swap(myCards, j, i + j);
                }

                for(int k = i + 2; k < 6; k++) {
                    if (myCards.get(k).rank.equals(myCards.get(k + 1).rank)) {
                        for (int l = 0; l < 2; l++) {
                            Collections.swap(myCards, 2 + l, k + l);
                        }
                        Collections.sort(myCards.subList(4, 7), Card.CompareByRankDecreasing);
                        return true;
                    }
                }

                return false;
            }
        }
        return false;
    }
    boolean is_pair() { //done
        Collections.sort(myCards, Card.CompareByRankDecreasing);

        for(int i = 0; i < 6; i++) {
            if(myCards.get(i).rank.equals(myCards.get(i + 1).rank)) {
                for(int j = 0; j < 2; j++) {
                    Collections.swap(myCards, j, i + j);
                }
                Collections.sort(myCards.subList(2, 7), Card.CompareByRankDecreasing);
                return true;
            }
        }
        return false;
    }
    boolean is_highCard() { //done
        Collections.sort(myCards, Card.CompareByRankDecreasing);
        return true;
    }
}
