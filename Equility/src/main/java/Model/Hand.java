/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author witek
 */
public class Hand {
    Card c1;
    Card c2;
    public Hand(String h){
        c1 = new Card(h.substring(0, 2));
        c2 = new Card(h.substring(2, 4));
    }
    
}
