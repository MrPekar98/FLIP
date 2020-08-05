package javacc.util;

public interface Table
{
  void open();
  void close();
  void insert(SymbolStructure element);
  boolean isClosed();
  boolean exists(String element);
  int count();
  void delete(SymbolStructure element);
  SymbolStructure get(String name);
}
