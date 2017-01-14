package annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;

import library.Tuple2;

class MethodInfo {
	public final String name;
	public final List<String> types;
	public final Set<String> lhsTvars;
	public final Set<String> usedTvars;
	public final List<String> genArgs;
	public final String returnType;
	private final ClassInfo belongsTo;
	public String syntax;
	public Element element;

	public MethodInfo(Element e, List<String> tvars, ClassInfo c) {
	    element = e;
		name = ClassInfo.elementToString(e);
		types = getArgTypes(e);
		returnType = getReturnType(e);
		lhsTvars = getLhsTvars(tvars);
		usedTvars = getUsedTvars(tvars);
		genArgs = genArgs(types.size());
		belongsTo = c;
//		syntax = getSyntax(e);
	}

	public MethodInfo(String name, List<String> types, Set<String> lhsTvars, Set<String> usedTvars, String returnType,
			ClassInfo belongsTo) {
		this.name = name;
		this.types = types;
		this.genArgs = genArgs(types.size());
		this.lhsTvars = lhsTvars;
		this.usedTvars = usedTvars;
		this.returnType = returnType;
		this.belongsTo = belongsTo;
	}

	public static List<String> getArgTypes(Element e) {
		return ((ExecutableType) e.asType()).getParameterTypes().stream().map(Object::toString)
				.collect(Collectors.toList());
	}

	public List<String> getArgs() {
		return ((ExecutableElement) element).getParameters().stream().map(Object::toString)
				.collect(Collectors.toList());
	}

	public static String getReturnType(Element e) {
		return ((ExecutableType) e.asType()).getReturnType().toString();
	}

	public static List<String> parseGenericType(String s) {
		List<String> ts = new ArrayList<>();
		int depth = 0;
		int splitPoint = 0;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '<':
				depth += 1;
				break;
			case '>':
				depth -= 1;
				break;
			case ',':
				if (depth == 0) {
					ts.add(s.substring(splitPoint, i).trim());
					splitPoint = i + 1;
				}
				break;
			}
		}
		ts.add(s.substring(splitPoint, s.length()));
		return ts;
	}

	public static Optional<Tuple2<String, List<String>>> getCarrierType(String type) {
		Matcher m = Pattern.compile("^(.*?)<(.*?)>$").matcher(type);
		return m.find() ? Optional.of(new Tuple2<>(m.group(1), parseGenericType(m.group(2)))) : Optional.empty();
	}

	public static List<String> genArgs(int n) {
		return IntStream.range(1, n + 1).mapToObj(i -> "p" + i).collect(Collectors.toList());
	}

	public boolean isConstructor() {
		return Character.isUpperCase(name.charAt(0));
	}

	public String mapTypeArgThenJoinWithComma(BiFunction<String, String, String> f) {
		return mapTypeArgThenJoin(f, ", ");
	}

	public String mapTypeArgThenJoin(BiFunction<String, String, String> f, String delimeter) {
		return IntStream.range(0, types.size()).mapToObj(i -> f.apply(types.get(i), genArgs.get(i)))
				.collect(Collectors.joining(delimeter));
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || getClass() != other.getClass())
			return false;
		return name.equals(((MethodInfo) other).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public String toString() {
		return name + "(" + mapTypeArgThenJoinWithComma((type, arg) -> type + " " + arg) + ")";
	}

	public String reduceArgs(Predicate<String> p, String init, String joiner, String postfix) {
		String reduce = ".reduce(" + init + ", " + joiner + ")";
		return reduce1(IntStream.range(0, types.size())
				.mapToObj(i -> reduceArg(types.get(i), genArgs.get(i), p, init, reduce, postfix)).collect(Collectors.toList()),
				reduce).orElse(init);
	}

	public Optional<String> reduceArg(String type, String arg, Predicate<String> p, String init, String reduce, String postfix) {
		return getCarrierType(type).map(pr -> reduceGenericArg(pr._1, pr._2, arg, p, init, reduce, postfix))
				.orElse(p.test(type) ? Optional.of("visit" + type + "(" + arg + ")" + postfix) : Optional.empty());
	}

	private static String genFreshName(String arg) {
		return "_" + arg.split("\\.")[0];
	}

	public Optional<String> reduceGenericArg(String tOut, List<String> tIn, String arg, Predicate<String> p,
			String init, String reduce, String postfix) {
		String var = genFreshName(arg);
		switch (tOut) {
		case "java.util.List":
		case "java.util.Set":
			return reduceArg(tIn.get(0), var, p, init, reduce, postfix)
					.map(s -> arg + ".stream().map(" + var + " -> " + s + ")" + reduce);
		case "java.util.Optional":
			return reduceArg(tIn.get(0), var, p, init, reduce, postfix)
					.map(s -> arg + ".map(" + var + " -> " + s + ").orElse(" + init + ")");
		default: // Tuples
			return reduce1(IntStream.range(0, tIn.size())
					.mapToObj(i -> reduceArg(tIn.get(i), arg + "._" + (i + 1), p, init, reduce, postfix))
					.collect(Collectors.toList()), reduce);
		}
	}

	private Optional<String> reduce1(List<Optional<String>> os, String reduce) {
		List<String> xs = os.stream().filter(o -> o.isPresent()).map(o -> o.get()).collect(Collectors.toList());
		switch (xs.size()) {
		case 0:
			return Optional.empty();
		case 1:
			return Optional.of(xs.get(0));
		default:
			return Optional.of("java.util.stream.Stream.of(" + String.join(", ", xs) + ")" + reduce);
		}
	}

	public String mapArgs(BiFunction<String, String, String> f) {
		return mapArgs(belongsTo.lhsTvars::contains, f, ", ");
	}

	public String mapArgs(Predicate<String> p, BiFunction<String, String, String> f, String delimeter) {
		return mapTypeArgThenJoin((type, arg) -> mapArg(type, arg, p, f), delimeter);
	}

	public String mapArg(String type, String arg, Predicate<String> p, BiFunction<String, String, String> f) {
		if (!innerTypeThatMeets(type, p))
			return arg;
		return getCarrierType(type).map(pr -> mapGenericArg(p, arg, pr._1, pr._2, f))
				.orElse(p.test(type) ? f.apply(type, arg) : arg);
	}

	public boolean innerTypeThatMeets(String type, Predicate<String> p) {
		return getCarrierType(type)
				.map(pr -> pr._2.stream().map(t -> innerTypeThatMeets(t, p)).reduce(false, (b1, b2) -> b1 || b2))
				.orElse(p.test(type));
	}

	private String mapGenericArg(Predicate<String> p, String arg, String tOut, List<String> tIn,
			BiFunction<String, String, String> f) {
		String var = genFreshName(arg);
		switch (tOut) {
		case "java.util.List":
		case "java.util.Set":
			return arg + ".stream().map(" + var + " -> " + mapArg(tIn.get(0), var, p, f)
					+ ").collect(java.util.stream.Collectors.to" + tOut.substring("java.util.".length()) + "())";
		case "java.util.Optional":
			return arg + ".map(" + var + " -> " + mapArg(tIn.get(0), var, p, f) + ")";
		default:
			return IntStream.range(0, tIn.size()).mapToObj(i -> mapArg(tIn.get(i), arg + "._" + (i + 1), p, f))
					.collect(Collectors.joining(", ", "new library.Tuple" + tIn.size() + "<>(", ")"));
		}
	}

	// assume return type is a lhs tvar
	public String substReturnType(Function<String, String> f) {
		return f.apply(returnType);
	}

	public String currying(String returnType) {
		if (types.size() == 0) return "java.util.function.Supplier<" + returnType + ">";
		return currying(types, returnType);
	}

	private String currying(List<String> types, String returnType) {
		if (types.size() == 0) {
			return returnType;
		}
		return "java.util.function.Function<" + toBoxed(types.get(0)) + ", "
				+ currying(types.subList(1, types.size()), returnType) + ">";
	}

	private static String toBoxed(String type) {
		switch (type) {
		case "int":
			return "Integer";
		case "char":
			return "Character";
		case "boolean":
			return "Boolean";
		case "byte":
			return "Byte";
		case "float":
			return "Float";
		case "long":
			return "Long";
		case "short":
			return "Short";
		case "double":
			return "Double";
		default:
			return type;
		}
	}

	private Set<String> getLhsTvars(List<String> tvars) {
		return getUsedTvars(returnType, tvars).collect(Collectors.toSet());
	}

	private Set<String> getUsedTvars(List<String> tvars) {
		return Stream.concat(Stream.of(returnType), types.stream()).flatMap(type -> getUsedTvars(type, tvars))
				.collect(Collectors.toSet());
	}

	private static Stream<String> getUsedTvars(String type, List<String> tvars) {
		Optional<Tuple2<String, List<String>>> pair = getCarrierType(type);
		return pair.map(pr -> pr._2.stream().flatMap(t -> getUsedTvars(t, tvars)))
				.orElse(tvars.indexOf(type) == -1 ? Stream.empty() : Stream.of(type));
	}

	public String substType(Predicate<String> p, String type, Function<String, String> f) {
		return getCarrierType(type)
				.map(pr -> pr._1
						+ pr._2.stream().map(t -> substType(p, t, f)).collect(Collectors.joining(", ", "<", ">")))
				.orElse(p.test(type) ? f.apply(type) : type);
	}

	public MethodInfo substTypes(Predicate<String> p, Function<String, String> f) {
		return new MethodInfo(name, types.stream().map(type -> substType(p, type, f)).collect(Collectors.toList()),
				lhsTvars, usedTvars, substReturnType(f), belongsTo);
	}
}