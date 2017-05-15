package benchmark;

class SumQueryImpl implements SumQuery<CList>, ListAlgVisitor<Integer> {}

public class Benchmark {
   static double ns2ms(long before, long after) {
       return (1.0 * (after -before)) / 1000000.0;
   }

   static void benchmark() {
       FList fl = new FNil();
       IList il = new Nil();
       ListAlgFactory alg = new ListAlgFactory();
       CList exl = alg.Nil();

       long t1, t2;
       for (int i = 0; i < 1000; i++) {
           il = new Cons(1, il);
           fl = new FCons(1, fl);
           exl = alg.Cons(1, exl);
       }
       for (int i = 0; i < 1000; i++) {
           il = new Link(false, il);
           fl = new FLink(false, fl);
           exl = alg.Link(false, exl);
       }

       FSum fsum = new FSum();
       t1 = System.nanoTime();
       for (int i = 0; i < 10000; i++) {
         fl.accept(fsum);
       }
       t2 = System.nanoTime();
       System.out.println("Functional: " + ns2ms(t1,t2) + " ms");

       t1 = System.nanoTime();
       for (int i = 0; i < 10000; i++) {
           SumVisitor isum = new SumVisitor();
           il.accept(isum);
       }
       t2 = System.nanoTime();

       System.out.println("Imperative: " + ns2ms(t1,t2) + " ms");
       t1 = System.nanoTime();
       for (int i = 0; i < 10000; i++) {
           SumRunabout sr = new SumRunabout();
           sr.visitAppropriate(il);
       }
       t2 = System.nanoTime();
       System.out.println("Runabout: " + ns2ms(t1,t2) + " ms");

       SumQueryImpl esum = new SumQueryImpl();
       t1 = System.nanoTime();
       for (int i = 0; i < 10000; i++) {
           exl.accept(esum);
       }
       t2 = System.nanoTime();
       System.out.println("EVF: " + ns2ms(t1,t2) + " ms");
   }

   public static void main(String[] args) {
       benchmark();
       System.out.println("after warmup:");
       benchmark();
   }
}
