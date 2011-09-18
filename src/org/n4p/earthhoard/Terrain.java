package org.n4p.earthhoard;

public class Terrain {
  public static enum Visibility {
    HIDDEN, VISIBLE, UNDEFINED
  };

  public static int count = 0;
  private Terrain[] children;
  public boolean isLeaf;
  private Visibility mVisible = Visibility.UNDEFINED;
  private int x, y, z;
  private static int sx, sy, sz;
  private int mx, my, mz;
  private int size;
  public TerrainType mType;

  public Terrain(Terrain parent, int x, int y, int z, int size, TerrainType type, Visibility v) {
    this.x = x;
    this.y = y;
    this.z = z;

    mx = x + size / 2;
    my = y + size / 2;
    mz = z + size / 2;

    this.size = size;
    this.isLeaf = true;
    this.mType = type;
    if(v == null)
      this.mVisible = Visibility.UNDEFINED;
    else
      this.mVisible = v;
    count++;
  }

  public TerrainType getTypeRecursive() {
    if (isLeaf)
      return (this.mType);
    else {
      if (Terrain.sx < mx) {
        if (Terrain.sy < my) {
          if (Terrain.sz < mz) {
            return (children[0].getTypeRecursive());
          } else {
            return (children[4].getTypeRecursive());
          }
        } else {
          if (Terrain.sz < mz) {
            return (children[2].getTypeRecursive());
          } else {
            return (children[6].getTypeRecursive());
          }
        }
      } else {
        if (Terrain.sy < my) {
          if (Terrain.sz < mz) {
            return (children[1].getTypeRecursive());
          } else {
            return (children[5].getTypeRecursive());
          }
        } else {
          if (Terrain.sz < mz) {
            return (children[3].getTypeRecursive());
          } else {
            return (children[7].getTypeRecursive());
          }
        }
      }
    }
  }

  public Terrain getRecursive() {
    if (isLeaf)
      return (this);
    else {
      if (Terrain.sx < mx) {
        if (Terrain.sy < my) {
          if (Terrain.sz < mz) {
            return (children[0].getRecursive());
          } else {
            return (children[4].getRecursive());
          }
        } else {
          if (Terrain.sz < mz) {
            return (children[2].getRecursive());
          } else {
            return (children[6].getRecursive());
          }
        }
      } else {
        if (Terrain.sy < my) {
          if (Terrain.sz < mz) {
            return (children[1].getRecursive());
          } else {
            return (children[5].getRecursive());
          }
        } else {
          if (Terrain.sz < mz) {
            return (children[3].getRecursive());
          } else {
            return (children[7].getRecursive());
          }
        }
      }
    }
  }

  public Terrain getAt(int x, int y, int z) {
    sx = x;
    sy = y;
    sz = z;
    if (sx > this.x + size || sx < this.x || sy > this.y + size || sy < this.y
        || sz > this.z + size || sz < this.z) {
      // System.out.printf("getAt:(%d,%d,%d) is out of bounds for %s\n",x,y,z,this);
      return (null);
    } else if (isLeaf)
      return (this);
    else {
      if (sx < mx) {
        if (sy < my) {
          if (sz < mz) {
            return (children[0].getRecursive());
          } else {
            return (children[4].getRecursive());
          }
        } else {
          if (Terrain.sz < mz) {
            return (children[2].getRecursive());
          } else {
            return (children[6].getRecursive());
          }
        }
      } else {
        if (Terrain.sy < my) {
          if (Terrain.sz < mz) {
            return (children[1].getRecursive());
          } else {
            return (children[5].getRecursive());
          }
        } else {
          if (Terrain.sz < mz) {
            return (children[3].getRecursive());
          } else {
            return (children[7].getRecursive());
          }
        }
      }
    }
  }

  public TerrainType getTypeAt(int x, int y, int z) {
    sx = x;
    sy = y;
    sz = z;
    if (sx > this.x + size || sx < this.x || sy > this.y + size || sy < this.y
        || sz > this.z + size || sz < this.z) {
      // System.out.printf("getTypeAt:(%d,%d,%d) is out of bounds for %s\n",x,y,z,this);
      return (TerrainType.get(TerrainType.NONE));
    } else if (isLeaf)
      return (this.mType);
    else {
      if (sx < mx) {
        if (sy < my) {
          if (sz < mz) {
            return (children[0].getTypeRecursive());
          } else {
            return (children[4].getTypeRecursive());
          }
        } else {
          if (Terrain.sz < mz) {
            return (children[2].getTypeRecursive());
          } else {
            return (children[6].getTypeRecursive());
          }
        }
      } else {
        if (Terrain.sy < my) {
          if (Terrain.sz < mz) {
            return (children[1].getTypeRecursive());
          } else {
            return (children[5].getTypeRecursive());
          }
        } else {
          if (Terrain.sz < mz) {
            return (children[3].getTypeRecursive());
          } else {
            return (children[7].getTypeRecursive());
          }
        }
      }
    }
  }

  public String toString() {
    return (String.format("(%d,%d,%d) %d", x, y, z, size));
  }

  public void consolidateDown() {
    if (isLeaf)
      return;

    for (Terrain child : children) {
      if (!child.isLeaf)
        child.consolidateDown();
    }

    if (!isLeaf && canConsolidate()) {
      mType = children[0].mType;
      mVisible = children[0].mVisible; 
      children = null;
      isLeaf = true;
      count -= 8;
    }
  }

//   public boolean canConsolidate() {
//   // If any children are not leaves, false
//   if(!children[0].isLeaf && !children[1].isLeaf &&
//       !children[2].isLeaf && !children[3].isLeaf &&
//       !children[4].isLeaf && !children[5].isLeaf &&
//       !children[6].isLeaf && !children[7].isLeaf)
//     return(false);
//     
//   // Sort children
//   int s, t[] = new int[] {
//   children[0].mType.getID(), children[1].mType.getID(),
//   children[2].mType.getID(), children[3].mType.getID(),
//   children[4].mType.getID(), children[5].mType.getID(),
//   children[6].mType.getID(), children[7].mType.getID()
//   };
//      
//   if(t[0] > t[7]) { s = t[0]; t[0] = t[7]; t[7] = s; }
//   if(t[1] > t[6]) { s = t[1]; t[1] = t[6]; t[6] = s; }
//   if(t[2] > t[5]) { s = t[2]; t[2] = t[5]; t[5] = s; }
//   if(t[3] > t[4]) { s = t[3]; t[3] = t[4]; t[4] = s; }
//   if(t[0] > t[3]) { s = t[0]; t[0] = t[3]; t[3] = s; }
//   if(t[4] > t[7]) { s = t[4]; t[4] = t[7]; t[7] = s; }
//   if(t[1] > t[2]) { s = t[1]; t[1] = t[2]; t[2] = s; }
//   if(t[5] > t[6]) { s = t[5]; t[5] = t[6]; t[6] = s; }
//   if(t[0] > t[1]) { s = t[0]; t[0] = t[1]; t[1] = s; }
//   if(t[2] > t[3]) { s = t[2]; t[2] = t[3]; t[3] = s; }
//   if(t[4] > t[5]) { s = t[4]; t[4] = t[5]; t[5] = s; }
//   if(t[6] > t[7]) { s = t[6]; t[6] = t[7]; t[7] = s; }
//   if(t[2] > t[4]) { s = t[2]; t[2] = t[4]; t[4] = s; }
//   if(t[3] > t[5]) { s = t[3]; t[3] = t[5]; t[5] = s; }
//   if(t[1] > t[2]) { s = t[1]; t[1] = t[2]; t[2] = s; }
//   if(t[3] > t[4]) { s = t[3]; t[3] = t[4]; t[4] = s; }
//   if(t[5] > t[6]) { s = t[5]; t[5] = t[6]; t[6] = s; }
//   if(t[2] > t[3]) { s = t[2]; t[2] = t[3]; t[3] = s; }
//   if(t[4] > t[5]) { s = t[4]; t[4] = t[5]; t[5] = s; }
//      
//   boolean r = t[0] == t[7];
//   return(r);
//      
//   }

  public boolean canConsolidate() {
    int id = children[0].mType.getID();
    Visibility visibility = children[0].getVisibility();
    if (id != children[7].mType.getID() ||
        visibility != children[7].getVisibility())
      return (false);
    for (int i = 0; i < 8; i++)
      if (!children[i].isLeaf || children[i].mType.getID() != id ||
          children[i].getVisibility() != visibility)
        return (false);

    return (true);
  }

  public void setVolume(int x1, int y1, int z1, int x2, int y2, int z2,
      TerrainType type) {
    int minSize = Math.min(Math.min(Math.abs(x1 - x2), Math.abs(y1 - y2)), Math
        .abs(z1 - z2)) / 2;
    if (minSize < 1)
      minSize = 1;

    for (int x = x1; x <= x2; x++) {
      for (int y = y1; y <= y2; y++) {
        for (int z = z1; z <= z2; z++) {
          articulate(x, y, z, type, null, minSize);
        }
      }
    }
    // consolidateDown();
  }

  public void setBlock(int x, int y, int z, TerrainType type) {
    articulate(x, y, z, type, null, 1);
  }
  
  public void setVisibilityAt(Visibility v,int x,int y,int z) {
    articulate(x,y,z, null, v, 1);
  }
  
  private void articulate(int x, int y, int z, TerrainType type, Visibility v, int minSize) {
    if (isLeaf && mType == type && mVisible == v)
      return;

    if (this.size <= minSize) {
      if(type != null) this.mType = type;
      if(v != null) this.mVisible = v;
      return; // If this node is already min size, do nothing
    }

    if (x > this.x + size || x < this.x || y > this.y + size || y < this.y
        || z > this.z + size || z < this.z) {
      System.out.printf("(%d,%d,%d) out of bonds for %s", x, y, z, this);
      return; // If out of bounds, do nothing
    }

    // Split if leaf
    if (isLeaf) {
      isLeaf = false;
      children = new Terrain[] {
          new Terrain(this, this.x, this.y, this.z, size / 2, mType, mVisible),
          new Terrain(this, mx, this.y, this.z, size / 2, mType, mVisible),
          new Terrain(this, this.x, my, this.z, size / 2, mType, mVisible),
          new Terrain(this, mx, my, this.z, size / 2, mType, mVisible),

          new Terrain(this, this.x, this.y, mz, size / 2, mType, mVisible),
          new Terrain(this, mx, this.y, mz, size / 2, mType, mVisible),
          new Terrain(this, this.x, my, mz, size / 2, mType, mVisible),
          new Terrain(this, mx, my, mz, size / 2, mType, mVisible) };
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

   public boolean isVisible(int x, int y, int z) {
   Terrain t = getAt(x,y,z);
   if(t == null) return(false);
      
   if (t.getVisibility() == Visibility.UNDEFINED)
   t.setVisibilityAt( ((getTypeAt(x + 1, y, z).getFlags()
   | getTypeAt(x - 1, y, z).getFlags()
   | getTypeAt(x, y + 1, z).getFlags()
   | getTypeAt(x, y - 1, z).getFlags()
   | getTypeAt(x, y, z + 1).getFlags()
   | getTypeAt(x, y, z - 1).getFlags()) & TerrainType.F_NO_OCCLUDE) ==
   TerrainType.F_NO_OCCLUDE ? Visibility.VISIBLE
   : Visibility.HIDDEN,x,y,z);
      
   return (t.getVisibility() == Visibility.VISIBLE ? true : false);
   }
//  public boolean isVisible(int x, int y, int z) {
//    return(((getTypeAt(x + 1, y, z).getFlags()
//           | getTypeAt(x - 1, y, z).getFlags()
//           | getTypeAt(x, y + 1, z).getFlags()
//           | getTypeAt(x, y - 1, z).getFlags()
//           | getTypeAt(x, y, z + 1).getFlags() 
//           | getTypeAt(x, y, z - 1).getFlags()) & TerrainType.F_NO_OCCLUDE) == TerrainType.F_NO_OCCLUDE ? true
//              : false);
//  }

  public Visibility getVisibility() {
    return mVisible;
  }

  public void setVisibility(Visibility v) {
    mVisible = v;
  }
}
