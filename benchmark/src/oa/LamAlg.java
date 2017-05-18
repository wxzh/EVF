package oa;

import com.zewei.annotation.processor.Algebra;

@Algebra public interface LamAlg<Exp> {
  Exp Var(String x);
  Exp Abs(String x, Exp e);
  Exp App(Exp e1, Exp e2);
  Exp Lit(int n);
  Exp Sub(Exp e1, Exp e2);
}