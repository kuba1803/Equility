/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import Controller.MainControler;
import GUI.GUIController;
import javafx.scene.image.ImageView;

import java.util.*;

/**
 *
 * @author kuba1_000
 */
public class MainModel implements MainControler {

    private ArrayList<Position> positions = new ArrayList<>();

    public MainModel()
    {
        positions.add(new Position("BB"));
        positions.add(new Position("SB"));
        positions.add(new Position("BU"));
        positions.add(new Position("CO"));
        positions.add(new Position("MP3"));
        positions.add(new Position("MP2"));
    }
    @Override
    public String getRange(String pos) {
        Iterator<Position>iter = positions.iterator();
        while(iter.hasNext())
        {
            Position p = iter.next();
            if(p.GetId().equals(pos))
            {
                return p.GetRange();
            }
        }
        return null;
    }

    @Override
    public HashMap<String, Double> getEquity(HashMap<String, String> ranges, HashSet<String> board) {
        return CountingOdds.count(ranges, board);
    }

    @Override
    public void setRange(String pos, String range) {
        Iterator<Position>iter = positions.iterator();
        while(iter.hasNext())
        {
            Position p = iter.next();
            if(p.GetId().equals(pos))
            {
                p.SetRange(range);
            }
        }
    }

    @Override
    public void Clear() {
        Iterator<Position>iter = positions.iterator();
        while(iter.hasNext())
        {
            Position p = iter.next();
            p.clear();
        }
    }

    @Override
    public boolean checkRange(String range) {
        return true;
    }

    @Override
    public String getFloop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFloop(String floop) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTurn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTurn(String turn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRiver() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void SetRiver(String river) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
