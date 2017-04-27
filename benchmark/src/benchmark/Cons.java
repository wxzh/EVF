package benchmark;

public class Cons implements IList {
  int head;
  IList tail;
  public void accept(Visitor v) {
    v.visit(this);
  }
  public Cons(int head, IList tail) {
      this.head = head;
      this.tail = tail;
  }
}