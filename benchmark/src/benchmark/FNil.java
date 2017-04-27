package benchmark;

public class FNil implements FList {
  public <O> O accept(FVisitor<O> v) {
    return v.Nil();
  }
}