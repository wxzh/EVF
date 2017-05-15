package pkg;

import pkg.alg.external.AlgFactory;
import pkg.alg.external.AlgVisitor;
import pkg.alg.external.Exp;

class EvalImpl implements Eval<Exp>, AlgVisitor<Integer> {}
class DoubleImpl implements Double<Exp>, AlgVisitor<Exp> {
  public Alg<Exp> alg() {
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