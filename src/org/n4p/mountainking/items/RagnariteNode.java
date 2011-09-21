package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class RagnariteNode extends ExposedNode {

  public RagnariteNode(Owner owner) {
    super(owner, ItemGraphic.EXPOSED_RAGNARITE);
  }

  @Override
  public String getLabel() {
    return ("Ragnarite Vein");
  }

}
