package javacc.visitors.codegeneration;

import java.util.ArrayList;
import java.util.List;

public class JsFunction {
  private String name;
  private List<String> params = new ArrayList<>();
  private String content;
  
  public JsFunction() {
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setContent(String content) {
    this.content = content;
  }
  
  public void addParameter(String param) {
    this.params.add(param);
  }
  
  public String getSignature() {
    return name + "(" + String.join(", ", params) + ")";
  }
  
  public String getContent() {
    return content;
  }
}
