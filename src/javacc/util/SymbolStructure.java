package javacc.util;

public abstract class SymbolStructure
{
  // Enum for each type of element in a symbol table.
  // No enum types for loops and functions. The are represented by SYMBOL_TABLE.
  public enum SymbolType
  {
    SYMBOL_TABLE,
    VARIABLE;
  }

  protected SymbolType type;
  protected String name;
  protected Integer scope = null;
  protected boolean global = false;

  // Constructor.
  protected SymbolStructure(SymbolType type, String name)
  {
    this.type = type;
    this.name = name;
  }

  // Getter to type.
  public SymbolType getType()
  {
    return this.type;
  }

  // Getter to globalist.
  public boolean isGlobal()
  {
    return this.global;
  }

  // Getter to name.
  public String getName()
  {
    return this.name;
  }

  // Getter to scope.
  public int getScope()
  {
    if (this.scope == null)
      throw new TableException("Element not inserted into symbol table.");

    return this.scope;
  }

  // Setter to globalism of structure.
  public void setGlobal(boolean value)
  {
    this.global = value;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
      return true;

    else if (!(o instanceof SymbolStructure))
      return false;

    SymbolStructure struct = (SymbolStructure) o;

    if (struct.getName().equals(this.getName()) && struct.getType() == this.getType())
      return true;

    return false;
  }
}
