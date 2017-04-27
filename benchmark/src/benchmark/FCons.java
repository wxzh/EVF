package benchmark;

public class FCons implements FList {
  int head;
  FList tail;
  FCons(int head, FList tail) {
    this.head = head;
    this.tail = tail;
  }
  public <O> O accept(FVisitor<O> v) {
    return v.Cons(head, tail);
  }
}