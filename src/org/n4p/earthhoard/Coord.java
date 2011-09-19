package org.n4p.earthhoard;

public class Coord {
  public int x, y, z;
  
  public Coord(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public static ScreenCoord toScreen(Coord w) {
    int screen_x = (int)(ScreenCoord.oX + (w.x * 7) + (w.y * 7));
    int screen_y = (int)(ScreenCoord.oY + (w.x * 4) - (w.y * 4) - (w.z * 8));
    return(new ScreenCoord(screen_x,screen_y));
  }
  
  @Override
  public String toString() {
    return(String.format("(%d,%d,%d)", x,y,z));
  }
}
