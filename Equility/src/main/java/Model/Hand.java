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
class Hand {
    public Hand(String h){
        f1=h.charAt(0);
        s1=h.charAt(1);
        f2=h.charAt(2);
        s2=h.charAt(3);
    }
    char f1, f2, s1, s2; //f - figura, s- kolor (jak suit)
    
}
