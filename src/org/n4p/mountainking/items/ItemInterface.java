package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;
import org.newdawn.slick.Image;

public interface ItemInterface {
  public void destroy();
  public Owner getOwner();
  public String getLabel();
  public int getFlags();
  public Image getWorldAppearance(boolean animState);
  public Image getInvAppearance();
}
