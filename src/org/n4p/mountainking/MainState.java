package org.n4p.mountainking;

import java.util.ArrayList;
import java.util.List;

import org.n4p.mountainking.items.ItemGraphic;
import org.n4p.mountainking.terrain.Terrain;
import org.n4p.mountainking.terrain.TerrainType;
import org.n4p.mountainking.units.Unit;
import org.n4p.mountainking.units.UnitGraphic;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainState extends BasicGameState {
  private static final long ANIM_INTERVAL = 500;
  private static final int VERTICAL_MOVEMENT_INTERVAL = 200;
  private static final long CONSOLIDATE_INTERVAL = 5000;
  private static final long PAN_INTERVAL = 34;
  
  private static final int MOVEMENT_SCALE = 14;

  private long mGameTime, mLastAnim, mLastConsolidate, mLastPan;
  private boolean mAnimFrame = true;
  private static final int STATE_ID = 1;

  private static final int MESSAGE_DURATION = 5000;

  private MountainKing mGame;
  private GameContainer mContainer;

  private boolean mPaused = false;

  private int mMovement = 0;
  private int mVerticalTimeout = 0;
  private int mRenderTime;
  private static List<Message> messages;

  private static class Message {
    public String text;
    public long timeStamp;

    public Message(String text, long timeStamp) {
      this.text = text;
      this.timeStamp = timeStamp;
    }
  }

  private boolean mShift = false;
  private boolean mCtrl = false;
  private boolean mDebug = false;
  public boolean mPlayerViewLock = false;
  static final int NORTH = 1;
  static final int SOUTH = 2;
  static final int WEST = 4;
  static final int EAST = 8;
  static final int UP = 16;
  static final int DOWN = 32;

  @Override
  public int getID() {
    return STATE_ID;
  }

  public static void message(String str) {
    messages.add(new Message(str, System.nanoTime() / 1000000));
  }

  @Override
  public void init(GameContainer container, StateBasedGame game)
      throws SlickException {
    mGame = (MountainKing) game;
    mContainer = container;

    messages = new ArrayList<Message>();

    System.out.println("Initializing terrain graphics...");
    TerrainType.init();
    System.out.println("Initializing unit graphics...");
    UnitGraphic.init();
    System.out.println("Initializing item graphics...");
    ItemGraphic.init();

    mGame.mWorld = World.getInstance();
    mGame.mWorld.init();

    mGameTime = 0;
    mLastAnim = 0;
    mLastConsolidate = 0;
    mContainer.setShowFPS(false);
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g)
      throws SlickException {
    
    // Render world
    mGame.mWorld.render(container, g, mAnimFrame);
        
    // Render messages
    int y = 0;
    for (Message m : messages) {
      g.drawString(m.text, 10, (y++) * 15);
    }
    
    // Render player information overlay
    
    // Render debug information 
    g.setColor(Color.white);
    if (mDebug) {
      g.drawString("" + mGameTime + (mPaused ? " PAUSED" : ""), 10, 25);
      g.drawString("Z = " + mGame.mWorld.getViewZ(), 10, 40);
      g.drawString("Count = " + Terrain.count, 10, 55);
    }

  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {
    int move_delta = delta / MOVEMENT_SCALE;

    mRenderTime += delta;
    mVerticalTimeout -= delta;
    switch (mMovement & ~(UP | DOWN)) {
    case NORTH:
      mGame.mWorld.adjustView(-move_delta, move_delta, 0);
      break;
    case NORTH | EAST:
      mGame.mWorld.adjustView(0, move_delta, 0);
      break;
    case EAST:
      mGame.mWorld.adjustView(move_delta, move_delta, 0);
      break;
    case SOUTH | EAST:
      mGame.mWorld.adjustView(move_delta, 0, 0);
      break;
    case SOUTH:
      mGame.mWorld.adjustView(move_delta, -move_delta, 0);
      break;
    case SOUTH | WEST:
      mGame.mWorld.adjustView(0, -move_delta, 0);
      break;
    case WEST:
      mGame.mWorld.adjustView(-move_delta, -move_delta, 0);
      break;
    case NORTH | WEST:
      mGame.mWorld.adjustView(-move_delta, 0, 0);
    }

    if ((mMovement & UP) == UP && mVerticalTimeout <= 0) {
      mGame.mWorld.adjustView(0, 0, 1);
      mVerticalTimeout = VERTICAL_MOVEMENT_INTERVAL;
    }

    else if ((mMovement & DOWN) == DOWN && mVerticalTimeout <= 0) {
      mGame.mWorld.adjustView(0, 0, -1);
      mVerticalTimeout = VERTICAL_MOVEMENT_INTERVAL;
    }

    if (mRenderTime - mLastAnim > ANIM_INTERVAL) {
      mAnimFrame = !mAnimFrame;
      mLastAnim = mRenderTime;
    }

    if (mRenderTime - mLastConsolidate > CONSOLIDATE_INTERVAL) {
      mGame.mWorld.getTerrain().doConsolidate();
      mLastConsolidate = mRenderTime;
    }

    if (!mPaused) {
      mGameTime += delta;
      ArrayList<Unit> units = mGame.mWorld.getUnitList();
      for (Unit u : units) {
        u.think(mGame.mWorld, delta);
      }
    }

    for (Message m : new ArrayList<Message>(messages)) {
      if (System.nanoTime() / 1000000 > m.timeStamp + MESSAGE_DURATION)
        messages.remove(m);
    }
    
    if (mRenderTime - mLastPan > PAN_INTERVAL) {
      mGame.mWorld.panViewToLocation();
      mLastPan = mRenderTime;
    }
  }

  @Override
  public void keyReleased(int key, char c) {
    switch (key) {
    case Input.KEY_LSHIFT:
    case Input.KEY_RSHIFT:
      mShift = false;
      break;
    case Input.KEY_LCONTROL:
    case Input.KEY_RCONTROL:
      mCtrl = false;
      break;
    case Input.KEY_ESCAPE:
      System.exit(0);
    case Input.KEY_Q:
      mMovement &= ~UP;
      break;
    case Input.KEY_E:
      mMovement &= ~DOWN;
      break;
    case Input.KEY_W:
      mMovement &= ~NORTH;
      break;
    case Input.KEY_A:
      mMovement &= ~WEST;
      break;
    case Input.KEY_S:
      mMovement &= ~SOUTH;
      break;
    case Input.KEY_D:
      mMovement &= ~EAST;
      break;
    }
  }

  @Override
  public void keyPressed(int key, char c) {
    switch (key) {
    case Input.KEY_LSHIFT:
    case Input.KEY_RSHIFT:
      mShift = true;
      break;
    case Input.KEY_LCONTROL:
    case Input.KEY_RCONTROL:
      mCtrl = true;
      break;
    case Input.KEY_ESCAPE:
      System.exit(0);
    case Input.KEY_Q:
      mMovement |= UP;
      break;
    case Input.KEY_E:
      mMovement |= DOWN;
      break;
    case Input.KEY_W:
      mMovement |= NORTH;
      break;
    case Input.KEY_A:
      mMovement |= WEST;
      break;
    case Input.KEY_S:
      mMovement |= SOUTH;
      break;
    case Input.KEY_D:
      mMovement |= EAST;
      break;
    case Input.KEY_P:
      mPaused = !mPaused;
      break;
    case Input.KEY_SPACE:
      mGame.mWorld.panTo(mGame.mWorld.getPlayer().getLocation());
      mGame.mWorld.getPlayer().setViewLock(true);
      break;
    case Input.KEY_Z:
      mGame.mWorld.getPlayer().toggleViewLock();
      break;

    // Save/recall views
    case Input.KEY_1:
      if (mShift)
        mGame.mWorld.saveView(0);
      else
        mGame.mWorld.recallView(0);
      break;
    case Input.KEY_2:
      if (mShift)
        mGame.mWorld.saveView(1);
      else
        mGame.mWorld.recallView(1);
      break;
    case Input.KEY_3:
      if (mShift)
        mGame.mWorld.saveView(2);
      else
        mGame.mWorld.recallView(2);
      break;
    case Input.KEY_4:
      if (mShift)
        mGame.mWorld.saveView(3);
      else
        mGame.mWorld.recallView(3);
      break;
    case Input.KEY_5:
      if (mShift)
        mGame.mWorld.saveView(4);
      else
        mGame.mWorld.recallView(4);
      break;
    case Input.KEY_6:
      if (mShift)
        mGame.mWorld.saveView(5);
      else
        mGame.mWorld.recallView(5);
      break;
    case Input.KEY_7:
      if (mShift)
        mGame.mWorld.saveView(6);
      else
        mGame.mWorld.recallView(6);
      break;
    case Input.KEY_8:
      if (mShift)
        mGame.mWorld.saveView(7);
      else
        mGame.mWorld.recallView(7);
      break;
    case Input.KEY_9:
      if (mShift)
        mGame.mWorld.saveView(8);
      else
        mGame.mWorld.recallView(8);
      break;

    // Debugging/Non game related
    case Input.KEY_F1:
      mDebug = !mDebug;
      break;
    }
  }

  @Override
  public void mousePressed(int button, int x, int y) {
    switch (button) {
    case 0:
      if (mCtrl)
        System.out.printf("%s\n", mGame.mWorld.getTerrain().getAt(
            mGame.mWorld.getCursorPos()));
      else {
        if (!mGame.mWorld.isTraversible(mGame.mWorld.getCursorPos()))
          return;
        Path p = PathFinder.findPath(mGame.mWorld.getPlayer().getLocation(),
            mGame.mWorld.getCursorPos());
        if(p != null)
          mGame.mWorld.getPlayer().setPath(p);
        else
          message("Error: Path is difficult or impossible.");
      }
      break;
    case 1:
      // Center to location
      mGame.mWorld.centerAtCursor();
      break;
    }
  }

  @Override
  public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    mGame.mWorld.setCursorPos(mGame.mWorld.screenToWorld(newx, newy));
  }
}
