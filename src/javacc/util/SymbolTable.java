package javacc.util;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable extends SymbolStructure implements Table
{
  protected final List<SymbolStructure> table = new ArrayList<>();
  private boolean closed;
  private final DefinePlayer defPlayer = new DefinePlayer();
  private final ObjectStruct cardStruct = new CardObject();
  private final ObjectStruct pileStruct = new PileObject();

  // Constructor.
  public SymbolTable(String name)
  {
    super(SymbolType.SYMBOL_TABLE, name);
    this.closed = false;
  }

  // Opens table.
  @Override
  public void open()
  {
    this.closed = false;
  }

  // Closes scope.
  @Override
  public void close()
  {
    this.closed = true;
  }

  // Getter to element.
  @Override
  public SymbolStructure get(String name)
  {
    for (int i = maxScopeLevel(); i >= 0; i--)
    {
      for (SymbolStructure struct : openScope(i))
      {
        if (struct.getName().equals(name))
          return struct;
      }
    }

    return null;
  }

  // Getter to DefinePlayer.
  public DefinePlayer getPlayerRecord()
  {
    return this.defPlayer;
  }

  // Getter to Card object structure.
  public ObjectStruct getCardRecord()
  {
    return this.cardStruct;
  }

  // Getter to Pile object structure.
  public ObjectStruct getPileRecord()
  {
    return this.pileStruct;
  }

  // Inserts element into symbol table.
  @Override
  public void insert(SymbolStructure element)
  {
    if ((element.getType() != SymbolType.SYMBOL_TABLE && isDeclared(element)) ||
            (element instanceof Function && isDeclared(element)))
      throw new TableException("Symbol '" + element.getName() + "' already declared.");

    else if (this.table.size() == 0)
    {
      this.table.add(element);
      element.scope = 0;
    }

    else
    {
      openScope(maxScopeLevel()).add(element);
      element.scope = maxScopeLevel();
    }
  }

  // Checks if element already is declared in scope.
  protected boolean isDeclared(SymbolStructure element)
  {
    List<SymbolStructure> scope = openScope(maxScopeLevel());

    for (SymbolStructure struct : scope)
    {
      if (struct.getName().equals(element.getName()))
        return true;
    }

    return false;
  }

  // Checks whether symbol table is closed.
  @Override
  public boolean isClosed()
  {
    return this.closed;
  }

  // Check for declaration of element.
  @Override
  public boolean exists(String element)
  {
    for (int i = maxScopeLevel(); i >= 0; i--)
    {
      for (SymbolStructure struct : openScope(i))
      {
        if (struct.getName().equals(element))
          return true;
      }
    }

    return false;
  }

  // Returns max scope level that is open.
  private int maxScopeLevel()
  {
    List<SymbolStructure> scope = this.table;
    int scopeCounter = 0, i = 0;

    while (i < scope.size())
    {
      if (scope.get(i).getType() == SymbolType.SYMBOL_TABLE && !((SymbolTable) scope.get(i)).isClosed())
      {
        scopeCounter++;
        scope = ((SymbolTable) scope.get(i)).table;
        i = 0;
        continue;
      }

      i++;
    }

    return scopeCounter;
  }

  // Returns open scope in argument specified scope level.
  private List<SymbolStructure> openScope(int scopeLevel)
  {
    int i = 0, scopeCounter = 0;
    List<SymbolStructure> scope = this.table;

    while (i < scope.size() && scopeCounter < scopeLevel)
    {
      if (scope.get(i).getType() == SymbolType.SYMBOL_TABLE && !((SymbolTable) scope.get(i)).isClosed())
      {
        scope = ((SymbolTable) scope.get(i)).table;
        scopeCounter++;
        i = 0;
        continue;
      }

      i++;
    }

    return scope;
  }

  // Returns amount of elements in symbol table.
  @Override
  public int count()
  {
    return tableCounter(this.table);
  }

  // Recursively counting all elements including nested tables and their elements.
  private static int tableCounter(List<SymbolStructure> symbolTable)
  {
    int counter = 0;

    for (SymbolStructure struct : symbolTable)
    {
      if (struct.getType() == SymbolType.SYMBOL_TABLE)
        counter += tableCounter(((SymbolTable) struct).table) + 1;

      else
        counter++;
    }

    return counter;
  }

  // Deletes symbol table element found by name, scope and type.
  @Override
  public void delete(SymbolStructure element)
  {
    recursiveDelete(this.table, element);
  }

  // Recursively deletes element in all scopes it is found.
  private static void recursiveDelete(List<SymbolStructure> scope, SymbolStructure element)
  {
    for (int i = 0; i < scope.size(); i++)
    {
      if (scope.get(i).equals(element))
        scope.remove(i);

      else if (scope.get(i).getType() == SymbolType.SYMBOL_TABLE)
        recursiveDelete(((SymbolTable) scope.get(i)).table, element);
    }
  }
}
