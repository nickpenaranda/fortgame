package org.n4p.earthhoard;

public class Terrain {
  public static int count = 0;
  private Terrain[] children;
  public boolean isLeaf;
  private int x, y, z;
  private int mx, my, mz;
  private int size;
  public TerrainType mType;

  public Terrain(Terrain parent, int x, int y, int z, int size, TerrainType type) {
    this.x = x;
    this.y = y;
    this.z = z;

    mx = x + size / 2;
    my = y + size / 2;
    mz = z + size / 2;

    this.size = size;
    this.isLeaf = true;
    this.mType = type;
    count++;
  }
  
  public TerrainType getTypeAt(int x, int y, int z) {
    if (x > this.x + size || x < this.x || y > this.y + size || y < this.y
        || z > this.z + size || z < this.z) {
      //System.out.printf("getTypeAt:(%d,%d,%d) is out of bounds for %s\n",x,y,z,this);
      return (null);
    } else if (isLeaf)
      return (this.mType);
    else {
      if (x < mx) {
        if (y < my) {
          if (z < mz) {
            return (children[0].getTypeAt(x, y, z));
          } else {
            return (children[4].getTypeAt(x, y, z));
          }
        } else {
          if (z < mz) {
            return (children[2].getTypeAt(x, y, z));
          } else {
            return (children[6].getTypeAt(x, y, z));
          }
        }
      } else {
        if (y < my) {
          if (z < mz) {
            return (children[1].getTypeAt(x, y, z));
          } else {
            return (children[5].getTypeAt(x, y, z));
          }
        } else {
          if (z < mz) {
            return (children[3].getTypeAt(x, y, z));
          } else {
            return (children[7].getTypeAt(x, y, z));
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
      children = null;
      isLeaf = true;
      count -= 8;
    }
  }

  public boolean canConsolidate() {
    int id = children[0].mType.getID();
    if (children[0].mType.getID() != children[7].mType.getID())
      return (false);
    for (int i = 0; i < 8; i++)
      if (!children[i].isLeaf || children[i].mType.getID() != id)
        return (false);

    return (true);
  }

  public void setVolume(int x1, int y1, int z1, int x2, int y2, int z2,
      TerrainType type) {
    int minSize = Math.min(Math.min(Math.abs(x1-x2),Math.abs(y1-y2)),Math.abs(z1-z2)) / 2;
    if(minSize < 1) minSize = 1;
    
    for (int x = x1; x <= x2; x++) {
      for (int y = y1; y <= y2; y++) {
        for (int z = z1; z <= z2; z++) {
          articulate(x, y, z, type,minSize);
        }
      }
    }
    //consolidateDown();
  }

  public void setBlock(int x, int y, int z, TerrainType type) {
    articulate(x, y, z, type,1);
  }
  
  private void articulate(int x, int y, int z, TerrainType type, int minSize) {
    if (isLeaf && mType == type)
      return;

    if (this.size <= minSize) {
      this.mType = type;
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
          new Terrain(this, this.x, this.y, this.z, size / 2, mType),
          new Terrain(this, mx, this.y, this.z, size / 2, mType),
          new Terrain(this, this.x, my, this.z, size / 2, mType),
          new Terrain(this, mx, my, this.z, size / 2, mType),

          new Terrain(this, this.x, this.y, mz, size / 2, mType),
          new Terrain(this, mx, this.y, mz, size / 2, mType),
          new Terrain(this, this.x, my, mz, size / 2, mType),
          new Terrain(this, mx, my, mz, size / 2, mType) };
    }

    // Pass articulation to proper child
    if (x < mx) {
      if (y < my) {
        if (z < mz) {
          children[0].articulate(x, y, z, type,minSize);
        } else {
          children[4].articulate(x, y, z, type,minSize);
        }
      } else {
        if (z < mz) {
          children[2].articulate(x, y, z, type,minSize);
        } else {
          children[6].articulate(x, y, z, type,minSize);
        }
      }
    } else {
      if (y < my) {
        if (z < mz) {
          children[1].articulate(x, y, z, type,minSize);
        } else {
          children[5].articulate(x, y, z, type,minSize);
        }
      } else {
        if (z < mz) {
          children[3].articulate(x, y, z, type,minSize);
        } else {
          children[7].articulate(x, y, z, type,minSize);
        }
      }
    }
  }
}
