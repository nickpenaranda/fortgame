package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class EnderiteNode extends ExposedNode {

  public EnderiteNode(Owner owner) {
    super(owner, ItemGraphic.EXPOSED_ENDERITE);
  }

  @Override
  public String getLabel() {
    return ("Enderite Node");
  }
}
