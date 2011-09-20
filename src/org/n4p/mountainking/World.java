package org.n4p.mountainking;

import java.util.ArrayList;

import org.n4p.mountainking.items.*;
import org.n4p.mountainking.terrain.Terrain;
import org.n4p.mountainking.terrain.Terrain.TerrainNode;
import org.n4p.mountainking.terrain.TerrainType;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class World {
  private Terrain mTerrain;

  private Coord viewPos = new Coord(0, 0, 0);
  private Coord cursorPos = new Coord(0, 0, 0);
  private StartMarker pathStart;
  private EndMarker pathEnd;
  private ArrayList<PathMarker> pathMarkers;
  
  
  private static int adjust_x = -31;
  private static int adjust_y = 11;

  private static int heightLimit = 1;
  private static int depthLimit = 6;

  private Image activeGrid, reticuleSelect;

  public static final int worldSize = 128;

  private static final Color depthFilter[] = { new Color(32, 32, 32),
      new Color(64, 64, 64), new Color(96, 96, 96), new Color(128, 128, 128),
      new Color(160, 160, 160), new Color(192, 192, 192) };

  public World() {
    mTerrain = new Terrain(worldSize);
  }

  public void init() {
    try {
      activeGrid = new Image("gfx/reticule1.png", Color.magenta);
      reticuleSelect = new Image("gfx/reticule2.png", Color.magenta);
    } catch (SlickException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("Initializing world...");

    // mmTerrain.setVolume(-64, -64, 0, 64, 64, 64, TerrainType
    // .get(TerrainType.AIR));

    // Overall geometry heightmap
    float heightmap[][] = new float[worldSize + 1][worldSize + 1];
    heightmap[0][0] = heightmap[0][worldSize] = heightmap[worldSize][0] = heightmap[worldSize][worldSize] = 0;
    diamond_square(heightmap, 0, 0, worldSize, worldSize,0.25f);

    for (int x = 0; x < worldSize ; ++x) {
      for (int y = 0; y < worldSize ; ++y) {
        mTerrain.setVolume(x - worldSize / 2, y - worldSize / 2,
            -worldSize / 2, x - worldSize / 2, y - worldSize / 2, Math
                .round(heightmap[x][y]), TerrainType.get(TerrainType.DIRT));
      }
    }
    mTerrain.doConsolidate();
    
    int rock_delta = MountainKing.r.nextInt(8)-4; // Rock strata "central" z is +/- 4 from 0
    // Re-process heightmap with lower k to create rock strata
    diamond_square(heightmap, 0, 0, worldSize, worldSize,0.05f);
    for (int x = 0; x < worldSize ; ++x) {
      for (int y = 0; y < worldSize ; ++y) {
        mTerrain.setSolidVolume(x - worldSize / 2, y - worldSize / 2,
            -worldSize / 2, x - worldSize / 2, y - worldSize / 2, Math
                .round(heightmap[x][y])+rock_delta, TerrainType.get(TerrainType.ROCK));
      }
    }
    mTerrain.doConsolidate();
    
    
    // Fill with water
    int waterLevel = MountainKing.r.nextInt(8) - 12;
    for (int x = -worldSize / 2; x < worldSize / 2; ++x) {
      for (int y = -worldSize / 2; y < worldSize / 2; ++y) {
        int surfZ = findSurfaceZ(x, y);
        if (waterLevel >= surfZ)
          mTerrain.setVolume(x, y, surfZ, x, y, waterLevel, TerrainType
              .get(TerrainType.WATER));
      }
    }
    mTerrain.doConsolidate();
    
    // Test--place mineral fixtures
    for(int n=0;n<5000;++n) {
    	TerrainNode t = mTerrain.getBlockAt(getUndergroundPoint());
    	switch(MountainKing.r.nextInt(8)) {
    	case 0: new IronNode(t); break;
    	case 1: new CopperNode(t); break;
    	case 2: new DrowiteNode(t); break;
    	case 3: new CarniumNode(t); break;
    	case 4: new AzraeliteNode(t); break;
    	case 5: new EnderiteNode(t); break;
    	case 6: new RagnariteNode(t); break;
    	case 7: new OdinstoneNode(t); break;

    	}
    }
    
    // Seed grass on dirt
    for(int n=0;n<30;n++) {
    	int surfZ = 0;
    	int x,y;
    	do {
	    	x = MountainKing.r.nextInt(worldSize)-64;
	    	y = MountainKing.r.nextInt(worldSize)-64;
	    	surfZ = findSurfaceZ(x,y);
    	} while(getTerrain().getAt(x,y,surfZ-1).getType().getID() != TerrainType.DIRT);
    	
    	getTerrain().setBlock(x, y, surfZ-1, TerrainType.get(TerrainType.GRASS));
    	ArrayList<Coord> spread = PathFinder.spread(new Coord(x,y,surfZ), MountainKing.r.nextInt(200)+200);
    	for(Coord c:spread) {
    		getTerrain().setBlock(c.x, c.y, c.z-1, TerrainType.get(TerrainType.GRASS));
    	}
    }
    
    pathStart = null;
    pathEnd = null;
    pathMarkers = null;
  }

  private float mean(float a, float b) {
    return ((a + b) / 2);
  }

  private void diamond_square(float[][] h, int x1, int y1, int x2, int y2,float k) {
    float s = x2 - x1;
    if (s > 1) {
      int mx = (x1 + x2) / 2;
      int my = (y1 + y2) / 2;
      float center = (h[x1][y1] + h[x2][y1] + h[x1][y2] + h[x2][y2]) / 4;
      h[mx][my] = (float) (center + MountainKing.r.nextGaussian() * (s / 2) * k);
      h[mx][y1] = mean(h[x1][y1], h[x2][y1]);
      h[mx][y2] = mean(h[x1][y2], h[x2][y2]);
      h[x1][my] = mean(h[x1][y1], h[x1][y2]);
      h[x2][my] = mean(h[x2][y1], h[x2][y2]);
      diamond_square(h, x1, y1, mx, my,k);
      diamond_square(h, mx, my, x2, y2,k);
      diamond_square(h, x1, my, mx, y2,k);
      diamond_square(h, mx, y1, x2, my,k);
    }
  }

  public int findSurfaceZ(int x, int y) {
    int z = worldSize / 2 - 1;
    TerrainType air = TerrainType.get(TerrainType.AIR);
    while (mTerrain.getTypeAt(x, y, z) == air)
      z--;
    if(z < -64) 
      z = -64;
    return (z);
  }

  public void adjustView(int dx, int dy, int dz) {
    viewPos.x += dx;
    viewPos.y += dy;
    viewPos.z += dz;

    // if(viewPos.x < -worldSize/2 - x_adjust)
    // viewPos.x = -worldSize/2 - x_adjust;
    // else if(viewPos.x > worldSize/2 - x_adjust)
    // viewPos.x = worldSize/2 - x_adjust;
    // if(viewPos.y < -worldSize/2 + y_adjust)
    // viewPos.y = -worldSize/2 + y_adjust;
    // else if(viewPos.y > worldSize/2 + y_adjust)
    // viewPos.y = worldSize/2 + y_adjust;
    // if(viewPos.z < -worldSize/2)
    // viewPos.z = -worldSize/2;
    // else if(viewPos.z > worldSize/2)
    // viewPos.z = worldSize/2;
  }

  public void render(GameContainer container, Graphics g, boolean mAnimFrame) {
    Color color;
    int sx = viewPos.x + adjust_x;
    int sy = viewPos.y + adjust_y;
    int x_off, y_off, y_off_z;
    int tx, ty;
    Terrain.TerrainNode t[] = new Terrain.TerrainNode[heightLimit + depthLimit + 1];

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
          
          t[z] = mTerrain.getAt(tx, ty, viewPos.z + z - depthLimit - 1);
          
          if (t[z] != null
              && mTerrain.isVisible(tx, ty, viewPos.z + z - depthLimit)) {
            
            y_off_z = y_off - ((z - 1) << 4) + (depthLimit << 4);
            if (z < depthLimit)
              color = depthFilter[z];
            else
              color = null;

            // Draw terrain
            Image img = t[z].getType().getImage(mAnimFrame);
            
            if (img != null) {
              
              if (z < depthLimit)
                color = depthFilter[z];
              else
                color = null;

              if (!(y_off_z < -32 || y_off_z > 480 + 32)) {
                
                if (z - depthLimit == 1 && tx == cursorPos.x
                    && ty == cursorPos.y)
                  g.drawImage(img, x_off + (x << 5), y_off_z, Color.yellow);
                else
                  g.drawImage(img, x_off + (x << 5), y_off_z, color);
                
                if (z - depthLimit == 0) {
                  if (tx == cursorPos.x && ty == cursorPos.y)
                    g.drawImage(reticuleSelect, x_off + (x << 5), y_off_z,
                        Color.yellow);
                  else
                    g.drawImage(activeGrid, x_off + (x << 5), y_off_z,
                        Color.white);
                }
              }
            }
            
            // Draw fixtures
            ArrayList<AbstractItem> items = t[z].getItems();
            if(items != null && !items.isEmpty()) {
              for(AbstractItem i:items) {
                g.drawImage(i.getWorldAppearance(mAnimFrame), x_off + (x << 5),y_off_z - 16,color);
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
    return viewPos.z;
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

  public void setCursorPos(int x, int y, int z) {
    cursorPos.x = x;
    cursorPos.y = y;
    cursorPos.z = z;
  }

  public void setCursorPos(Coord c) {
    cursorPos = c;
  }

  public Coord screenToWorld(int x, int y) {
    int wx = viewPos.x + 7
        + Math.round(((x - 16) / (float) 32) + ((y) / (float) 16)) + adjust_x;
    int wy = viewPos.y - 6
        + Math.round(((x - 16) / (float) 32) - ((y) / (float) 16)) + adjust_y;

    return new Coord(wx, wy, viewPos.z);
  }
  
  public void centerAt(Coord c) {
    viewPos = c;
  }
  
  public void centerAt(int x,int y,int z) {
    viewPos.setTo(x,y,z);
  }

  public void centerAtCursor() {
    centerAt(cursorPos);
  }
  
  public boolean isTraversible(Coord c) {
    return(
        (mTerrain.getAt(c.move(0,0,-1)).getType().getFlags() & TerrainType.SOLID) == TerrainType.SOLID &&
        (mTerrain.getAt(c).getType().getFlags() & TerrainType.CAN_TRAVEL) == TerrainType.CAN_TRAVEL &&
        (mTerrain.getAt(c.move(0,0,1)).getType().getFlags() & TerrainType.CAN_TRAVEL) == TerrainType.CAN_TRAVEL
    );
  }
  
  public static boolean isInBounds(Coord c) {
    return(c.x >= -worldSize/2 && c.x < worldSize/2 &&
           c.y >= -worldSize/2 && c.y < worldSize/2 &&
           c.z >= -worldSize/2 && c.z < worldSize/2);
  }

  public void setPathStart() {
    if(!isInBounds(cursorPos) || !isTraversible(cursorPos)) return;
    if(pathStart != null) {
      pathStart.destroy();
    }
    TerrainNode t = mTerrain.getBlockAt(cursorPos);
    pathStart = new StartMarker(t);
  }

  public void setPathEnd() {
    if(!isInBounds(cursorPos) || !isTraversible(cursorPos)) return;
    if(pathEnd != null) {
      pathEnd.destroy();
    }
    TerrainNode t = mTerrain.getBlockAt(cursorPos);
    pathEnd = new EndMarker(t);
  }

  public void findPath() {
  	Path path = null;
  	
    if(pathStart != null && pathEnd != null) {
      if(pathMarkers != null) {
        for(PathMarker p:pathMarkers) {
          p.destroy();
        }
        pathMarkers.clear();
      }
      path = PathFinder.findPath(pathStart.getOwner().getPosition(),pathEnd.getOwner().getPosition());
    }
    
    if(path != null) {
    	pathMarkers = new ArrayList<PathMarker>();
      for(Coord c:path) {
      	TerrainNode t = mTerrain.getBlockAt(c);
        pathMarkers.add(new PathMarker(t));
      }
    }
  }

  public Coord getCursorPos() {
    return(cursorPos);
  }
  
  public Coord getUndergroundPoint() {
  	Coord c;
  	do {
  	c = new Coord(MountainKing.r.nextInt(worldSize)-worldSize/2,
			MountainKing.r.nextInt(worldSize)-worldSize/2,
			MountainKing.r.nextInt(worldSize)-worldSize/2);
  	} while((mTerrain.getTypeAt(c).getFlags() & TerrainType.SOLID) != TerrainType.SOLID);
  	
  	return(c);
  }
}