package javacc.visitors;

import javacc.generated.*;
import javacc.generated.Comparator;
import javacc.generated.Function;
import javacc.util.*;
import java.util.*;

public class TypeCheckerVisitor implements FlipParserVisitor {
  private final SymbolTable table = new SymbolTable("prog");
  private boolean inSetup = false;
  private boolean inSetupScope = false;
  private boolean inFunction = false;
  private boolean inStage = false;
  private boolean inStageBlock = false;
  private boolean inPlayerDef = false;
  private boolean onlyFunctionSignature = true;
  private boolean playerCountVisited = false;
  private boolean pilesDefVisited = false;
  private int definePlayersVisitedAmount = 0;

  @Override
  public Object visit(SetupBlock node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }

  @Override
  public Object visit(PlayerDef node, Object data) {
    if (!this.inSetup)
      throw new TypeException("'PlayerDef' may not be used outside setup.");

    this.definePlayersVisitedAmount++;
    SymbolTable playerScope = new SymbolTable("player");
    table.insert(playerScope);
    this.inPlayerDef = true;
    node.childrenAccept(this, data);
    this.inPlayerDef = false;
    playerScope.close();

    return null;
  }

  @Override
  public Object visit(PileDef node, Object data) {
    if (!this.inSetup)
      throw new TypeException("'PileDef' may not be used outside setup.");

    else if (node.jjtGetChild(0).jjtAccept(this, null) != Variable.DataType.NUMBER)
      throw new TypeException("Assigment of pile 'DefinePiles' must be of type Number.");

    else if ((Integer) ((NumberLiteral) node.jjtGetChild(0)).jjtGetValue() < 0)
      throw new TypeException("'DefinePiles' must be non-negative.");

    else if (this.table.exists("DefinePiles"))
      throw new TypeException("'DefinePiles' can not be redefined.");

    this.table.insert(new Variable(Variable.DataType.NUMBER, "DefinePiles"));
    this.pilesDefVisited = true;
    return null;
  }

  @Override
  public Object visit(PlayerCount node, Object data) {
    if (!this.inSetup)
      throw new TypeException("'PlayerCount' may not be used outside setup.");

    else if (this.table.exists("PlayerCount"))
      throw new TypeException("'PlayerCount' can not be redefined.");

    this.playerCountVisited = true;
    this.table.insert(new Variable(Variable.DataType.NUMBER, "PlayerCount"));
    return node.jjtGetChild(0).jjtAccept(this, data);
  }

  @Override
  public Object visit(PlayCall node, Object data) {
    final Variable.DataType secondType = (Variable.DataType) node.jjtGetChild(1).jjtAccept(this, data);

    if (!this.inStage || (this.inStage && !this.inStageBlock))
      throw new TypeException("'play()' not allowed outside second block of stages.");

    else if (node.jjtGetChild(0).jjtAccept(this, data) != Variable.DataType.PILE)
      throw new TypeException("First argument of 'play()' must be of type Pile.");

    else if (secondType != Variable.DataType.SEQUENCE && secondType != Variable.DataType.CARD && secondType != Variable.DataType.PILE)
      throw new TypeException("Second argument of 'play()' must either be of type Card or Sequence.");

    return null;
  }

  @Override
  public Object visit(MoveCall node, Object data) {
    final Variable.DataType thirdType = (Variable.DataType) node.jjtGetChild(2).jjtAccept(this, data);

    if (node.jjtGetChild(0).jjtAccept(this, data) != Variable.DataType.PILE)
      throw new TypeException("First argument of 'move()' must be of type Pile.");

    else if (node.jjtGetChild(1).jjtAccept(this, data) != Variable.DataType.PILE)
      throw new TypeException("Second argument of 'move()' must be of type Pile.");

    else if (thirdType != Variable.DataType.CARD && thirdType != Variable.DataType.SEQUENCE && thirdType != Variable.DataType.PILE)
      throw new TypeException("Third argument of 'move()' must either be of type Card or Sequence.");

    return null;
  }

  @Override
  public Object visit(DealCall node, Object data) {
    if (node.jjtGetChild(0).jjtAccept(this, data) != Variable.DataType.PILE)
      throw new TypeException("First argument of 'deal()' must be of type Pile.");

    else if (node.jjtGetChild(1).jjtAccept(this, data) != Variable.DataType.NUMBER)
      throw new TypeException("Second argument of 'deal()' must be of type Number.");

    return null;
  }

  @Override
  public Object visit(PlayerCall node, Object data) {
    if (node.jjtGetChild(0).jjtAccept(this, data) != Variable.DataType.NUMBER)
      throw new TypeException("Argument of 'player()' must be of type Number.");

    return Variable.DataType.PLAYER;
  }

  @Override
  public Object visit(PileCall node, Object data) {
    if (node.jjtGetChild(0).jjtAccept(this, data) != Variable.DataType.NUMBER)
      throw new TypeException("Argument of 'pile()' must be of type Number.");

    return Variable.DataType.PILE;
  }

  // Returns null on purpose.
  @Override
  public Object visit(ActionReturnType node, Object data) {
    return null;
  }

  @Override
  public Object visit(TextType node, Object data) {
    return Variable.DataType.TEXT;
  }

  @Override
  public Object visit(SimpleNode node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }

  // Checks that two nodes are of an expression type: Sequence, Number, Pile, Card and Rank.
  private static boolean expressionComparable(Variable.DataType type1, Variable.DataType type2)
  {
    if ((type1 == Variable.DataType.SEQUENCE || type1 == Variable.DataType.NUMBER || type1 == Variable.DataType.PILE ||
            type1 == Variable.DataType.CARD || type1 == Variable.DataType.RANK) && (type2 == Variable.DataType.SEQUENCE ||
            type2 == Variable.DataType.NUMBER || type2 == Variable.DataType.PILE ||
            type2 == Variable.DataType.CARD || type2 == Variable.DataType.RANK) && type1 == type2)
      return true;

    else if ((type1 == Variable.DataType.CARD && type2 == Variable.DataType.PILE) ||
            (type1 == Variable.DataType.PILE && type2 == Variable.DataType.CARD))
      return true;

    return false;
  }

  @Override
  public Object visit(OrExpr node, Object data) {
    for (int i = 0; i < node.jjtGetNumChildren(); i++)
    {
      if ((Variable.DataType) node.jjtGetChild(i).jjtAccept(this, null) != Variable.DataType.BOOLEAN)
        throw new TypeException("Or expression not boolean.");
    }

    return node.jjtGetChild(0).jjtAccept(this, data);
  }

  @Override
  public Object visit(AndExpr node, Object data) {
    for (int i = 0; i < node.jjtGetNumChildren(); i++)
    {
      if ((Variable.DataType) node.jjtGetChild(i).jjtAccept(this, null) != Variable.DataType.BOOLEAN)
        throw new TypeException("Or expression not boolean.");
    }

    return node.jjtGetChild(0).jjtAccept(this, data);
  }

  @Override
  public Object visit(NotExpr node, Object data) {
    Variable.DataType type = (Variable.DataType) node.jjtGetChild(0).jjtAccept(this, data);

    if (type != Variable.DataType.BOOLEAN)
      throw new TypeException("Negating a proposition must be of type Boolean.");

    return type;
  }
  
  @Override
  public Object visit(EqualsExpr node, Object data) {
    if (node.jjtGetNumChildren() == 1)
      return node.jjtGetChild(0).jjtAccept(this, data);
    
    for (int i = 1; i < node.jjtGetNumChildren(); i += 1) {
      Variable.DataType left = (Variable.DataType) node.jjtGetChild(i - 1).jjtAccept(this, data);
      Variable.DataType right = (Variable.DataType) node.jjtGetChild(i).jjtAccept(this, data);
      if (left != right)
        throw new TypeException("Equals expressions require operands of same type. " + left.name() + ", " + right.name() + " given.");
    }
    
    return Variable.DataType.BOOLEAN;
  }

  @Override
  public Object visit(CompareExpr node, Object data) {
    // Only true when derived from ReturnStatement.
    if (data instanceof Properties)
      ((Properties) data).setProperty("Type", node.jjtGetChild(0).jjtAccept(this, null).toString());

    if (node.jjtGetNumChildren() == 1)
      return node.jjtGetChild(0).jjtAccept(this, data);

    // When comparator is 'contains'.
    else if (((Comparator) node.jjtGetChild(1)).jjtGetValue().toString().equals("CONTAINS"))
    {
      String id1;
      String id2;
      SequenceTypeCheck sequence1 = null;
      SequenceTypeCheck sequence2 = null;
      final Variable.DataType type1 = (Variable.DataType) node.jjtGetChild(0).jjtAccept(this, data);
      final Variable.DataType type2 = (Variable.DataType) node.jjtGetChild(2).jjtAccept(this, data);

      if (type1 != Variable.DataType.PILE && type1 != Variable.DataType.SEQUENCE)
        throw new TypeException("First operand of 'contains' must be a collection.");

      else if (node.jjtGetChild(0).toString().equals("ArrowExpr"))
      {
        id1 = ((Identifier) node.jjtGetChild(0).jjtGetChild(node.jjtGetChild(0).jjtGetNumChildren() - 1)).jjtGetValue().toString();

        if (type1 == Variable.DataType.SEQUENCE)
          sequence1 = this.table.getPlayerRecord().getSequence(id1);
      }

      else
      {
        id1 = ((Identifier) node.jjtGetChild(0)).jjtGetValue().toString();
        sequence1 = getSequenceElementType(node.jjtGetChild(0));
      }

      if (node.jjtGetChild(2).toString().equals("ArrowExpr"))
      {
        id2 = ((Identifier) node.jjtGetChild(2).jjtGetChild(node.jjtGetChild(2).jjtGetNumChildren() - 1)).jjtGetValue().toString();

        if (type2 == Variable.DataType.SEQUENCE)
          sequence2 = this.table.getPlayerRecord().getSequence(id2);
      }

      else
        sequence2 = getSequenceElementType(node.jjtGetChild(2));

      if (type1 == Variable.DataType.PILE && type2 != Variable.DataType.CARD)
        throw new TypeException("Second operand of 'contains' must be of type Card when first operand is of type Pile.");

      else if (type1 == Variable.DataType.SEQUENCE && sequence2 == null &&
              (sequence1.getDataType() != type2 || sequence1.getScope() != 1))
        throw new TypeException("Elements in sequence '" + id1 + "' does not match right operand of 'contains' operator.");

      else if (type1 == Variable.DataType.SEQUENCE && sequence2 != null &&
              (sequence1.getScope() != sequence2.getScope() + 1 || sequence1.getDataType() != sequence2.getDataType()))
        throw new TypeException("Left operand '" + id1 + "' does not match type of right operand of 'contains' operation.");

      return Variable.DataType.BOOLEAN;
    }

    for (int i = 2; i < node.jjtGetNumChildren(); i += 2)
    {
      if (!expressionComparable((Variable.DataType) node.jjtGetChild(i - 2).jjtAccept(this, data),
              (Variable.DataType) node.jjtGetChild(i).jjtAccept(this, data)))
        throw new TypeException("Expression is not completely of type Number.");
    }

    return Variable.DataType.BOOLEAN;
  }

  @Override
  public Object visit(TermExpr node, Object data) {
    if (node.jjtGetNumChildren() == 1)
      return node.jjtGetChild(0).jjtAccept(this, data);

    node.setTermType(Variable.DataType.NUMBER);

    for (int i = 2; i < node.jjtGetNumChildren(); i += 2)
    {
      if (node.jjtGetChild(i - 2).jjtAccept(this, data) == Variable.DataType.SEQUENCE ||
              node.jjtGetChild(i).jjtAccept(this, data) == Variable.DataType.SEQUENCE)
        sequenceTermCheck(node, i, data);

      else if ((node.jjtGetChild(i - 2).jjtAccept(this, data) == Variable.DataType.NUMBER && node.jjtGetChild(i).jjtAccept(this, data) != Variable.DataType.NUMBER) ||
              (node.jjtGetChild(i - 2).jjtAccept(this, data) != Variable.DataType.NUMBER && node.jjtGetChild(i).jjtAccept(this, data) == Variable.DataType.NUMBER))
        throw new TypeException("Illegal addition using type Number.");

      if (node.jjtGetChild(i - 2).toString().equals("Identifier") && node.jjtGetChild(i).toString().equals("Identifier"))
      {
        if (((Sequence) this.table.get(((Identifier) node.jjtGetChild(i - 2)).jjtGetValue().toString())).getElementType() !=
                ((Sequence) this.table.get(((Identifier) node.jjtGetChild(i)).jjtGetValue().toString())).getElementType())
          throw new TypeException("Both sequence must contain elements of same type in order to be added.");

        node.setTermType(Variable.DataType.SEQUENCE);
        node.setSequence(true);
      }

      else if (node.jjtGetChild(i - 2).toString().equals("Identifier") && !node.jjtGetChild(i).toString().equals("Identifier"))
      {
        if (node.jjtGetChild(i).jjtGetChild(0).jjtGetNumChildren() == 0)
          continue;

        final Variable.DataType seqLit = (Variable.DataType) node.jjtGetChild(i).jjtGetChild(0).jjtAccept(this, data);
        final String id = ((Identifier) node.jjtGetChild(i - 2)).jjtGetValue().toString();

        if (((Sequence) this.table.get(id)).getElementType().getDataType() != seqLit)
          throw new TypeException("The two sequence element types do not match.");
      }

      else if (!node.jjtGetChild(i - 2).toString().equals("Identifier") && node.jjtGetChild(i).toString().equals("Identifier"))
      {
        if (node.jjtGetChild(i - 2).jjtGetChild(0).jjtGetNumChildren() == 0)
          continue;

        final Variable.DataType seqLit = (Variable.DataType) node.jjtGetChild(i - 2).jjtGetChild(0).jjtAccept(this, data);
        final String id = ((Identifier) node.jjtGetChild(i)).jjtGetValue().toString();

        if (((Sequence) this.table.get(id)).getElementType().getDataType() != seqLit)
          throw new TypeException("The two sequence element types do not match.");
      }
    }

    return node.getTermType();
  }

  // Type checking for sequences.
  private void sequenceTermCheck(TermExpr node, int i, Object data)
  {
    if (node.jjtGetChild(i - 2).jjtAccept(this, data) == Variable.DataType.SEQUENCE &&
            node.jjtGetChild(i).jjtAccept(this, data) != Variable.DataType.SEQUENCE)
    {
      if (getSequenceElementType(node.jjtGetChild(i - 2)).getScope() == 1 && getSequenceElementType(node.jjtGetChild(i)) == null &&
              getSequenceElementType(node.jjtGetChild(i - 2)).getDataType() == node.jjtGetChild(i).jjtAccept(this, data))
        return;

      else if (node.jjtGetChild(i - 2) instanceof Identifier &&
              !((Sequence) this.table.get(((Identifier) node.jjtGetChild(i - 2)).jjtGetValue().toString())).getElementType().equals(getSequenceElementType(node.jjtGetChild(i))))
        throw new TypeException("Element of addition must be of same type as types of elements in sequence.");

      else if (node.jjtGetChild(i - 2).toString().equals("SequenceLiteral") &&
              getSequenceElementType(node.jjtGetChild(i - 2)).getDataType() != node.jjtGetChild(i).jjtAccept(this, data))
        throw new TypeException("Element of addition must be of same type as types of elements in sequence.");

      node.setTermType(Variable.DataType.SEQUENCE);
      node.setSequence(true);
    }

    else if (node.jjtGetChild(i).jjtAccept(this, data) == Variable.DataType.SEQUENCE &&
            node.jjtGetChild(i - 2).jjtAccept(this, data) != Variable.DataType.SEQUENCE)
    {
      if (getSequenceElementType(node.jjtGetChild(i - 2)) == null && getSequenceElementType(node.jjtGetChild(i)).getScope() == 1 &&
              node.jjtGetChild(i - 2).jjtAccept(this, data) == getSequenceElementType(node.jjtGetChild(i)).getDataType())
        return;

      else if (node.jjtGetChild(i) instanceof Identifier &&
              !((Sequence) this.table.get(((Identifier) node.jjtGetChild(i)).jjtGetValue().toString())).getElementType().equals(getSequenceElementType(node.jjtGetChild(i - 2))))
        throw new TypeException("Element of addition must be of same type as types of elements in sequence.");

      else if (node.jjtGetChild(i).toString().equals("SequenceLiteral") &&
              getSequenceElementType(node.jjtGetChild(i)) != node.jjtGetChild(i - 2).jjtAccept(this, data))
        throw new TypeException("Element of addition must be of same type as types of elements in sequence.");

      node.setTermType(Variable.DataType.SEQUENCE);
      node.setSequence(true);
    }

    else if (node.jjtGetChild(i).jjtAccept(this, data) == Variable.DataType.SEQUENCE &&
            node.jjtGetChild(i - 2).jjtAccept(this, data) == Variable.DataType.SEQUENCE)
    {
      if (getSequenceElementType(node.jjtGetChild(i - 2)) != null && getSequenceElementType(node.jjtGetChild(i)) != null &&
              Math.abs(getSequenceElementType(node.jjtGetChild(i - 2)).getScope() - getSequenceElementType(node.jjtGetChild(i)).getScope()) == 1 &&
              getSequenceElementType(node.jjtGetChild(i - 2)).getDataType() == getSequenceElementType(node.jjtGetChild(i)).getDataType())
        return;

      else if (node.jjtGetChild(i - 2) instanceof Identifier && node.jjtGetChild(i) instanceof Identifier)
      {
        String s1 = ((Identifier) node.jjtGetChild(i - 2)).jjtGetValue().toString();
        String s2 = ((Identifier) node.jjtGetChild(i)).jjtGetValue().toString();

        if (!this.table.get(s1).equals(this.table.get(s2)))
          throw new TypeException("Addition of sequences '" + s1 + "' and '" + s2 + "' are not of the same type.");
      }

      else if (node.jjtGetChild(i - 2) instanceof Identifier && !(node.jjtGetChild(i) instanceof Identifier))
      {
        String s = ((Identifier) node.jjtGetChild(i - 2)).jjtGetValue().toString();

        if (!getSequenceElementType(node.jjtGetChild(i)).equals(((Sequence) this.table.get(s)).getElementType()))
          throw new TypeException("Addition of sequences '" + s + "' and Sequence literal are not of the same type.");
      }

      else if (!(node.jjtGetChild(i - 2) instanceof Identifier) && node.jjtGetChild(i) instanceof Identifier)
      {
        String s = ((Identifier) node.jjtGetChild(i)).jjtGetValue().toString();

        if (!getSequenceElementType(node.jjtGetChild(i - 2)).equals(((Sequence) this.table.get(s)).getElementType()))
          throw new TypeException("Addition of sequences Sequence literal and '" + s + "' and are not of the same type.");
      }

      else if (Math.abs(getSequenceElementType(node.jjtGetChild(i - 2)).getScope() - getSequenceElementType(node.jjtGetChild(i)).getScope()) == 1 &&
              getSequenceElementType(node.jjtGetChild(i - 2)).getDataType() == getSequenceElementType(node.jjtGetChild(i)).getDataType())
      {
        node.setTermType(Variable.DataType.SEQUENCE);
        node.setSequence(true);
        return;
      }

      else if (!getSequenceElementType(node.jjtGetChild(i - 2)).equals(getSequenceElementType(node.jjtGetChild(i))))
        throw new TypeException("Addition of sequences are not of the same type.");

      node.setTermType(Variable.DataType.SEQUENCE);
      node.setSequence(true);
    }
  }

  @Override
  public Object visit(FactorExpr node, Object data)
  {
    if (node.jjtGetNumChildren() == 1)
      return node.jjtGetChild(0).jjtAccept(this, data);

    for (int i = 0; i < node.jjtGetNumChildren(); i += 2)
    {
      if (node.jjtGetChild(i).jjtAccept(this, data) != Variable.DataType.NUMBER)
        throw new TypeException("Expression is not completely of type Number.");
    }

    return node.jjtGetChild(0).jjtAccept(this, data);
  }

  @Override
  public Object visit(NegativeExpr node, Object data)
  {
    Variable.DataType type = (Variable.DataType) node.jjtGetChild(0).jjtAccept(this, data);

    if (type != Variable.DataType.NUMBER)
      throw new TypeException("Value must be of type Number in order to be negative.");

    return type;
  }
  
  @Override
  public Object visit(ArrowExpr node, Object data)
  {
    if (node.jjtGetNumChildren() == 1)
      return node.jjtGetChild(0).jjtAccept(this, data);
    
    // Type checking of several arrows.
    Variable.DataType type = (Variable.DataType) node.jjtGetChild(0).jjtAccept(this, data);
    Identifier id = null;
    
    for (int i = 1; i < node.jjtGetNumChildren(); i++)
    {
      id = (Identifier) node.jjtGetChild(i);
      
      if (type == Variable.DataType.PLAYER)
      {
        if (!this.table.getPlayerRecord().isField(id.jjtGetValue().toString()))
          throw new TypeException("'" + id.jjtGetValue().toString() + "' is not a field of Player.");
        
        type = this.table.getPlayerRecord().getDataType(id.jjtGetValue().toString());

        if (type == null)
          type = Variable.DataType.SEQUENCE;
      }

      else if (type == Variable.DataType.CARD)
      {
        if (!this.table.getCardRecord().isField(id.jjtGetValue().toString()))
          throw new TypeException("'" + id.jjtGetValue().toString() + "' is not a field of Card.");
        
        type = this.table.getCardRecord().getDataType(id.jjtGetValue().toString());
      }

      else if (type == Variable.DataType.PILE)
      {
        if (!this.table.getPileRecord().isField(id.jjtGetValue().toString()))
          throw new TypeException("'" + id.jjtGetValue().toString() + "' is not a field of Pile.");
        
        if (id.jjtGetValue().toString().equals("size")) {
          id.setIsSize(true);
        }
        type = this.table.getPileRecord().getDataType(id.jjtGetValue().toString());
      }
    }
    
    return type;
  }
  
  @Override
  public Object visit(ValueExpr node, Object data)
  {
    return node.childrenAccept(this, data);
  }

  @Override
  public Object visit(Function node, Object data)
  {
    if (this.onlyFunctionSignature)
    {
      javacc.util.Function f = new javacc.util.Function(((Identifier) node.jjtGetChild(1)).jjtGetValue().toString(),
              getReturnType(node), functionArguments(node));
      this.table.insert(f);
      f.close();
      return null;
    }

    SymbolTable funcBlock = new SymbolTable("FunctionBlock");

    if (getReturnType(node) == javacc.util.Function.ReturnType.ACTION)
      return acceptActionBlock((Block) node.jjtGetChild(node.jjtGetNumChildren() - 1), funcBlock, functionArguments(node));

    this.table.insert(funcBlock);
    insertArguments(functionArguments(node));
    Properties props = new Properties(1);
    node.jjtGetChild(node.jjtGetNumChildren() - 1).jjtAccept(this, props);
    funcBlock.close();

    if (!getReturnType(node).toString().equals(props.getProperty("Type")))
      throw new TypeException("Return type is " + getReturnType(node).toString() + ", but returns " + props.getProperty("Type"));

    return null;
  }

  // Accepts block for Action functions.
  private Object acceptActionBlock(Block node, SymbolTable functionBlock, List<Variable> args)
  {
    this.table.insert(functionBlock);
    insertArguments(args);
    Object result = node.childrenAccept(this, null);
    functionBlock.close();
    return result;
  }

  // Returns DataType of function declaration. If Action, null is returned.
  private javacc.util.Function.ReturnType getReturnType(Node n)
  {
    if (n.jjtGetChild(0).jjtAccept(this, null) == null)
      return javacc.util.Function.ReturnType.ACTION;

    switch (n.jjtGetChild(0).jjtAccept(this, null).toString())
    {
      case "Number":
        return javacc.util.Function.ReturnType.NUMBER;

      case "Boolean":
        return javacc.util.Function.ReturnType.BOOLEAN;

      case "DefinePlayer":
        return javacc.util.Function.ReturnType.PLAYER;

      case "Card":
        return javacc.util.Function.ReturnType.CARD;

      case "Suit":
        return javacc.util.Function.ReturnType.SUIT;

      case "Rank":
        return javacc.util.Function.ReturnType.RANK;

      case "Sequence":
        return javacc.util.Function.ReturnType.SEQUENCE;

      case "Pile":
        return javacc.util.Function.ReturnType.PILE;

      case "Text":
        return javacc.util.Function.ReturnType.TEXT;

      default:
        return null;
    }
  }

  // Returns list of arguments.
  private List<Variable> functionArguments(Node functionNode)
  {
    List<Variable> args = new ArrayList<>();

    for (int i = 2; i < functionNode.jjtGetNumChildren() - 1; i++)
    {
      Variable.DataType type = (Variable.DataType) functionNode.jjtGetChild(i).jjtGetChild(0).jjtAccept(this, null);
      String varName = ((Identifier) functionNode.jjtGetChild(i).jjtGetChild(1)).jjtGetValue().toString();
      args.add(new Variable(type, varName));
    }

    return args;
  }

  @Override
  public Object visit(Block node, Object data) {
    if (data instanceof Variable)
    {
      SymbolTable foreach = new SymbolTable("foreach");
      this.table.insert(foreach);
      this.table.insert((Variable) data);
      node.childrenAccept(this, null);
      foreach.close();
    }

    else if (data == null)
    {
      SymbolTable block = new SymbolTable("block");
      this.table.insert(block);
      node.childrenAccept(this, null);
      block.close();
    }

    return null;
  }

  // Inserts arguments of function into symbol table as variables.
  private void insertArguments(List<Variable> args)
  {
    for (Variable var : args)
    {
      this.table.insert(var);
    }
  }

  @Override
  public Object visit(ReturnStatement node, Object data) {
    ((Properties) data).setProperty("Type", node.jjtGetChild(0).jjtAccept(this, data).toString());
    return null;
  }

  @Override
  public Object visit(FunctionBlock node, Object data) {
    for (int i = 0; i < node.jjtGetNumChildren() - 1; i++)
    {
      node.jjtGetChild(i).jjtAccept(this, null);
    }

    return node.jjtGetChild(node.jjtGetNumChildren() - 1).jjtAccept(this, data);
  }

  @Override
  public Object visit(Option node, Object data) {
    if (!this.inStage || (this.inStage && !this.inStageBlock))
      throw new TypeException("Options not allowed outside of second block of a stage.");

    Variable var = null;

    for (int i = 0; i < node.jjtGetNumChildren() - 1; i++)
    {
      if (i == node.jjtGetNumChildren() - 2 && !node.jjtGetChild(i).toString().equals("OptionParameter") &&
              !node.jjtGetChild(i).toString().equals("TextLiteral"))
      {
        if (node.jjtGetChild(i).jjtAccept(this, data) != Variable.DataType.BOOLEAN)
          throw new TypeException("Optional expression of option must be of type Boolean.");
      }

      else if (node.jjtGetChild(i).toString().equals("OrExpr") && node.jjtGetChild(i).jjtAccept(this, data) != Variable.DataType.BOOLEAN)
        throw new TypeException("Optional expression in option must be of type Boolean.");

      else if (node.jjtGetChild(i).toString().equals("OptionParameter"))
        this.table.insert((var = new Variable((Variable.DataType) node.jjtGetChild(i).jjtGetChild(0).jjtAccept(this, data),
                ((Identifier) node.jjtGetChild(i).jjtGetChild(1)).jjtGetValue().toString())));

      else
        node.jjtGetChild(i).jjtAccept(this, data);
    }

    this.table.delete(var);
    SymbolTable optionBlock = new SymbolTable("Option");
    this.table.insert(optionBlock);

    if (var != null)
      this.table.insert(var);

    if (node.jjtGetChild(node.jjtGetNumChildren() - 1).jjtGetNumChildren() != 0)
      node.jjtGetChild(node.jjtGetNumChildren() - 1).jjtGetChild(0).jjtAccept(this, data);

    optionBlock.close();
    return null;
  }

  @Override
  public Object visit(OptionParameter node, Object data) {
    return node.jjtGetChild(0).jjtAccept(this, data);
  }

  @Override
  public Object visit(Statement node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }

  @Override
  public Object visit(AssignmentStatement node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);
    return null;
  }

  @Override
  public Object visit(DeclarationStatement node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }

  @Override
  public Object visit(ExpressionStatement node, Object data) {
    return node.jjtGetChild(0).jjtAccept(this, data);
  }
  
  @Override
  public Object visit(GameStateStatement node, Object data) {
    return node.jjtGetChild(0).jjtAccept(this, data);
  }
  
  @Override
  public Object visit(DoneState node, Object data) {
    if (!this.inStage || (this.inStage && !this.inStageBlock))
      throw new TypeException("done(...) is not allowed outside of second block of a stage.");
    return node.childrenAccept(this, data);
  }
  
  @Override
  public Object visit(NextState node, Object data) {
    if (!this.inStage || (this.inStage && !this.inStageBlock))
      throw new TypeException("done(...) is not allowed outside of second block of a stage.");
    return null;
  }
  
  @Override
  public Object visit(OutState node, Object data) {
    if (!this.inStage || (this.inStage && !this.inStageBlock))
      throw new TypeException("done(...) is not allowed outside of second block of a stage.");
    return node.childrenAccept(this, data);
  }
  
  @Override
  public Object visit(WinState node, Object data) {
    return node.childrenAccept(this, data);
  }
  
  @Override
  public Object visit(SkipState node, Object data) {
    if (!this.inStage || (this.inStage && !this.inStageBlock))
      throw new TypeException("done(...) is not allowed outside of second block of a stage.");
    return node.childrenAccept(this, data);
  }

  @Override
  public Object visit(Declaration node, Object data) {
    if (!this.inSetup && !this.inFunction && !this.inStage)
      return null;

    final String id = ((Identifier) node.jjtGetChild(1)).jjtGetValue().toString();

    if (this.inPlayerDef)
    {
      if (node.jjtGetChild(0).jjtAccept(this, data) == Variable.DataType.SEQUENCE)
        this.table.getPlayerRecord().addSequence(id, new SequenceTypeCheck(leaveType(node.jjtGetChild(0), 0).getScope() / 2, leaveType(node.jjtGetChild(0), 0).getDataType()));

      else
        this.table.getPlayerRecord().addField(id, (Variable.DataType) node.jjtGetChild(0).jjtAccept(this, data));

      ((Identifier) node.jjtGetChild(1)).setType(this.table.getPlayerRecord().getDataType(id));
    }

    else if (node.jjtGetChild(0).jjtAccept(this, data) == Variable.DataType.SEQUENCE)
    {
      SequenceTypeCheck seqType = leaveType(node.jjtGetChild(0), 0);
      seqType = new SequenceTypeCheck(seqType.getScope() / 2, seqType.getDataType());

      if (node.jjtGetNumChildren() >= 3 && node.jjtGetChild(2).jjtAccept(this, data) != Variable.DataType.SEQUENCE)
        throw new TypeException("Declaration type does not match type of assignment for '" + id + "'.");

      else if (node.jjtGetNumChildren() >= 3 && getSequenceElementType(node.jjtGetChild(2)) != null &&
              !getSequenceElementType(node.jjtGetChild(2)).equals(seqType))
        throw new TypeException("Type of sequence '" + id + "' in expression does not match its declaration.");

      this.table.insert(new Sequence(id, seqType));
      ((Identifier) node.jjtGetChild(1)).setType(Variable.DataType.SEQUENCE);

      if (this.inSetup && !this.inSetupScope)
      {
        this.table.get(id).setGlobal(true);
        ((Identifier) node.jjtGetChild(1)).setGlobal(true);
      }

      return null;
    }

    else if (node.jjtGetNumChildren() >= 3 &&
            node.jjtGetChild(0).jjtAccept(this, data) != node.jjtGetChild(2).jjtAccept(this, data))
      throw new TypeException("Type of variable' " + id + "' does not match type of expression.");

    this.table.insert(new Variable((Variable.DataType) node.jjtGetChild(0).jjtAccept(this, data), id));
    ((Identifier) node.jjtGetChild(1)).setType((Variable.DataType) node.jjtGetChild(0).jjtAccept(this, null));
    node.jjtGetChild(1).jjtAccept(this, null);

    return null;
  }

  // Returns type and scope level of elements in first found sequence in expression of declaration.
  private SequenceTypeCheck getSequenceElementType(Node n)
  {
    Properties props = new Properties(2);
    props.setProperty("Scope", String.valueOf(0));
    findId(n, props);

    if (props.getProperty("ID") != null)
    {
      if (this.table.get(props.getProperty("ID")) instanceof Sequence)
        return ((Sequence) this.table.get(props.getProperty("ID"))).getElementType();

      else
        return new SequenceTypeCheck(1, ((Variable) this.table.get(props.getProperty("ID"))).getVarType());
    }

     Node lit = n;
     int scope = 1;

    // For literals.
    while (lit.jjtGetNumChildren() > 0)
    {
      if (lit.toString().equals("TermExpr"))
      {
        if (lit.jjtGetChild(0).toString().equals("SequenceLiteral"))
          lit = lit.jjtGetChild(0);

        else
          lit = lit.jjtGetChild(2);
      }

      else if (lit.jjtGetChild(0).jjtAccept(this, null) == Variable.DataType.SEQUENCE)
      {
        lit = lit.jjtGetChild(0);
        scope++;
      }

      else
        return new SequenceTypeCheck(scope, (Variable.DataType) lit.jjtGetChild(0).jjtAccept(this, null));
    }

    return null;
  }

  // Recursive method setting id to an identifier if it is found in OrExpr.
  private static void findId(Node n, Properties properties)
  {
    if (n instanceof  Identifier)
    {
      properties.setProperty("ID", ((Identifier) n).jjtGetValue().toString());
      return;
    }

    for (int i = 0; i < n.jjtGetNumChildren(); i++)
    {
      findId(n.jjtGetChild(i), properties);
    }
  }

  // Returns type of leave from given node.
  private SequenceTypeCheck leaveType(Node node, int scopeStart)
  {
    if (node.jjtGetNumChildren() == 0)
      return new SequenceTypeCheck(scopeStart, (Variable.DataType) node.jjtAccept(this, null));

    return leaveType(node.jjtGetChild(0), scopeStart + 1);
  }

  @Override
  public Object visit(Assignment node, Object data)
  {
    if (!(node.jjtGetChild(0) instanceof ArrowExpr))
    {
      SymbolStructure var = this.table.get(((Identifier) node.jjtGetChild(0)).jjtGetValue().toString());
      Variable.DataType exprType = (Variable.DataType) node.jjtGetChild(1).jjtAccept(this, data);
      node.jjtGetChild(0).jjtAccept(this, data);

      if (var == null)
        throw new TypeException("Variable '" + ((Identifier) node.jjtGetChild(0)).jjtGetValue().toString() + "' not declared.");

      else if (var instanceof Sequence)
      {
        if (!((Sequence) var).getElementType().equals(largestSequence(node.jjtGetChild(1))))
          throw new TypeException("Assignment of '" + var.getName() + "' not legal because of wrong types.");
      }

      else if (((Variable) var).getVarType() != exprType)
        throw new TypeException(var.getName() + " is not of type " + exprType.toString());

      return null;
    }

    final String id = ((Identifier) node.jjtGetChild(0).jjtGetChild(node.jjtGetChild(0).jjtGetNumChildren() - 1)).jjtGetValue().toString();

    if (node.jjtGetChild(0).jjtAccept(this, data) != node.jjtGetChild(1).jjtAccept(this, data))
      throw new TypeException("Expression does not match type of '" + id + "'.");

    return null;
  }

  // Returns SequenceTypeCheck for biggest sequence in expression.
  private SequenceTypeCheck largestSequence(Node node)
  {
    SequenceTypeCheck result = getSequenceElementType(node);

    for (int i = 2; i < node.jjtGetNumChildren(); i += 2)
    {
      if (getSequenceElementType(node.jjtGetChild(i - 2)) == null && getSequenceElementType(node.jjtGetChild(i)) != null)
      {
        if (getSequenceElementType(node.jjtGetChild(i)).getScope() > result.getScope())
          result = getSequenceElementType(node.jjtGetChild(i));
      }

      else if (getSequenceElementType(node.jjtGetChild(i - 2)) != null && getSequenceElementType(node.jjtGetChild(i)) == null)
      {
        if (getSequenceElementType(node.jjtGetChild(i - 2)).getScope() > result.getScope())
          result = getSequenceElementType(node.jjtGetChild(i - 2));
      }

      else if (getSequenceElementType(node.jjtGetChild(i - 2)).getScope() > getSequenceElementType(node.jjtGetChild(i)).getScope() &&
              getSequenceElementType(node.jjtGetChild(i - 2)).getScope() > result.getScope())
        result = getSequenceElementType(node.jjtGetChild(i - 2));

      else if (getSequenceElementType(node.jjtGetChild(i)).getScope() > result.getScope())
        result = getSequenceElementType(node.jjtGetChild(i));
    }

    return result;
  }

  @Override
  public Object visit(IfStatement node, Object data) {
    if (node.jjtGetChild(0).jjtAccept(this, data) != Variable.DataType.BOOLEAN)
      throw new TypeException("If condition must be boolean.");

    final boolean isAlreadyTrue = this.inSetupScope;

    if (!isAlreadyTrue)
      this.inSetupScope = true;

    node.jjtGetChild(1).jjtAccept(this, data);

    if (node.jjtGetNumChildren() >= 2)
    {
      for (int i = 2; i < node.jjtGetNumChildren(); i++)
      {
        node.jjtGetChild(i).jjtAccept(this, data);
      }
    }

    this.inSetupScope = isAlreadyTrue;
    return null;
  }

  @Override
  public Object visit(ElseIfStatement node, Object data) {
    if (node.jjtGetChild(0).jjtAccept(this, data) != Variable.DataType.BOOLEAN)
      throw new TypeException("Condition is not boolean.");

    final boolean isAlreadyTrue = this.inSetupScope;

    if (!isAlreadyTrue)
      this.inSetupScope = true;

    node.jjtGetChild(1).jjtAccept(this, data);

    this.inSetupScope = isAlreadyTrue;
    return null;
  }

  @Override
  public Object visit(ElseStatement node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }

  @Override
  public Object visit(WhileLoop node, Object data) {
    if (node.jjtGetChild(0).jjtAccept(this, data) != Variable.DataType.BOOLEAN)
      throw new TypeException("Condition is not boolean.");

    final boolean isAlreadyTrue = this.inSetupScope;

    if (!isAlreadyTrue)
      this.inSetupScope = true;

    node.jjtGetChild(1).jjtAccept(this, null);

    this.inSetupScope = isAlreadyTrue;
    return null;
  }

  @Override
  public Object visit(ForEachLoop node, Object data)
  {
    final String collName = ((Identifier) node.jjtGetChild(1)).jjtGetValue().toString();

    if (node.jjtGetChild(2).jjtAccept(this, data) != Variable.DataType.PILE &&
        node.jjtGetChild(2).jjtAccept(this, data) != Variable.DataType.SEQUENCE)
      throw new TypeException("'" + node.jjtGetChild(2).toString() + "' is not a collection.");

    else if (node.jjtGetChild(2).jjtAccept(this, null) == Variable.DataType.SEQUENCE &&
            getSequenceElementType(node.jjtGetChild(0)).getScope() != 1 &&
            getSequenceElementType(node.jjtGetChild(0)).getScope() + 1 != getSequenceElementType(node.jjtGetChild(2)).getScope())
      throw new TypeException("Identifier '" + collName + "' is not of same type as collection.");

    else if (node.jjtGetChild(2).jjtAccept(this, null) == Variable.DataType.SEQUENCE &&
            node.jjtGetChild(0).jjtAccept(this, null) != getSequenceElementType(node.jjtGetChild(2)).getDataType())
      throw new TypeException("'" + collName + "' is of a different type than collection.");

    else if (node.jjtGetChild(2).jjtAccept(this, data) == Variable.DataType.PILE &&
            node.jjtGetChild(0).jjtAccept(this, data) != Variable.DataType.CARD)
      throw new TypeException("'" + ((Identifier) node.jjtGetChild(1)).jjtGetValue().toString() + "' must be of type Card.");

    final boolean isAlreadyTrue = this.inSetupScope;

    if (!isAlreadyTrue)
      this.inSetupScope = true;

    node.jjtGetChild(3).jjtAccept(this, new Variable((Variable.DataType) node.jjtGetChild(0).jjtAccept(this, null),
            ((Identifier) node.jjtGetChild(1)).jjtGetValue().toString()));

    this.inSetupScope = isAlreadyTrue;
    return null;
  }

  @Override
  public Object visit(Modifier node, Object data)
  {
    return null;
  }

  @Override
  public Object visit(MandatoryModifier node, Object data)
  {
    return null;
  }

  @Override
  public Object visit(RepeatModifier node, Object data)
  {
    return null;
  }

  @Override
  public Object visit(Comparator node, Object data)
  {
    return null;
  }

  @Override
  public Object visit(TermOperator node, Object data)
  {
    return null;
  }

  @Override
  public Object visit(FactorOperator node, Object data)
  {
    return null;
  }

  @Override
  public Object visit(CurrentPlayer node, Object data)
  {
    if (!this.inStageBlock)
      throw new TypeException("'CURRENT_PLAYER' can only be referred to within second block of a stage.");

    return Variable.DataType.PLAYER;
  }

  @Override
  public Object visit(Identifier node, Object data) {
    final String id = node.jjtGetValue().toString();

    if (!this.table.exists(id))
      throw new TypeException("'" + id + "' not declared.");

    else if (this.table.get(id) instanceof javacc.util.Function &&
            ((javacc.util.Function) table.get(id)).getReturnType() == javacc.util.Function.ReturnType.ACTION)
      return null;

    else if (this.table.get(id) instanceof javacc.util.Function)
    {
      switch (((javacc.util.Function) this.table.get(id)).getReturnType().toString())
      {
        case "Boolean":
          node.setType(Variable.DataType.BOOLEAN);
          return Variable.DataType.BOOLEAN;

        case "Number":
          node.setType(Variable.DataType.NUMBER);
          return Variable.DataType.NUMBER;

        case "DefinePlayer":
          node.setType(Variable.DataType.PLAYER);
          return Variable.DataType.PLAYER;

        case "Card":
          node.setType(Variable.DataType.CARD);
          return Variable.DataType.CARD;

        case "Suit":
          node.setType(Variable.DataType.SUIT);
          return Variable.DataType.SUIT;

        case "Rank":
          node.setType(Variable.DataType.RANK);
          return Variable.DataType.RANK;

        case "Sequence":
          node.setType(Variable.DataType.SEQUENCE);
          return Variable.DataType.SEQUENCE;

        case "Pile":
          node.setType(Variable.DataType.PILE);
          return Variable.DataType.PILE;

        case "Text":
          node.setType(Variable.DataType.TEXT);
          return Variable.DataType.TEXT;

        default:
          return null;
      }
    }

    if (this.inSetup && !this.inSetupScope)
    {
      this.table.get(id).setGlobal(true);
      node.setGlobal(true);
    }

    else if (this.table.get(id).isGlobal() && !this.inSetupScope)
      node.setGlobal(true);

    return ((Variable) this.table.get(id)).getVarType();
  }

  @Override
  public Object visit(Parameter node, Object data) {
    node.childrenAccept(this, null);
    return null;
  }

  @Override
  public Object visit(Type node, Object data) {
    return node.jjtGetChild(0).jjtAccept(this, data);
  }

  @Override
  public Object visit(PlayerType node, Object data) {
    return Variable.DataType.PLAYER;
  }

  @Override
  public Object visit(NumberType node, Object data)
  {
    return Variable.DataType.NUMBER;
  }

  @Override
  public Object visit(CardType node, Object data) {
    return Variable.DataType.CARD;
  }

  @Override
  public Object visit(RankType node, Object data) {
    return Variable.DataType.RANK;
  }

  @Override
  public Object visit(SuitType node, Object data) {
    return Variable.DataType.SUIT;
  }

  @Override
  public Object visit(SequenceType node, Object data) {
    return Variable.DataType.SEQUENCE;
  }

  @Override
  public Object visit(PileType node, Object data) {
    return Variable.DataType.PILE;
  }

  @Override
  public Object visit(SequenceLiteral node, Object data) {
    for (int i = 1; i < node.jjtGetNumChildren(); i++)
    {
      if (node.jjtGetChild(i - 1).jjtAccept(this, data) != node.jjtGetChild(i).jjtAccept(this, data))
        throw new TypeException("Types of elements in sequence literal do not match.");
    }

    return Variable.DataType.SEQUENCE;
  }

  @Override
  public Object visit(BooleanType node, Object data) {
    return Variable.DataType.BOOLEAN;
  }

  @Override
  public Object visit(RangeLiteral node, Object data) {
    if ((Integer) ((NumberLiteral) node.jjtGetChild(0)).jjtGetValue() < 1 ||
            (Integer) ((NumberLiteral) node.jjtGetChild(1)).jjtGetValue() < 1)
      throw new TypeException("Negative range literals now allowed.");

    else if ((Integer) ((NumberLiteral) node.jjtGetChild(0)).jjtGetValue() >=
            (Integer) ((NumberLiteral) node.jjtGetChild(1)).jjtGetValue())
      throw new TypeException("Second value in range literal must be greater than first value.");

    return null;
  }

  @Override
  public Object visit(Call node, Object data) {
    if (node.jjtGetNumChildren() > 1)
    {
      SymbolStructure f = this.table.get(((Identifier) node.jjtGetChild(0)).jjtGetValue().toString());

      if (!(f instanceof javacc.util.Function) || !this.table.exists(f.getName()))
        throw new TypeException("Function '" + ((Identifier) node.jjtGetChild(0)).jjtGetValue().toString() +
                "' is not declared.");

      else if (((javacc.util.Function) f).getParameters().size() != node.jjtGetNumChildren() - 1)
        throw new TypeException("Amount of arguments in function call of '" + f.getName() + "' does not match the amount of arguments in its declaration.");

      for (int i = 0; i < ((javacc.util.Function) f).getParameters().size(); i++)
      {
        if (((javacc.util.Function) f).getParameters().get(i).getVarType() != node.jjtGetChild(i + 1).jjtAccept(this, data))
          throw new TypeException("Argument " + (i + 1) + " in '" + f.getName() + "' expected to be of type " +
                  ((javacc.util.Function) f).getParameters().get(i).getVarType().toString() + ".");
      }
    }

    return node.jjtGetChild(0).jjtAccept(this, data);
  }

  @Override
  public Object visit(Literal node, Object data) {
    return node.jjtGetChild(0).jjtAccept(this, data);
  }

  @Override
  public Object visit(TextLiteral node, Object data) {
    return Variable.DataType.TEXT;
  }

  @Override
  public Object visit(RankLiteral node, Object data) {
    return Variable.DataType.RANK;
  }

  @Override
  public Object visit(CardLiteral node, Object data) {
    return Variable.DataType.CARD;
  }

  @Override
  public Object visit(BooleanLiteral node, Object data) {
    return Variable.DataType.BOOLEAN;
  }

  @Override
  public Object visit(SuitLiteral node, Object data) {
    return Variable.DataType.SUIT;
  }

  @Override
  public Object visit(NumberLiteral node, Object data) {
    return Variable.DataType.NUMBER;
  }

  @Override
  public Object visit(Setup node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }

  @Override
  public Object visit(Stage node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }

  @Override
  public Object visit(Prog node, Object data) {
    this.table.insert(new Variable(Variable.DataType.PLAYER, "CURRENT_PLAYER"));
    this.table.insert(new Variable(Variable.DataType.PLAYER, "DefinePlayer"));

    // Adding function signatures to symbol table.
    for (int i = 0; i < node.jjtGetNumChildren(); i++)
    {
      if (node.jjtGetChild(i).toString().equals("Function"))
        node.jjtGetChild(i).jjtAccept(this, data);
    }

    this.onlyFunctionSignature = false;
    this.inSetup = true;
    node.jjtGetChild(0).jjtAccept(this, data);
    this.inSetup = false;
    this.inFunction = true;

    for (int i = 0; i < node.jjtGetNumChildren(); i++)
    {
      if (node.jjtGetChild(i).toString().equals("Function"))
        node.jjtGetChild(i).jjtAccept(this, data);
    }
    this.inFunction = false;

    this.inStage = true;
    typeCheckStages(node, data);

    if (this.definePlayersVisitedAmount > 1)
      throw new TypeException("'DefinePlayer' can not be defined more than once.");

    this.table.close();
    return null;
  }

  // Type checks stages given a Prog node.
  private void typeCheckStages(Prog node, Object data)
  {
    // Inserting stage identifiers.
    for (int i = 0; i < node.jjtGetNumChildren(); i++)
    {
      if (node.jjtGetChild(i).toString().equals("Stage"))
        this.table.insert(new Variable(null, ((Identifier) node.jjtGetChild(i).jjtGetChild(0)).jjtGetValue().toString()));
    }

    if (!this.playerCountVisited)
      throw new TypeException("Amount of players have not been specified.");

    else if (!this.pilesDefVisited)
      throw new TypeException("Amount of piles have not been specified.");

    // Type checking stages.
    for (int i = 0; i < node.jjtGetNumChildren(); i++)
    {
      if (node.jjtGetChild(i).toString().equals("Stage"))
      {
        if (!this.table.exists(((Identifier) node.jjtGetChild(i).jjtGetChild(1)).jjtGetValue().toString()))
          throw new TypeException("'" + ((Identifier) node.jjtGetChild(i).jjtGetChild(1)).jjtGetValue().toString() + "' stage is not declared.");

        acceptStageBlocks(node.jjtGetChild(i), data);
      }
    }
  }

  // Type checking method for both stage block.
  private void acceptStageBlocks(Node n, Object data)
  {
    SymbolTable stageScope = new SymbolTable(((Identifier) n.jjtGetChild(0)).jjtGetValue().toString());
    SymbolTable block = new SymbolTable(((Identifier) n.jjtGetChild(0)).jjtGetValue().toString() + "_2");
    this.table.insert(stageScope);
    ((Block) n.jjtGetChild(2)).childrenAccept(this, data);
    stageScope.close();
    this.table.insert(block);
    this.inStageBlock = true;
    ((Block) n.jjtGetChild(3)).childrenAccept(this, data);
    this.inStageBlock = false;
    block.close();
  }
}