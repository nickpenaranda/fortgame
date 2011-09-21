package org.n4p.mountainking.terrain;

import java.util.ArrayList;

import org.n4p.mountainking.Coord;
import org.n4p.mountainking.Owner;
import org.n4p.mountainking.items.Item;
import org.n4p.mountainking.items.ItemException;
import org.n4p.mountainking.units.Unit;

public class Terrain {
  public static enum Visibility {
    HIDDEN, VISIBLE, UNDEFINED
  };

  private TerrainNode mRoot;
  private static int cWorldSize;

  public static int count;

  public Terrain(int worldSize) {
    cWorldSize = worldSize;
    mRoot = new TerrainNode(null, -worldSize / 2, -worldSize / 2,
        -worldSize / 2, worldSize, TerrainType.get(TerrainType.AIR), null);
    count = 0;
  }

  public boolean isVisible(Coord c) {
    return (isVisible(c.x, c.y, c.z));
  }

  public boolean isVisible(int x, int y, int z) {
    TerrainNode t = getAt(x, y, z);
    if (t == null)
      return (false);

    if (t.getVisibility() == Visibility.UNDEFINED)
      setVisibilityAt(
          ((getTypeAt(x + 1, y, z).getFlags()
              | getTypeAt(x - 1, y, z).getFlags()
              | getTypeAt(x, y + 1, z).getFlags()
              | getTypeAt(x, y - 1, z).getFlags()
              | getTypeAt(x, y, z + 1).getFlags() | getTypeAt(x, y, z - 1)
              .getFlags()) & TerrainType.NO_OCCLUDE) == TerrainType.NO_OCCLUDE ? Visibility.VISIBLE
              : Visibility.HIDDEN, x, y, z);

    return (getAt(x, y, z).getVisibility() == Visibility.VISIBLE ? true : false);
  }

  public void setBlock(int x, int y, int z, TerrainType type) {
    mRoot.articulate(x, y, z, type, null, 1);
  }

  public void setVisibilityAt(Visibility v, int x, int y, int z) {
    mRoot.articulate(x, y, z, null, v, 1);
  }

  public void setVolume(int x1, int y1, int z1, int x2, int y2, int z2,
      TerrainType type) {
    int minSize = Math.min(Math.min(Math.abs(x1 - x2), Math.abs(y1 - y2)), Math
        .abs(z1 - z2)) / 2;
    if (minSize < 1)
      minSize = 1;

    for (int x = x1; x <= x2; ++x) {
      for (int y = y1; y <= y2; ++y) {
        for (int z = z1; z <= z2; ++z) {
          mRoot.articulate(x, y, z, type, null, minSize);
        }
      }
    }
    // consolidateDown();
  }

  public void setSolidVolume(int x1, int y1, int z1, int x2, int y2, int z2,
      TerrainType type) {
    int minSize = Math.min(Math.min(Math.abs(x1 - x2), Math.abs(y1 - y2)), Math
        .abs(z1 - z2)) / 2;
    if (minSize < 1)
      minSize = 1;

    for (int x = x1; x <= x2; ++x) {
      for (int y = y1; y <= y2; ++y) {
        for (int z = z1; z <= z2; ++z) {
          if ((getAt(x, y, z).getType().getFlags() & TerrainType.SOLID) == TerrainType.SOLID)
            mRoot.articulate(x, y, z, type, null, minSize);
        }
      }
    }
    // consolidateDown();
  }

  public TerrainNode getAt(Coord c) {
    return (getAt(c.x, c.y, c.z));
  }

  public TerrainNode getAt(int x, int y, int z) {
    if (x >= cWorldSize / 2 || x < -cWorldSize / 2 || y >= cWorldSize / 2
        || y < -cWorldSize / 2 || z >= cWorldSize / 2 || z < -cWorldSize / 2)
      return (null);
    else
      return (mRoot.getRecursive(x, y, z));
  }

  public TerrainType getTypeAt(Coord c) {
    TerrainNode n = getAt(c.x, c.y, c.z);
    return (n != null ? n.getType() : TerrainType.get(TerrainType.NONE));
  }

  public TerrainType getTypeAt(int x, int y, int z) {
    TerrainNode n = getAt(x, y, z);
    return (n != null ? n.getType() : TerrainType.get(TerrainType.NONE));
  }

  public ArrayList<Item> getItemsAt(Coord c) {
    mRoot.articulate(c.x, c.y, c.z, null, null, 1); // Articulate to individual
    // block, if not already
    TerrainNode t = getAt(c.x, c.y, c.z);
    return (t.getItems());
  }
  
  public TerrainNode getBlockAt(Coord c) {
    mRoot.articulate(c.x, c.y, c.z, null, null, 1); // Articulate to individual
    // block, if not already
    return (getAt(c.x, c.y, c.z));
  }

  public void doConsolidate() {
    long start = System.nanoTime() / 1000000;
    mRoot.consolidate();
    System.out.printf("Consolidated in %dms\n",System.nanoTime() / 1000000 - start);
  }

  public static class TerrainNode implements Owner {
    private TerrainNode[] children;
    public boolean isLeaf;
    private Visibility mVisible = Visibility.UNDEFINED;
    private int x, y, z;
    private int mx, my, mz;
    private int size;
    private TerrainType mType;

    private ArrayList<Item> mItems = null;
    private ArrayList<Unit> mUnits = null;

    public TerrainNode(TerrainNode parent, int x, int y, int z, int size,
        TerrainType type, Visibility v) {

      this.x = x;
      this.y = y;
      this.z = z;

      mx = x + (size >> 1);
      my = y + (size >> 1);
      mz = z + (size >> 1);

      this.size = size;
      this.isLeaf = true;

      if (type == null)
        this.mType = TerrainType.get(TerrainType.NONE);
      else
        this.mType = type;

      if (v == null)
        this.mVisible = Visibility.UNDEFINED;
      else
        this.mVisible = v;

      Terrain.count++;
    }

    public TerrainNode getRecursive(int x, int y, int z) {
      if (isLeaf)
        return (this);
      else {
        if (x < mx) {
          if (y < my) {
            if (z < mz) {
              return (children[0].getRecursive(x, y, z));
            } else {
              return (children[4].getRecursive(x, y, z));
            }
          } else {
            if (z < mz) {
              return (children[2].getRecursive(x, y, z));
            } else {
              return (children[6].getRecursive(x, y, z));
            }
          }
        } else {
          if (y < my) {
            if (z < mz) {
              return (children[1].getRecursive(x, y, z));
            } else {
              return (children[5].getRecursive(x, y, z));
            }
          } else {
            if (z < mz) {
              return (children[3].getRecursive(x, y, z));
            } else {
              return (children[7].getRecursive(x, y, z));
            }
          }
        }
      }
    }

    public String toString() {
      return (String.format("(%d,%d,%d) %s %d", x, y, z, isLeaf ? "LEAF"
          : "NODE", size));
    }

    private void consolidate() {
      if (isLeaf)
        return;

      for (TerrainNode child : children) {
        if (!child.isLeaf)
          child.consolidate();
      }

      if (!isLeaf && canConsolidate()) {
        mType = children[0].mType;
        mVisible = children[0].mVisible;
        children = null;
        isLeaf = true;
        count -= 8;
      }
    }

    private boolean canConsolidate() {
      int j, k;
      for (int i = 0; i < 8; i++) {
        j = i == 7 ? 0 : i + 1;
        k = i == 0 ? 7 : i - 1;
        if (!children[i].isLeaf || !children[i].equals(children[j])
            || !children[i].equals(children[k]))
          return (false);
      }
      return (true);
    }

    private void articulate(int x, int y, int z, TerrainType type,
        Visibility v, int minSize) {
      if (isLeaf && mType == type && mVisible == v)
        return;

      if (this.size <= minSize) {
        if (type != null)
          this.mType = type;
        if (v != null)
          this.mVisible = v;
        return; // If this node is already min size, do nothing
      }

      if (x >= this.x + size || x < this.x || y >= this.y + size || y < this.y
          || z >= this.z + size || z < this.z) {
        return; // If out of bounds, do nothing
      }

      // Split if leaf
      if (isLeaf) {
        isLeaf = false;
        children = new TerrainNode[] {
            new TerrainNode(this, this.x, this.y, this.z, size / 2, mType,
                mVisible),
            new TerrainNode(this, mx, this.y, this.z, size / 2, mType, mVisible),
            new TerrainNode(this, this.x, my, this.z, size / 2, mType, mVisible),
            new TerrainNode(this, mx, my, this.z, size / 2, mType, mVisible),

            new TerrainNode(this, this.x, this.y, mz, size / 2, mType, mVisible),
            new TerrainNode(this, mx, this.y, mz, size / 2, mType, mVisible),
            new TerrainNode(this, this.x, my, mz, size / 2, mType, mVisible),
            new TerrainNode(this, mx, my, mz, size / 2, mType, mVisible) };
      }

      // Pass articulation to proper child
      if (x < mx) {
        if (y < my) {
          if (z < mz) {
            children[0].articulate(x, y, z, type, v, minSize);
          } else {
            children[4].articulate(x, y, z, type, v, minSize);
          }
        } else {
          if (z < mz) {
            children[2].articulate(x, y, z, type, v, minSize);
          } else {
            children[6].articulate(x, y, z, type, v, minSize);
          }
        }
      } else {
        if (y < my) {
          if (z < mz) {
            children[1].articulate(x, y, z, type, v, minSize);
          } else {
            children[5].articulate(x, y, z, type, v, minSize);
          }
        } else {
          if (z < mz) {
            children[3].articulate(x, y, z, type, v, minSize);
          } else {
            children[7].articulate(x, y, z, type, v, minSize);
          }
        }
      }
    }

    public Visibility getVisibility() {
      return mVisible;
    }

    public TerrainType getType() {
      return mType;
    }

    public void setType(TerrainType type) {
      mType = type;
    }

    public void setVisibility(Visibility v) {
      mVisible = v;
    }

    public void initItems() {
      if (mItems != null)
        mItems.clear();
      else
        mItems = new ArrayList<Item>();
    }
    

    public ArrayList<Item> getItems() {
      if (mItems == null)
        initItems();
      return (mItems);
    }
    
    public void initUnits() {
      if (mUnits != null)
        mUnits.clear();
      else
        mUnits = new ArrayList<Unit>();
    }

    public ArrayList<Unit> getUnits() {
      if (mUnits == null)
        initUnits();
      return (mUnits);
    }

    @Override
    public boolean equals(Object aThat) {
      if (this == aThat)
        return (true);
      if (!(aThat instanceof TerrainNode))
        return (false);
      TerrainNode that = (TerrainNode) aThat;

      // Equivalent iff...
      return (this.mVisible == that.mVisible
          && // Both visible or both hidden
          // or both undefined
          this.mType.getID() == that.mType.getID()
          && // Both same type
          (this.mItems == null || this.mItems.isEmpty())
          && // This has no
          // Items
          (that.mItems == null || that.mItems.isEmpty())
          && (this.mUnits == null || this.mUnits.isEmpty()) && (that.mUnits == null || that.mUnits
          .isEmpty())); // That has no
      // Items
    }

    @Override
    public void give(Item item) throws ItemException {
      if (size > 1)
        throw new ItemException(
            "Attempted to give non-block TerrainNode an item!");
      else if (mItems == null)
        initItems();
      mItems.add(item);
    }

    @Override
    public void take(Item item) throws ItemException {
      if (size > 1)
        throw new ItemException(
            "Attempted to take item from non-block TerrainNode!");
      mItems.remove(item);
    }

    @Override
    public Coord getLocation() {
      return (new Coord(x, y, z));
    }
  }
}
