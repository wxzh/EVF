package benchmark;

public class Nil implements IList {
  public void accept(Visitor v) {
    v.visit(this);
  }
}