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
public class Position {
    private final String id;
    private String range;
    public Position(String id)
    {
        this.id= id;
        range = "";
    }
    public void SetRange(String str)
    {
        range = str;
    }
    public String GetRange()
    {
        return range;
    }
    public String GetId(){
        return id;
    }
}
