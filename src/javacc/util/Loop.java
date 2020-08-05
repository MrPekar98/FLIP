package javacc.util;

public class Loop extends SymbolTable
{
  protected int start, end, increment;

  // Constructor
  public Loop(int start, int end, int incrementBy)
  {
    super("Loop");
    this.start = start;
    this.end = end;
    this.increment = incrementBy;
  }

  // Getters
  public int getStart()
  {
    return this.start;
  }

  public int getEnd()
  {
    return this.end;
  }

  public int getIncrement()
  {
    return this.increment;
  }
}
