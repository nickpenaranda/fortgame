package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class EmbeddedCarnium extends EmbeddedNode {

  public EmbeddedCarnium(Owner owner) {
    super(owner, ItemGraphic.EMBEDDED_CARNIUM, CarniumNode.class);
  }

  @Override
  public String getLabel() {
    return ("Carnium Deposit (Embedded)");
  }

}
