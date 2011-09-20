package org.n4p.mountainking;

import org.n4p.mountainking.items.AbstractItem;
import org.newdawn.slick.SlickException;

public interface Owner {
	public void give(AbstractItem item) throws SlickException;
	public void take(AbstractItem item) throws SlickException;
	public Coord getPosition();
}
