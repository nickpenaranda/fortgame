package org.n4p.mountainking.units;

import org.n4p.mountainking.Coord;
import org.n4p.mountainking.MountainKing;
import org.n4p.mountainking.Path;
import org.n4p.mountainking.PathFinder;
import org.n4p.mountainking.World;
import org.newdawn.slick.Image;

public class Snake extends Unit {
  private static final int THINK_INTERVAL = 1000;
  private Path mPath;
  private int mThinkTimeout = 0;

  public Snake(Coord location) {
    super(location);
    mPath = new Path();
  }

  @Override
  public Image getImage() {
    return (UnitGraphics.SNAKE.getImage());
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return ("Snake");
  }

  @Override
  public void think(World world, int delta) {
    mThinkTimeout += delta;

    while (mThinkTimeout > THINK_INTERVAL) {
      mThinkTimeout -= THINK_INTERVAL;
      if (MountainKing.r.nextBoolean()) // 1/2 chance
        wander();
      
      if (mPath != null && !mPath.isEmpty()) {
        Coord dest = mPath.pop();
        moveTo(dest);
      }
    }
  }
  
  private void wander() {
    Coord c;
    do {
      c = getLocation().move(MountainKing.rInt(-3, 4), MountainKing.rInt(-3, 4), MountainKing.rInt(-3, 4));
    } while(c != getLocation() && !World.getInstance().isTraversible(c));
    mPath = PathFinder.findPath(getLocation(), c);
    mThinkTimeout -= MountainKing.r.nextInt(1000);
  }

}
