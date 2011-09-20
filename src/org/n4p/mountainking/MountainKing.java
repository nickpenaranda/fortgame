package org.n4p.mountainking;

import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MountainKing extends StateBasedGame {
  World mWorld;
  public static final Random r = new Random();

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
  
  public static int rInt(int min, int max) {
    return (min + r.nextInt(max - min - 1));
  }
}
