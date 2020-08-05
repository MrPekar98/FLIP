package javacc.util;

import java.util.HashMap;
import java.util.Map;

public class CardObject extends SymbolStructure implements ObjectStruct
{
  private final Map<String, Variable.DataType> fields = new HashMap<>();

  // Constructor
  public CardObject()
  {
    super(null, "Card Structure");
    this.fields.put("rank", Variable.DataType.RANK);
    this.fields.put("suit", Variable.DataType.SUIT);
  }

  // Adds new field.
  @Override
  public void addField(String fieldName, Variable.DataType type)
  {
    if (this.fields.containsKey(fieldName))
      throw new TableException("'" + fieldName + "' is already declared.");

    this.fields.put(fieldName, type);
  }

  // Checks whether argument is a field.
  public boolean isField(String name)
  {
    return this.fields.containsKey(name);
  }

  // Returns data type of specific field.
  @Override
  public Variable.DataType getDataType(String name)
  {
    return this.fields.get(name);
  }
}
