package javacc.visitors;

public class TypeException extends RuntimeException
{
  public TypeException()
  {
    super("A type exception occured.");
  }

  public TypeException(String msg)
  {
    super(msg);
  }
}
