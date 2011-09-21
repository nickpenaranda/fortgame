package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;
import org.newdawn.slick.Image;

public abstract class EmbeddedNode extends Item {
  private ItemGraphic mItemGraphic;
  private Class<ExposedNode> mProduceOnDestroy;
  public EmbeddedNode(Owner owner, ItemGraphic itemGraphic, Class<? extends ExposedNode> produceOnDestroy) {
    super(owner);
    mItemGraphic = itemGraphic;
  }

  @Override
  public int getFlags() {
    // TODO Auto-generated method stub
    return 0;
  }
  
  @Override
  public void destroy() {
    ExposedNode n = null;
    try {
      n = mProduceOnDestroy.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    n.setOwner(getOwner());
    try {
      getOwner().give(n);
    } catch (ItemException e) {
      e.printStackTrace();
    }
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
