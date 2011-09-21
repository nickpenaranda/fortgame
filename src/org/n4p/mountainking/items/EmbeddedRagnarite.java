package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class EmbeddedRagnarite extends EmbeddedNode {

  public EmbeddedRagnarite(Owner owner) {
    super(owner, ItemGraphic.EMBEDDED_RAGNARITE, RagnariteNode.class);
  }

  @Override
  public String getLabel() {
    return ("Ragnarite Vein (Embedded)");
  }

}
