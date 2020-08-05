package javacc.visitors.codegeneration;

public class JsVariable {
  private String id;
  private String value;
  
  public JsVariable(String id) {
    this.id = id;
  }
  
  public JsVariable(String id, String value) {
    this.id = id;
    this.value = value;
  }
  
  public String getId() {
    return id;
  }
  
  public String getValue() {
    return value;
  }
}
