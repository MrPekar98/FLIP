package javacc.util;

public class Sequence extends Variable
{
  private SequenceTypeCheck type;

  // Constructor
  public Sequence(String name, SequenceTypeCheck type)
  {
    super(DataType.SEQUENCE, name);
    this.type = type;
  }

  // Getter
  public SequenceTypeCheck getElementType()
  {
    return this.type;
  }

  // Overriden equals() method.
  @Override
  public boolean equals(Object obj)
  {
    if (obj == this)
      return true;

    else if (!(obj instanceof Sequence))
      return false;

    Sequence s = (Sequence) obj;

    if (this.type.equals(s.getElementType()))
      return true;

    return false;
  }
}
