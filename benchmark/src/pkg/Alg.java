package pkg;

import annotation.Visitor;
import pkg.alg.shared.AlgTransform;
import pkg.alg.shared.GAlg;

@Visitor public interface Alg<Exp> {
  Exp Lit(int n);
  Exp Add(Exp e1, Exp e2);
}

interface Eval<Exp> extends GAlg<Exp, Integer> {
  default Integer Lit(int n) {
    return n;
  }
  default Integer Add(Exp e1, Exp e2) {
    return visitExp(e1) + visitExp(e2);
  }
}

interface Double<Exp> extends AlgTransform<Exp> {
  default Exp Lit(int n) {
    return alg().Lit(n*2);
  }
}
