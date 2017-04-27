require 'fileutils'
require 'csv'
require 'tempfile'

`rm AllEval1.java`

JAVA_PATH = 'src/'
OCAML_PATH = 'ocaml/'
dirs = Dir.entries(OCAML_PATH) & Dir.entries(JAVA_PATH)
newPackages = Dir.entries(JAVA_PATH) - Dir.entries(OCAML_PATH)
dir_regex  = '(' + dirs.join('|') + ')'

def cloc(dir, option)
  csv = CSV.new(`cloc #{dir} #{option} --quiet --csv`)
  csv.to_a[2]
end

def loc(dir, option)
  cloc(dir, option)[4].to_i
end

def percentage(a, b)
  ((b.to_f - a) / b * 100).ceil
end

def is_dot_dir(dir)
  dir =~ /^\./
end

def result(text, java, ocaml)
  puts "#{text} & #{java} & #{ocaml} & #{percentage(java,ocaml)}\\%"
end

dirs.each do |dir|
  unless is_dot_dir(dir)
    path = OCAML_PATH + dir + '/'
    # extract datatype definitions
    lines = File.readlines(path + 'syntax.mli')
            .drop_while{ |line| not line =~ /type/}
            .take_while{ |line| not line =~ /type command/}
    File.write(path + "adt.mli", lines.join + "\n")

    # extract eval1 definitions
    lines = File.readlines(path + 'core.ml')
            .drop_while{ |line| not line =~ /let rec eval1/}
            .take_while{ |line| line != "\n"}
    File.write(path + "eval1.ml", lines.join + "\n")
  end
end

newPackages.each do |dir|
  unless is_dot_dir(dir)
    total_java = loc(JAVA_PATH + dir, "--not-match-f='(ConstFunElim|GetBodyFromTmAbs|IsVarUsed).java'")
    puts "#{dir} & #{total_java} & N/A & N/A"
  end
end

Dir.entries(OCAML_PATH).each do |dir|
  unless is_dot_dir(dir)
    total_java = loc(JAVA_PATH + dir, "--not-match-f='(ConstFunElim|GetBodyFromTmAbs|IsVarUsed).java'")
    total_ocaml = loc(OCAML_PATH + dir, "--match-f='^(core|syntax).ml$'")
    result(dir, total_java, total_ocaml)
  end
end

# total_ocaml = loc(OCAML_PATH, "--match-f='^(core|syntax).ml$'")
# total_java = loc(JAVA_PATH, "--not-match-f='Tests.java'")
# result("Total", total_java, total_ocaml)

# remove headers and merge the content into a single tmp file
open('AllEval1.java', 'a+') do |f|
  Dir[JAVA_PATH + '*/Eval1.java'].each do |file|
    lines = File.readlines(file).drop_while{ |line| not line =~ /public interface/}
    f.puts lines
  end
end


adt_ocaml = loc(OCAML_PATH, "--match-f='^adt.mli$'")
csv = cloc(JAVA_PATH, "--match-f='^(Term|Ty|Binding)Alg.java$'")
adt_java = csv[4].to_i - (csv[0].to_i * 3)

result("Datatype Definition", adt_java, adt_ocaml)
eval1_ocaml = loc(OCAML_PATH, "--match-f='^eval1.ml$'")
eval1_java = loc('.', "--match-f='AllEval1.java'")
result("Small-step Evaluator", eval1_java, eval1_ocaml)

