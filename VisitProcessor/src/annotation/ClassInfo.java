package annotation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

class ClassInfo {
	public final String name;
	public final String packageName;
	public final Set<MethodInfo> methods;
	public final Set<MethodInfo> allMethods;
	public final int numOfTvars;
	public final List<String> usedTvars;
	public final List<String> lhsTvars;
	public final List<String> rhsTvars;
	private List<String> newLhsTvars;
	public final List<ClassInfo> parents;
	public final List<String> tvars;

	public final String O = "O";

	public List<String> getNewLhsTvars() {
		return newLhsTvars;
	}

	public ClassInfo(TypeElement element) {
		name = elementToString(element);
		packageName = parsePackageName(element);
		tvars = parseTvars(element);
		numOfTvars = tvars.size();
		methods = parseMethods(element);
		parents = parseParents(element, tvars);
		allMethods = getAllMethods();
		lhsTvars = getLhsTvars();
		rhsTvars = tvars.stream().filter(t -> !lhsTvars.contains(t)).collect(Collectors.toList());
		usedTvars = getUsedTvars();
	}

	private List<String> getLhsTvars() {
		Set<String> thisLhsTvars = methods.stream().flatMap(m -> m.lhsTvars.stream()).collect(Collectors.toSet());
		Set<String> parentsLhsTvars = parents.stream().flatMap(c -> c.lhsTvars.stream()).collect(Collectors.toSet());
		newLhsTvars = thisLhsTvars.stream().filter(tvar -> !parentsLhsTvars.contains(tvar)).collect(Collectors.toList());
		Set<String> lhsTvars = Stream.concat(thisLhsTvars.stream(), parentsLhsTvars.stream()).collect(Collectors.toSet());
		return tvars.stream().filter(lhsTvars::contains).collect(Collectors.toList());
	}

	private List<String> getUsedTvars() {
		Set<String> usedTvars =  Stream.concat(methods.stream().flatMap(m -> m.usedTvars.stream()), parents.stream().flatMap(c -> c.usedTvars.stream())).collect(Collectors.toSet());
		return tvars.stream().filter(usedTvars::contains).collect(Collectors.toList());
	}


	public String joinMethodsWithNewline(Function<MethodInfo, String> f) {
		return mapThenJoin(methods, f, "\n", "", "");
	}

	public String joinAllMethodsWithNewline(Function<MethodInfo, String> f) {
		return mapThenJoin(allMethods, f, "\n", "", "");
	}

	public String joinTvars() {
		return joinTvars(t -> t);
	}

	public String joinTvars(Function<String, String> f) {
		return mapThenJoin(tvars, f, ", ", "", "");
	}

//	public String joinTvarsAndLhsTvars() {
//		return joinTvarsAndLhsTvars(t -> I + t, t -> O + t);
//	}

	public String joinTvarsAndLhsTvars(Function<String, String> f1, Function<String, String> f2) {
		return Stream.concat(tvars.stream().map(f1), lhsTvars.stream().map(f2)).collect(Collectors.joining(", "));
	}

	public String joinNewLhsTvars(Function<String, String> f) {
		return mapThenJoin(newLhsTvars, f, "\n", "", "");
	}

	public boolean hasParent() {
		return parents.size() != 0;
	}

	public String mapParents(String start, Function<ClassInfo, String> f) {
		return mapThenJoin(parents, f, ", ", start, "");
	}

	public String joinLhsTvars() {
		return joinLhsTvars(t -> t);
	}

	public String joinRhsTvars() {
		return joinRhsTvars(t -> t);
	}

	public String joinUsedTvars() {
		return joinUsedTvars(t -> t);
	}

	public String joinUsedTvars(Function<String, String> f) {
		return mapThenJoin(usedTvars, f, ", ", "", "");
	}

	public String joinLhsTvars(Function<String, String> f) {
		return mapThenJoin(lhsTvars, f, ", ", "", "");
	}

	public String joinRhsTvars(Function<String, String> f) {
		return mapThenJoin(rhsTvars, f, ", ", "", "");
	}

	private static <T> String mapThenJoin(Collection<T> set, Function<T, String> f, String delimiter, String start,
			String end) {
		return set.size() == 0 ? ""
				: set.stream().map(f).collect(Collectors.joining(delimiter, start, end));
	}

	private static List<String> parseTvars(Element element) {
		String s = ((DeclaredType) element.asType()).getTypeArguments().toString();
		return Arrays.asList(s.substring(1, s.length() - 1).split(", "));
	}

	public static String elementToString(Element element) {
		return element.getSimpleName().toString();
	}

	public static String parsePackageName(Element element) {
		return ((PackageElement) element.getEnclosingElement()).getQualifiedName().toString();
	}

	public Set<MethodInfo> getAllMethods() {
		return Stream.concat(parents.stream().flatMap(c -> c.allMethods.stream()), methods.stream()).collect(Collectors.toSet());
	}

	public static TypeElement typeMirror2TypeElement(TypeMirror t) {
		return (TypeElement) ((DeclaredType) t).asElement();
	}

	public Set<MethodInfo> parseMethods(Element element) {
		return element.getEnclosedElements().stream().map(e -> new MethodInfo(e, tvars, this))
				.filter(MethodInfo::isConstructor).collect(Collectors.toSet());
	}

	public static List<ClassInfo> parseParents(TypeElement element, List<String> tvars) {
		return element.getInterfaces().stream().map(t -> getParent(t, tvars)).collect(Collectors.toList());
	}

	public static ClassInfo getParent(TypeMirror t, List<String> tvars) {
		return new ClassInfo(typeMirror2TypeElement(t));
	}

	public static List<Integer> getTypeArgs(String type, List<String> rawTvars) {
		Matcher m = Pattern.compile("^.*?<(.*?)>$").matcher(type);
		return m.find() ? Stream.of(m.group(1).split(",")).map(t -> rawTvars.indexOf(t)).collect(Collectors.toList())
				: Collections.emptyList();
	}

	// for debugging
	@Override
	public String toString() {
		return "name:" + name + "\ntvars" + tvars + "\nlhsTvars:" + lhsTvars + "\nnewLhsTvars:" + newLhsTvars
				 + "\nall methods\n"
				+ joinAllMethodsWithNewline(m -> m.lhsTvars + " " + m.returnType + m)
				+ mapParents("\nparents:\n", ClassInfo::toString);
	}
}