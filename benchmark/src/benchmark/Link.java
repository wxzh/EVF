package benchmark;

public class Link implements IList {
    boolean color;
    IList list;
    public Link(boolean color, IList list) {
        this.color = color;
        this.list = list;
    }
    public void accept(Visitor v) {
       v.visit(this);
    }
}
