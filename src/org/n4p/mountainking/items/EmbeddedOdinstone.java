package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class EmbeddedOdinstone extends EmbeddedNode {

  public EmbeddedOdinstone(Owner owner) {
    super(owner, ItemGraphic.EMBEDDED_ODINSTONE, OdinstoneNode.class);
  }

  @Override
  public String getLabel() {
    return ("Odinstone Shard (Embedded)");
  }

}
