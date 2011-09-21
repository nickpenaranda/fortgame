package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;

public abstract class Item implements ItemInterface {
	protected Owner mOwner;
	
	public Item(Owner owner) {
		mOwner = owner;
		try {
			mOwner.give(this);
		} catch(ItemException e) {
			e.printStackTrace();
		}
	}
	
	public void destroy() {
		try {
			mOwner.take(this);
		} catch (ItemException e) {
			e.printStackTrace();
		}
	}
	
	public Owner getOwner() {
		return(mOwner);
	}
	
	public void setOwner(Owner newOwner) {
	  try {
      mOwner.take(this);
      newOwner.give(this);
      mOwner = newOwner;
    } catch (ItemException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
	}
}
