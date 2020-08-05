/* Generated By:JJTree: Do not edit this line. Identifier.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javacc.generated;

import javacc.util.Variable;

public
class Identifier extends SimpleNode {
  private boolean isGlobal = false;
  private boolean isSize = false;
  private Variable.DataType dataType;

  public Identifier(int id) {
    super(id);
  }

  public Identifier(FlipParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(FlipParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }

  public void setGlobal(boolean value)
  {
    this.isGlobal = value;
  }

  public void setType(Variable.DataType type)
  {
    this.dataType = type;
  }

  public boolean isGlobal()
  {
    return this.isGlobal;
  }

  public Variable.DataType getType()
  {
    return this.dataType;
  }
  
  public boolean isSize() {
    return isSize;
  }
  
  public void setIsSize(boolean size) {
    isSize = size;
  }
  
  @Override
  public String toString() {
    return "\"" + this.jjtGetValue().toString() + "\" (Identifier)";
  }
}
/* JavaCC - OriginalChecksum=85217c647bd7c4a9473a647f05c7a945 (do not edit this line) */
