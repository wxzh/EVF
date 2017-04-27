% EVF User Guide

System Requirements
===================

- JDK 8 or newer
- Eclipse
- Git

Download
========

- Open Eclipse
- Click File -> Import -> Git - Projects from Git -> Clone URI -> Next

  - Fill in URI: https://github.com/wxzh/EVF
  - Click Next for several times and Finish

Three Eclipse projects, `VisitProcessor` and `tapl`, will be imported, which are:

- `VisitProcessor` contains the implementation of **EVF**, an expressive and extensible Java visitor framework.
- `tapl` contains the case study on “Types and Programming Languages”.
- `benchmark` contains the micro-benchmark

If everything is OK, a folder called `generated` will appear after `tapl` is imported. Otherwise, please follow the steps below to manually configure annotation processing in eclipse:

1.  Open Properties -> Java Compiler -> Annotation Processing
  - tick "Enable project specific settings"
  - tick "Enable annotation processing"
  - tick "Enable processing in editor"
  - change the default name of "Generated source directory" to `generated`

2.  Open Properties -> Java Compiler -> Annotation Processing -> Factory Path
  - tick "Enable project specific settings"
  - tick "Add JARs...", select `VisitProcessor` and choose `visitprocessor.jar`.
  - apply the changes

An Example
==========

We are going to model a tiny expression language that supports only literals and additions.
Inside project `tapl`, create a new package called `pkg`.
Inside that package, create a file called `Alg.java` that contains the following code:

```
package pkg;
import annotation.Visitor;

@Visitor interface Alg<Exp> {
  Exp Lit(int n);
  Exp Add(Exp e1, Exp e2);
}
```

which is an Object Algebra interface that describes the language.
By annotating `Alg` with `@Visitor` and saving the file in eclipse, code generation will be triggered.
You will see some new files will appear under the `generated` directory:

- `pkg.Alg.internal` package: internal visitor infrastructure
- `pkg.Alg.external` package: external visitor infrastructure
- `pkg.Alg.shared` package:  traversal templates

We will use the generated code for implementing the semantics of this language next.

For example, the evaluator for the language looks like this:

```
interface Eval<Exp> extends GAlg<Exp, Integer> {
  default Integer Lit(int n) {
    return n;
  }
  default Integer Add(Exp e1, Exp e2) {
    return visitExp(e1) + visitExp(e2);
  }
}
```
where `GAlg` is the generated modular external visitor interface from `pkg.Alg.shared`.

Another example that illustrates how to use the traversal template to eliminate boilerplate is as follows:

```
interface Double<Exp> extends AlgTransform<Exp> {
  default Exp Lit(int n) {
    return alg().Lit(n*2);
  }
}
```

`Double` doubles every literal in an expression. The transformation template `AlgTransform` covers the `Add` case, which recursively calls `Double` on two sub-expressions and then forms a new addition.

We need code under `pkg.Alg.external` for instantiating `Eval` and `Double` as classes:

```
class EvalImpl implements Eval<Exp>, AlgVisitor<Integer> {}
class DoubleImpl implements Double<Exp>, AlgVisitor<Exp> {
  public GAlg<Exp,Exp> alg() {
    return new AlgFactory();
  }
}
```
We instantiate the type parameter using the generated AST type of the same and mixin in an `AlgVisitor` for giving an implementation of `visitExp`.
For `Double`, we additionally need the generated factory class for fulfilling the dependency.

Here is some client code illustrating how to use what we have defined:

```
AlgFactory f = new AlgFactory();
Exp e = f.Add(f.Lit(1), f.Lit(2));
EvalImpl eval = new EvalImpl();
DoubleImpl dbl = new DoubleImpl();
System.out.println(eval.visitExp(e)); // 3
System.out.println(eval.visitExp(dbl.visitExp(e))); // 6
```

We use the generated factory to construct an expression `1+2`.
Then we evaluate the expression which returns 3.
By calling dbl on the expression, we transform it to be `2+4` and the evaluation is therefore 6.

For more examples, please have a look on the paper and files under `tapl` such as `arith` and its test file `TestArith.java`.

Case Study
===

Run `ruby casestudy.rb` for collecting SLOC statistics (Table 1 & 2)

Performance Measurement
===

Run `Benchmark.java` under the project `benchmark` for measuring the performance (Table 3)
