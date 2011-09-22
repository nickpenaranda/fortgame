package org.n4p.mountainking;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MountainKing extends StateBasedGame {
  World mWorld;

  public MountainKing() {
    super("The Mountain King");
  }

  public static void main(String[] args) {
    try {
      AppGameContainer app = new AppGameContainer(new MountainKing());
      app.setDisplayMode(640, 480, false);
      app.setMinimumLogicUpdateInterval(20);
      app.setMaximumLogicUpdateInterval(20);
      app.start(); 
    } catch(SlickException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initStatesList(GameContainer container) throws SlickException {
    addState(new MainState());
  }
}
