/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.*;


public class Deck {
    final static String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    final static String[] suits = {"c", "d", "h", "s"};
    List<Card> cards;
    public Deck(){
        cards = new ArrayList<Card>();
        for(String s: suits)
            for(String r: ranks)
                cards.add(new Card(r,s));
    }
    public void shuffle(){
        //long seed = System.nanoTime();
        //Collections.shuffle(cards, new Random(seed));
        Collections.shuffle(cards);
    }
    
    public void removeHand(String h){
        cards.remove(new Card(h.substring(0, 2)));
        cards.remove(new Card(h.substring(2)));
    }
    
    public Card drawACard(){
        return cards.remove(cards.size() - 1);
    }
    
    @Override
    public String toString(){
       return cards.toString();
    }
}
