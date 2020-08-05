package javacc.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SymbolStructureTest {

  @Test
  void testEquals()
  {
    SymbolStructure struct1 = new Variable(Variable.DataType.NUMBER, "Var1");
    SymbolStructure struct2 = new Variable(Variable.DataType.NUMBER, "Var1");
    SymbolStructure struct3 = new Variable(Variable.DataType.BOOLEAN, "Var1");

    assertTrue(struct1.equals(struct2));
    assertFalse(struct2.equals(struct3));
  }
}