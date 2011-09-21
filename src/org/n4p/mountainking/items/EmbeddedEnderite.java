package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class EmbeddedEnderite extends EmbeddedNode {

  public EmbeddedEnderite(Owner owner) {
    super(owner, ItemGraphic.EMBEDDED_ENDERITE, EnderiteNode.class);
  }

  @Override
  public String getLabel() {
    return ("Enderite Node (Embedded)");
  }

}
