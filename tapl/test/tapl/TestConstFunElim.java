package tapl;

import java.util.Optional;
import java.util.function.Function;

import untyped.CTerm;
import untyped.ConstFunElim;
import untyped.GTermAlg;
import untyped.GetBodyFromTmAbs;
import untyped.IsVarUsed;
import untyped.TermAlg;
import untyped.TermAlgFactory;
import untyped.TermAlgVisitor;
import untyped.TmMap;
import utils.TmMapCtx;

public class TestConstFunElim {
  static class PrintImpl implements GTermAlg<CTerm, String>, TermAlgVisitor<String> {
      public String TmApp(CTerm p1, CTerm p2) {
          return "(" + visitTerm(p1) + ") " + visitTerm(p2);
      }
      public String TmVar(int p1, int p2) {
          return "" + p1;
      }

      public String TmAbs(String p1, CTerm p2) {
          return "\\" + visitTerm(p2);
      }
  }
  static class GetBodyFromTmAbsImpl implements GetBodyFromTmAbs<CTerm>, TermAlgVisitor<Optional<CTerm>> {}
  static class IsVarUsedImpl implements IsVarUsed<CTerm>, TermAlgVisitor<Function<Integer,Boolean>> {}
  static class TmMapImpl implements TmMap<CTerm>, TermAlgVisitor<Function<TmMapCtx<CTerm>, CTerm>> {
    public TermAlg<CTerm> alg() {
        return new TermAlgFactory();
    }
  }
  static class ElimImpl implements ConstFunElim<CTerm>, TermAlgVisitor<CTerm> {
    public boolean isVarUsed(int n, CTerm t) {
      return new IsVarUsedImpl().visitTerm(t).apply(n);
    }
    public Optional<CTerm> getBodyFromTmAbs(CTerm t) {
      return new GetBodyFromTmAbsImpl().visitTerm(t);
    }
    public TermAlg<CTerm> alg() {
        return new TermAlgFactory();
    }
    @Override public CTerm termShift(int d, CTerm t) {
      return new TmMapImpl().termShift(d, t);
    }
  }

  static void test(CTerm t) {
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
      CTerm t1 = alg.TmAbs("z", alg.TmApp(alg.TmAbs("y", alg.TmVar(3, 2)), alg.TmVar(0, 1)));
      CTerm t2 = alg.TmAbs("x", alg.TmApp(alg.TmAbs("y", alg.TmVar(1, 2)), alg.TmVar(2, 1)));
      System.out.println(new IsVarUsedImpl().visitTerm(t2).apply(0));
      test(t1);
      test(t2);
      test(alg.TmApp(t1, t2));
      test(alg.TmApp(t2, t1));
      test(alg.TmApp(alg.TmAbs("x", alg.TmAbs("y", alg.TmVar(1, 2))), alg.TmVar(0, 1)));
      test(alg.TmApp(alg.TmAbs("x", alg.TmAbs("y", alg.TmVar(0, 2))), alg.TmVar(0, 1)));
  }
}
