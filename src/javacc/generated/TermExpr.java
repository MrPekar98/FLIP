/* Generated By:JJTree: Do not edit this line. TermExpr.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc.generated;

import javacc.util.Variable;

public
class TermExpr extends SimpleNode {
  private Variable.DataType termType;
  private boolean isSequence = false;

  public TermExpr(int id) {
    super(id);
  }

  public TermExpr(FlipParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(FlipParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }

  public void setTermType(Variable.DataType type)
  {
    this.termType = type;
  }

  public void setSequence(boolean value)
  {
    this.isSequence = value;
  }

  public Variable.DataType getTermType()
  {
    return this.termType;
  }

  public boolean isSequence()
  {
    return this.isSequence;
  }
}
/* JavaCC - OriginalChecksum=6a0aeca377523329d2b03370057f8079 (do not edit this line) */
