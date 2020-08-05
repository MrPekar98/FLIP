package javacc.util;

public final class SequenceTypeCheck
{
  private int scope;
  private Variable.DataType type;

  // Constructor
  public SequenceTypeCheck(int scope, Variable.DataType dataType)
  {
    this.scope = scope;
    this.type = dataType;
  }

  // Getters.
  public int getScope()
  {
    return this.scope;
  }

  public Variable.DataType getDataType()
  {
    return this.type;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == this)
      return true;

    else if (!(obj instanceof SequenceTypeCheck))
      return false;

    SequenceTypeCheck stc = (SequenceTypeCheck) obj;

    if (stc.getDataType() == this.getDataType() && stc.getScope() == this.scope)
      return true;

    return false;
  }
}
