package benchmark;

import annotation.Visitor;
import benchmark.listalg.shared.ListAlgQuery;
import library.Monoid;

@Visitor interface ListAlg<List> {
  List Nil();
  List Cons(Integer head, List tail);
  List Link(boolean color, List list);
}

class AddMonoid implements Monoid<Integer> {
  public Integer empty() { return 0; }
  public Integer join(Integer x, Integer y) { return x + y; }
}

interface SumQuery<List> extends ListAlgQuery<List,Integer> {
  default Monoid<Integer> m() {
    return new AddMonoid();
  }
  @Override default Integer Cons(Integer x, List l) {
    return x + visitList(l);
  }
}