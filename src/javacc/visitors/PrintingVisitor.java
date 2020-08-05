package javacc.visitors;

import javacc.util.Operator;
import javacc.generated.*;

import java.util.List;

public class PrintingVisitor implements FlipParserVisitor {
  private int indentation = 0;
  private boolean startOfLine = true;
  
  @Override
  public Object visit(SetupBlock node, Object data) {
    return null;
  }
  
  @Override
  public Object visit(PlayerCount node, Object data) {
    return null;
  }
  
  @Override
  public Object visit(RangeLiteral node, Object data) {
    return null;
  }
  
  @Override
  public Object visit(PlayerDef node, Object data) {
    return null;
  }
  
  @Override
  public Object visit(Declaration node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);
    print(" ");
    node.jjtGetChild(1).jjtAccept(this, data);
    
    if (node.jjtGetNumChildren() == 3) {
      print(" " + Operator.ASSIGN.getSymbol() + " ");
      node.jjtGetChild(2).jjtAccept(this, data);
    }
    return null;
  }
  
  @Override
  public Object visit(Assignment node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);
    print(" " + Operator.ASSIGN.getSymbol() + " ");
    node.jjtGetChild(1).jjtAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(OrExpr node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);
    
    // Has at least one or.
    for (int i = 1; i < node.jjtGetNumChildren(); i += 1) {
      print(" " + Operator.OR.getSymbol() + " ");
      node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    return null;
  }
  
  @Override
  public Object visit(AndExpr node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);
    
    // Has at least one and.
    for (int i = 1; i < node.jjtGetNumChildren(); i += 1) {
      print(" " + Operator.AND.getSymbol() + " ");
      node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    return null;
  }
  
  @Override
  public Object visit(NotExpr node, Object data) {
    // Has a not.
    if (node.jjtGetValue() != null) {
      print(Operator.NOT.getSymbol() + " ");
    }
    
    node.jjtGetChild(0).jjtAccept(this, data);
    
    return null;
  }
  
  @Override
  public Object visit(EqualsExpr node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);
    
    // At least one equals.
    for (int i = 1; i < node.jjtGetNumChildren(); i += 1) {
      print(" " + Operator.EQUALS.getSymbol() + " ");
      node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    return null;
  }
  
  @Override
  public Object visit(CompareExpr node, Object data) {
    // Just accept children, since comparator is its own node.
    node.childrenAccept(this, data);
    
    return null;
  }
  
  @Override
  public Object visit(TermExpr node, Object data) {
    // Just accept children, since term operator is its own node.
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(FactorExpr node, Object data) {
    // Just accept children, since factor operator is its own node.
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(NegativeExpr node, Object data) {
    if (node.jjtGetValue() != null) {
      print(Operator.NEGATIVE.getSymbol());
    }
    
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(ArrowExpr node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);
    
    // Has at least one xor.
    for (int i = 1; i < node.jjtGetNumChildren(); i += 1) {
      print(" " + Operator.PROPERTY.getSymbol() + " ");
      node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    return null;
  }
  
  @Override
  public Object visit(ValueExpr node, Object data) {
    // If child is an OrExpr then add parentheses.
    if (node.jjtGetChild(0) instanceof OrExpr) {
      print("(");
      node.childrenAccept(this, data);
      print(")");
    } else {
      node.childrenAccept(this, data);
    }
  
    return null;
  }
  
  @Override
  public Object visit(ReturnStatement node, Object data) {
    print("return ");
    node.childrenAccept(this, data);
    printLine(";");
    return null;
  }
  
  @Override
  public Object visit(FunctionBlock node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(GameStateStatement node, Object data) {
    node.childrenAccept(this, data);
    printLine(";");
    return null;
  }
  
  @Override
  public Object visit(DoneState node, Object data) {
    print("done(");
    node.childrenAccept(this, data);
    print(")");
    return null;
  }
  
  @Override
  public Object visit(NextState node, Object data) {
    print("next()");
    return null;
  }
  
  @Override
  public Object visit(OutState node, Object data) {
    print("out(");
    node.childrenAccept(this, data);
    print(")");
    return null;
  }
  
  @Override
  public Object visit(WinState node, Object data) {
    print("win(");
    node.childrenAccept(this, data);
    print(")");
    return null;
  }
  
  @Override
  public Object visit(SkipState node, Object data) {
    print("skip(");
    node.childrenAccept(this, data);
    print(")");
    return null;
  }
  
  @Override
  public Object visit(Function node, Object data) {
    printLine("");
    node.jjtGetChild(0).jjtAccept(this, data);
    
    print(" ");
    node.jjtGetChild(1).jjtAccept(this, data);
    
    print("(");
    
    int i = 2;
    
    if (node.jjtGetNumChildren() > 4) {
      while (node.jjtGetChild(i) instanceof Parameter) {
        node.jjtGetChild(i).jjtAccept(this, data);
        i++;
      }
    }
    
    printLine(") {");
    indent();
    
    node.jjtGetChild(i).jjtAccept(this, data);
    
    unindent();
    printLine("}");
    return null;
  }
  
  @Override
  public Object visit(ActionReturnType node, Object data) {
    print("Action");
    return null;
  }
  
  @Override
  public Object visit(Block node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(PlayerCall node, Object data) {
    print("play(");
    
    node.jjtGetChild(0).jjtAccept(this, data);
    
    print(")");
    
    return null;
  }
  
  @Override
  public Object visit(PileDef node, Object data) {
    print("DefinePiles := ");
    node.jjtGetChild(0).jjtAccept(this, data);
    print(";");
    return null;
  }
  
  @Override
  public Object visit(PileCall node, Object data) {
    return null;
  }
  
  @Override
  public Object visit(CurrentPlayer node, Object data) {
    print("CURRENT_PLAYER");
    return null;
  }
  
  @Override
  public Object visit(PlayCall node, Object data) {
    print("play(");
    
    node.jjtGetChild(0).jjtAccept(this, data);
    print(", ");
    
    node.jjtGetChild(1).jjtAccept(this, data);
    
    print(")");
    
    return null;
  }
  
  @Override
  public Object visit(MoveCall node, Object data) {
    print("move(");
    
    node.jjtGetChild(0).jjtAccept(this, data);
    print(", ");
    node.jjtGetChild(1).jjtAccept(this, data);
    print(", ");
    node.jjtGetChild(2).jjtAccept(this, data);
    
    print(")");
    
    return null;
  }
  
  
  @Override
  public Object visit(DealCall node, Object data) {
    print("deal(");
    
    node.jjtGetChild(0).jjtAccept(this, data);
    print(", ");
    
    node.jjtGetChild(1).jjtAccept(this, data);
    
    print(")");
    return null;
  }
  
  @Override
  public Object visit(Statement node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(AssignmentStatement node, Object data) {
    node.childrenAccept(this, data);
    printLine(";");
    return null;
  }
  
  @Override
  public Object visit(DeclarationStatement node, Object data) {
    node.childrenAccept(this, data);
    printLine(";");
    return null;
  }
  
  @Override
  public Object visit(ExpressionStatement node, Object data) {
    node.childrenAccept(this, data);
    printLine(";");
    return null;
  }
  
  @Override
  public Object visit(IfStatement node, Object data) {
    print("if (");
    // Accept "if" expression.
    node.jjtGetChild(0).jjtAccept(this, data);
    printLine(") {");
    indent();
    
    // Accept "if" body.
    node.jjtGetChild(1).jjtAccept(this, data);
    
    unindent();
    
    if (node.jjtGetNumChildren() == 2) {
      printLine("}");
      return null;
    }
    
    print("} ");
    
    // Has "else if" and or "else".
    for (int i = 2; i < node.jjtGetNumChildren(); i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    return null;
  }
  
  @Override
  public Object visit(ElseIfStatement node, Object data) {
    print("else if (");
    node.jjtGetChild(0).jjtAccept(this, data);
    printLine(") {");
    indent();
    
    // Block
    node.jjtGetChild(1).jjtAccept(this, data);
    
    unindent();
    printLine("}");
    
    return null;
  }
  
  @Override
  public Object visit(ElseStatement node, Object data) {
    printLine("else {");
    indent();
    
    node.jjtGetChild(0).jjtAccept(this, data);
    
    unindent();
    printLine("}");
    
    return null;
  }
  
  @Override
  public Object visit(WhileLoop node, Object data) {
    print("while (");
    node.jjtGetChild(0).jjtAccept(this, data);
    
    printLine(") {");
    indent();
    node.jjtGetChild(1).jjtAccept(this, data);
    
    unindent();
    printLine("}");
    
    return null;
  }
  
  @Override
  public Object visit(ForEachLoop node, Object data) {
    print("for (");
    
    node.jjtGetChild(0).jjtAccept(this, data);
    node.jjtGetChild(1).jjtAccept(this, data);
    
    print(" in ");
    
    node.jjtGetChild(2).jjtAccept(this, data);
    
    printLine(") {");
    
    indent();
    node.jjtGetChild(3).jjtAccept(this, data);
    unindent();
    
    printLine("}");
    
    return null;
  }
  
  @Override
  public Object visit(Call node, Object data) {
    if (node.jjtGetChild(0) instanceof DealCall || node.jjtGetChild(0) instanceof PlayCall) {
      node.childrenAccept(this, data);
      return null;
    }
    
    node.jjtGetChild(0).jjtAccept(this, data);
    
    print("(");
    for (int i = 1; i < node.jjtGetNumChildren(); i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
    }
    print(")");
    
    return null;
  }
  
  @Override
  public Object visit(Type node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(PlayerType node, Object data) {
    print("Player");
    return null;
  }
  
  @Override
  public Object visit(NumberType node, Object data) {
    print("Number");
    return null;
  }
  
  @Override
  public Object visit(CardType node, Object data) {
    print("Card");
    return null;
  }
  
  @Override
  public Object visit(RankType node, Object data) {
    print("Rank");
    return null;
  }
  
  @Override
  public Object visit(SuitType node, Object data) {
    print("Suit");
    return null;
  }
  
  @Override
  public Object visit(SequenceType node, Object data) {
    print("Sequence<");
    node.childrenAccept(this, data);
    print(">");
    return null;
  }
  
  @Override
  public Object visit(PileType node, Object data) {
    print("Pile");
    return null;
  }
  
  @Override
  public Object visit(BooleanType node, Object data) {
    print("Boolean");
    return null;
  }
  
  @Override
  public Object visit(TextType node, Object data) {
    print("Text");
    return null;
  }
  
  @Override
  public Object visit(Literal node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(RankLiteral node, Object data) {
    print(node.jjtGetValue().toString());
    return null;
  }
  
  @Override
  public Object visit(BooleanLiteral node, Object data) {
    print(node.jjtGetValue().toString());
    return null;
  }
  
  @Override
  public Object visit(SequenceLiteral node, Object data) {
    print("{");
    
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
      
      if (i < node.jjtGetNumChildren() - 1) {
        print(", ");
      }
    }
    print("}");
    
    return null;
  }
  
  @Override
  public Object visit(SimpleNode node, Object data) {
    node.childrenAccept(this, node);
    return null;
  }
  
  @Override
  public Object visit(OptionParameter node, Object data) {
    return null;
  }
  
  @Override
  public Object visit(Option node, Object data) {
    int i = 0;
    
    if (node.jjtGetChild(0) instanceof Modifier) {
      node.jjtGetChild(0).jjtAccept(this, data);
      
      print(" ");
      i++;
    }
    
    print("Option(");
    
    while (node.jjtGetChild(i) instanceof Parameter) {
      node.jjtGetChild(i).jjtAccept(this, data);
      
      if (node.jjtGetChild(i + 1) instanceof Parameter) {
        print(", ");
      }
      
      i++;
    }
    
    print(") ");
    
    if (node.jjtGetChild(i) instanceof OrExpr) {
      print(": ");
      node.jjtGetChild(i).jjtAccept(this, data);
      i++;
    }
    
    printLine(" {");
    indent();
    node.jjtGetChild(i).jjtAccept(this, data);
    unindent();
    printLine("}");
    
    return null;
  }
  
  @Override
  public Object visit(Modifier node, Object data) {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
      
      if (i < node.jjtGetNumChildren() - 1) {
        print(" ");
      }
    }
    return null;
  }
  
  @Override
  public Object visit(MandatoryModifier node, Object data) {
    print("mandatory");
    return null;
  }
  
  @Override
  public Object visit(RepeatModifier node, Object data) {
    print("<");
    node.childrenAccept(this, data);
    print(">");
    return null;
  }
  
  @Override
  public Object visit(Comparator node, Object data) {
    Operator c = (Operator) node.jjtGetValue();
    print(" " + c.getSymbol() + " ");
    return null;
  }
  
  @Override
  public Object visit(TermOperator node, Object data) {
    Operator c = (Operator) node.jjtGetValue();
    print(" " + c.getSymbol() + " ");
    return null;
  }
  
  @Override
  public Object visit(FactorOperator node, Object data) {
    Operator c = (Operator) node.jjtGetValue();
    print(" " + c.getSymbol() + " ");
    return null;
  }
  
  @Override
  public Object visit(Identifier node, Object data) {
    print(node.jjtGetValue().toString());
    return null;
  }
  
  @Override
  public Object visit(Parameter node, Object data) {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
    }
    return null;
  }
  
  @Override
  public Object visit(CardLiteral node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);
    print(" of ");
    node.jjtGetChild(1).jjtAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(SuitLiteral node, Object data) {
    print(node.jjtGetValue().toString());
    return null;
  }
  
  @Override
  public Object visit(TextLiteral node, Object data) {
    print("\"" + node.jjtGetValue() + "\"");
    return null;
  }
  
  @Override
  public Object visit(NumberLiteral node, Object data) {
    print(node.jjtGetValue().toString());
    return null;
  }
  
  @Override
  public Object visit(Setup node, Object data) {
    printLine("Setup {");
    indent();
    
    node.childrenAccept(this, data);
    
    unindent();
    printLine("}");
    return null;
  }
  
  @Override
  public Object visit(Stage node, Object data) {
    printLine("");
    print("Stage ");
    node.jjtGetChild(0).jjtAccept(this, data);
    print(" to ");
    node.jjtGetChild(1).jjtAccept(this, data);
    printLine("");
    
    for (int i = 2; i < node.jjtGetNumChildren(); i++) {
      printLine("{");
      indent();
      node.jjtGetChild(i).jjtAccept(this, data);
      unindent();
      printLine("}");
    }
    
    return null;
  }
  
  @Override
  public Object visit(Prog node, Object data) {
    printLine("/* ---- This is a program written in FLIP ----*/\n");
    node.childrenAccept(this, data);
    return null;
  }
  
  private void printInline(String elem) {
    System.out.print(elem);
  }
  
  private void print(String elem) {
    if (startOfLine) {
      System.out.print(
              new String(new char[indentation]).replace("\0", "  ")
                      + elem
      );
      startOfLine = false;
    } else {
      printInline(elem);
    }
  }
  
  private void printLine(String elem) {
    print(elem);
    System.out.print('\n');
    startOfLine = true;
  }
  
  private void indent() {
    indentation++;
  }
  
  private void unindent() {
    indentation--;
  }
}
