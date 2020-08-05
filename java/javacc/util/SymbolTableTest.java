package javacc.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SymbolTableTest
{
  private final SymbolTable table = new SymbolTable("Test");
  private final Variable var = new Variable(Variable.DataType.NUMBER, "testVariable3");

  @BeforeEach
  void setup()
  {
    SymbolTable program = new SymbolTable("program");
    this.table.insert(program);
    SymbolTable mainFunction = new SymbolTable("main");
    this.table.insert(mainFunction);
    mainFunction.insert(new Variable(Variable.DataType.NUMBER,"testVariable1"));
    SymbolTable loop = new SymbolTable("loop");
    this.table.insert(loop);
    this.table.insert(new Variable(Variable.DataType.NUMBER, "testVariable2"));
    loop.close();
    this.table.insert(var);
    mainFunction.close();
    program.close();
    this.table.close();
  }

  // Tests size of symbol table.
  @Test
  void testCount()
  {
    assertEquals(6, this.table.count());
  }

  // Tests that "program", "main", "loop", "testVariable1", "testVariable2", and "testVariable3" exist.
  @Test
  void testExistence()
  {
    SymbolTable program = new SymbolTable("program");
    this.table.insert(program);
    Function mainFunction = new Function("main", Function.ReturnType.ACTION);
    this.table.insert(mainFunction);
    assertTrue(this.table.exists("program"));
    mainFunction.insert(new Variable(Variable.DataType.NUMBER,"testVariable1"));
    assertTrue(this.table.exists("main"));
    SymbolTable loop = new SymbolTable("loop");
    this.table.insert(loop);
    this.table.insert(new Variable(Variable.DataType.NUMBER, "testVariable2"));
    assertTrue(this.table.exists("testVariable1"));
    assertTrue(this.table.exists("loop"));
    assertTrue(this.table.exists("testVariable2"));
    loop.close();
    this.table.insert(new Variable(Variable.DataType.NUMBER,"testVariable3"));
    assertTrue(this.table.exists("testVariable3"));
    mainFunction.close();
    program.close();
    this.table.close();
  }

  // Tests that "var1" and "var2" do not exists.
  @Test
  void testNonExistence()
  {
    assertFalse(this.table.exists("var1"));
    assertFalse(this.table.exists("var2"));
  }

  // Tests deleting a single variable. Above tests must work for this test to work.
  @Test
  void testDeleteVar()
  {
    this.table.delete(new Variable(Variable.DataType.NUMBER,"testVariable3"));
    assertEquals(5, this.table.count());
  }

  // Test deleting a scope. Above tests must work for this test to work.
  @Test
  void testDeleteScope()
  {
    this.table.delete(new SymbolTable("main"));
    assertEquals(1, this.table.count());
  }

  // Tests scope of an testVariable3.
  @Test
  void testScope()
  {
    assertEquals(2, var.getScope());
  }

  // Tests that an exception is thrown when a variable is already declared.
  @Test
  void testAlreadyDeclared1()
  {
    SymbolTable program = new SymbolTable("program");
    SymbolTable mainFunction = new Function("main", Function.ReturnType.ACTION);
    program.insert(mainFunction);
    mainFunction.insert(new Variable(Variable.DataType.NUMBER,"testVariable1"));
    assertThrows(TableException.class, () -> program.insert(new Variable(Variable.DataType.NUMBER,"testVariable1")));
  }

  @Test
  void testAlreadyDeclared2()
  {
    SymbolTable program = new SymbolTable("program");
    SymbolTable mainFunction = new Function("main", Function.ReturnType.ACTION);
    program.insert(mainFunction);
    mainFunction.insert(new Variable(Variable.DataType.NUMBER,"testVariable1"));
    SymbolTable loop = new SymbolTable("loop");
    program.insert(loop);
    loop.close();
    assertDoesNotThrow(() -> program.insert(new SymbolTable("loop")));
  }

  @Test
  void testAlreadyDeclared3()
  {
    SymbolTable program = new SymbolTable("program");
    SymbolTable mainFunction = new SymbolTable("main");
    program.insert(mainFunction);
    mainFunction.insert(new Variable(Variable.DataType.NUMBER,"testVariable1"));
    SymbolTable loop = new SymbolTable("loop");
    program.insert(loop);
    loop.close();
    mainFunction.close();
    assertThrows(TableException.class, () -> program.insert(new Function("main", Function.ReturnType.ACTION)));
  }

  // Tests getter to element in symbol table.
  @Test
  void testGetter()
  {
    SymbolTable program = new SymbolTable("program");
    SymbolTable mainFunction = new Function("main", Function.ReturnType.ACTION);
    program.insert(mainFunction);
    mainFunction.insert(new Variable(Variable.DataType.NUMBER,"testVariable1"));
    SymbolTable loop = new SymbolTable("loop");
    program.insert(loop);
    assertTrue(program.get("testVariable1").equals(new Variable(Variable.DataType.NUMBER,"testVariable1")));
  }
}