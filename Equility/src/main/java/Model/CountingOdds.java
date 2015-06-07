package Model;

import sun.plugin.dom.html.ns4.HTMLAnchorCollection;

import java.util.*;

/**
 * Created by kaszubki on 30.05.15.
 */
public class CountingOdds {
    final static String[] suits = {"s", "h", "d", "c"};
    static int numberOfDrawings = 20000;
    static int actualNumberOfDrawings;
    static HashSet<Card> avaliableCards;

    static int factorial(int a) {
        int res = 1;
        for (int i = 2; i <= a; i++) res *= i;
        return res;
    }

    static void losuj(ArrayList<ArrayList<Hand>> ranges, ArrayList<Card> board, int punkt, int tab[]) {
        avaliableCards = new HashSet<Card>();
        for (String rank : Card.ranks) {
            for (String suit : suits) {
                avaliableCards.add(new Card(rank, suit));
            }
        }

        ArrayList<ArrayList<Card>> playersPokerhandsBuilders = new ArrayList<ArrayList<Card>>();

        for (int i = 0; i < ranges.size(); i++) {
            ArrayList<Card> tmp = new ArrayList<>();
            for (Card c : board) tmp.add(c);
            playersPokerhandsBuilders.add(tmp);
        }

        for (Card c : board) {
            avaliableCards.remove(c);
        }

        Random generator = new Random();

        ArrayList<Hand> chosenHands = new ArrayList<>();


        for (int i = 0; i < ranges.size(); i++) {
            chosenHands.add(ranges.get(i).get((generator.nextInt(ranges.get(i).size()))));
        }


        boolean didntFindFit = false;
        for (int i = 0; i < chosenHands.size(); i++) {
            Card c1 = chosenHands.get(i).c1;
            Card c2 = chosenHands.get(i).c2;
            if (avaliableCards.contains(c1) && avaliableCards.contains(c2)) {
                playersPokerhandsBuilders.get(i).add(c1);
                playersPokerhandsBuilders.get(i).add(c2);
                avaliableCards.remove(c1);
                avaliableCards.remove(c2);
            } else {
                didntFindFit = true;
            }
        }
        if (didntFindFit) {
            actualNumberOfDrawings--;
            return;
        }

        ArrayList<Card> arrayListOfAvaliableCards = new ArrayList<Card>(avaliableCards);

        Collections.shuffle(arrayListOfAvaliableCards);
        for (int i = 0; i < 5 - board.size(); i++) {
            for (int ii = 0; ii < ranges.size(); ii++) {
                playersPokerhandsBuilders.get(ii).add(arrayListOfAvaliableCards.get(i));
            }
        }

        ArrayList<Pokerhand> playersPokerhands = new ArrayList<Pokerhand>();
        for (int i = 0; i < ranges.size(); i++) {
            playersPokerhands.add(new Pokerhand(playersPokerhandsBuilders.get(i)));
        }

        Pokerhand theBestPokerhand = playersPokerhands.get(playersPokerhands.size() - 1);
        HashSet<Integer> winners = new HashSet<Integer>();
        winners.add(playersPokerhands.size() - 1);


        for (int i = 0; i < playersPokerhands.size() - 1; i++) {
            if (playersPokerhands.get(i).whichPokerhand < theBestPokerhand.whichPokerhand) {
                winners.clear();
                winners.add(i);
                theBestPokerhand = playersPokerhands.get(i);
            } else if (playersPokerhands.get(i).whichPokerhand == theBestPokerhand.whichPokerhand) {
                boolean notEqualPokerhands = false;
                for (int j = 0; j < 5; j++) {
                    if (playersPokerhands.get(i).myCards.get(j).prank < theBestPokerhand.myCards.get(j).prank) {
                        winners.clear();
                        winners.add(i);
                        theBestPokerhand = playersPokerhands.get(i);
                        notEqualPokerhands = true;
                        break;
                    }
                    if (playersPokerhands.get(i).myCards.get(j).prank > theBestPokerhand.myCards.get(j).prank) {
                        notEqualPokerhands = true;
                        break;
                    }
                }
                if (!notEqualPokerhands) {
                    winners.add(i);
                }
            }
        }

        for (Integer i : winners) {
            tab[i] += (punkt / winners.size());
        }
    }

    public static HashMap<String, Double> count(HashMap<String, String> ranges, Set<String> board) {

        ArrayList<ArrayList<Hand>> exactRanges = new ArrayList<ArrayList<Hand>>();
        ArrayList<String> ids = new ArrayList<String>();
        for (String id : ranges.keySet()) {
            String s = ranges.get(id);
            ArrayList<Hand> act = new ArrayList<Hand>();
            int i = 0;
            while (i < s.length()) {
                String tmp = "";
                while (i < s.length() && s.charAt(i) != ' ') {
                    tmp = tmp + s.substring(i, i + 1);
                    i++;
                }

                if (tmp.length() == 2 || tmp.charAt(2) == 'o') {
                    for (String suit1 : suits) {
                        for (String suit2 : suits) {
                            if (!suit1.equals(suit2)) {
                                String ple = tmp.substring(0, 1) + suit1 + tmp.substring(1, 2) + suit2;
                                if (suit1.compareTo(suit2) > 0 && tmp.substring(0, 1).equals(tmp.substring(1, 2))) {

                                } else {
                                    act.add(new Hand(ple));
                                }
                            }
                        }
                    }
                } else {
                    for (String suit : suits) {
                        String ple = tmp.substring(0, 1) + suit + tmp.substring(1, 2) + suit;
                        act.add(new Hand(ple));
                    }
                }

                i++;
            }
            exactRanges.add(act);
            ids.add(id);
        }

        int n = ranges.size();
        int onePoint = CountingOdds.factorial(n);
        int points[] = new int[n];
        HashMap result = new HashMap<String, Double>();
        ArrayList<Card> cardsOnBoard = new ArrayList<Card>();
        numberOfDrawings = 20000;
        actualNumberOfDrawings = numberOfDrawings;

        for (String s : board) {
            cardsOnBoard.add(new Card(s.substring(0, 1), s.substring(1, 2)));
        }

        for (int i = 0; i < numberOfDrawings; i++) {
            losuj(exactRanges, cardsOnBoard, onePoint, points);
        }


        if (actualNumberOfDrawings == 0) {
            for (int i = 0; i < n; i++) {
                result.put(ids.get(i), 100.0 / ((double) n));
            }
            result.put("impossible", 0.0);
            return result;
        }

        for (int i = 0; i < n; i++) {
            double d = (double) points[i];
            d /= (double) onePoint;
            d /= (double) actualNumberOfDrawings;
            d *= 100;

            result.put(ids.get(i), d);
        }

        return result;
    }

    public static HashMap<String, Double> countTraining(ArrayList<Card> heroCards, HashSet<String> range, ArrayList<Card> board) {
        ArrayList<ArrayList<Hand>> exactRanges = new ArrayList<>();

        ArrayList<Hand> hero = new ArrayList<>(); hero.add(new Hand(heroCards.get(0), heroCards.get(1)));
        ArrayList<Hand> villain = new ArrayList<>();

        for (String s : range) {
            int i = 0;
            while (i < s.length()) {
                String tmp = "";
                while (i < s.length() && s.charAt(i) != ' ') {
                    tmp = tmp + s.substring(i, i + 1);
                    i++;
                }

                if (tmp.length() == 2 || tmp.charAt(2) == 'o') {
                    for (String suit1 : suits) {
                        for (String suit2 : suits) {
                            if (!suit1.equals(suit2)) {
                                String ple = tmp.substring(0, 1) + suit1 + tmp.substring(1, 2) + suit2;
                                if (suit1.compareTo(suit2) > 0 && tmp.substring(0, 1).equals(tmp.substring(1, 2))) {

                                } else {
                                    villain.add(new Hand(ple));
                                }
                            }
                        }
                    }
                } else {
                    if(tmp.charAt(0) == tmp.charAt((1))) continue; //JJs

                    for (String suit : suits) {
                        String ple = tmp.substring(0, 1) + suit + tmp.substring(1, 2) + suit;
                        villain.add(new Hand(ple));
                    }
                }
                i++;
            }
        }
        //id graczy
        ArrayList<String> ids = new ArrayList<String>();
        ids.add("Hero");
        ids.add("Villain");
        exactRanges.add(hero);
        exactRanges.add(villain);

        int n = 2;
        int onePoint = 2;
        int points[] = new int[2];
        HashMap result = new HashMap<String, Double>();
        ArrayList<Card> cardsOnBoard = new ArrayList<Card>(board);
        numberOfDrawings = 100000;
        actualNumberOfDrawings = numberOfDrawings;

        for (int i = 0; i < numberOfDrawings; i++) {
            losuj(exactRanges, cardsOnBoard, onePoint, points);
        }

        for (int i = 0; i < n; i++) {
            double d = (double) points[i];
            d /= (double) onePoint;
            d /= (double) actualNumberOfDrawings;
            d *= 100;

            result.put(ids.get(i), d);
        }

        return result;
    }
}