package org.n4p.mountainking.units;

import java.util.Random;

import org.n4p.mountainking.Coord;
import org.n4p.mountainking.Path;
import org.n4p.mountainking.PathFinder;
import org.n4p.mountainking.R;
import org.n4p.mountainking.World;

public abstract class Creature extends Unit {
  private static final int THINK_INTERVAL = 1000;
  private Path mPath;
  private int mThinkTimeout = 0;

  public Creature(Coord location) {
    super(location);
    mPath = new Path();
  }

  @Override
  public void think(World world, int delta) {
    mThinkTimeout += delta;

    while (mThinkTimeout > THINK_INTERVAL) {
      mThinkTimeout -= THINK_INTERVAL;
      if (R.getInstance().nextBoolean()) // 1/2 chance
        wander();
      
      if (mPath != null && !mPath.isEmpty()) {
        Coord dest = mPath.pop();
        moveTo(dest);
      }
    }
  }
  
  private void wander() {
    Coord c;
    Random r = R.getInstance();
    do {
      c = getLocation().move(r.nextInt(7)-3, r.nextInt(7)-3, r.nextInt(7)-3);
    } while(c != getLocation() && !World.getInstance().isTraversible(c));
    mPath = PathFinder.findPath(getLocation(), c);
    mThinkTimeout -= r.nextInt(1000);
  }

}
