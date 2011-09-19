package org.n4p.earthhoard;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainState extends BasicGameState {
  static final int types[] = { 1, 2, 3, 5 };
  Random mRandom = new Random();
  private long mGameTime;
  private long mLastAnimTick;
  private long mLastConsolidate;
  private boolean mAnimFrame = true;
  private static final int ID = 1;
  private EarthHoard mGame = null;
  private boolean mPaused = false;
  private int mMovement = 0;
  private int mMovementTimeout = 0;
  private int mRenderTime;
  
	private boolean mCtrl = false;
	private boolean mShift = false;
	private boolean mSlow = false;
  private GameContainer mContainer;

  static final int NORTH = 1;
  static final int SOUTH = 2;
  static final int WEST = 4;
  static final int EAST = 8;
  static final int UP = 16;
  static final int DOWN = 32;
  private static final long ANIM_INTERVAL = 500;
  private static final int VERTICAL_MOVEMENT_INTERVAL = 200;
  private static final Color skyColor = new Color(82,112,149);
  private static final long CONSOLIDATE_INTERVAL = 1000;
  @Override
  public int getID() {
    return ID;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game)
      throws SlickException {
    mGame = (EarthHoard) game;
    TerrainType.init();
    mGame.mWorld = new World();
    mGame.mWorld.init();
    
    mGameTime = 0;
    mLastAnimTick = 0;
    mLastConsolidate = 0;
    mContainer = container;
    mContainer.setShowFPS(true);
    //mContainer.setClearEachFrame(false);

  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g)
      throws SlickException {
    //g.setBackground(skyColor);
    mGame.mWorld.render(container, g, mAnimFrame);
    g.setColor(Color.white);
    g.drawString("" + mGameTime + (mPaused ? " PAUSED" : ""), 10, 25);
    g.drawString("Z = " + mGame.mWorld.getViewZ(), 10, 40);
    g.drawString("Count = " + Terrain.count, 10, 55);
    if(World.oldRender) g.drawString(">>OLD RENDERER<<", 10, 70);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {
    int move_delta = delta / 14;
    mRenderTime +=delta;
    mMovementTimeout -= delta;
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

    if ((mMovement & UP) == UP && mMovementTimeout <= 0) {
      mGame.mWorld.adjustView(0, 0, 1);
      mMovementTimeout  = VERTICAL_MOVEMENT_INTERVAL;
    }
    else if ((mMovement & DOWN) == DOWN  && mMovementTimeout <= 0) {
      mGame.mWorld.adjustView(0, 0, -1);
      mMovementTimeout  = VERTICAL_MOVEMENT_INTERVAL;
    }
    if(mRenderTime - mLastAnimTick > ANIM_INTERVAL) {
      mAnimFrame = !mAnimFrame;
      mLastAnimTick = mRenderTime;
      //System.out.println("Animation >>TICK<<");
    }
    if(mRenderTime - mLastConsolidate > CONSOLIDATE_INTERVAL) {
      mGame.mWorld.getTerrain().consolidateDown();
      mLastConsolidate = mRenderTime;
    }
    
    if (!mPaused) {
      mGameTime += delta;
      // Do stuff
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
    case Input.KEY_LBRACKET:
    	if(mShift)
    		World.setHeightLimit(World.getHeightLimit()-1);
    	else
    		World.setDepthLimit(World.getDepthLimit()-1);
    	break;
    case Input.KEY_RBRACKET:
    	if(mShift)
    		World.setHeightLimit(World.getHeightLimit()+1);
    	else
    		World.setDepthLimit(World.getDepthLimit()+1);
    	break;
    case Input.KEY_X:
      mSlow = !mSlow;
      if(mSlow) mContainer.setTargetFrameRate(1);
      else mContainer.setTargetFrameRate(-1);
      break;
    case Input.KEY_Z:
      World.oldRender = !World.oldRender;
      break;
    }
  }

  @Override
  public void mousePressed(int button, int x, int y) {
    // TODO Auto-generated method stub
    System.out.printf("Mouse clicked @ (%d,%d)\n",x,y);
    Coord w = mGame.mWorld.screenToWorld(x,y);
    System.out.printf("World coords (%d,%d,%d)\n",(int)w.x,(int)w.y,(int)w.z);
    
    super.mousePressed(button, x, y);
  }

  @Override
  public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    // TODO Auto-generated method stub
    mGame.mWorld.screenToWorld(newx,newy);
    super.mouseMoved(oldx, oldy, newx, newy);
  }
  
  
}
