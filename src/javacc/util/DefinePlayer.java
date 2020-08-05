package javacc.util;

import java.util.HashMap;
import java.util.Map;

public class DefinePlayer extends SymbolStructure implements ObjectStruct
{
  private final Map<String, Variable.DataType> fields = new HashMap<>();
  private final Map<String, SequenceTypeCheck> sequences = new HashMap<>();

  public DefinePlayer()
  {
    super(null, "DefinePlayer");
    this.fields.put("hand", Variable.DataType.PILE);
  }

  // Adds field to record.
  @Override
  public void addField(String fieldName, Variable.DataType type)
  {
    if (this.fields.containsKey(name) || this.sequences.containsKey(name))
      throw new TableException("'" + name + "' is already declared.");

    this.fields.put(fieldName, type);
  }

  // Adds sequence.
  public void addSequence(String name, SequenceTypeCheck sequence)
  {
    if (this.fields.containsKey(name) || this.sequences.containsKey(name))
      throw new TableException("'" + name + "' is already declared.");

    this.sequences.put(name, sequence);
  }

  // Getter to sequence.
  public SequenceTypeCheck getSequence(String name)
  {
    return this.sequences.get(name);
  }

  // Checks for existence of field in set.
  @Override
  public boolean isField(String name)
  {
    return this.fields.containsKey(name) || this.sequences.containsKey(name);
  }

  // Returns data type of field.
  public Variable.DataType getDataType(String field)
  {
    if (this.fields.containsKey(field))
      return this.fields.get(field);

    return null;
  }
}
