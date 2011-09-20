package org.n4p.mountainking;

import java.util.ArrayList;

import org.n4p.mountainking.items.Item;
import org.n4p.mountainking.items.ItemException;

public interface Owner {
	public void give(Item item) throws ItemException;
	public void take(Item item) throws ItemException;
	
	public ArrayList<Item> getItems();
	public Coord getLocation();
}
