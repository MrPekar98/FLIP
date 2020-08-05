package javacc.util;

import javacc.generated.Node;

public class TreePrinter {
  
  private static String createTree(Node node, String prefix, boolean isRightMost, boolean isLeftMost, StringBuilder sb) {
    int halfSize = (node.jjtGetNumChildren() + 1) / 2;
    
    for (int i = node.jjtGetNumChildren() - 1; i >= halfSize; i--) {
      Node child = node.jjtGetChild(i);
      createTree(child,
              prefix + (isRightMost && !isLeftMost ? "    " : "│   "),
              i == node.jjtGetNumChildren() - 1,
              i == 0,
              sb);
    }
    sb.append(prefix).
            append(isRightMost && isLeftMost ? "└── " : "").
            append(isRightMost && !isLeftMost ? "┌── " : "").
            append(isLeftMost && !isRightMost ? "└── " : "").
            append(!isRightMost && !isLeftMost ? "├── " : "").
            append(node.toString()).
            append("\n");
    
    for (int i = halfSize - 1; i >= 0; i--) {
      Node child = node.jjtGetChild(i);
      createTree(child,
              prefix + (isLeftMost ? "    " : "│   "),
              i == node.jjtGetNumChildren() - 1,
              i == 0,
              sb);
    }
    
    return sb.toString();
  }
  
  public static void printTree(Node node, String prefix) {
    StringBuilder sb = new StringBuilder();
    String tree = createTree(node, prefix, false, false, sb);
    System.out.println(tree);
  }
}
