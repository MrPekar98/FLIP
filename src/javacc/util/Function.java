package javacc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Function extends SymbolTable
{
  // Enum for function return types.
  public enum ReturnType
  {
    ACTION("Action"), BOOLEAN("Boolean"), NUMBER("Number"), PLAYER("DefinePlayer"), CARD("Card"),
    SUIT("Suit"), RANK("Rank"), SEQUENCE("Sequence"), PILE("Pile"), TEXT("Text");

    private String value;

    ReturnType(String str)
    {
      this.value = str;
    }

    @Override
    public String toString()
    {
      return this.value;
    }
  }

  private ReturnType returnType;
  private final List<Variable> parameters = new ArrayList<>();

  // Constructors.
  public Function(String name, ReturnType type)
  {
    super(name);
    this.returnType = type;
  }

  public Function(String name, ReturnType type, Variable ... parameters)
  {
    this(name, type);

    for (Variable var : parameters)
    {
      this.parameters.add(var);
    }
  }

  public Function(String name, ReturnType type, Collection<Variable> parameters)
  {
    this(name, type);
    this.parameters.addAll(parameters);
  }

  // Getters
  public ReturnType getReturnType()
  {
    return this.returnType;
  }

  public List<Variable> getParameters()
  {
    return this.parameters;
  }

  // Overridden equals method.
  @Override
  public boolean equals(Object o)
  {
    if (o == this)
      return true;

    else if (!(o instanceof Function))
      return false;

    Function f = (Function) o;

    if (this.returnType == f.getReturnType() && this.parameters.equals(((Function) o).getParameters()))
      return true;

    return false;
  }
}
