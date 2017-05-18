Prerequisite
===================

- JDK 8 or newer
- Eclipse
- Git

Additional requirement for running the script

- Ruby
- [cloc](https://github.com/AlDanial/cloc)

Download
========

- Open Eclipse
- Click File -> Import -> Git - Projects from Git -> Clone URI -> Next

  - Fill in URI: https://github.com/wxzh/EVF
  - Click Next for several times and Finish

Three Eclipse projects will be imported, which are:

- `VisitProcessor` contains the implementation of an annotation processor;
- `tapl` contains the case study on “Types and Programming Languages”;
- `benchmark` contains the micro-benchmark.

If you see a folder `generated` under the folder `tapl` that contains the generated code,  you have successfully imported the projects.

If not, please follow the steps below to manually configure annotation processing in Eclipse:

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

To have a rough idea of the functionality of the framework, 
we are going to model a tiny expression language that supports only literals and additions (full code can be found in `benchmark/evf/Example.java`).

Create a file called `Alg.java` inside some package and paste the following code to that file:

```java
import annotation.Visitor;

@Visitor interface Alg<Exp> {
  Exp Lit(int n);
  Exp Add(Exp e1, Exp e2);
}
```

which is an Object Algebra interface that describes the language annotated with `@Visitor`.
By saving the file in Eclipse, you will see some new files generated under the `generated` directory.
These files are code for AST and traversal templates associated to `Alg`. 

We will use the generated code for implementing the semantics of this language next.

For example, the evaluator for the language looks like this:

```java
interface Eval<E> extends GAlg<E, Integer> {
  default Integer Lit(int n) {
    return n;
  }
  default Integer Add(E e1, E e2) {
    return visitE(e1) + visitE(e2);
  }
}
```
where `GAlg` is the generated modular external visitor interface.
You can directly the generated code without import statements as it is of the same package.

Another example that illustrates how to use the traversal template to eliminate boilerplate is as follows:

```java
interface Double<E> extends AlgTransform<E> {
  default Exp Lit(int n) {
    return alg().Lit(n*2);
  }
}
```

`Double` doubles every literal in an expression. The transformation template `AlgTransform` covers the `Add` case, which recursively calls `Double` on two sub-expressions and then forms a new addition.

To instantiate `Eval` and `Double` as classes, we define:

```java
class EvalImpl implements Eval<CE>, AlgVisitor<Integer> {}
class DoubleImpl implements Double<CE>, AlgVisitor<CE> {
  public Alg<CE> alg() {
    return new AlgFactory();
  }
}
```

We instantiate the type parameter using the generated AST type `CExp` and mixin in an `AlgVisitor` for giving an implementation of `visitExp`.
For `Double`, we additionally need the generated factory class for fulfilling the dependency.

Here is some client code illustrating how to use what we have defined:

```java
AlgFactory f = new AlgFactory();
CE e = f.Add(f.Lit(1), f.Lit(2));
EvalImpl eval = new EvalImpl();
DoubleImpl dbl = new DoubleImpl();
System.out.println(eval.visitE(e)); // 3
System.out.println(eval.visitE(dbl.visitE(e))); // 6
```

We use the generated factory to construct an expression `1+2`.
Then we evaluate the expression which returns 3.
By calling dbl on the expression, we transform it to be `2+4` and the evaluation is therefore 6.

For more examples, please have a look on the paper and files under `tapl` such as `arith` and its test file `TestArith.java`.

Case Study
===

Run `ruby casestudy.rb` to collect SLOC statistics (Table 1 & 2)

Benchmark
===

Run `Benchmark.java` under the project `benchmark` for measuring the performance (Table 3)
