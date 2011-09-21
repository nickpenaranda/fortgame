package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class OdinstoneNode extends ExposedNode {

  public OdinstoneNode(Owner owner) {
    super(owner, ItemGraphic.EXPOSED_ODINSTONE);
  }

  @Override
  public String getLabel() {
    return ("Odinstone Shard");
  }
}
