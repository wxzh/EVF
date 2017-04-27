package benchmark;

interface FList {
  <O> O accept(FVisitor<O> v);
}