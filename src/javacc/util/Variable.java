package javacc.util;

public class Variable extends SymbolStructure
{
  // Enum representing data type of variable.
  public enum DataType
  {
    BOOLEAN("Boolean"), NUMBER("Number"), PLAYER("Player"), CARD("Card"), SUIT("Suit"), RANK("Rank"),
    SEQUENCE("Sequence"), PILE("Pile"), TEXT("Text");

    private String name;

    DataType(String type)
    {
      this.name = type;
    }
  
    public static DataType fromString(String type) {
      for (DataType t : DataType.values()) {
        if (t.name.equals(type)) {
          return t;
        }
      }
    
      return null;
    }

    @Override
    public String toString()
    {
      return this.name;
    }
  }

  // Class representing a value of a variable.
  public static class Value<E extends Comparable<Object>>
  {
    private E value;

    // Constructor.
    public Value(E val)
    {
      this.value = val;
    }

    // Getter.
    public E getValue()
    {
      return this.value;
    }

    @Override
    public boolean equals(Object o)
    {
      if (o == this)
        return true;

      else if (!(o instanceof Value))
        return false;

      Value v = (Value) o;

      if (getValue().toString().equals(v.getValue().toString()) && this.value.compareTo(v) == 0)
        return true;

      return false;
    }
  }

  protected DataType type;
  private Value value = null;

  // Constructors.
  public Variable(DataType type, String name)
  {
    super(SymbolType.VARIABLE, name);
    this.type = type;
  }

  public Variable(DataType type, String name, Value value)
  {
    this(type, name);
    this.value = value;
  }

  // Setters
  public void setValue(Value value)
  {
    this.value = value;
  }

  // Getters
  public DataType getVarType()
  {
    return this.type;
  }

  public Value getVarValue()
  {
    return this.value;
  }

  @Override
  public boolean equals(Object o)
  {
    SymbolStructure struct = (SymbolStructure) o;

    if (!(o instanceof Variable))
      return false;

    else if (this.value != null)
    {
      if (struct.equals(this) && this.getVarType() == ((Variable) struct).getVarType() &&
            this.getVarValue().equals(((Variable) struct).getVarValue()) && super.name.equals(struct.getName()))
      return true;
    }

    else if (struct.getType() == super.type && struct.getName().equals(super.name) &&
            this.getVarType() == ((Variable) struct).getVarType())
      return true;

    return false;
  }
}
