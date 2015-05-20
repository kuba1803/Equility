/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author kuba1_000
 */
public class Position {
    private final String id;
    private String range;
    private List<String> explicitRange;
    public Position(String id)
    {
        this.id= id;
        range = "";
       explicitRange = new ArrayList<>();
    }
    public void SetRange(String str)
    {
        range = str;
        Scanner scanner = new Scanner(str);
        while(scanner.hasNext())
            explicitRange.add(scanner.next());
        
    }
    public String GetRange()
    {
        return range;
    }
    public String GetId(){
        return id;
    }
    public void clear()
    {
        range = "";
    }
}
