package org.n4p.mountainking.units;

import org.n4p.mountainking.Coord;
import org.n4p.mountainking.Path;
import org.n4p.mountainking.World;
import org.newdawn.slick.Image;

public class Player extends Unit {
  private static final int THINK_INTERVAL = 100;
  private int mThinkTimeout = 0;
  
  Path mPath = null;
  
  public Player(Coord location) {
    super(location);
  }

  @Override
  public String getName() {
    return("The Mountain King");
  }

  @Override
  public void think(World world, int delta) {
    mThinkTimeout += delta;
    
    while(mThinkTimeout > THINK_INTERVAL) {
      mThinkTimeout -= THINK_INTERVAL;
      if(mPath != null && !mPath.isEmpty()) {
        Coord dest = mPath.pop();
        moveTo(dest);
      }
    }
  }
  
  public void setPath(Path p) {
    mPath = p;
  }

  @Override
  public Image getImage() {
    return(UnitGraphics.PLAYER.getImage());
  }
}
