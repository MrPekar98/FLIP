package javacc.util;

public interface ObjectStruct
{
  void addField(String fieldName, Variable.DataType type);
  boolean isField(String name);
  Variable.DataType getDataType(String name);
}
