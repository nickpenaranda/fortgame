package org.n4p.mountainking.units;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public enum UnitGraphics {
  PLAYER(2,4),
  SNAKE(0,9);
  
  private Image mImage;
  private final int mIndexX, mIndexY;
  
  UnitGraphics(int indexX,int indexY) {
    mIndexX = indexX;
    mIndexY = indexY;
    mImage = null;
  }
  
  public Image getImage() {
    if(mImage == null)
      mImage = spriteSheet.getSprite(mIndexX,mIndexY);
    return(mImage);
  }
  
  private static SpriteSheet spriteSheet;
  public static void init() {
    try {
      spriteSheet = new SpriteSheet("gfx/units/units.png",32,32,Color.magenta);
    } catch (SlickException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}