package org.n4p.mountainking.units;

import org.n4p.mountainking.Coord;
import org.n4p.mountainking.Path;
import org.n4p.mountainking.World;
import org.newdawn.slick.Image;

public class Player extends Unit {
  private static final int THINK_INTERVAL = 100;
  private static final int VIEWLOCK_INTERVAL = 750;
  private int mThinkTimeout = 0;
  private int mViewLockTimeout = 0;

  private boolean mViewLock = false;

  Path mPath = null;

  public Player(Coord location) {
    super(location);
  }

  @Override
  public String getName() {
    return ("The Mountain King");
  }

  @Override
  public void think(World world, int delta) {
    mThinkTimeout -= delta;
    mViewLockTimeout -= delta;

    if (mThinkTimeout <= 0) {
      mThinkTimeout = THINK_INTERVAL;
      if (mPath != null && !mPath.isEmpty()) {
        Coord dest = mPath.pop();
        moveTo(dest);
        if (mViewLock) {
          int wz = world.getView().z;
          if (dest.z > wz)
            world.getView().moveThis(0, 0, 1);
          else if (dest.z < wz - 3)
            world.getView().moveThis(0, 0, -1);
          
          if (mViewLockTimeout <= 0) {
            world.centerAt(mLocation.x, mLocation.y, world.getView().z);
            mViewLockTimeout = VIEWLOCK_INTERVAL;
          }
        }
      }
    }
  }

  public void setPath(Path p) {
    mPath = p;
  }

  @Override
  public Image getImage() {
    return (UnitGraphic.PLAYER.getImage());
  }

  public void toggleViewLock() {
    mViewLock = !mViewLock;
  }

  public void setViewLock(boolean b) {
    mViewLock = b;
  }

}
