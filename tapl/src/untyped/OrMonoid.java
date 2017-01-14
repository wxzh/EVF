package untyped;

import library.Monoid;

public class OrMonoid implements Monoid<Boolean> {
  public Boolean empty() {
    return false;
  }
  public Boolean join(Boolean x, Boolean y) {
    return x || y;
  }
}
