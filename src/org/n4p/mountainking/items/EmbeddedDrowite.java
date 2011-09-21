package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class EmbeddedDrowite extends EmbeddedNode {

  public EmbeddedDrowite(Owner owner) {
    super(owner, ItemGraphic.EMBEDDED_DROWITE, DrowiteNode.class);
  }

  @Override
  public String getLabel() {
    return ("Drowite Crystal (Embedded)");
  }

}
