/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author kuba1_000
 */
public class Card {
    String rank;
    String suit;
    
    public Card(String rank, String suit){
        this.suit = suit;
        this.rank = rank;
    }

    public Card(String s) {
        this.rank = s.substring(0, 1);
        this.suit = s.substring(1);
    }
    
}
