package org.n4p.mountainking.units;

import org.n4p.mountainking.Coord;
import org.newdawn.slick.Image;

public class Snake extends Creature {

  public Snake(Coord location) {
    super(location);
  }

  @Override
  public Image getImage() {
    return(UnitGraphic.SNAKE.getImage());
  }

  @Override
  public String getName() {
    return("Snake");
  }

}
