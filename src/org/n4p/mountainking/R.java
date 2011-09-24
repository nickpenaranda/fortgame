package org.n4p.mountainking;

import java.util.Random;

public class R extends Random {

  private static final long serialVersionUID = 2850230497158797196L;

  private static class RLoader {
    public static final Random instance = new Random();
  }

  public static Random getInstance() {
    return RLoader.instance;
  }

  @Override
  public boolean nextBoolean() {
    return getInstance().nextBoolean();
  }

  @Override
  public double nextDouble() {
    // TODO Auto-generated method stub
    return getInstance().nextDouble();
  }

  @Override
  public float nextFloat() {
    // TODO Auto-generated method stub
    return getInstance().nextFloat();
  }

  @Override
  public synchronized double nextGaussian() {
    // TODO Auto-generated method stub
    return getInstance().nextGaussian();
  }

  @Override
  public int nextInt() {
    // TODO Auto-generated method stub
    return getInstance().nextInt();
  }

  @Override
  public int nextInt(int n) {
    // TODO Auto-generated method stub
    return getInstance().nextInt(n);
  }

  @Override
  public long nextLong() {
    // TODO Auto-generated method stub
    return getInstance().nextLong();
  }
}
