package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class EmbeddedIron extends EmbeddedNode {

  public EmbeddedIron(Owner owner) {
    super(owner, ItemGraphic.EMBEDDED_IRON, IronNode.class);
  }

  @Override
  public String getLabel() {
    return ("Iron Vein (Embedded)");
  }

}
