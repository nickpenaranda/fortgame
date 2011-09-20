package org.n4p.mountainking.items;

import org.n4p.mountainking.Owner;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class AbstractItem {
	protected Owner mOwner;
	
	public AbstractItem(Owner owner) {
		mOwner = owner;
		try {
			mOwner.give(this);
		} catch(SlickException e) {
			e.printStackTrace();
		}
		init();
	}
	
	public void destroy() {
		try {
			mOwner.take(this);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public Owner getOwner() {
		return(mOwner);
	}
	
	public abstract Image getWorldAppearance(boolean animState);
	public abstract Image getInvAppearance();
	
	public abstract void init();
	
	public abstract String getLabel();
	public abstract int getFlags();
}
