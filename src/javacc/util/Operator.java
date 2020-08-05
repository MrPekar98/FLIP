package javacc.util;

public enum Operator {
  ASSIGN(":="),
  
  OR("or"),
  
  AND("and"),
  
  XOR("xor"),
  
  NOT("not"),
  
  EQUALS("="),
  
  GREATER_THAN(">"),
  GREATEN_THAN_EQUALS(">="),
  LESS_THAN("<"),
  LESS_THAN_EQUALS("<="),
  CONTAINS("contains"),
  
  ADDITION("+"),
  SUBTRACTION("-"),
  
  MULTIPLICATION("*"),
  DIVISION("/"),
  MODULO("mod"),
  
  NEGATIVE("-"),
  
  PROPERTY("->");
  
  
  private String symbol;
  
  Operator(String symbol) {
    this.symbol = symbol;
  }
  
  public String getSymbol() {
    return this.symbol;
  }
  
  public static Operator fromSymbol(String symbol) {
    for (Operator o : Operator.values()) {
      if (o.symbol.equals(symbol)) {
        return o;
      }
    }
    
    return null;
  }
}
