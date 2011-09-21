package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class EmbeddedAzraelite extends EmbeddedNode {

  public EmbeddedAzraelite(Owner owner) {
    super(owner, ItemGraphic.EMBEDDED_AZRAELITE, AzraeliteNode.class);
  }

  @Override
  public String getLabel() {
    return ("Azraelite Crystal (Embedded)");
  }

}
