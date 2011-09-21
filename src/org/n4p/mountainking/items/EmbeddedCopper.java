package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class EmbeddedCopper extends EmbeddedNode {

  public EmbeddedCopper(Owner owner) {
    super(owner, ItemGraphic.EMBEDDED_COPPER, CopperNode.class);
  }

  @Override
  public String getLabel() {
    return ("Copper Vein (Embedded)");
  }

}
