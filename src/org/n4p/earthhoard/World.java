package org.n4p.earthhoard;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class World {
  private static Random mRandom = new Random();
  private Terrain mTerrain;
  private int vx = 0, vy = 0, vz = 0;
  public int rx = 0, ry = 0;
  public static boolean oldRender = false;
  private static int heightLimit = 1;
  private static int depthLimit = 6;
  private Image reticule, reticuleSelect;
  public static final int worldSize = 128;
  public static final Color depthFilter[] = {
    new Color(32,32,32), new Color(64,64,64), new Color(96,96,96),
    new Color(128,128,128), new Color(160,160,160), new Color(192,192,192)
  };
  
  public static int rInt(int min, int max) {
    return (min + mRandom.nextInt(max - min - 1));
  }

  public World() {
    mTerrain = new Terrain(null, -worldSize/2, -worldSize/2, -worldSize/2, worldSize, TerrainType
        .get(TerrainType.AIR),null);  }

  public void init() {
    try {
      reticule = new Image("gfx/reticule1.png",Color.magenta);
      reticuleSelect = new Image("gfx/reticule2.png",Color.magenta);
    } catch (SlickException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    System.out.println("Initializing world...");

    // mTerrain.setVolume(-64, -64, 0, 64, 64, 64, TerrainType
    // .get(TerrainType.AIR));

    float heightmap[][] = new float[worldSize+1][worldSize+1];
    heightmap[0][0] = heightmap[0][worldSize] = heightmap[worldSize][0] = heightmap[worldSize][worldSize] = 0;
    diamond_square(heightmap, 0, 0, worldSize, worldSize);

    for (int x = 0; x < worldSize+1; ++x) {
      for (int y = 0; y < worldSize+1; ++y) {
        mTerrain.setVolume(x - worldSize/2, y - worldSize/2, -worldSize/2, x - worldSize/2, y - worldSize/2, Math
            .round(heightmap[x][y]), TerrainType.get(TerrainType.DIRT));
      }
    }
    mTerrain.consolidateDown();

    // Fill with water
    int waterLevel = mRandom.nextInt(8) - 8;
    for (int x = -worldSize/2; x <= worldSize/2; ++x) {
      for (int y = -worldSize/2; y <= worldSize/2; ++y) {
        int surfZ = findSurfaceZ(x, y);
        if (waterLevel >= surfZ)
          mTerrain.setVolume(x, y, surfZ, x, y, waterLevel, TerrainType
              .get(TerrainType.WATER));
      }
    }
    mTerrain.consolidateDown();
  }

  private float mean(float a, float b) {
    return ((a + b) / 2);
  }

  private void diamond_square(float[][] h, int x1, int y1, int x2, int y2) {
    float s = x2 - x1;
    float k = 0.25F;
    if (s > 1) {
      int mx = (x1 + x2) / 2;
      int my = (y1 + y2) / 2;
      float center = (h[x1][y1] + h[x2][y1] + h[x1][y2] + h[x2][y2]) / 4;
      h[mx][my] = (float) (center + mRandom.nextGaussian() * (s / 2) * k);
      h[mx][y1] = mean(h[x1][y1], h[x2][y1]);
      h[mx][y2] = mean(h[x1][y2], h[x2][y2]);
      h[x1][my] = mean(h[x1][y1], h[x1][y2]);
      h[x2][my] = mean(h[x2][y1], h[x2][y2]);
      diamond_square(h, x1, y1, mx, my);
      diamond_square(h, mx, my, x2, y2);
      diamond_square(h, x1, my, mx, y2);
      diamond_square(h, mx, y1, x2, my);
    }
  }

  public int findSurfaceZ(int x, int y) {
    int z = worldSize/2;
    TerrainType air = TerrainType.get(TerrainType.AIR);
    while (mTerrain.getTypeAt(x, y, z) == air)
      z--;
    return (z);
  }
  
  public void adjustView(int dx, int dy, int dz) {
    vx += dx;
    vy += dy;
    vz += dz;
    
//    if(vx < -worldSize/2 - x_adjust)
//      vx = -worldSize/2 - x_adjust;
//    else if(vx > worldSize/2 - x_adjust)
//      vx = worldSize/2 - x_adjust;
//    if(vy < -worldSize/2 + y_adjust)
//      vy = -worldSize/2 + y_adjust;
//    else if(vy > worldSize/2 + y_adjust)
//      vy = worldSize/2 + y_adjust;
//    if(vz < -worldSize/2)
//      vz = -worldSize/2;
//    else if(vz > worldSize/2)
//      vz = worldSize/2;
  }

  public void render(GameContainer container, Graphics g, boolean mAnimFrame) {
    Color color;
    int sx = vx;
    int sy = vy;
    int x_off, y_off, y_off_z;
    int tx, ty;
    Terrain t[] = new Terrain[heightLimit + depthLimit + 1];
    
    for (int y = -(depthLimit << 1); y < 61 + heightLimit; ++y) {
      y_off = ((y - 3) << 3);
      if ((y & 1) == 0) {// Even row
        x_off = 0;
        ++sx;
      } else {
        x_off = -16;
        --sy;
      }
      tx = sx;
      ty = sy;
      for (int x = 0; x < 21; ++x) {
        for (int z = 0; z < depthLimit + 1 + heightLimit; ++z) {
          t[z] = mTerrain.getAt(tx, ty,vz + z - depthLimit);
          if(t[z] != null && mTerrain.isVisible(tx,ty,vz+z - depthLimit)) {
            Image img = t[z].mType.getImage(mAnimFrame);
            if (img != null) {
              if(z < depthLimit )
                color = depthFilter[z];
              else
                color = null;
              y_off_z = y_off - (z - 1 << 4) + (depthLimit << 4);
              if (!(y_off_z < -32 || y_off_z > 480 + 32)) {
                if(z - depthLimit == 1 && tx == rx && ty == ry)
                  g.drawImage(img, x_off + (x << 5), y_off_z, Color.yellow);
                else
                  g.drawImage(img, x_off + (x << 5), y_off_z, color);
                if(z - depthLimit == 0) {
                  if(tx == rx && ty == ry)
                    g.drawImage(reticuleSelect, x_off + (x << 5), y_off_z, Color.yellow);
                  else
                    g.drawImage(reticule, x_off + (x << 5), y_off_z, Color.white);
                } else if (z - depthLimit == 1) {
                  
                }
              }
            }
          }
        }
        ++tx;
        ++ty;
      }
    }
  }

  public Terrain getTerrain() {
    return (mTerrain);
  }

  public int getViewZ() {
    // TODO Auto-generated method stub
    return vz;
  }

  public static int getHeightLimit() {
    return heightLimit;
  }

  public static int getDepthLimit() {
    return depthLimit;
  }

  public static void setHeightLimit(int heightLimit) {
    if (heightLimit < 0)
      return;
    World.heightLimit = heightLimit;
  }

  public static void setDepthLimit(int depthLimit) {
    if (depthLimit < 0 || depthLimit > 6)
      return;
    World.depthLimit = depthLimit;
  }

  public Coord screenToWorld(int x, int y) {    
    int wx = vx + 7 + Math.round(((x - 16) / (float)32) + ((y) / (float)16));
    int wy = vy - 6 + Math.round(((x - 16) / (float)32) - ((y) / (float)16));
    //if((y_row & 1) != 0)
    //  wy--;
    // TODO Auto-generated method stub
    rx = wx;
    ry = wy;
    return new Coord(wx,wy,vz);
  }
}
