package benchmark;

interface Visitor {
  void visit(Nil nil);
  void visit(Cons cons);
  void visit(Link link);
}
class SumVisitor implements Visitor {
  int sum = 0;
  public void visit(Nil nil) {}
  public void visit(Cons cons) {
    sum += cons.head;
    cons.tail.accept(this);
  }
  public void visit(Link link) {
    link.list.accept(this);
  }
}
public class Imperative {
   static void benchmark() {

   }
   public static void main(String[] args) {
       IList l = new Nil();
       for (int i = 0; i < 2000; i++) {
           l = new Cons(1, l);
       }

       SumVisitor sum = new SumVisitor();
       SumRunabout sw = new SumRunabout(); // 100x slower

       long t1 = System.currentTimeMillis();
       for (int i = 0; i < 10000; i++) {
           l.accept(sum);
       }
       long t2 = System.currentTimeMillis();

       System.out.println("Imperative: " + (t2 - t1) + "ms");
       System.out.println("before");
       t1 = System.currentTimeMillis();
       for (int i = 0; i < 10000; i++) {
           sw.visitAppropriate(l);
       }
       System.out.println("after");

       t2 = System.currentTimeMillis();
       System.out.println("Runabout: " + (t2 - t1) + " ms");
   }
}