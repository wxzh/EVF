package visitor;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


interface LamAlg<O> {
  O Var(String x);
  O Abs(String x, Exp e);
  O App(Exp e1, Exp e2);
  O Lit(int n);
  O Sub(Exp e1, Exp e2);
}
interface Exp {
  <O> O accept(LamAlg<O> v);
}
class Var implements Exp {
  String x;
  Var(String x) { this.x = x; }
  public <O> O accept(LamAlg<O> v) {
    return v.Var(x);
  }
}
class Abs implements Exp {
  String x;
  Exp e;
  Abs(String x, Exp e) { this.x = x; this.e = e; }
  public <O> O accept(LamAlg<O> v) {
    return v.Abs(x, e);
  }
}
class App implements Exp {
  Exp e1, e2;
  App(Exp e1, Exp e2) { this.e1 = e1; this.e2 = e2; }
  public <O> O accept(LamAlg<O> v) {
    return v.App(e1, e2);
  }
}
class Lit implements Exp {
  int n;
  Lit(int n) { this.n = n; }
  public <O> O accept(LamAlg<O> v) {
    return v.Lit(n);
  }
}
class Sub implements Exp {
  Exp e1, e2;
  Sub(Exp e1, Exp e2) { this.e1 = e1; this.e2 = e2; }
  public <O> O accept(LamAlg<O> v) {
    return v.Sub(e1, e2);
  }
}
class FreeVars implements LamAlg<Set<String>> {
  public Set<String> Var(String x) {
    return Collections.singleton(x);
  }
  public Set<String> Abs(String x, Exp e) {
    return e.accept(this).stream().filter(y -> !y.equals(x))
      .collect(Collectors.toSet());
  }
  public Set<String> App(Exp e1, Exp e2) {
    return Stream.concat(e1.accept(this).stream(), e2.accept(this).stream())
      .collect(Collectors.toSet());
  }
  public Set<String> Lit(int n) {
    return Collections.emptySet();
  }
  public Set<String> Sub(Exp e1, Exp e2) {
    return Stream.concat(e1.accept(this).stream(), e2.accept(this).stream())
      .collect(Collectors.toSet());
  }
}
class SubstVar implements LamAlg<Exp> {
  String x;
  Exp s;
  SubstVar(String x, Exp s) { this.x = x; this.s = s; }
  public Exp Abs(String y, Exp e) {
    if(y.equals(x)) return new Abs(x, e);
    if(s.accept(new FreeVars()).contains(x)) throw new RuntimeException();
    return new Abs(x, e.accept(this));
  }
  public Exp App(Exp e1, Exp e2) {
    return new App(e1.accept(this), e2.accept(this));
  }
  public Exp Var(String y) {
    return y.equals(x) ? s : new Var(x);
  }
  public Exp Lit(int n) {
    return new Lit(n);
  }
  public Exp Sub(Exp e1, Exp e2) {
    return new Sub(e1.accept(this), e2.accept(this));
  }
}

class LC {
  public static void main(String[] args) {
    Exp e = new App(new Abs("y", new Var("y")), new Var("x"));
    System.out.println(e.accept(new FreeVars()));
    System.out.println(e.accept(new SubstVar("x", new Var("z"))));
  }
}
