package org.n4p.earthhoard.fixtures;

import java.util.TreeMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

public class Fixture {
  private String mName;
  private Image mImage, mImage2;
  private int mID;
  
  private static TreeMap<Integer, Fixture> mFixtures = new TreeMap<Integer, Fixture>();
  public static final int START_MARKER = 0;
  public static final int END_MARKER = 1;
  public static final int PATH_MARKER = 2;
  
  
  public Fixture(String name,Image img1, Image img2) {
    mName = name;
    mImage = img1;
    mImage2 = img2;
  }
  
  public Fixture(String name,Image img) {
    mName = name;
    mImage = img;
    mImage2 = img;
  }
  
  public static void init() {
    try {
      add(START_MARKER,new Fixture("Start Marker",new Image("gfx/fixtures/start.png",Color.magenta),
          new Image("gfx/fixtures/start2.png",Color.magenta)));
      add(END_MARKER,new Fixture("End Marker",new Image("gfx/fixtures/end.png",Color.magenta),
          new Image("gfx/fixtures/end2.png",Color.magenta)));
      add(PATH_MARKER,new Fixture("End Marker",new Image("gfx/fixtures/pathmarker.png",Color.magenta),
          new Image("gfx/fixtures/pathmarker2.png",Color.magenta)));
    } catch(SlickException e) {
      e.printStackTrace();
    }
  }

  public void setID(int id) {mID = id;}
  public int getID() {return mID;}
  
  public Image getImage(boolean flip) {
    return (flip ? mImage2 : mImage);
  }
  
  public static void add(int id, Fixture f) {
    mFixtures.put(id, f);
    f.setID(id);
  }
  
  public static Fixture get(int id) { return(mFixtures.get(id)); }
  
  @Override
  public boolean equals(Object aThat) {
    if(this == aThat) return(true);
    if(!(aThat instanceof Fixture)) return(false);
    Fixture that = (Fixture)aThat;
    return(this.getID() == that.getID());
  }
}
