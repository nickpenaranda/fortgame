package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class CopperNode extends ExposedNode {

  public CopperNode(Owner owner) {
    super(owner, ItemGraphic.EXPOSED_COPPER);
  }

  @Override
  public String getLabel() {
    return ("Copper Vein");
  }

}
