package annotation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.antlr.v4.Tool;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.ast.GrammarRootAST;

public class AlgGrammar {
    Map<String, List<Alternative>> prods;
    String name;
    String pkg;
    String signature;
    String path;

    public AlgGrammar(Map<String, List<Alternative>> prods, String name, String pkg, String path) {
        this.prods = prods;
        this.name = name;
        this.pkg = pkg;
        this.path = path;
    }

    private String header() {
        return "@header{\n"
                + "package " + pkg + ";\n"
                + "import " + pkg + ".*;\n"
                + "import static java.util.stream.Collectors.toList;\n"
                + "}\n";
    }

    private String parserMembers() {
        String factory = name + "Factory";
        return "@parser::members{\n"
                + "private " + factory + " alg = new " + factory + "();\n"
                + "}\n";
    }
    public String toString() {
        return "grammar " + name + ";\n"
                + header()
                + parserMembers()
                + prods.entrySet().stream().map(e -> {
                    String nt = e.getKey();
                    String Nt = nt.substring(0,1).toUpperCase() + nt.substring(1,nt.length());
                    return nt + " returns [" + Nt + " _" + nt + "]:\n"
                            + e.getValue().stream().map(alt -> alt + "\n").collect(Collectors.joining("  | ", "    ", "  ;\n"));
                }).collect(Collectors.joining("\n"))
                + footer();
    }

    // TODO: buggy
    // not possible: as writing to files in different ways?
    public void generate() {
        Tool t = new Tool();
        new Tool();
        GrammarRootAST root = t.parseGrammarFromString(this.toString());
        Grammar g = t.createGrammar(root);
        t.gen_listener = false;
        t.gen_visitor = false;
        t.gen_dependencies = false;
        g.fileName = path;
        t.process(g, true);
    }

    public String genParser() {
        String alg = "TermAlg";
        String prog = "term";
        String lexer = alg + "Lexer";
        String parser = alg + "Parser";
        return lexer + " lexer = new " + lexer + ";\n"
                + "CommonTokenStream tokens = new CommonTokenStream(lexer);\n"
                + parser + " parser = new " + parser + ";\n"
                + "return parser." + prog + "()." + prog;
    }

    private String footer() {
        return "num returns [int _num]: i=NUM {$_num = Integer.parseInt($i.text);};\n" +
               "string returns [String _string]: s=STRING {$_string = $s.text;};\n" +
               "\n" +
               "NUM: DIGIT+;\n" +
               "fragment DIGIT: [0-9];\n" +
               "STRING: LETTER (LETTER | DIGIT)*;\n" +
               "fragment LETTER: [a-zA-Z];\n" +
               "WS: [ \\t\\n\\r] -> skip;";
    }
}