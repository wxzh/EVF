package annotation;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(value = { "*" })
@SupportedSourceVersion(SourceVersion.RELEASE_8)

public class VisitProcessor extends AbstractProcessor {
    private static final String VISITOR = "Visitor";
    private static final String FACTORY = "Factory";
    private static final String APPLIER = "Applier";
    private static final String MAPPER = "Mapper";
    private static final String MATCHER = "Matcher";
    private static final String DEFAULT = "Default";
    private static final String QUERY = "Query";
    private static final String TRANSFORM = "Transform";
    private static final String I = "I";
    private static final String O = "O";
    private static final String _O_ = "<O>";
    private static final String TAB = "\t";
    private static final String TAB2 = "\t\t";
    private static final String TAB3 = "\t\t\t";
    private static final String TAB4 = "\t\t\t\t";

    private Filer filer;
    private ClassInfo self;

    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);
        filer = env.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
        try {
            Set<? extends Element> internalAnnotated = env.getElementsAnnotatedWith(Visitor.class);
            for (TypeElement element : ElementFilter.typesIn(internalAnnotated)) {
                self = new ClassInfo(element);
                genShared();
//                genInternal();
                genExternal();
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Kind.ERROR, e.getMessage());
        }
        return true;
    }

//    private void setFolderAndPackage(String name) {
////        String[] elements = { self.packageName, self.name.toLowerCase(), name };
////        folder = String.join("/", elements);
////        toPackage = String.join(".", elements);
//        folder = self.packageName;
//        toPackage = self.packageName;
//    }

    private void genShared() throws IOException {
        genGeneralizedAlg();
        genAlgDefaultInterface();
        genAlgQueryInterface();
        genAlgContextualQueryInterface();
        genAlgGeneralizedMonoidQueryInterface();
        genAlgTransformInterface();
        genAlgContextualTransformInterface();
    }

    private void genGeneralizedAlg() throws IOException {
        // String content = "/*\n" + self.toString() + "*/\n"; // log
        String content = declarePackage();
        String tvarString = printTvars(self.joinTvarsAndLhsTvars(t -> t, t -> O + t));

        content += "public interface " + generalizedName(self) + tvarString;
        content += self.mapParents(" extends ", parent -> qualifiedName(parent, generalizedName(parent))
                + printTvars(parent.joinTvarsAndLhsTvars(t -> t, t -> O + t)));
        content += " {\n";
        content += self.joinMethodsWithNewline(
                method -> TAB + method.substReturnType(t -> O + t) + " " + method + ";") + "\n";
        for (String tvar : self.getNewLhsTvars())
            content += TAB + O + tvar + " " + "visit" + tvar + "(" + tvar + " e);\n";
        content += "}";

        write(generalizedName(self), content);
    }

    private void genAlgVisitor() throws IOException {
        String name = self.name + VISITOR;
        String content = declarePackage();
        content += "public interface " + name;

        content += printTvars(self.joinTvars(t -> self.lhsTvars.contains(t) ? O + t : t)) + " extends "
                + qualifiedName(self, generalizedName(self))
                + printTvars(self.joinTvarsAndLhsTvars(this::thisLhsTvarWithElement, t -> O + t))
                + " {\n";
        for (String tvar : self.lhsTvars) {
            content += TAB + "default " + O + tvar + " visit" + tvar + "(" + thisLhsTvarWithElement(tvar) + " e) {\n"
                    +  TAB2 + "return e.accept(this);\n"
                    +  TAB + "}\n";
        }
        content += "}\n";

        write(name, content);
    }

    private void genExternal() throws IOException {
        genCommon();
        genPatternMatching();
    }

    private void genCommon() throws IOException {
        genAlgVisitor();
        for (String tvar : self.lhsTvars) {
            genAlgTvarElement(tvar);
        }
        genAlgFactory();
    }

    private void genPatternMatching() throws IOException {
        genAlgMapperInterface();
        genAlgApplierInterface();
        genAlgMatcherInterface();
        genAlgMatcherImplClass();
    }

    private void genAlgDefaultInterface() throws IOException {
        String name = thisWith(DEFAULT);
        String content = declarePackage();
        String tvarString = printTvars(self.joinTvarsAndLhsTvars(t -> t, t -> O));

        content += "public interface " + name + printTvars(self.joinTvars(), O) + " extends "
                + qualifiedName(self, generalizedName(self)) + tvarString;
        content += self.mapParents(", ", parent -> qualifiedName(parent) + DEFAULT + printTvars(parent.joinTvars(), O));
        content += " {\n";
        if (!self.hasParent())
            content += TAB + "library.Zero<" + O + "> m();\n\n";
        content += self.joinMethodsWithNewline(method -> TAB + "default " + O + " " + method + " {\n" + TAB2 + "return "
                + "m().empty();\n" + TAB + "}\n");
        content += "}";
        write(name, content);
    }

    private void genAlgTransformInterface() throws IOException {
        String name = thisWith(TRANSFORM);
        String content = declarePackage();
        String tvarString = printTvars(self.joinTvarsAndLhsTvars(t -> t, t -> t));

        content += "public interface " + name + printTvars(self.joinTvars()) + " extends "
                + qualifiedName(self, generalizedName(self)) + tvarString;
        content += self.mapParents(", ", parent -> qualifiedName(parent) + TRANSFORM + printTvars(parent.joinTvars()));
        content += " {\n";
        content += factoryDependency() + "\n";
        content += self.joinMethodsWithNewline(method -> TAB + "default " + method.returnType + " " + method + " {\n"
                + TAB2 + "return alg()." + method.name + "("
                + method.mapArgs((tvar, arg) -> "visit" + tvar + "(" + arg + ")") + ");\n" + TAB + "}\n");
        content += "}";

        write(name, content);
    }

    private String factoryDependency() {
      return TAB + self.packageName + "." + self.name + printTvars(self.joinTvars()) + " alg();\n";
    }

    private void genAlgContextualTransformInterface() throws IOException {
        String name = thisWith(TRANSFORM) + "WithCtx";
        String content = declarePackage();
        Function<String, String> toFun = t -> "java.util.function.Function" + printTvars(O, t);
        String tvarString = printTvars(self.joinTvarsAndLhsTvars(t -> t, toFun));

        content += "public interface " + name + printTvars(O, self.joinTvars()) + " extends "
                + qualifiedName(self, generalizedName(self)) + tvarString;
        content += self.mapParents(", ",
                parent -> qualifiedName(parent, parent.name) + TRANSFORM + "WithCtx" + printTvars(O, parent.joinTvars()));
        content += " {\n";
        content += factoryDependency() + "\n";
        content += self.joinMethodsWithNewline(method -> TAB + "default " + method.substReturnType(toFun) + " " + method
                + " {\n" + TAB2 + "return c -> alg()." + method.name + "("
                + method.mapArgs((tvar, arg) -> "visit" + tvar + "(" + arg + ").apply(c)") + ");\n" + TAB + "}\n");
        content += "}";

        write(name, content);
    }

    private void genAlgFactory() throws IOException {
        String name = thisWith(FACTORY);
        String tvarString = printTvars(self.joinRhsTvars());
        String alg = self.packageName + "." + self.name + printTvars(self.joinTvars(this::thisLhsTvarWithElement));

        String content = declarePackage();
        content += "public class " + name + tvarString + " implements " + alg + " {\n";
        content += self.joinAllMethodsWithNewline(this::genConstructor);
        content += "}";

        write(name, content);
    }

    private String genConstructor(MethodInfo method) {
        String tvar = method.returnType;
        String returnType = method.substReturnType(this::thisLhsTvarWithElement);
        String tvarString = self.lhsTvars.contains(tvar) ? printTvars(self.joinLhsTvars(t -> O + t))
                : printTvars(method.returnType, self.joinLhsTvars());
        // declare parameters
        String content = TAB + "public " + returnType + " "
                + method.substTypes(self.lhsTvars::contains, this::thisLhsTvarWithElement) + " {\n";
        content += TAB2 + "return new " + returnType + "() {\n";

        // override accept method
        content += TAB3 + "public " + tvarString + " " + O + tvar + " accept(" + getVisitorType() + " v) {\n";
        content += TAB4 + "return v." + method.name + "(" + method.mapTypeArgThenJoinWithComma((type, arg) -> arg) + ");\n";
        content += TAB3 + "}\n";
        content += TAB2 + "};\n";
        content += TAB + "}\n";

        return content;
    }

    private void genAlgTvarElement(String tvar) throws IOException {
        String content = declarePackage();
        String tvarString = printTvars(self.joinRhsTvars(t -> t));
        String returnType = printTvars(self.joinLhsTvars(t -> O + t));
        content += "public interface " + "C" + tvar + tvarString + " {\n";
        content += TAB + returnType + " " + O + tvar + " accept(" + getVisitorType() + " v);\n";
        content += "}\n";

        write("C"+tvar, content);
    }

    private void genAlgMapperInterface() throws IOException {
        String name = thisWith(MAPPER);
        String content = declarePackage();
        String tvarString = printTvars(self.joinUsedTvars(t -> I + t), O);

        content += "public interface " + name + tvarString;
        content += self.mapParents(" extends ",
                parent -> qualifiedName(parent) + MAPPER + printTvars(parent.joinUsedTvars(t -> I + t), O));
        content += " {\n";
        content += self.joinMethodsWithNewline(method -> TAB
                + method.substTypes(self.tvars::contains, t -> I + t).currying(O) + " " + method.name + MAPPER + "();");
        content += "}";

        write(name, content);
    }

    private void genAlgMatcherImplClass() throws IOException {
        String name = self.name + MATCHER + "Impl";
        String matcherTvarString = printTvars(self.joinTvars(this::thisLhsTvarWithElement), O);
        String content = declarePackage();
        content += genAlgApplierWithVisitorInterface() + "\n";
        content += "public class " + name + printTvars(self.joinRhsTvars(), O) + " implements " + thisWith(MATCHER)
                + matcherTvarString + " {\n";
        // fields
        content += self.joinAllMethodsWithNewline(
                method -> TAB + "private " + getMapperTvarString(method) + " " + method.name + " = null;") + "\n\n";
        // getters
        content += self.joinAllMethodsWithNewline(method -> TAB + "public " + getMapperTvarString(method) + " "
                + method.name + MAPPER + "() {\n" + TAB2 + "return " + method.name + ";\n" + TAB + "}\n");
        // setters
        content += self.joinAllMethodsWithNewline(method -> TAB + "public " + thisWith(MATCHER) + matcherTvarString
                + " " + method.name + "(" + getMapperTvarString(method) + " " + method.name + ") {\n" + TAB2 + "this."
                + method.name + " = " + method.name + ";\n" + TAB2 + "return this;\n" + TAB + "}\n");
        // otherwise
        content += TAB + "public " + qualifiedName(self, generalizedName(self))
                + printTvars(self.joinTvarsAndLhsTvars(this::thisLhsTvarWithElement, t -> O))
                + " otherwise(java.util.function.Supplier" + _O_ + " otherwise) {\n";
        content += self
                .joinAllMethodsWithNewline(method -> TAB2 + "if (" + method.name + " == null) " + method.name + " = "
                        + (method.genArgs.size() == 0 ? "() -> "
                                : method.genArgs.stream().map(arg -> arg + " -> ").collect(joining("")))
                        + "otherwise.get();");
        content += "\n\n";
        content += TAB2 + "return new " + (self.name + APPLIER + "With" + VISITOR) + printTvars(self.joinRhsTvars(), O)
                + "() {\n";
        content += TAB3 + "public " + thisWith(MAPPER) + printTvars(self.joinUsedTvars(this::thisLhsTvarWithElement), O)
                + " mapper() {\n";
        content += TAB4 + "return " + name + ".this;\n";
        content += TAB3 + "}\n";
        content += TAB2 + "};\n";
        content += TAB + "}\n";
        content += "}\n";

        write(name, content);
    }

    private void genAlgApplierInterface() throws IOException {
        String content = declarePackage();
        content += "public interface " + thisWith(APPLIER) + printTvars(self.joinTvars(t -> I + t), O) + " extends "
                + qualifiedName(self, generalizedName(self)) + printTvars(self.joinTvarsAndLhsTvars(t -> I + t, t -> O));
        content += self.mapParents(", ",
                parent -> qualifiedName(parent) + APPLIER + printTvars(parent.joinTvars(t -> I + t), O));
        content += " {\n";
        content += TAB + thisWith(MAPPER) + printTvars(self.joinUsedTvars(t -> I + t), O) + " mapper();\n\n";
        content += self
                .joinMethodsWithNewline(method -> TAB + "default " + O + " "
                        + method.substTypes(self.tvars::contains,
                                t -> I + t)
                        + " {\n" + TAB2 + "return mapper()." + method.name + MAPPER + "()"
                        + (method.genArgs.size() == 0 ? ".get()"
                                : method.genArgs.stream().map(arg -> ".apply(" + arg + ")")
                                        .collect(joining("")))
                        + ";\n" + TAB + "}\n");
        content += "}";

        write(thisWith(APPLIER), content);
    }

    private String genAlgApplierWithVisitorInterface() {
        String visitorsToMixin = self.name + VISITOR
                + printTvars(self.joinTvars(t -> self.lhsTvars.contains(t) ? O : t));
        if (visitorsToMixin.length() != 0)
            visitorsToMixin = ", " + visitorsToMixin;
        return "interface " + thisWith("ApplierWithVisitor") + printTvars(self.joinRhsTvars(), O) + " extends "
                + thisWith(APPLIER) + printTvars(self.joinTvars(this::thisLhsTvarWithElement), O) + visitorsToMixin
                + " {\n}\n";
    }

    private void genAlgMatcherInterface() throws IOException {
        String content = declarePackage();
        String tvarString = printTvars(self.joinTvars(t -> I + t), O);
        content += "public interface " + thisWith(MATCHER) + tvarString + " extends " + thisWith(MAPPER)
                + printTvars(self.joinUsedTvars(t -> I + t), O);
        content += self.mapParents(", ",
                parent -> qualifiedName(parent) + MATCHER + printTvars(parent.joinTvars(t -> I + t), O));
        content += " {\n";
        content += self.joinAllMethodsWithNewline(method -> TAB + thisWith(MATCHER) + tvarString + " " + method.name
                + "(" + method.substTypes(self.tvars::contains, t -> I + t).currying(O) + " " + method.name + ");");
        content += "\n";
        content += TAB + qualifiedName(self, generalizedName(self))
                + printTvars(self.joinTvarsAndLhsTvars(t -> I + t, t -> O)) + " otherwise(java.util.function.Supplier"
                + _O_ + " Otherwise);\n";
        content += "}\n";

        write(thisWith(MATCHER), content);
    }

    private void genAlgQueryInterface() throws IOException {
        String name = thisWith(QUERY);
        String content = declarePackage();

        content += "public interface " + name + printTvars(self.joinTvars(), O) + " extends "
                + qualifiedName(self, generalizedName(self)) + printTvars(self.joinTvarsAndLhsTvars(t -> t, t -> O));
        content += self.mapParents(", ",
                parent -> qualifiedName(parent) + QUERY + printTvars(parent.joinTvars(), O));
        content += " {\n";
        if (!self.hasParent())
            content += TAB + "library.Monoid<" + O + "> m();\n\n";
        content += self.joinMethodsWithNewline(method -> TAB + "default " + O + " " + method + " {\n" + TAB2 + "return "
                + method.reduceArgs(self.lhsTvars::contains, "m().empty()", "m()::join", "") + ";\n" + TAB + "}\n");
        content += "}\n";
        write(name, content);
    }

    private void genAlgContextualQueryInterface() throws IOException {
        String name = thisWith(QUERY) + "WithCtx";
        String content = declarePackage();
        Function<String, String> toFun = t -> "java.util.function.Function" + printTvars(I, O);
        String tvarString = printTvars(self.joinTvarsAndLhsTvars(t -> t, toFun));

        content += "public interface " + name + printTvars(I, O, self.joinTvars()) + " extends "
                + qualifiedName(self, generalizedName(self)) + tvarString;
        content += self.mapParents(", ",
                parent -> qualifiedName(parent, parent.name) + QUERY + "WithCtx" + printTvars(I, O, parent.joinTvars()));
        content += " {\n";
        if (!self.hasParent())
            content += TAB + "library.Monoid<" + O + "> m();\n\n";
        content += self.joinMethodsWithNewline(method -> TAB + "default " + method.substReturnType(toFun) + " " + method + " {\n" + TAB2 + "return c -> "
                + method.reduceArgs(self.lhsTvars::contains, "m().empty()", "m()::join", ".apply(c)") + ";\n" + TAB + "}\n");
        content += "}";

        write(name, content);
    }
    private void genAlgGeneralizedMonoidQueryInterface() throws IOException {
        String name = "G_" + thisWith(QUERY);
        String tvarString = printTvars(self.joinTvarsAndLhsTvars(t -> t, t -> O + t));
        String content = declarePackage();
        content += "public interface " + name + tvarString + " extends " + qualifiedName(self, generalizedName(self))
                + tvarString;
        content += self.mapParents(", ", parent -> qualifiedName(parent, "G_" + parent.name + QUERY)
                + printTvars(parent.joinTvarsAndLhsTvars(t -> t, t -> O + t)));
        content += " {\n";
        content += self.joinNewLhsTvars(tvar -> TAB + "library.Monoid<" + O + tvar + "> m" + tvar + "();");
        content += "\n\n";
        content += self.joinMethodsWithNewline(method -> TAB + "default " + method.substReturnType(t -> O + t) + " "
                + method + " {\n" + TAB2
                + "return " + method.reduceArgs(tvar -> tvar.equals(method.returnType),
                        "m" + method.returnType + "().empty()", "m" + method.returnType + "()::join", "")
                + ";\n" + TAB + "}\n");
        content += "}\n";
        write(name, content);
    }

    // utility functions
    private String declarePackage() {
        return "package " + self.packageName + ";\n\n";
    }

    private String thisWith(String name) {
        return self.name + name;
    }

    private String thisLhsTvarWithElement(String tvar) {
        return self.lhsTvars.contains(tvar) ? "C" + tvar + printTvars(self.joinRhsTvars()) : tvar;
    }

    private void write(String name, String content) throws IOException {
        String filePath = self.packageName + "/" + name;
        File f = new File(filePath);
        if (!f.exists()) {
            JavaFileObject file = filer.createSourceFile(filePath);
            file.openWriter().append(content).close();
        }
    }

    private static String printTvars(String... tvars) {
        String str = Stream.of(tvars).filter(s -> s.length() != 0).collect(joining(", "));
        return str.length() == 0 ? "" : "<" + str + ">";
    }

    private String getVisitorType() {
        return self.name + VISITOR + printTvars(self.joinTvars(t -> self.lhsTvars.contains(t) ? O + t : t));
    }

    private String getMapperTvarString(MethodInfo method) {
        return method.substTypes(self.lhsTvars::contains, this::thisLhsTvarWithElement).currying(O);
    }

    private String generalizedName(ClassInfo c) {
        return "G" + c.name;
    }

    private String qualifiedName(ClassInfo c) {
      return qualifiedName(c, c.name);
    }

    private String qualifiedName(ClassInfo c, String name) {
      return c.packageName + "." + name;
    }
}