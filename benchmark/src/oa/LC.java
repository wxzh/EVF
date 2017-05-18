package oa;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import evf.SetMonoid;
import library.Monoid;
import query.LamAlgQuery;

class FreeVars implements LamAlgQuery<Set<String>> {
  public Monoid<Set<String>> m() {
    return new SetMonoid<>();
  }
  public Set<String> Var(String x) {
    return Collections.singleton(x);
  }
  public Set<String> Abs(String x, Set<String> e) {
    return e.stream().filter(y -> !y.equals(x))
      .collect(Collectors.toSet());
  }
}
interface IFV {
  Set<String> FV();
}
interface ISubst<Exp> {
  Exp before();
  Exp after();
}
class SubstVar<Exp extends IFV> implements LamAlg<ISubst<Exp>> {
  String x;
  Exp s;
  LamAlg<Exp> alg;
  SubstVar(String x, Exp s, LamAlg<Exp> alg) {
    this.x = x; this.s = s; this.alg = alg;
  }
  public ISubst<Exp> Var(String y) {
    return new ISubst<Exp>() {
      public Exp before() {
        return alg.Var(y);
      }
      public Exp after() {
        return y.equals(x) ? s : alg.Var(y);
    }};
  }
  public ISubst<Exp> Abs(String y, ISubst<Exp> e) {
    return new ISubst<Exp>() {
      public Exp before() {
        return alg.Abs(y, e.before());
      }
      public Exp after() {
        if(y.equals(x)) return alg.Abs(y, e.before());
        if(s.FV().contains(y)) throw new RuntimeException();
        return alg.Abs(y, e.after());
    }};
  }
  public ISubst<Exp> App(ISubst<Exp> e1, ISubst<Exp> e2) {
    return new ISubst<Exp>() {
      public Exp before() {
        return alg.App(e1.before(), e2.before());
      }
      public Exp after() {
        return alg.App(e1.after(), e2.after());
    }};
  }
  public ISubst<Exp> Lit(int n) {
    return new ISubst<Exp>() {
      public Exp before() {
        return alg.Lit(n);
      }
      public Exp after() {
        return alg.Lit(n);
    }};
  }
  public ISubst<Exp> Sub(ISubst<Exp> e1, ISubst<Exp> e2) {
    return new ISubst<Exp>() {
      public Exp before() {
        return alg.Sub(e1.before(), e2.before());
      }
      public Exp after() {
        return alg.Sub(e1.after(), e2.after());
    }};
  }
}