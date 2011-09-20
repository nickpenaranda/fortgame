package org.n4p.mountainking.units;

public class UnitException extends Exception {
  private static final long serialVersionUID = 2833555039032470866L;

  public UnitException(String s) {
    super("UnitException: " + s);
  }
  
}
