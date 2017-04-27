package benchmark;

public interface FVisitor<O> {
  O Nil();
  O Cons(int head, FList tail);
  O Link(boolean color, FList list);
}