package benchmark;

class FSum implements FVisitor<Integer> {
    public Integer Nil() {
        return 0;
    }
    public Integer Cons(int head, FList tail) {
        return head + tail.accept(this);
    }
    public Integer Link(boolean color, FList list) {
        return list.accept(this);
    }
}