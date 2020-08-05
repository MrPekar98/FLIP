package javacc.util;

import java.util.HashMap;
import java.util.Map;

public class PileObject extends SymbolStructure implements ObjectStruct
{
  private final Map<String, Variable.DataType> fields = new HashMap<>();

  // Constructor
  public PileObject()
  {
    super(null, "Pile Structure");
    this.fields.put("size", Variable.DataType.NUMBER);
  }

  // Adds field to object.
  @Override
  public void addField(String fieldName, Variable.DataType type)
  {
    if (this.fields.containsKey(fieldName))
      throw new TableException("'" + fieldName + "' is already declared.");

    this.fields.put(fieldName, type);
  }

  // Check whether input argument is a field.
  @Override
  public boolean isField(String name)
  {
    return this.fields.containsKey(name);
  }

  // Gets data type of given input.
  @Override
  public Variable.DataType getDataType(String name)
  {
    return this.fields.get(name);
  }
}
