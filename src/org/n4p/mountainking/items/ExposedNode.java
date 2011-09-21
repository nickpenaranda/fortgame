package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;
import org.newdawn.slick.Image;

public abstract class ExposedNode extends Item {
  protected ItemGraphic mItemGraphic;

  public ExposedNode(Owner owner, ItemGraphic itemGraphic) {
    super(owner);
    mItemGraphic = itemGraphic;
  }

  @Override
  public int getFlags() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Image getInvAppearance() {
    return (null); // Nodes cannot be in unit inventories!!!
  }

  @Override
  public Image getWorldAppearance(boolean animState) {
    return (animState ? mItemGraphic.getImage(1) : mItemGraphic.getImage(0));
  }

}
