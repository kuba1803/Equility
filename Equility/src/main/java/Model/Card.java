/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Comparator;

/**
 *
 * @author kuba1_000
 */
public class Card {

    final static String[] ranks = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
    int prank;
    public String rank;
    public String suit;

    public Card(String rank, String suit){
        this.suit = suit;
        this.rank = rank;
        for(int i = 0; i < 13; i++) {
            if(Card.ranks[i].equals(rank)) {
                prank = i;
                break;
            }
        }
    }

    public Card(String s) {
        this.rank = s.substring(0, 1);
        this.suit = s.substring(1);
        for(int i = 0; i < 13; i++) {
            if(Card.ranks[i].equals(rank)) {
                prank = i;
                break;
            }
        }
    }

    public int hashCode() {
        return prank * 1000 + suit.charAt(0) - 'a';
    }

    public boolean equals(Object other) {
        return (rank.equals(((Card)other).rank) && suit.equals(((Card)other).suit));
    }

    @Override
    public String toString(){
        return rank+suit;
    }

    //komparatory:
    public static Comparator<Card> CompareByRankDecreasing = new Comparator<Card>() {
        @Override
        public int compare(Card c1, Card c2) {
            if(c1.prank == c2.prank) {
                return c1.suit.compareTo(c2.suit);
            }

            if(c1.prank > c2.prank) return 1;
            if(c1.prank < c2.prank) return -1;

            return 1;
        }
    };

    public static Comparator<Card> CompareBySuit = new Comparator<Card>() {
        @Override
        public int compare(Card c1, Card c2) {
            if(c1.suit.equals(c2.suit)) {
                if(c1.prank < c2.prank) return -1;
                if(c1.prank > c2.prank) return 1;
                return 1;
            }

            return c1.suit.compareTo(c2.suit);
        }
    };

}
