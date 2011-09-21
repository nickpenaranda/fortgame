package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class AzraeliteNode extends ExposedNode {

  public AzraeliteNode(Owner owner) {
    super(owner, ItemGraphic.EXPOSED_AZRAELITE);
  }

  @Override
  public String getLabel() {
    return ("Azraelite Crystal");
  }

}
