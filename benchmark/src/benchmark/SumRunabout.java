package benchmark;

import org.grothoff.Runabout;

class SumRunabout0 extends Runabout {
  int sum = 0;
  public void visit(Nil l) {}
  public void visit(Cons l) {
      sum += l.head;
      visitAppropriate(l.tail);
  }
}

public class SumRunabout extends SumRunabout0 {
  public void visit(Link l) {
      visitAppropriate(l.list);
  }
}
