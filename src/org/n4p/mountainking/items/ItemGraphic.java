package org.n4p.mountainking.items;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public enum ItemGraphic {
  EMBEDDED_IRON(0,0,1,0),
  EMBEDDED_COPPER(2,0,3,0),
  EMBEDDED_CARNIUM(4,0,5,0),
  EMBEDDED_DROWITE(6,0,7,0),
  EMBEDDED_ENDERITE(8,0,9,0),
  EMBEDDED_AZRAELITE(10,0,11,0),
  EMBEDDED_ODINSTONE(12,0,13,0),
  EMBEDDED_RAGNARITE(14,0,15,0),
  
  EXPOSED_IRON(0,1,1,1),
  EXPOSED_COPPER(2,1,3,1),
  EXPOSED_CARNIUM(4,1,5,1),
  EXPOSED_DROWITE(6,1,7,1),
  EXPOSED_ENDERITE(8,1,9,1),
  EXPOSED_AZRAELITE(10,1,11,1),
  EXPOSED_ODINSTONE(12,1,13,1),
  EXPOSED_RAGNARITE(14,1,15,1);
  
  private Image mImage[];
  private final int mNumFrames;
  private final int mIndexX[], mIndexY[];
  
  ItemGraphic(int ... params) throws IllegalArgumentException {
    if((params.length & 1) != 0) 
      throw new IllegalArgumentException("Invalid number of parameters passed in ItemGraphics constructor; must be (x,y) pairs");
    mNumFrames = params.length >> 1;
    mIndexX = new int[mNumFrames];
    mIndexY = new int[mNumFrames];
    mImage = new Image[mNumFrames];
    
    for(int i=0;i<mNumFrames;i++) {
      mIndexX[i] = params[(i << 1)];
      mIndexY[i] = params[1+(i << 1)];
    }
  }
  
  public Image getImage() {
    return(getImage(0));
  }
  
  public Image getImage(int i) {
    if(i >= mNumFrames) return(null);
    
    if(mImage[i] == null)
      mImage[i] = spriteSheet.getSprite(mIndexX[i],mIndexY[i]);
    return(mImage[i]);
  }
  
  private static SpriteSheet spriteSheet;
  
  public static void init() {
    try {
      spriteSheet = new SpriteSheet("gfx/items/items.png",32,32,Color.magenta);
    } catch (SlickException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}