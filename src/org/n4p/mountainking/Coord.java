package org.n4p.mountainking;

public class Coord {
  public int x, y, z;
  
  public Coord(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Coord(Coord c) {
    this.x = c.x;
    this.y = c.y;
    this.z = c.z;
  }
  
  public static ScreenCoord toScreen(Coord w) {
    int screen_x = (int)(ScreenCoord.oX + (w.x * 7) + (w.y * 7));
    int screen_y = (int)(ScreenCoord.oY + (w.x * 4) - (w.y * 4) - (w.z * 8));
    return(new ScreenCoord(screen_x,screen_y));
  }
  
  public Coord move(int x,int y,int z) {
    return(new Coord(this.x+x,this.y+y,this.z+z));
  }
  
  public void moveThis(int x,int y,int z) {
    this.x+=x;
    this.y+=y;
    this.z+=z;
  }
  
  @Override
  public String toString() {
    return(String.format("(%d,%d,%d)", x,y,z));
  }

  public void setTo(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;    
  }

  @Override
  public boolean equals(Object aThat) {
    if(this == aThat) return(true);
    if(!(aThat instanceof Coord)) return(false);
    Coord that = (Coord) aThat;
    return(this.x == that.x && this.y == that.y && this.z == that.z);
  }
  
  
}
