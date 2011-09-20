package org.n4p.mountainking.units;

import org.n4p.mountainking.Coord;
import org.n4p.mountainking.World;
import org.newdawn.slick.Image;

public interface UnitInterface {
  // Called for every unit on each tick
  public void think(World world,int delta);
  
  // Called when some other unit (or environment) does something to this unit
  public void actedUpon(Unit other, Action act, Integer ... params) throws UnitException;
    
  // Returns actual amount of damage rendered
  public int damage(int amount);
  
  // Called (usually) when health reaches 0
  public void die();
  
  public void moveTo(Coord dest);
  
  public String getName();
  public Image getImage();
  public Coord getLocation();
  
}
