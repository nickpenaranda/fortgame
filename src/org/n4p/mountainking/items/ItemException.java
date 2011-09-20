package org.n4p.mountainking.items;

public class ItemException extends Exception {
  private static final long serialVersionUID = 3188632235378565942L;
  public ItemException(String s) {
    super("ItemException: " + s);
  }
}
