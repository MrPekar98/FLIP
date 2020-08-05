package javacc.visitors;

import javacc.util.Operator;
import javacc.generated.*;
import javacc.util.TreePrinter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static javacc.generated.FlipParserTreeConstants.JJTARROWEXPR;
import static javacc.generated.FlipParserTreeConstants.JJTASSIGNMENT;

public class FixExpressionsVisitor implements FlipParserVisitor {
  private int indentation = 0;
  private boolean startOfLine = true;
  
  @Override
  public Object visit(SetupBlock node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(PlayerCount node, Object data) {
    return null;
  }
  
  @Override
  public Object visit(PlayerDef node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(Declaration node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);
    node.jjtGetChild(1).jjtAccept(this, data);
    
    if (node.jjtGetNumChildren() == 3) {
      // Change expression tree.
      SimpleNode expr = (SimpleNode) node.jjtGetChild(2).jjtAccept(this, data);
      node.jjtAddChild(expr, 2);
    }
    
    return null;
  }
  
  
  @Override
  public Object visit(Assignment node, Object data) {
    Node child1 = node.jjtGetChild(0);
    Node child2 = node.jjtGetChild(1);
    
    if (node.jjtGetNumChildren() == 3) {
      Node initialiseExpression = node.jjtGetChild(2);
      // Change child to ArrowExpr.
      ArrowExpr arrowExpr = new ArrowExpr(JJTARROWEXPR);
      // Add children (id and id (selector)).
      arrowExpr.jjtAddChild(child1, 0);
      arrowExpr.jjtAddChild(child2, 1);
      child1.jjtSetParent(arrowExpr);
      child2.jjtSetParent(arrowExpr);
      
      Node parent = node.jjtGetParent();
      
      // Create a new Assignment node.
      node = new Assignment(JJTASSIGNMENT);
      node.jjtAddChild(arrowExpr, 0);
      node.jjtAddChild(initialiseExpression, 1);
      // Set new node to be parent of ArrowExpr.
      arrowExpr.jjtSetParent(node);
      // Set parent of new node to be parent of current node.
      node.jjtSetParent(node.jjtGetParent());
      
      // Set new Assignment node as child of AssignmentStatement node.
      parent.jjtAddChild(node, 0);
    }
    
    
    // Change expression tree.
    SimpleNode expr = (SimpleNode) node.jjtGetChild(1).jjtAccept(this, data);
    node.jjtAddChild(expr, 1);
    return null;
  }
  
  @Override
  public Object visit(OrExpr node, Object data) {
    Object ob = binaryOperator(node, data != null ? data : node.jjtGetParent());
    return ob;
  }
  
  @Override
  public Object visit(AndExpr node, Object data) {
    Object ob = binaryOperator(node, data != null ? data : node.jjtGetParent());
    return ob;
  }
  
  @Override
  public Object visit(NotExpr node, Object data) {
    Object ob = unaryOperator(node, data != null ? data : node.jjtGetParent());
    return ob;
  }
  
  @Override
  public Object visit(EqualsExpr node, Object data) {
    Object ob = binaryOperator(node, data != null ? data : node.jjtGetParent());
    return ob;
  }
  
  @Override
  public Object visit(CompareExpr node, Object data) {
    Object ob = binaryOperator(node, data != null ? data : node.jjtGetParent());
    return ob;
  }
  
  @Override
  public Object visit(TermExpr node, Object data) {
    Object ob = binaryOperator(node, data != null ? data : node.jjtGetParent());
    return ob;
  }
  
  @Override
  public Object visit(FactorExpr node, Object data) {
    Object ob = binaryOperator(node, data != null ? data : node.jjtGetParent());
    return ob;
  }
  
  @Override
  public Object visit(NegativeExpr node, Object data) {
    Object ob = unaryOperator(node, data != null ? data : node.jjtGetParent());
    return ob;
  }
  
  @Override
  public Object visit(ArrowExpr node, Object data) {
    Object ob = binaryOperator(node, data != null ? data : node.jjtGetParent());
    return ob;
  }
  
  @Override
  public Object visit(ValueExpr node, Object data) {
    Object ob = node.jjtGetChild(0).jjtAccept(this, data);
    return ob;
  }
  
  @Override
  public Object visit(ReturnStatement node, Object data) {
    // Change expression tree.
    SimpleNode expr = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
    node.jjtAddChild(expr, 0);
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
    return null;
  }
  
  @Override
  public Object visit(DoneState node, Object data) {
    if (node.jjtGetNumChildren() > 0) {
      SimpleNode expr = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
      node.jjtAddChild(expr, 0);
    }
    return null;
  }
  
  @Override
  public Object visit(NextState node, Object data) {
    return null;
  }
  
  @Override
  public Object visit(OutState node, Object data) {
    if (node.jjtGetNumChildren() > 0) {
      SimpleNode expr = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
      node.jjtAddChild(expr, 0);
    }
    return null;
  }
  
  @Override
  public Object visit(WinState node, Object data) {
    if (node.jjtGetNumChildren() > 0) {
      SimpleNode expr = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
      node.jjtAddChild(expr, 0);
    }
    return null;
  }
  
  @Override
  public Object visit(SkipState node, Object data) {
    // Change expression trees.
    if (node.jjtGetNumChildren() > 0) {
      SimpleNode expr = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
      node.jjtAddChild(expr, 0);
    }
    return null;
  }
  
  @Override
  public Object visit(Function node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);
    
    node.jjtGetChild(1).jjtAccept(this, data);
    
    int i = 2;
    
    if (node.jjtGetNumChildren() > 3) {
      while (node.jjtGetChild(i) instanceof Parameter) {
        node.jjtGetChild(i).jjtAccept(this, data);
        i++;
      }
    }
    
    node.jjtGetChild(i).jjtAccept(this, data);
    
    return null;
  }
  
  @Override
  public Object visit(ActionReturnType node, Object data) {
    return null;
  }
  
  @Override
  public Object visit(Block node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(CurrentPlayer node, Object data) {
    return node;
  }
  
  @Override
  public Object visit(PileDef node, Object data) {
    return null;
  }
  
  @Override
  public Object visit(PileCall node, Object data) {
    // Change expression trees.
    SimpleNode expr1 = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
    
    node.jjtAddChild(expr1, 0);
    
    node.jjtSetParent((Node) data);
    
    return node;
    
  }
  
  @Override
  public Object visit(PlayerCall node, Object data) {
    // Change expression trees.
    SimpleNode expr1 = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
    
    node.jjtAddChild(expr1, 0);
    
    node.jjtSetParent((Node) data);
    
    return node;
  }
  
  @Override
  public Object visit(PlayCall node, Object data) {
    // Change expression trees.
    SimpleNode expr1 = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
    SimpleNode expr2 = (SimpleNode) node.jjtGetChild(1).jjtAccept(this, data);
    
    node.jjtAddChild(expr1, 0);
    node.jjtAddChild(expr2, 1);
    
    node.jjtSetParent((Node) data);
    
    return node;
  }
  
  @Override
  public Object visit(MoveCall node, Object data) {
    // Change expression trees.
    SimpleNode expr1 = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
    SimpleNode expr2 = (SimpleNode) node.jjtGetChild(1).jjtAccept(this, data);
    SimpleNode expr3 = (SimpleNode) node.jjtGetChild(2).jjtAccept(this, data);
    
    node.jjtAddChild(expr1, 0);
    node.jjtAddChild(expr2, 1);
    node.jjtAddChild(expr3, 2);
    
    node.jjtSetParent((Node) data);
    
    return node;
  }
  
  @Override
  public Object visit(DealCall node, Object data) {
    // Change expression trees.
    SimpleNode expr1 = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
    SimpleNode expr2 = (SimpleNode) node.jjtGetChild(1).jjtAccept(this, data);
    
    node.jjtAddChild(expr1, 0);
    node.jjtAddChild(expr2, 1);
    
    node.jjtSetParent((Node) data);
    
    return node;
  }
  
  @Override
  public Object visit(Statement node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(AssignmentStatement node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(DeclarationStatement node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(ExpressionStatement node, Object data) {
    // Change expression tree.
    SimpleNode expr = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
    node.jjtAddChild(expr, 0);
    return null;
  }
  
  @Override
  public Object visit(IfStatement node, Object data) {
    // Change expression tree for if statement.
    SimpleNode expr = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
    node.jjtAddChild(expr, 0);
    
    // Accept "if" body.
    node.jjtGetChild(1).jjtAccept(this, data);
    
    if (node.jjtGetNumChildren() == 2) {
      return null;
    }
    
    // Has "else if" and or "else".
    for (int i = 2; i < node.jjtGetNumChildren(); i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    return null;
  }
  
  @Override
  public Object visit(ElseIfStatement node, Object data) {
    // Change expression tree.
    SimpleNode expr = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
    node.jjtAddChild(expr, 0);
    
    node.jjtGetChild(1).jjtAccept(this, data);
    
    
    return null;
  }
  
  @Override
  public Object visit(ElseStatement node, Object data) {
    
    
    node.jjtGetChild(0).jjtAccept(this, data);
    
    
    return null;
  }
  
  @Override
  public Object visit(WhileLoop node, Object data) {
    // Change expression tree.
    SimpleNode expr = (SimpleNode) node.jjtGetChild(0).jjtAccept(this, data);
    node.jjtAddChild(expr, 0);
    
    node.jjtGetChild(1).jjtAccept(this, data);
    
    return null;
  }
  
  @Override
  public Object visit(ForEachLoop node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);
    node.jjtGetChild(1).jjtAccept(this, data);
    
    // Change expression tree.
    SimpleNode expr = (SimpleNode) node.jjtGetChild(2).jjtAccept(this, data);
    node.jjtAddChild(expr, 2);
    
    node.jjtGetChild(3).jjtAccept(this, data);
    
    
    return null;
  }
  
  @Override
  public Object visit(Call node, Object data) {
    // If predefined call, this accept will also fix expressions in arguments of that call.
    node.jjtGetChild(0).jjtAccept(this, data);
    
    // If custom call, this loop will execute.
    for (int i = 1; i < node.jjtGetNumChildren(); i++) {
      // Change expression trees.
      SimpleNode expr = (SimpleNode) node.jjtGetChild(i).jjtAccept(this, data);
      node.jjtAddChild(expr, i);
    }
    
    return node;
  }
  
  @Override
  public Object visit(Type node, Object data) {
    node.childrenAccept(this, data);
    return null;
  }
  
  @Override
  public Object visit(PlayerType node, Object data) {
    
    return null;
  }
  
  @Override
  public Object visit(NumberType node, Object data) {
    
    return null;
  }
  
  @Override
  public Object visit(CardType node, Object data) {
    
    return null;
  }
  
  @Override
  public Object visit(RankType node, Object data) {
    
    return null;
  }
  
  @Override
  public Object visit(SuitType node, Object data) {
    
    return null;
  }
  
  @Override
  public Object visit(SequenceType node, Object data) {
    
    node.childrenAccept(this, data);
    
    return null;
  }
  
  @Override
  public Object visit(PileType node, Object data) {
    
    return null;
  }
  
  @Override
  public Object visit(BooleanType node, Object data) {
    
    return null;
  }
  
  @Override
  public Object visit(TextType node, Object data) {
    
    return null;
  }
  
  @Override
  public Object visit(Literal node, Object data) {
    return node.jjtGetChild(0).jjtAccept(this, data != null ? data : node.jjtGetParent());
  }
  
  @Override
  public Object visit(RangeLiteral node, Object data) {
    return null;
  }
  
  @Override
  public Object visit(RankLiteral node, Object data) {
    
    node.jjtSetParent((Node) data);
    return node;
  }
  
  @Override
  public Object visit(BooleanLiteral node, Object data) {
    
    node.jjtSetParent((Node) data);
    return node;
  }
  
  @Override
  public Object visit(SequenceLiteral node, Object data) {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
      
      // Change expression tree.
      SimpleNode expr = (SimpleNode) node.jjtGetChild(i).jjtAccept(this, data);
      node.jjtAddChild(expr, i);
    }
    
    node.jjtSetParent((Node) data);
    return node;
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
      i++;
    }
    
    // Skip TextLiteral node.
    i++;
    
    if (node.jjtGetChild(i) instanceof OptionParameter) {
      i++;
    }
    
    if (node.jjtGetChild(i) instanceof OrExpr) {
      SimpleNode expr = (SimpleNode) node.jjtGetChild(i).jjtAccept(this, data);
      node.jjtAddChild(expr, i);
      i++;
    }
    
    // Accept block (VERY important).
    node.jjtGetChild(i).jjtAccept(this, data);
    
    return null;
  }
  
  @Override
  public Object visit(Modifier node, Object data) {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
      
      if (i < node.jjtGetNumChildren() - 1) {
      
      }
    }
    return null;
  }
  
  @Override
  public Object visit(MandatoryModifier node, Object data) {
    
    return null;
  }
  
  @Override
  public Object visit(RepeatModifier node, Object data) {
    
    node.childrenAccept(this, data);
    
    return node;
  }
  
  @Override
  public Object visit(Comparator node, Object data) {
    Operator c = (Operator) node.jjtGetValue();
    
    return node;
  }
  
  @Override
  public Object visit(TermOperator node, Object data) {
    Operator c = (Operator) node.jjtGetValue();
    
    return node;
  }
  
  @Override
  public Object visit(FactorOperator node, Object data) {
    Operator c = (Operator) node.jjtGetValue();
    
    return node;
  }
  
  @Override
  public Object visit(Identifier node, Object data) {
    
    return node;
  }
  
  @Override
  public Object visit(Parameter node, Object data) {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    return node;
  }
  
  @Override
  public Object visit(CardLiteral node, Object data) {
    node.jjtSetParent((Node) data);
    data = node;
    
    node.jjtGetChild(0).jjtAccept(this, data);
    
    node.jjtGetChild(1).jjtAccept(this, data);
    
    return node;
  }
  
  @Override
  public Object visit(SuitLiteral node, Object data) {
    
    node.jjtSetParent((Node) data);
    return node;
  }
  
  @Override
  public Object visit(TextLiteral node, Object data) {
    
    node.jjtSetParent((Node) data);
    return node;
  }
  
  @Override
  public Object visit(NumberLiteral node, Object data) {
    
    node.jjtSetParent((Node) data);
    return node;
  }
  
  @Override
  public Object visit(Setup node, Object data) {
    node.childrenAccept(this, data);
    
    return node;
  }
  
  @Override
  public Object visit(Stage node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);
    node.jjtGetChild(1).jjtAccept(this, data);
    
    for (int i = 2; i < node.jjtGetNumChildren(); i++) {
      node.jjtGetChild(i).jjtAccept(this, data);
    }
    
    return node;
  }
  
  @Override
  public Object visit(Prog node, Object data) {
    
    node.childrenAccept(this, data);
    return node;
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
  
  /**
   * Flattens expression tree (removes unused nodes) and makes it pretty.
   *
   * @param node Node that is being visited.
   * @param data Last valid parent node.
   * @return
   */
  private Object binaryOperator(SimpleNode node, Object data) {
    // TODO: Doesn't work with "x or y or z" etc.
    if (node.jjtGetNumChildren() > 1) {
      try {
        String className = node.getClass().toString().substring(6);
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getConstructor(Integer.TYPE);
        SimpleNode compactExpression = (SimpleNode) constructor.newInstance(new Object[]{node.getId()});
        compactExpression.jjtSetParent((Node) data);
        
        // This node IS in the tree; set the data object to this node, so children can get the correct parent.
        data = compactExpression;
        
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
          Node child = (Node) node.jjtGetChild(i).jjtAccept(this, data);
          child.jjtSetParent(compactExpression);
          compactExpression.jjtAddChild(child, i);
        }
        
        // For binary operator
        if (node.jjtGetNumChildren() > 3 || node instanceof ArrowExpr) {
          SimpleNode deepAndCompactExpression = createTree(0, compactExpression, compactExpression.jjtGetParent());
          return deepAndCompactExpression;
        }
        
        return compactExpression;
      } catch (IllegalAccessException | InstantiationException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
    
    return node.jjtGetChild(0).jjtAccept(this, data);
  }
  
    SimpleNode createTree(int offset, SimpleNode rootNode, Node parent) {
    if (rootNode.jjtGetNumChildren() - offset == 1) {
      return (SimpleNode) rootNode.jjtGetChild(0);
    }
    
    SimpleNode rightChild = (SimpleNode) rootNode.jjtGetChild(rootNode.jjtGetNumChildren() - offset - 1);
    SimpleNode centerChild = null;
    SimpleNode leftChild;
    
    try {
      String className = rightChild.jjtGetParent().getClass().toString().substring(6);
      Class<?> clazz = Class.forName(className);
      Constructor<?> constructor = clazz.getConstructor(Integer.TYPE);
      SimpleNode resultNode = (SimpleNode) constructor.newInstance(new Object[]{rightChild.jjtGetParent().getId()});
      resultNode.jjtSetParent(parent);
      
      // If there are three child nodes.
      if (rootNode.jjtGetNumChildren() % 2 != 0 && !(rootNode instanceof ArrowExpr)) {
        centerChild = (SimpleNode) rootNode.jjtGetChild(rootNode.jjtGetNumChildren() - offset - 2);
        leftChild = createTree(offset + 2, rootNode, resultNode);
      }
      
      // If there are two child nodes.
      else {
        leftChild = createTree(offset + 1, rootNode, resultNode);
      }
      leftChild.jjtSetParent(resultNode);
      rightChild.jjtSetParent(resultNode);
      resultNode.jjtAddChild(leftChild, 0);
      if (centerChild != null) {
        centerChild.jjtSetParent(resultNode);
        resultNode.jjtAddChild(centerChild, 1);
        resultNode.jjtAddChild(rightChild, 2);
      } else {
        resultNode.jjtAddChild(rightChild, 1);
      }
      
      return resultNode;
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

//  SimpleNode createTree(int offset, SimpleNode node, Object data) {
//    try {
//      String className = node.getClass().toString().substring(6);
//      Class<?> clazz = Class.forName(className);
//      Constructor<?> constructor = clazz.getConstructor(Integer.TYPE);
//      SimpleNode resultNode = (SimpleNode) constructor.newInstance(new Object[]{node.getId()});
//
//      if (node.jjtGetNumChildren() - offset <= 3) {
//        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
//          resultNode.jjtAddChild((Node) node.jjtGetChild(i).jjtAccept(this, data), i);
//        }
//
//        return resultNode;
//      }
//
//      SimpleNode rightChild = (SimpleNode) node.jjtGetChild(node.jjtGetNumChildren() - 1 - offset).jjtAccept(this, data);
//      SimpleNode centerChild = (SimpleNode) node.jjtGetChild(node.jjtGetNumChildren() - 2 - offset).jjtAccept(this, data);
//      SimpleNode leftChild = (SimpleNode) createTree(offset + 2, node, resultNode, data).jjtAccept(this, data);
//
//      resultNode.jjtAddChild(leftChild, 0);
//      resultNode.jjtAddChild(centerChild, 1);
//      resultNode.jjtAddChild(rightChild, 2);
//
//      return resultNode;
//
//    } catch (IllegalAccessException | InstantiationException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException e) {
//      throw new RuntimeException(e);
//    }
//  }
  
  
  /**
   * Flattens expression tree (removes unused nodes) and makes it pretty.
   *
   * @param node Node that is being visited.
   * @param data Last valid parent node.
   * @return
   */
  private Object unaryOperator(SimpleNode node, Object data) {
    if (node.jjtGetValue() != null) {
      // Use this unary expression.
      node.jjtSetParent((Node) data);
      
      // Set parent to this node, since it is used.
      data = node;
      
      node.jjtAddChild(((SimpleNode) node.jjtGetChild(0).jjtAccept(this, data)), 0);
      return node;
    }
    
    // This unary expression is not used, so just accept the child (data is still old parent).
    return node.jjtGetChild(0).jjtAccept(this, data);
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
