package pkg;

import annotation.Visitor;
import pkg.alg.external.AlgFactory;
import pkg.alg.external.AlgVisitor;
import pkg.alg.external.Exp;
import pkg.alg.shared.AlgTransform;
import pkg.alg.shared.GAlg;

@Visitor interface Alg<Exp> {
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

class EvalImpl implements Eval<Exp>, AlgVisitor<Integer> {}
class DoubleImpl implements Double<Exp>, AlgVisitor<Exp> {
  public GAlg<Exp,Exp> alg() {
    return new AlgFactory();
  }
}

class Example {
  public static void main(String[] args) {
    AlgFactory f = new AlgFactory();
    Exp e = f.Add(f.Lit(1), f.Lit(2));
    EvalImpl eval = new EvalImpl();
    DoubleImpl dbl = new DoubleImpl();
    System.out.println(eval.visitExp(e));
    System.out.println(eval.visitExp(dbl.visitExp(e)));
  }
}