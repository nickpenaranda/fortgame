package org.n4p.earthhoard;

import org.n4p.earthhoard.fixtures.Fixture;
import org.n4p.earthhoard.terrain.Terrain;
import org.n4p.earthhoard.terrain.TerrainType;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

@SuppressWarnings("unused")
public class MainState extends BasicGameState {
  private static final long ANIM_INTERVAL = 500;
  private static final int VERTICAL_MOVEMENT_INTERVAL = 200;
  private static final long CONSOLIDATE_INTERVAL = 1000;
  private static final int MOVEMENT_SCALE = 14;

  private long mGameTime, mLastAnim, mLastConsolidate;
  private boolean mAnimFrame = true;
  private static final int STATE_ID = 1;
  
  private EarthHoard mGame;
  private GameContainer mContainer;
  
  private boolean mPaused = false;
  
  private int mMovement = 0;
  private int mVerticalTimeout = 0;
  private int mRenderTime;
  
  private boolean mCtrl = false;
	private boolean mShift = false;

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

  @Override
  public void init(GameContainer container, StateBasedGame game)
      throws SlickException {
    mGame = (EarthHoard) game;
    mContainer = container;

    TerrainType.init();
    Fixture.init();
    
    mGame.mWorld = new World();
    mGame.mWorld.init();
    
    PathFinder.init(mGame.mWorld);
    
    mGameTime = 0;
    mLastAnim = 0;
    mLastConsolidate = 0;
    mContainer.setShowFPS(true);
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g)
      throws SlickException {
    mGame.mWorld.render(container, g, mAnimFrame);
    
    g.setColor(Color.white);
    g.drawString("" + mGameTime + (mPaused ? " PAUSED" : ""), 10, 25);
    g.drawString("Z = " + mGame.mWorld.getViewZ(), 10, 40);
    g.drawString("Count = " + Terrain.count, 10, 55);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {
    int move_delta = delta / MOVEMENT_SCALE ;
    
    mRenderTime +=delta;
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
      mVerticalTimeout  = VERTICAL_MOVEMENT_INTERVAL;
    }
    
    else if ((mMovement & DOWN) == DOWN  && mVerticalTimeout <= 0) {
      mGame.mWorld.adjustView(0, 0, -1);
      mVerticalTimeout  = VERTICAL_MOVEMENT_INTERVAL;
    }
    
    if(mRenderTime - mLastAnim > ANIM_INTERVAL) {
      mAnimFrame = !mAnimFrame;
      mLastAnim = mRenderTime;
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
    }
  }

  @Override
  public void mousePressed(int button, int x, int y) {
    switch(button) {
    case 1:
      // Center to location
      mGame.mWorld.centerAtCursor();
      break;
    case 0:
      if(mShift) mGame.mWorld.setPathStart();
      else mGame.mWorld.setPathEnd();
      mGame.mWorld.findPath();
      break;
    }
  }

  @Override
  public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    // TODO Auto-generated method stub
    
    mGame.mWorld.setCursorPos(mGame.mWorld.screenToWorld(newx,newy));
  }
}
