package benchmark;

public class FLink implements FList {
    boolean color;
    FList list;
    public FLink(boolean color, FList list) {
        this.color = color;
        this.list = list;
    }
    public <O> O accept(FVisitor<O> v) {
       return v.Link(color, list);
    }
}
