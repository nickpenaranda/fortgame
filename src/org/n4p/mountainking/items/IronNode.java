package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class IronNode extends ExposedNode {

  public IronNode(Owner owner) {
    super(owner, ItemGraphic.EXPOSED_IRON);
  }

  @Override
  public String getLabel() {
    return ("Iron Vein");
  }

}
