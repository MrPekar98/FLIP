package javacc.visitors.codegeneration;

import javacc.util.Operator;
import javacc.generated.*;
import javacc.visitors.codegeneration.templates.css.StylesCss;
import javacc.visitors.codegeneration.templates.html.MainHtml;
import javacc.visitors.codegeneration.templates.js.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


// TODO: Fix indentation when saving to string.

public class CodeGenerationVisitor implements FlipParserVisitor {
  private int indentation = 0;
  private boolean startOfLine = true;
  
  List<JsStage> stages = new ArrayList<>();
  List<JsFunction> functions = new ArrayList<>();
  
  PlayerJs playerJs = new PlayerJs();
  GameJs gameJs = new GameJs();
  int optionCount = 0;
  int minPlayerCount;
  int maxPlayerCount;
  
  @Override
  public Object visit(SetupBlock node, Object data) {
    String result = "";
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      result += node.jjtGetChild(i).jjtAccept(this, data).toString();
    }
    
    return result;
  }
  
  @Override
  public Object visit(PlayerCount node, Object data) {
    RangeLiteral rangeNode = (RangeLiteral) node.jjtGetChild(0);
    minPlayerCount = Integer.valueOf(((SimpleNode) rangeNode.jjtGetChild(0)).jjtGetValue().toString());
    maxPlayerCount = Integer.valueOf(((SimpleNode) rangeNode.jjtGetChild(1)).jjtGetValue().toString());
    
    return "";
  }
  
  
  @Override
  public Object visit(PlayerDef node, Object data) {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node declaration = node.jjtGetChild(i).jjtGetChild(0);
//      String type = declaration.jjtGetChild(0).jjtAccept(this, false).toString();
      String id = ((SimpleNode) declaration.jjtGetChild(1)).jjtGetValue().toString();
      
      if (declaration.jjtGetNumChildren() == 3) {
        String literal = declaration.jjtGetChild(2).jjtAccept(this, false).toString();
        playerJs.addField(id, literal);
      } else {
        if (declaration.jjtGetChild(0).jjtGetChild(0) instanceof PileType) {
          playerJs.usePileImport();
          playerJs.addField(id, "new Pile(true, `Player ${index + 1}'s " + id + "`, \"" + id + "\")");
        } else {
          playerJs.addField(id);
        }
      }
    }
    
    return "";
  }
  
  @Override
  public Object visit(Declaration node, Object data) {
    Type type = (Type) node.jjtGetChild(0);
    Identifier id = (Identifier) node.jjtGetChild(1);
    
    String result = printString("");
    
    if (!id.isGlobal()) {
      result += type.jjtAccept(this, data) + " ";
    }
    
    result += node.jjtGetChild(1).jjtAccept(this, data);
    
    if (node.jjtGetNumChildren() == 3) {
      result += " = " + node.jjtGetChild(2).jjtAccept(this, data);
    }
    
    return result;
  }
  
  @Override
  public Object visit(Assignment node, Object data) {
    String result = printString(node.jjtGetChild(0).jjtAccept(this, data)
            + " = "
            + node.jjtGetChild(1).jjtAccept(this, data));
    
    return result;
  }
  
  @Override
  public Object visit(OrExpr node, Object data) {
    String result = printString("(" + node.jjtGetChild(0).jjtAccept(this, data));
    
    for (int i = 1; i < node.jjtGetNumChildren(); i += 1) {
      result += " || " + node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    result += ")";
    
    return result;
  }
  
  @Override
  public Object visit(AndExpr node, Object data) {
    String result = printString("(" + node.jjtGetChild(0).jjtAccept(this, data));
    
    for (int i = 1; i < node.jjtGetNumChildren(); i += 1) {
      result += " && " + node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    result += ")";
    
    return result;
  }
  
  @Override
  public Object visit(NotExpr node, Object data) {
    String result = printString("(");
    
    if (node.jjtGetValue() != null) {
      result += "!";
    }
    
    result += node.jjtGetChild(0).jjtAccept(this, data) + ")";
    
    return result;
  }
  
  @Override
  public Object visit(EqualsExpr node, Object data) {
    String result = printString("(" + node.jjtGetChild(0).jjtAccept(this, data));

    result += " == " + node.jjtGetChild(1).jjtAccept(this, data);

    result += ")";
    
    return result;
  }
  
  @Override
  public Object visit(CompareExpr node, Object data) {
    boolean isContains = ((SimpleNode) node.jjtGetChild(1)).jjtGetValue() == Operator.CONTAINS;
    
    String result = printString("(");
    
    if (isContains) {
      result += "Utils.contains(" + node.jjtGetChild(0).jjtAccept(this, data)
              + ", " + node.jjtGetChild(2).jjtAccept(this, data) + "))";
      return result;
    } else {
      
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
        result += node.jjtGetChild(i).jjtAccept(this, data);
      }
      
      result += ")";
      
      return result;
    }
  }
  
  @Override
  public Object visit(TermExpr node, Object data) {
    Node leftOperand = node.jjtGetChild(0);
    Node rightOperand = node.jjtGetChild(2);
    Operator operator = (Operator) ((SimpleNode) node.jjtGetChild(1)).jjtGetValue();
    
    String result = "(";
    
    if (node.isSequence()) {
      String methodName = "Utils.sequenceComplement";
      
      if (operator == Operator.ADDITION) {
        methodName = "Utils.sequenceUnion";
      }
      
      result += printString(methodName + "(")
              + leftOperand.jjtAccept(this, false).toString()
              + (", ")
              + rightOperand.jjtAccept(this, false).toString()
              + ")";
    } else {
      // Just accept. Other types have normal addition/subtraction.
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
        result += printString(node.jjtGetChild(i).jjtAccept(this, data).toString());
      }
    }
    
    result += printString(")");
    return result;
  }
  
  @Override
  public Object visit(FactorExpr node, Object data) {
    String result = printString("(");
    
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      result += node.jjtGetChild(i).jjtAccept(this, data);
    }
    result += ")";
    
    return result;
  }
  
  @Override
  public Object visit(NegativeExpr node, Object data) {
    String result = printString("(");
    
    // Has a "-"
    if (node.jjtGetValue() != null) {
      result += "-";
    }
    
    result += node.jjtGetChild(0).jjtAccept(this, data) + ")";
    
    return result;
  }
  
  @Override
  public Object visit(ArrowExpr node, Object data) {
    String result = printString("(" + node.jjtGetChild(0).jjtAccept(this, data));
    
    for (int i = 1; i < node.jjtGetNumChildren(); i++) {
      Identifier lastChild = (Identifier) node.jjtGetChild(node.jjtGetNumChildren() - 1);
      if (lastChild.isSize()) {
        result += ".cards.length";
      } else {
        result += "." + node.jjtGetChild(i).jjtAccept(this, data);
      }
    }
    
    result += ")";
    
    return result;
  }
  
  @Override
  public Object visit(ValueExpr node, Object data) {
    String result = printString("(");
    
    // If child is an OrExpr then add parentheses.
    if (node.jjtGetChild(0) instanceof OrExpr) {
      result += "(" + node.jjtGetChild(0).jjtAccept(this, data) + ")";
    } else {
      result += node.jjtGetChild(0).jjtAccept(this, data);
    }
    
    return result;
  }
  
  @Override
  public Object visit(ReturnStatement node, Object data) {
    String result = printString("return ")
            + node.jjtGetChild(0).jjtAccept(this, data)
            + printLineString(";");
    
    return result;
  }
  
  @Override
  public Object visit(FunctionBlock node, Object data) {
    String result = "";
    
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      result += node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    return result;
  }
  
  @Override
  public Object visit(GameStateStatement node, Object data) {
    String result = printLineString(node.jjtGetChild(0).jjtAccept(this, data).toString() + ";");
    result += printLineString("return;");
    return result;
  }
  
  @Override
  public Object visit(DoneState node, Object data) {
    String argument = "this.$players$[$currentPlayer$]";
    
    if (node.jjtGetNumChildren() > 0) {
      argument = node.jjtGetChild(0).jjtAccept(this, false).toString();
    }
    
    return printString(argument + ".state = State.DONE");
  }
  
  @Override
  public Object visit(NextState node, Object data) {
    return printString("this.$players$[$currentPlayer$].state = State.NEXT");
  }
  
  @Override
  public Object visit(OutState node, Object data) {
    String argument = "this.$players$[$currentPlayer$]";
    
    if (node.jjtGetNumChildren() > 0) {
      argument = node.jjtGetChild(0).jjtAccept(this, false).toString();
    }
    
    return printString(argument + ".state = State.OUT");
  }
  
  @Override
  public Object visit(WinState node, Object data) {
    String argument = "this.$players$[$currentPlayer$]";
    
    if (node.jjtGetNumChildren() > 0) {
      argument = node.jjtGetChild(0).jjtAccept(this, false).toString();
    }
    
    return printString("this.$winner$ = " + argument);
  }
  
  @Override
  public Object visit(SkipState node, Object data) {
    String argument = "this.$players$[$currentPlayer$]";
    
    if (node.jjtGetNumChildren() > 0) {
      argument = node.jjtGetChild(0).jjtAccept(this, false).toString();
    }
    return printString(argument + ".state = State.SKIP");
  }
  
  @Override
  public Object visit(Function node, Object data) {
    JsFunction function = new JsFunction();
    
    String name = node.jjtGetChild(1).jjtAccept(this, false).toString();
    function.setName(name);
    
    int i = 2;
    Node nextChild = node.jjtGetChild(i);
    
    while (nextChild instanceof Parameter) {
      String param = node.jjtGetChild(i).jjtGetChild(1).jjtAccept(this, false).toString();
      function.addParameter(param);
      nextChild = node.jjtGetChild(++i);
    }
    
    String block = nextChild.jjtAccept(this, false).toString();
    function.setContent(block);
    
    functions.add(function);
    
    return null;
  }
  
  @Override
  public Object visit(ActionReturnType node, Object data) {
    // Return types are not specified in JavaScript.
    return null;
  }
  
  @Override
  public Object visit(Block node, Object data) {
    String result = printString("");
    
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      result += node.jjtGetChild(i).jjtAccept(this, data).toString();
    }
    
    return result;
  }
  
  @Override
  public Object visit(PlayerCall node, Object data) {
    return printString("this.$players$[" + node.jjtGetChild(0).jjtAccept(this, data) + "]");
  }
  
  @Override
  public Object visit(PileDef node, Object data) {
    int pileCount = Integer.valueOf(((SimpleNode) node.jjtGetChild(0)).jjtGetValue().toString());
    
    gameJs.setPileCount(pileCount);
    return "";
  }
  
  @Override
  public Object visit(PileCall node, Object data) {
    return printString("this.$piles$[" + node.jjtGetChild(0).jjtAccept(this, data) + "]");
  }
  
  @Override
  public Object visit(CurrentPlayer node, Object data) {
    return printString("this.$players$[$currentPlayer$]");
  }
  
  @Override
  public Object visit(PlayCall node, Object data) {
    String result = printString("Utils.play(this.$players$[$currentPlayer$].hand, ")
            + node.jjtGetChild(0).jjtAccept(this, data)
            + ", "
            + node.jjtGetChild(1).jjtAccept(this, data)
            + ")";
    
    return result;
  }
  
  @Override
  public Object visit(MoveCall node, Object data) {
    String result = printString("Utils.move(")
            + node.jjtGetChild(0).jjtAccept(this, data)
            + ", "
            + node.jjtGetChild(1).jjtAccept(this, data)
            + ", "
            + node.jjtGetChild(2).jjtAccept(this, data)
            + ")";
    
    return result;
  }
  
  @Override
  public Object visit(DealCall node, Object data) {
    String result = printString("Utils.deal(this.$deck$, ")
            + node.jjtGetChild(0).jjtAccept(this, data)
            + ", "
            + node.jjtGetChild(1).jjtAccept(this, data)
            + ")";
    
    return result;
  }
  
  @Override
  public Object visit(Statement node, Object data) {
    return node.jjtGetChild(0).jjtAccept(this, data).toString();
  }
  
  @Override
  public Object visit(AssignmentStatement node, Object data) {
    String result = printString("");
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      result += node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    result += printLineString(";");
    
    return result;
  }
  
  @Override
  public Object visit(DeclarationStatement node, Object data) {
    String result = printString("");
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      result += node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    result += printLineString(";");
    
    return result;
  }
  
  @Override
  public Object visit(ExpressionStatement node, Object data) {
    return printLineString(node.jjtGetChild(0).jjtAccept(this, data) + ";");
  }
  
  @Override
  public Object visit(IfStatement node, Object data) {
    String result = printLineString("if (" + node.jjtGetChild(0).jjtAccept(this, data) + ") {");
    indent();
    
    // Accept "if" body.
    result += printString(node.jjtGetChild(1).jjtAccept(this, data).toString());
    
    unindent();
    
    if (node.jjtGetNumChildren() == 2) {
      result += printLineString("}");
      return result;
    }
    
    result += printString("} ");
    
    // Has "else if" and or "else".
    for (int i = 2; i < node.jjtGetNumChildren(); i++) {
      result += node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    return result;
  }
  
  @Override
  public Object visit(ElseIfStatement node, Object data) {
    String result = printLineString("else if (" + node.jjtGetChild(0).jjtAccept(this, data) + ") {");
    indent();
    
    // Block
    result += printString(node.jjtGetChild(1).jjtAccept(this, data).toString());
    
    unindent();
    result += printLineString("}");
    
    return result;
  }
  
  @Override
  public Object visit(ElseStatement node, Object data) {
    String result = printLineString("else {");
    indent();
    
    result += printString(node.jjtGetChild(0).jjtAccept(this, data).toString());
    
    unindent();
    result += printLineString("}");
    
    return result;
  }
  
  @Override
  public Object visit(WhileLoop node, Object data) {
    String result = printLineString("while (" + node.jjtGetChild(0).jjtAccept(this, data) + ") {");
    indent();
    result += printString(node.jjtGetChild(1).jjtAccept(this, data).toString());
    
    unindent();
    result += printLineString("}");
    
    return result;
  }
  
  @Override
  public Object visit(ForEachLoop node, Object data) {
    Node type = node.jjtGetChild(0);
    String local = node.jjtGetChild(1).jjtAccept(this, false).toString();
    Node n = node.jjtGetChild(2);
    String sequence = node.jjtGetChild(2).jjtAccept(this, false).toString();
    boolean isPile = type instanceof PileType;
    
    String result = printString(sequence);
    
    if (isPile) {
      result += printString(".cards");
    }
    
    result += printLineString(".forEach(" + local + " => {");
    
    indent();
    result += printString(node.jjtGetChild(3).jjtAccept(this, false).toString());
    unindent();
    
    result += printLineString("});");
    
    return result;
  }
  
  @Override
  public Object visit(Call node, Object data) {
    Node call = node.jjtGetChild(0);
    
    if (call instanceof DealCall || call instanceof PlayCall || call instanceof MoveCall ||
            call instanceof PlayerCall || call instanceof PileCall) {
      return printString(node.jjtGetChild(0).jjtAccept(this, data).toString());
    }
    
    String result = printString("this.");
    result += node.jjtGetChild(0).jjtAccept(this, data) + "(";
    
    for (int i = 1; i < node.jjtGetNumChildren(); i++) {
      result += node.jjtGetChild(i).jjtAccept(this, data);
      
      if (i < node.jjtGetNumChildren() - 1) {
        result += ", ";
      }
    }
    result += ")";
    
    return result;
  }
  
  @Override
  public Object visit(Type node, Object data) {
    return printString("let");
  }
  
  @Override
  public Object visit(PlayerType node, Object data) {
    return printString("Player");
  }
  
  @Override
  public Object visit(NumberType node, Object data) {
    return printString("int");
  }
  
  @Override
  public Object visit(CardType node, Object data) {
    return printString("Card");
  }
  
  @Override
  public Object visit(RankType node, Object data) {
    return printString("Rank");
  }
  
  @Override
  public Object visit(SuitType node, Object data) {
    return printString("Suit");
  }
  
  @Override
  public Object visit(SequenceType node, Object data) {
    String type = printString("[]");
    
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      type += node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    type += " >";
    
    return type;
  }
  
  @Override
  public Object visit(PileType node, Object data) {
    return printString("Pile");
  }
  
  @Override
  public Object visit(BooleanType node, Object data) {
    return printString("Boolean");
  }
  
  @Override
  public Object visit(TextType node, Object data) {
    return printString("String");
  }
  
  @Override
  public Object visit(Literal node, Object data) {
    return printString(node.jjtGetChild(0).jjtAccept(this, data).toString());
  }
  
  @Override
  public Object visit(RangeLiteral node, Object data) {
    // Will never be visited because PlayerCount node doesn't call accept on it's children.
    return null;
  }
  
  @Override
  public Object visit(RankLiteral node, Object data) {
    return printString("Rank." + node.jjtGetValue().toString().toUpperCase());
  }
  
  @Override
  public Object visit(BooleanLiteral node, Object data) {
    return printString(node.jjtGetValue().toString());
  }
  
  @Override
  public Object visit(SequenceLiteral node, Object data) {
    String literal = printString("[");
    
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      literal += node.jjtGetChild(i).jjtAccept(this, data);
      
      if (i < node.jjtGetNumChildren() - 1) {
        literal += ", ";
      }
    }
    
    literal += "]";
    return literal;
  }
  
  @Override
  public Object visit(SimpleNode node, Object data) {
    node.childrenAccept(this, node);
    return null;
  }
  
  @Override
  public Object visit(OptionParameter node, Object data) {
    // Not used, since Option node handles this.
    return null;
  }
  
  @Override
  public Object visit(Option node, Object data) {
    int loopCount = 0;
    int i = 0;
    
    String repeatCount = null; // If still null, use while(true). Otherwise for loop.
    String buttonOk = "Select";
    String buttonCancel = "Skip";
    
    Node firstChild = node.jjtGetChild(0);
    
    // Get modifier
    if (firstChild instanceof Modifier) {
      i = 1;
      SimpleNode firstModifier = (SimpleNode) firstChild.jjtGetChild(0);
      
      if (firstModifier instanceof MandatoryModifier) {
        buttonCancel = null;
        
        if (firstChild.jjtGetNumChildren() > 1) {
          repeatCount = ((SimpleNode) firstChild.jjtGetChild(1)).jjtGetValue().toString();
        }
      } else if (firstModifier instanceof RepeatModifier) {
        repeatCount = firstModifier.jjtGetValue().toString();
      }
    }
    
    String optionName = ((TextLiteral) node.jjtGetChild(i++)).jjtGetValue().toString();
    
    String argumentName = null;
    String selectFrom = null;
    String selectFromName = null;
    
    if (node.jjtGetChild(i) instanceof OptionParameter) {
      OptionParameter optionParameter = (OptionParameter) node.jjtGetChild(i++);
      
      argumentName = ((Identifier) optionParameter.jjtGetChild(1)).jjtGetValue().toString();
      
      Node type = optionParameter.jjtGetChild(0);
      
      if (type instanceof PlayerType) {
        selectFrom = "this.$players$";
      } else if (type instanceof SuitType) {
        selectFrom = "Suit";
      } else if (type instanceof RankType) {
        selectFrom = "Rank";
      } else if (type instanceof PileType) {
        selectFrom = "this.$piles$";
      } else if (type instanceof CardType) {
        selectFrom = "Utils.allCards";
      }
    } else {
      // No selection parameter.
      if (buttonCancel == null) {
        // Mandatory
        buttonOk = "Continue";
      } else {
        // Not mandatory
        buttonCancel = "No";
        buttonOk = "Yes";
      }
    }
    
    String conditionExpression = null;
    
    if (!(node.jjtGetChild(i) instanceof Block)) {
      Node exp = node.jjtGetChild(i);
      i++;
      
      conditionExpression = exp.jjtAccept(this, false).toString();
    }
    
    if (conditionExpression != null || selectFrom != null) {
      // If there is an expression or something to select from, create variable to hold map/expression.
      selectFromName = "$selectFrom_" + optionCount++ + "$";
    }
    
    Block block = (Block) node.jjtGetChild(i);
    
    String result = "";
    
    String indexName = "$i_" + loopCount++ + "$";
    
    if (repeatCount != null) {
      result += printLineString("for (let " + indexName + " = 0; " + indexName + " < " + repeatCount + "; " + indexName + "++) {");
    } else {
      result += printLineString("while (true) {");
    }
    indent();
    
    // Create boolean or selectMap if there is an expression or selection.
    if (selectFromName != null) {
      result += printString("let " + selectFromName + " = ");
      
      if (selectFrom != null) {
        // There is an expression but nothing to select from.
        result += printLineString("{};");
      } else {
        // There is an expression but nothing to select from.
        result += printLineString(conditionExpression + ";");
      }
      
      result += printLineString("");
    }
    
    // Create forEach loop, if there is something to select from.
    if (selectFrom != null) {
      result += printLineString(selectFrom + ".forEach(" + argumentName + " => {");
      indent();
      
      // If there is a condition, create if statement.
      if (conditionExpression != null) {
        result += printLineString("if(" + conditionExpression + ") {");
        indent();
      }
      
      result += printLineString(selectFromName + "[" + argumentName + ".toString()] = new FormElement(" + argumentName + ", " + argumentName + ".toString());");
      
      // If there is a condition, close if statement.
      if (conditionExpression != null) {
        unindent();
        result += printLineString("}");
      }
      unindent();
      result += printLineString("});");
      result += printLineString("");
    }
    
    // If there is an expression or selection, generate if statement around dialog.
    if (selectFromName != null) {
      result += printString("if (");
      
      if (selectFrom != null) {
        // If there is something to select from use Object.keys(...).length.
        result += printString("Object.keys(" + selectFromName + ").length");
      } else {
        // Otherwise, just use the variable (boolean).
        result += printString(selectFromName);
      }
      
      result += printLineString(") {");
      indent();
    }
    
    String selectArgument = selectFromName != null ? selectFromName : "{}";
    String optionArgument = "{ buttonOk: \"" + buttonOk + "\"" + (buttonCancel != null ? ", buttonCancel: \"" + buttonCancel + "\"" : "") + " }";
    result += printLineString("let $result$ = await UI.createDialog(" +
            selectArgument + ", " +
            "\"" + optionName + "\", "
            + optionArgument + ");");
    
    result += printLineString("if (!$result$ || ($result$ && $result$.result === false)) {");
    indent();
    result += printLineString("break;");
    unindent();
    result += printLineString("}");
    if (selectFrom != null) {
      // Only create result variable, it was an option with a selection argument.
      result += printLineString("let " + argumentName + " = $result$.value;");
    }
    result += printLineString("");
    
    result += printLineString("// Option block begins here");
    result += printString(block.jjtAccept(this, false).toString());
    result += printLineString("// Option block ends here");
    
    if (selectFromName != null) {
      // Close if, and create break expression not satisfied.
      unindent();
      result += printLineString("} else {");
      
      // Break out of loop if expression is no longer satisfied (no more to select from or expression == false).
      indent();
      result += printLineString("break;");
      
      unindent();
      result += printLineString("}");
    }
    
    // Close for/while loop.
    unindent();
    result += printLineString("}");
    
    return result;
  }
  
  @Override
  public Object visit(Modifier node, Object data) {
    // Not used. Handled in Option node.
    return null;
  }
  
  @Override
  public Object visit(MandatoryModifier node, Object data) {
    // Not used. Handled in Option node.
    return null;
  }
  
  @Override
  public Object visit(RepeatModifier node, Object data) {
    // Not used. Handled in Option node.
    return null;
  }
  
  @Override
  public Object visit(Comparator node, Object data) {
    Operator c = (Operator) node.jjtGetValue();
    
    return printString(" " + c.getSymbol() + " ");
  }
  
  @Override
  public Object visit(TermOperator node, Object data) {
    Operator c = (Operator) node.jjtGetValue();
    
    return printString(" " + c.getSymbol() + " ");
  }
  
  @Override
  public Object visit(FactorOperator node, Object data) {
    Operator c = (Operator) node.jjtGetValue();
    
    return printString(" " + c.getSymbol() + " ");
  }
  
  @Override
  public Object visit(Identifier node, Object data) {
    String prefix = "";
    if (node.isGlobal()) {
      prefix = "this.";
    }
    
    return printString(prefix + node.jjtGetValue().toString());
  }
  
  @Override
  public Object visit(Parameter node, Object data) {
    return node.jjtGetChild(1).jjtAccept(this, data).toString();
  }
  
  @Override
  public Object visit(CardLiteral node, Object data) {
    return printString("new Card(" +
            node.jjtGetChild(0).jjtAccept(this, data)
            + ", " + node.jjtGetChild(1).jjtAccept(this, data)
            + ")");
  }
  
  @Override
  public Object visit(SuitLiteral node, Object data) {
    return printString("Suit." + node.jjtGetValue().toString().toUpperCase());
  }
  
  @Override
  public Object visit(TextLiteral node, Object data) {
    return printString("\"" + node.jjtGetValue() + "\"");
  }
  
  @Override
  public Object visit(NumberLiteral node, Object data) {
    return printString(node.jjtGetValue().toString());
  }
  
  @Override
  public Object visit(Setup node, Object data) {
    String result = printString("");
    
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      result += printString(node.jjtGetChild(i).jjtAccept(this, false).toString());
    }
    
    gameJs.setGlobals(result.replaceAll("\n([^ ])", "\n    $1"));
    return null;
  }
  
  @Override
  public Object visit(Stage node, Object data) {
    String fromStage = ((Identifier) node.jjtGetChild(0)).jjtGetValue().toString();
    String toStage = ((Identifier) node.jjtGetChild(1)).jjtGetValue().toString();
    String preCode = printString(node.jjtGetChild(2).jjtAccept(this, false).toString());
    String code = printString(node.jjtGetChild(3).jjtAccept(this, false).toString());
    
    JsStage stage = new JsStage(fromStage, toStage, preCode, code);
    this.stages.add(stage);
    
    return null;
  }
  
  @Override
  public Object visit(Prog node, Object data) {
    System.out.println("/* ---- This is a program written in FLIP and compiled to JavaScript ----*/\n");
    node.childrenAccept(this, data);
    
    gameJs.addStages(stages);
    gameJs.addFunctions(functions);
    
    MainHtml html = new MainHtml();
    
    List<CodeFile> files = new ArrayList<>();
    files.add(new UiJs());
    files.add(gameJs);
    files.add(playerJs);
    files.add(new CardJs());
    files.add(new CardImagesJs());
    files.add(new RenderingJs());
    files.add(new UtilsJs());
    files.add(new PileJs());
    files.add(new MainJs(minPlayerCount, maxPlayerCount));
    files.add(new UnderscoreJs());
    files.add(new UnderscoreObserveJs());
    files.add(new StylesCss());
    html.addImports(files);
    
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("main.html"))) {
      writer.write(html.getContent());
    } catch (IOException e) {
    
    }
    
    for (int i = 0; i < files.size(); i++) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(files.get(i).getName()))) {
        writer.write(files.get(i).getContent());
      } catch (IOException e) {
      
      }
    }
    
    return null;
  }
  
  private void printInline(String elem) {
    System.out.print(elem);
  }
  
  private String printString(String elem) {
    if (startOfLine) {
      startOfLine = false;
      
      return new String(new char[indentation]).replace("\0", "  ")
              + elem;
    } else {
      return elem;
    }
  }
  
  private String printLineString(String elem) {
    String result = printString(elem) + "\n";
    startOfLine = true;
    return result;
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
