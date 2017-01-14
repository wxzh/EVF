package annotation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Alternative implements Comparable<Alternative> {
    private int level;
    private List<String> syms;
    private String nt;
    private MethodInfo m;
    private Map<String, String> arg2nt;

    public Alternative(int level, String syntax, MethodInfo m) {
	    this.syms = Arrays.asList(syntax.split(" "));
        this.level = level;
        this.m = m;
        this.nt = decapitalize(m.returnType);
        this.arg2nt = IntStream.range(0, m.types.size()).boxed().collect(Collectors.toMap(i -> m.getArgs().get(i), i -> type2NT(m.types.get(i))));
    }

    public String getNT() {
        return nt;
    }

    public String decapitalize(String s) {
        if (s == null) return null;
        return s.substring(0, 1).toLowerCase() + s.substring(1, s.length());
    }

//    private void validateSyntax() {
//    }

    public String toString() {
        String prod = "";
        Map<String, String> args = new HashMap<>();
        for (String s : syms) {
            if (isLiteral(s)) prod += s;
            else if (isSepList(s)) {
                String arg = getSepListArg(s);
                String nt = arg2nt(arg);
                String argAssign = arg + "+=" + nt;
                prod += "(" + argAssign + " (" + getSepListToken(s) + " " + argAssign + ")*)";
                if (s.charAt(s.length() - 1) == '*') prod += '?';
                args.put(arg, "$" + arg + ".stream().map(x -> x._" + nt + ").collect(toList())");
            } else if (isList(s)) {
                String arg = getListArg(s);
                String nt = arg2nt(arg);
                prod += "(" + arg + "+=" + nt + ")" + s.charAt(s.length()-1);
                args.put(arg, "$" + arg + ".stream().map(x -> x._" + nt + ").collect(toList())");
            }
            else { // arg
                String nt = arg2nt(s);
                prod += s + "=" + nt;
                args.put(s, "$" + s + "._" + nt);
            }
            prod += " ";
        }
        String fields = m.getArgs().stream().map(arg -> args.get(arg)).collect(Collectors.joining(","));
        String action = "{$_" + nt + " = " + "alg" + "." + m.name + "(" + fields + ");}";

	    return prod + action;
    }

    @Override
    public int compareTo(Alternative other) {
        return Integer.compare(other.level, level);
    }

    public String type2NT(String t) {
        switch (t) {
        case "int": return "num";
        case "java.lang.String": return "string";
        default:
            if (t.startsWith("java.util.List<"))
                return type2NT(t.substring(t.indexOf('<')+1, t.indexOf('>')));
            return decapitalize(t);
        }
    }

    static boolean isToken(String s) {
        return s.matches("^[A-Z][a-zA-Z]$");
    }

    static boolean isLiteral(String s) {
        return s.matches("^'.*'$");
    }

    static boolean isNonTerminal(String s) {
        return s.matches("^[a-z][a-zA-Z]*$");
    }

    static boolean isRegular(String s) {
        return s.matches("^[a-z][a-zA-Z]*[*+?]$");
    }

    boolean isSepList(String s) {
        return s.contains("@");
    }

    boolean isList(String s) {
        return s.matches("^[a-z][a-zA-Z]*[*+?]$");
//        return s.endsWith("*") || s.endsWith("+");
    }

    static String getListArg(String s) {
        return s.substring(0, s.length() - 1);
    }

    static String getSepListArg(String s) {
        return s.substring(0, s.indexOf('@'));
    }

    static String getSepListToken(String s) {
        return s.substring(s.indexOf('@') + 1, s.length() - 1);
    }

    static String getRegularSymbol(String s) {
        return s.substring(0, s.length() - 1);
    }

    boolean isInfix() {
        return syms.size() == 3 && (isToken(getOp()) || isLiteral(getOp()));
    }

    String getOp() {
        return syms.get(1);
    }

    String arg2nt(String arg) {
        return arg2nt.get(arg);
    }
}
