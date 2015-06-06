/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author kuba1_000
 */
public interface MainControler {
    public String getRange(String pos);
    public HashMap<String, Double> getEquity(HashMap<String, String> ranges, HashSet<String> board);
    public void setRange(String pos,String range);
    public void Clear();
    public boolean checkRange(String range);
    public String getFloop();
    public void setFloop(String floop);
    public String getTurn();
    public void setTurn(String turn);
    public String getRiver();
    public void SetRiver(String river);
}
