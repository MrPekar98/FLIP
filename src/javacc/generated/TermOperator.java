/* Generated By:JJTree: Do not edit this line. TermOperator.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc.generated;

import javacc.util.Operator;

public
class TermOperator extends SimpleNode {
  public TermOperator(int id) {
    super(id);
  }

  public TermOperator(FlipParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(FlipParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
  
  @Override
  public String toString() {
    return "\"" + ((Operator)value).getSymbol() + "\" (TermOperator)";
  }
}
/* JavaCC - OriginalChecksum=0c56ed2e7dce664303454c761d730ccb (do not edit this line) */