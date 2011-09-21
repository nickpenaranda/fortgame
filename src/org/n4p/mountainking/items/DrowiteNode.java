package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class DrowiteNode extends ExposedNode {

  public DrowiteNode(Owner owner) {
    super(owner, ItemGraphic.EXPOSED_DROWITE);
  }

  @Override
  public String getLabel() {
    return ("Drowite Crystal");
  }

}
