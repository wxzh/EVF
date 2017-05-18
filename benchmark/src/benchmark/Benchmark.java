package benchmark;

class SumQueryImpl implements SumQuery<CList>, ListAlgVisitor<Integer> {}

public class Benchmark {
   static final int SIZE = 1000;
   static final int TIMES = 10000;
   static double ns2ms(long before, long after) {
       return (1.0 * (after -before)) / 1000000.0;
   }

   static void benchmark(boolean warmup) {
       FList fl = new FNil();
       IList il = new Nil();
       ListAlgFactory alg = new ListAlgFactory();
       CList exl = alg.Nil();

       long t1, t2;
       for (int i = 0; i < SIZE; i++) {
           il = new Cons(1, il);
           fl = new FCons(1, fl);
           exl = alg.Cons(1, exl);
       }
       for (int i = 0; i < SIZE; i++) {
           il = new Link(false, il);
           fl = new FLink(false, fl);
           exl = alg.Link(false, exl);
       }

       t1 = System.nanoTime();
       for (int i = 0; i < TIMES; i++) {
           SumVisitor isum = new SumVisitor();
           il.accept(isum);
       }
       t2 = System.nanoTime();
       if(!warmup) System.out.println("Imperative: " + ns2ms(t1,t2) + " ms");

       FSum fsum = new FSum();
       t1 = System.nanoTime();
       for (int i = 0; i < TIMES; i++) {
         fl.accept(fsum);
       }
       t2 = System.nanoTime();
       if(!warmup) System.out.println("Functional: " + ns2ms(t1,t2) + " ms");

       t1 = System.nanoTime();
       for (int i = 0; i < TIMES; i++) {
           SumRunabout sr = new SumRunabout();
           sr.visitAppropriate(il);
       }
       t2 = System.nanoTime();
       if(!warmup) System.out.println("Runabout: " + ns2ms(t1,t2) + " ms");

       SumQueryImpl esum = new SumQueryImpl();
       t1 = System.nanoTime();
       for (int i = 0; i < TIMES; i++) {
           exl.accept(esum);
       }
       t2 = System.nanoTime();
       if(!warmup) System.out.println("EVF: " + ns2ms(t1,t2) + " ms");

   }

   public static void main(String[] args) {
       System.out.println("WARM UP...");
       for (int i = 0; i < 10; i ++)
         benchmark(true);
       benchmark(false);
   }
}
