package org.n4p.earthhoard;

import java.util.TreeMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TerrainType {
  public static final int NONE = 0;
  public static final int AIR = 1;
  public static final int ROCK = 2;
  public static final int DIRT = 3;
  public static final int WATER = 4;
  public static final int GRASS = 5;

  public static final int NO_OCCLUDE = 1;

  private static TreeMap<Integer, TerrainType> mTerrainTypes = new TreeMap<Integer, TerrainType>();
  private int mFlags;
  private Image mImage;
  private Image mImage2;
  private int mID;

  public TerrainType(String name, int flags, Image img) {
    mFlags = flags;
    mImage = img;
    mImage2 = img;
  }

  public TerrainType(String name, int flags, Image img, Image img2) {
    mFlags = flags;
    mImage = img;
    mImage2 = img2;
  }

  public static void init() {
    try {
      TerrainType.add(NONE, new TerrainType("None", 0, null));

      TerrainType.add(AIR, new TerrainType("Air", NO_OCCLUDE, null));

      TerrainType.add(ROCK, new TerrainType("Rock", 0, new Image(
          "gfx/terrain/rock.png", Color.magenta)));

      TerrainType.add(DIRT, new TerrainType("Dirt", 0, new Image(
          "gfx/terrain/dirt.png", Color.magenta)));

      TerrainType.add(WATER, new TerrainType("Water", 0, new Image(
          "gfx/terrain/water.png", Color.magenta), new Image(
          "gfx/terrain/water2.png", Color.magenta)));

      TerrainType.add(GRASS, new TerrainType("Grass", 0, new Image(
          "gfx/terrain/grass.png", Color.magenta), new Image(
          "gfx/terrain/grass2.png", Color.magenta)));
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }

  public static void add(int id, TerrainType t) {
    t.mID = id;
    mTerrainTypes.put(id, t);
  }

  public static TerrainType get(int id) {
    return (mTerrainTypes.get(id));
  }

  public int getID() {
    return (mID);
  }

  public Image getImage(boolean flip) {
    return (flip ? mImage2 : mImage);
  }

  public int getFlags() {
    // TODO Auto-generated method stub
    return (mFlags);
  }
}
