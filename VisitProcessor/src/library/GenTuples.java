package library;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenTuples {
	static final int MAX = 15;
	static final String PATH = "." + File.separator + "src" + File.separator + "library" + File.separator;

	public static BiFunction<Function<Integer, String>, String, String> rangeToString(int from, int to) {
		return (f, sep) -> IntStream.range(from, to+1).mapToObj(n -> f.apply(n)).collect(Collectors.joining(sep));
	}

	public static void main(String[] args) {
		for (int i = 2; i <= MAX; i++) {
			final String name = "Tuple" + i;
			BiFunction<Function<Integer, String>, String, String> one2i = rangeToString(1, i);

			try (FileWriter writer = new FileWriter(PATH + name + ".java")) {
				String content = "package library;\n\n";
				content += "public class " + name + "<" + one2i.apply(n -> "T" + n, ", ") + "> {\n";
				content += one2i.apply((n -> "\tpublic final T" + n + " _" + n + ";"), "\n");
				content += "\n\tpublic " + name + "(" + one2i.apply((n -> "T" + n + " arg" + n), ", ") + ") {\n";
				content += one2i.apply(n -> "\t\tthis._" + n + " = arg" + n + ";", "\n");
				content += "\n\t}\n";

				// toString
				content += "\tpublic String toString() {\n";
				content += "\t\treturn String.format(\"(" + one2i.apply(n -> "%s", ", ") + ")\", " + one2i.apply(n -> "_" + n, ", ") + ");\n";
				content += "\t}\n";
				content += "}";

				writer.write(content);
				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
