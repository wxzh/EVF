package tapl;

import java.util.Optional;
import java.util.function.Function;

import untyped.ConstFunElim;
import untyped.GetBodyFromTmAbs;
import untyped.IsVarUsed;
import untyped.TermShift;
import untyped.TmMap;
import untyped.termalg.external.Term;
import untyped.termalg.external.TermAlgFactory;
import untyped.termalg.external.TermAlgVisitor;
import untyped.termalg.shared.GTermAlg;
import varapp.TmMapCtx;

class PrintImpl implements GTermAlg<Term, String>, TermAlgVisitor<String> {
    public String TmApp(Term p1, Term p2) {
        return "(" + visitTerm(p1) + ") " + visitTerm(p2);
    }
    public String TmVar(int p1, int p2) {
        return "" + p1;
    }

    public String TmAbs(String p1, Term p2) {
        return "\\" + visitTerm(p2);
    }
}
  class GetBodyFromTmAbsImpl implements GetBodyFromTmAbs<Term>, TermAlgVisitor<Optional<Term>> {}
  class IsVarUsedImpl implements IsVarUsed<Term>, TermAlgVisitor<Function<Integer,Boolean>> {}
  class TmMapImpl implements TmMap<Term>, TermAlgVisitor<Function<TmMapCtx<Term>, Term>> {
    public GTermAlg<Term, Term> alg() {
        return new TermAlgFactory();
    }
  }
  class ElimImpl implements ConstFunElim<Term>, TermAlgVisitor<Term> {
    public GetBodyFromTmAbs<Term> getBodyFromTmAbs() {
        return new GetBodyFromTmAbsImpl();
    }
    public untyped.termalg.shared.GTermAlg<Term, Term> alg() {
        return new TermAlgFactory();
    }
    public IsVarUsed<Term> isVarUsed() {
        return new IsVarUsedImpl();
    }
    public TermShift<Term> termShift(int d) {
        class TermShiftImpl implements TermShift<Term>, TermAlgVisitor<Function<Integer, untyped.termalg.external.Term>> {
            public int d() { return d; }
            public GTermAlg<Term, Term> alg() {
                return new TermAlgFactory();
            }
        }
        return new TermShiftImpl();
    }
  }
public class TestConstFunElim {

  static void test(untyped.termalg.external.Term t) {
      PrintImpl print = new PrintImpl();
      ElimImpl elim = new ElimImpl();
      System.out.println(print.visitTerm(t) + " -> " + print.visitTerm(elim.visitTerm(t)));
  }
  public static void main(String[] args) {
      TermAlgFactory alg = new TermAlgFactory();
      // (\z.(\y.x) z) (\x.(\y.x) z)
      // (\z.x) (\x.(\y.x) z)
      // x

      // (\x.(\y.x) z) (\z.(\y.x) z)
      // (\x.x) (\z.(\y.x) z)
      // (\x.x) (\z.x)

      // (\x.(\y.x)) x
      // (\x.(\y.x))

      // (\x.(\y.y)) x
      // (\y.y)
      untyped.termalg.external.Term t1 = alg.TmAbs("z", alg.TmApp(alg.TmAbs("y", alg.TmVar(3, 2)), alg.TmVar(0, 1)));
      untyped.termalg.external.Term t2 = alg.TmAbs("x", alg.TmApp(alg.TmAbs("y", alg.TmVar(1, 2)), alg.TmVar(2, 1)));
      System.out.println(new IsVarUsedImpl().visitTerm(t2).apply(0));
      test(t1);
      test(t2);
      test(alg.TmApp(t1, t2));
      test(alg.TmApp(t2, t1));
      test(alg.TmApp(alg.TmAbs("x", alg.TmAbs("y", alg.TmVar(1, 2))), alg.TmVar(0, 1)));
      test(alg.TmApp(alg.TmAbs("x", alg.TmAbs("y", alg.TmVar(0, 2))), alg.TmVar(0, 1)));
  }
}
