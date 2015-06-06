package Model;

import java.util.*;

/**
 * Created by kaszubki on 30.05.15.
 */
public class CountingOdds {
    final static String[] suits = {"s", "h", "d", "c"};
    final static int numberOfDrawings = 10000;
    static int actualNumberOfDrawings;

    static Set<Card> avaliableCards;

    static int factorial(int a) {
        int res = 1;
        for(int i = 2; i <= a; i++) res *= i;
        return res;
    }

    static void losuj(ArrayList<ArrayList<Hand> > ranges, ArrayList<Card> board, int punkt, int tab[]) {
        avaliableCards = new HashSet<Card>();
        for(String rank : Card.ranks) {
            for(String suit : suits) {
                avaliableCards.add(new Card(rank, suit));
            }
        }

        ArrayList<ArrayList<Card> > playersPokerhandsBuilders = new ArrayList<ArrayList<Card> >();

        for(int i = 0; i < ranges.size(); i++) {
            playersPokerhandsBuilders.add(new ArrayList<Card>(board));
        }

        for(Card c : board) {
            avaliableCards.remove(c);
        }

        for(int i = 0; i < ranges.size(); i++) {
            Collections.shuffle(ranges.get(i));
            boolean foundFit = false;
            for(int j = 0; j < ranges.get(i).size(); j++) {
                Card c1 = ranges.get(i).get(j).c1;
                Card c2 = ranges.get(i).get(j).c2;

                if(avaliableCards.contains(c1) && avaliableCards.contains(c2)) {
                    playersPokerhandsBuilders.get(i).add(c1);
                    playersPokerhandsBuilders.get(i).add(c2);
                    avaliableCards.remove(c1);
                    avaliableCards.remove(c2);

                    foundFit = true;
                    break;
                }
            }
            if(!foundFit) {
                actualNumberOfDrawings--;
                return;
            }
        }

        //add missing board cards
        ArrayList<Card> arrayListOfAvaliableCards = new ArrayList<Card>(avaliableCards);

        //System.out.println(arrayListOfAvaliableCards);

        Collections.shuffle(arrayListOfAvaliableCards);
        for(int i = 0; i < 5 - board.size(); i++) {
            for(int ii = 0; ii < ranges.size(); ii++) {
                playersPokerhandsBuilders.get(ii).add(arrayListOfAvaliableCards.get(i));
            }
        }

        ArrayList<Pokerhand> playersPokerhands = new ArrayList<Pokerhand>();
        for(int i = 0; i < ranges.size(); i++) {
            playersPokerhands.add(new Pokerhand(playersPokerhandsBuilders.get(i)));
        }


        //determine who is the winner
        Pokerhand theBestPokerhand = playersPokerhands.get(0);
        HashSet<Integer> winners = new HashSet<Integer>();
        winners.add(0);


        for(int i = 1; i < playersPokerhands.size(); i++) {

            //System.out.println(playersPokerhands.get(i).whichPokerhand + " " + playersPokerhands.get(i).myCards + " " + theBestPokerhand.myCards + " " + theBestPokerhand.whichPokerhand);

            if(playersPokerhands.get(i).whichPokerhand < theBestPokerhand.whichPokerhand) {
                winners.clear();
                winners.add(i);
                theBestPokerhand = playersPokerhands.get(i);
            } else if (playersPokerhands.get(i).whichPokerhand == theBestPokerhand.whichPokerhand) {
                boolean notEqualPokerhands = false;
                //System.out.println(playersPokerhands.get(i).myCards.size() + " " + playersPokerhands.get(i).myCards.size());
                for(int j = 0; j < 5; j++) {
                    if(playersPokerhands.get(i).myCards.get(j).prank < theBestPokerhand.myCards.get(j).prank) {
                        winners.clear();
                        winners.add(i);
                        theBestPokerhand = playersPokerhands.get(i);
                        notEqualPokerhands = true;
                        break;
                    }
                }
                if(!notEqualPokerhands) {
                    winners.add(i);
                }
            }
            // System.out.println(theBestPokerhand.myCards + " " + theBestPokerhand.whichPokerhand);
        }

        //add points
        for(Integer i : winners) {
            tab[i] += (punkt / winners.size());
        }
    }

    public static HashMap<String, Double> count(HashMap<String, String> ranges, Set<String> board) {

        ArrayList<ArrayList<Hand> > exactRanges = new ArrayList<ArrayList<Hand> >();
        ArrayList<String> ids = new ArrayList<String>();
        for(String id : ranges.keySet()) {
            String s = ranges.get(id);
            ArrayList<Hand> act = new ArrayList<Hand>();
            int i = 0;
            while(i < s.length()) {
                String tmp = "";
                while(i < s.length() && s.charAt(i) != ' ') {
                    tmp = tmp + s.substring(i, i + 1);
                    i++;
                }

                if(tmp.length() == 2 || tmp.charAt(2) == 'o') {
                    for(String suit1 : suits) {
                        for(String suit2 : suits) {
                            if(!suit1.equals(suit2)) {
                                String ple = tmp.substring(0, 1) + suit1 + tmp.substring(1, 2) + suit2;
                                if(suit1.compareTo(suit2) > 0 && tmp.substring(0, 1).equals(tmp.substring(1, 2))) {

                                } else {
                                    act.add(new Hand(ple));
                                }
                            }
                        }
                    }
                } else {
                    for(String suit : suits) {
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
        actualNumberOfDrawings = numberOfDrawings;

        for(String s : board) {
            cardsOnBoard.add(new Card(s.substring(0, 1), s.substring(1, 2)));
        }


        for(int i = 0; i < numberOfDrawings; i++){
            losuj(exactRanges, cardsOnBoard, onePoint, points);
        }



        if(actualNumberOfDrawings == 0) {
            for(int i = 0; i < n; i++) {
                result.put(ids.get(i), 100.0/((double)n));
            }

            return result;
        }

        for(int i = 0; i < n; i++) {
            double d = (double) points[i];
            d /= (double)onePoint;
            d /= (double) actualNumberOfDrawings;
            d *= 100;

            result.put(ids.get(i), d);
        }

        return result;
    }
}