package evf;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import annotation.Visitor;
import library.Monoid;

@Visitor interface LamAlg<Exp> {
  Exp Var(String x);
  Exp Abs(String x, Exp e);
  Exp App(Exp e1, Exp e2);
  Exp Lit(int n);
  Exp Sub(Exp e1, Exp e2);
}
interface FreeVars<Exp> extends LamAlgQuery<Exp, Set<String>> {
  default Monoid<Set<String>> m() {
    return new SetMonoid<>();
  }
  default Set<String> Var(String x) {
    return Collections.singleton(x);
  }
  default Set<String> Abs(String x, Exp e) {
    return visitExp(e).stream().filter(y -> !y.equals(x))
      .collect(Collectors.toSet());
  }
}
interface SubstVar<Exp> extends LamAlgTransform<Exp> {
  String x();
  Exp s();
  Set<String> FV(Exp e);
  default Exp Var(String y) {
    return y.equals(x()) ? s() : alg().Var(y);
  }
  default Exp Abs(String y, Exp e) {
    if (y.equals(x())) return alg().Abs(y, e);
    if (FV(s()).contains(y)) throw new RuntimeException();
    return alg().Abs(y, visitExp(e));
  }
}

class FreeVarsImpl implements FreeVars<CExp>, LamAlgVisitor<Set<String>> {}
class SubstVarImpl implements SubstVar<CExp>, LamAlgVisitor<CExp> {
  String x;
  CExp s;
  public SubstVarImpl(String x, CExp s) { this.x = x; this.s = s; }
  public String x() { return x; }
  public CExp s() { return s; }
  public Set<String> FV(CExp e) { return new FreeVarsImpl().visitExp(e); }
  public LamAlg<CExp> alg() { return new LamAlgFactory(); }
}
public interface LC {
  public static void main(String[] args) {
    LamAlgFactory alg = new LamAlgFactory();
    CExp exp = alg.App(alg.Abs("y", alg.Var("y")), alg.Var("x")); // (\y.y) x
    System.out.println(new FreeVarsImpl().visitExp(exp)); // {"x"}
    new SubstVarImpl("x", alg.Lit(1)).visitExp(exp); // (\y.y) 1
  }
}
