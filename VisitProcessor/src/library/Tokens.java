package library;

public interface Tokens {
    default int num(String x) {
        return Integer.parseInt(x);
    }
    default String string(String x) {
        return x;
    }
//    @Token("Id = DIGIT | LETTER")
    default String id(String x) {
        return x;
    }
    default float floatNum(String x) {
        return Float.parseFloat(x);
    }
    default double doubleNum(String x) {
        return Double.parseDouble(x);
    }
}