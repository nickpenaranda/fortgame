package org.n4p.mountainking.units;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.n4p.mountainking.Coord;
import org.n4p.mountainking.Owner;
import org.n4p.mountainking.World;
import org.n4p.mountainking.items.Item;
import org.n4p.mountainking.items.ItemException;
import org.n4p.mountainking.terrain.Terrain.TerrainNode;

public abstract class Unit implements UnitInterface, Owner {
  protected UUID mID;
  protected Coord mLocation;
  protected int mMaxHealth = 100, mHealth = 100;
  protected HashMap<Unit, Integer> mRelationships;
  protected ArrayList<Item> mInventory;
  protected TerrainNode mBlock;
  
  protected boolean mIsDead;

  public Unit(Coord location) {
    mID = UUID.randomUUID();
    mIsDead = false;
    mLocation = location;
    mBlock = World.getInstance().getTerrain().getBlockAt(location);
    mBlock.getUnits().add(this);
    mRelationships = new HashMap<Unit, Integer>();
    World.getInstance().getUnitList().add(this);
  }

  private void updateRelationship(Unit other, int delta) {
    int oldRelationship = mRelationships.get(other);
    mRelationships.put(other, oldRelationship + delta);
  }

  @Override
  public void actedUpon(Unit other, Action act, Integer... params)
      throws UnitException {
    switch (act) {
    case ATTACK:
      if (params.length != 1) {
        throw new UnitException(String.format(
            "Incorrect number of parameters for action: %s (ATTACK) %s", other,
            this));
      }
      updateRelationship(other, -params[0]);
      break;
    case HEAL:
      if (params.length != 1) {
        throw new UnitException(String.format(
            "Incorrect number of parameters for action: %s (HEAL) %s", other,
            this));
      }
      updateRelationship(other, params[0]);
      break;
    }
  }

  @Override
  public int damage(int amount) {
    mHealth -= amount;
    if(mHealth <= 0) die();
    
    return(mHealth);
  }
  
  @Override
  public void die() {
    mRelationships.clear();
    TerrainNode t = World.getInstance().getTerrain().getBlockAt(mLocation);
    for(Item i:mInventory) {
      try {
        i.getOwner().take(i);
        t.give(i);
      } catch (ItemException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    mInventory.clear();
    mIsDead = true;
    mHealth = 0;
  }

  @Override
  public Coord getLocation() {
    return(mLocation);
  }

  @Override
  public void give(Item item) {
    mInventory.add(item);
  }

  @Override
  public void take(Item item) {
    mInventory.remove(item);
  }
  
  @Override
  public void moveTo(Coord dest) {
    mBlock.getUnits().remove(this);
    mBlock = World.getInstance().getTerrain().getBlockAt(dest);
    mBlock.getUnits().add(this);
    mLocation = dest;
  }

  @Override
  public ArrayList<Item> getItems() {
    return(mInventory);
  }
  
  @Override
  public String toString() {
    return(String.format("Unit %s @ %s",mID,mLocation));
  }
  
  @Override
  public boolean equals(Object aThat) {
    if(this == aThat) return(true);
    if(!(aThat instanceof Unit)) return(false);
    Unit that = (Unit) aThat;
    return(this.mID.equals(that.mID));
  }
}
