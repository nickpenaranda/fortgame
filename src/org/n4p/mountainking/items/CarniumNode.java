package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public class CarniumNode extends ExposedNode {

  public CarniumNode(Owner owner) {
    super(owner, ItemGraphic.EXPOSED_CARNIUM);
  }

  @Override
  public String getLabel() {
    return ("Carnium Deposit");
  }

}
