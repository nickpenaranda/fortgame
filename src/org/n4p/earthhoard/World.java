package org.n4p.earthhoard;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;

public class World {
	private static Random mRandom = new Random();
	private Terrain mTerrain;
	private int vx = 0, vy = 0, vz = 0;
	private static int heightLimit = 0;
	private static int depthLimit = 2;

	public static int rInt(int min, int max) {
		return (min + mRandom.nextInt(max - min - 1));
	}

	public World() {
		mTerrain = new Terrain(null, -64, -64, -64, 128,
				TerrainType.get(TerrainType.AIR));
	}

	public void init() {
		System.out.println("Initializing world...");

		// mTerrain.setVolume(-64, -64, 0, 64, 64, 64, TerrainType
		// .get(TerrainType.AIR));

		float heightmap[][] = new float[129][129];
		heightmap[0][0] = heightmap[0][128] = heightmap[128][0] = heightmap[128][128] = 0;
		diamond_square(heightmap, 0, 0, 128, 128);

		for (int x = 0; x < 129; ++x) {
			for (int y = 0; y < 129; ++y) {
				mTerrain.setVolume(x - 64, y - 64, -64, x - 64, y - 64,
						Math.round(heightmap[x][y]), TerrainType.get(TerrainType.GRASS));
			}
		}
		mTerrain.consolidateDown();

		// Fill with water
		int waterLevel = mRandom.nextInt(25) - 24;
		for (int x = -64; x <= 64; ++x) {
			for (int y = -64; y <= 64; ++y) {
				int surfZ = findSurfaceZ(x, y);
				if (waterLevel >= surfZ)
					mTerrain.setVolume(x, y, surfZ, x, y, waterLevel,
							TerrainType.get(TerrainType.WATER));
			}
		}
		mTerrain.consolidateDown();
	}

	private float mean(float a, float b) {
		return ((a + b) / 2);
	}

	private void diamond_square(float[][] h, int x1, int y1, int x2, int y2) {
		float s = x2 - x1;
		if (s > 1) {
			int mx = (x1 + x2) / 2;
			int my = (y1 + y2) / 2;
			float center = (h[x1][y1] + h[x2][y1] + h[x1][y2] + h[x2][y2]) / 4;
			h[mx][my] = (float) (center + mRandom.nextGaussian() * (s / 2));
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
		int z = 64;
		TerrainType air = TerrainType.get(TerrainType.AIR);
		while (mTerrain.getTypeAt(x, y, z) == air)
			z--;
		return (z);
	}

	public void adjustView(int dx, int dy, int dz) {
		vx += dx;
		vy += dy;
		vz += dz;
	}

	public void seedVisibility() {
		if (mTerrain.getTypeAt(0, 0, 0) == TerrainType.get(TerrainType.AIR)) {

		}
	}

	public void render(GameContainer container, Graphics g, boolean mAnimFrame) {
    Color color;
    int sx = vx;
    int sy = vy;
    int x_off, y_off,y_off_z;
    int tx, ty;
    new TerrainPolygon();
    TerrainType type[] = new TerrainType[heightLimit + depthLimit + 1];
    
    for (int y = 0; y < 72 + heightLimit; y++) {
      y_off = ((y - 3) << 3);
      if (y % 2 == 0) {// Even row
        x_off = 0;
        ++sx;
      } else {
        x_off = -16;
        --sy;
      }
      tx = sx;
      ty = sy;
      for (int x = 0; x < 21; x++) {
        for (int z = 0; z < depthLimit + 1 + heightLimit; ++z) {
          type[z] = mTerrain.getTypeAt(tx, ty, vz + z - depthLimit);
          if (type[z] != null) {
            Image img = type[z].getImage(mAnimFrame);
            if (img != null) {
              if (z - depthLimit <= -3)
                color = Color.darkGray;
              else {
              	switch(z - depthLimit) {
              	case -2:
                  color = Color.gray;
                  break;
              	case -1:
              		color = Color.lightGray;
              		break;
              	case 0:
              		color = null;
              		break;
              	default:
              		color = Color.cyan;
              		break;
              	}
              }
              y_off_z = y_off - (z - 1 << 4) + (depthLimit << 4);
              if(!(y_off_z < -32 || y_off_z > 480 + 32))
              	g.drawImage(img, x_off + (x << 5), y_off_z, color);
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

	private static class TerrainPolygon extends Polygon {
		private static final long serialVersionUID = -2252726383272443092L;

		public TerrainPolygon() {
			addPoint(0, 8); // Left
			addPoint(16, 0); // Top
			addPoint(32, 8); // Right
			addPoint(16, 16); // Bottom
		}
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
		if(heightLimit < 0) return;
		World.heightLimit = heightLimit;
	}

	public static void setDepthLimit(int depthLimit) {
		if(depthLimit < 0) return;
		World.depthLimit = depthLimit;
	}
}
