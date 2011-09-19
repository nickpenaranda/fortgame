package org.n4p.earthhoard;

import java.util.ArrayList;
import java.util.Collections;

public class PathFinder {
  private static ArrayList<Node> open;
  private static ArrayList<Node> closed;
  private static World mWorld;

  static {
    open = new ArrayList<Node>();
    closed = new ArrayList<Node>();
  }

  public static void init(World world) {
    mWorld = world;
  }

  public static Path findPath(Coord start, Coord end) {
    boolean pathFound = false;
    int attempts = 0;
    Path p = null;
    Node c = null;
    if (!open.isEmpty())
      open.clear();
    if (!closed.isEmpty())
      closed.clear();

    open.add(new Node(start, end, null, 0));

    while (!pathFound && attempts++ < 1000) {
      Collections.sort(open);

      if (open.isEmpty())
        break;

      c = open.get(0);

      if (c.getLoc().equals(end)) {
        pathFound = true;
        break;
      }

      closed.add(c);
      open.remove(c);

      for (int x = -1; x <= 1; ++x) {
        for (int y = -1; y <= 1; ++y) {
          for (int z = -1; z <= 1; ++z) {
            if (!(x == 0 && y == 0 && z == 0))
              checkAdjacent(c, x, y, z);
          }
        }
      }
    }

    if (pathFound) {
      p = new Path();
      do {
        p.addLast(c.getLoc());
        c = c.getParent();
      } while (c.getParent() != null);
    }

    return (p);
  }

  private static void checkAdjacent(Node n,int dx,int dy,int dz) {
    int cost = (int) (Math.sqrt(dx * dx + dy * dy + dz * dz) * 100);
    Coord me = new Coord(n.getLoc().move(dx, dy, dz));
    if (World.isInBounds(me)) { // Bound check
      Node m = new Node(me,n.getEnd(),n,cost);
      if(!mWorld.isTraversible(me) || closed.contains(m))
        return;
      else {
        if (!open.contains(m)) {
          open.add(m);
        } else {
          Node o = open.get(open.indexOf(m));
          if (o.projG(n, cost) < o.getG()) {
            o.setParent(n, cost);
            Collections.sort(open);
          }
        }
      }
    }
  }

  private static class Node implements Comparable<Node> {
    private Coord mLoc, mEnd;
    private Node mParent;
    private int mCostToParent;

    private int F = 0, G = 0, H = 0;

    public Node(Coord loc, Coord end, Node parent, int costToParent) {
      mLoc = loc;
      mEnd = end;
      setParent(parent, costToParent);
      H = calcH();
      F = G + H;
    }

    public void setParent(Node parent, int costToParent) {
      mParent = parent;
      mCostToParent = costToParent;
      G = calcG();
      F = G + H;
    }

    public int calcH() {
      int dx = mEnd.x - mLoc.x, dy = mEnd.y - mLoc.y, dz = mEnd.z - mLoc.z;
      return ((int) (Math.sqrt(dx * dx + dy * dy + dz * dz) * 100));
    }

    public int calcG() {
      int g = 0;
      Node n = this;
      while (n.getParent() != null) {
        g += n.getCostToParent();
        n = n.getParent();
      }
      return (g);
    }

    public int projG(Node parent, int costToParent) {
      int g = 0;
      Node n = this;
      while (n.getParent() != null) {
        if (n == this) {
          g += costToParent;
          n = parent;
        } else {
          g += n.getCostToParent();
          n = n.getParent();
        }
      }
      return (g);
    }

    public int getCostToParent() {
      return mCostToParent;
    }

    public Node getParent() {
      return mParent;
    }

    public int getF() {
      return F;
    }
    
    public int getG() {
      return G;
    }

    public Coord getLoc() {
      return mLoc;
    }

    public Coord getEnd() {
      return mEnd;
    }

    @Override
    public int compareTo(Node o) {
      return (F - o.getF());
    }

    @Override
    public boolean equals(Object aThat) {
      if (this == aThat)
        return (true);
      if (!(aThat instanceof Node))
        return (false);
      Node that = (Node) aThat;
      return (this.getLoc().equals(that.getLoc()));
    }
    
    @Override
    public String toString() {
      return(String.format("%s F = %d G=%d",this.mLoc,F,G));
    }
  }
}
