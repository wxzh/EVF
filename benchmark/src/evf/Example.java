package evf;

import annotation.Visitor;

@Visitor interface Alg<E> {
  E Lit(int n);
  E Add(E e1, E e2);
}

interface Eval<Exp> extends GAlg<Exp, Integer> {
  default Integer Lit(int n) {
    return n;
  }
  default Integer Add(Exp e1, Exp e2) {
    return visitE(e1) + visitE(e2);
  }
}

interface Double<Exp> extends AlgTransform<Exp> {
  default Exp Lit(int n) {
    return alg().Lit(n*2);
  }
}
class EvalImpl implements Eval<CE>, AlgVisitor<Integer> {}
class DoubleImpl implements Double<CE>, AlgVisitor<CE> {
  public Alg<CE> alg() {
    return new AlgFactory();
  }
}

class Example {
  public static void main(String[] args) {
    AlgFactory f = new AlgFactory();
    CE e = f.Add(f.Lit(1), f.Lit(2));
    EvalImpl eval = new EvalImpl();
    DoubleImpl dbl = new DoubleImpl();
    System.out.println(eval.visitE(e));
    System.out.println(eval.visitE(dbl.visitE(e)));
  }
}