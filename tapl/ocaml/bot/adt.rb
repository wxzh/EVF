file = File.readlines("syntax.mli").take_while{ |line| not line =~ /type command/}
puts file
