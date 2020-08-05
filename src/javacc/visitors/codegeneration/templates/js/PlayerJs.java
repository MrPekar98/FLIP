package javacc.visitors.codegeneration.templates.js;

import javacc.visitors.codegeneration.JsVariable;

import java.util.ArrayList;
import java.util.List;

public class PlayerJs implements JsModuleFile {
  
  private List<JsVariable> fields = new ArrayList<>();
  private boolean usePileImport = false;
  
  public void addField(String id) {
    fields.add(new JsVariable(id));
  }
  
  public void addField(String id, String value) {
    fields.add(new JsVariable(id, value));
  }
  
  private String fieldsToCpp() {
    String res = "constructor(index) {\n" +
            "    // Default fields\n" +
            "    this.index = index\n" +
            "    this.state = State.NEXT;\n" +
            "    this.hand = new Pile(true, `Player ${index + 1}'s hand`, \"hand\");";
    
    for (int i = 0; i < fields.size(); i++) {
      if (i == 0) {
        res += "\n\n    // Custom fields";
      }
      
      JsVariable var = fields.get(i);
      res += "\n    this." + var.getId();
      
      if (var.getValue() != null) {
        res += " = " + var.getValue();
      }
      
      res += ";";
    }
    
    
    res += "\n  }";
    return res;
  }
  
  public void usePileImport() {
    this.usePileImport = true;
  }
  
  @Override
  public String getExportedNames() {
    return "State, Player";
  }
  
  @Override
  public String getName() {
    return "player.js";
  }
  
  @Override
  public String getContent() {
    return "import { Pile } from \"./pile.js\";\n\n" +
            "export let State = Object.freeze({" +
            "NEXT: 1, DONE: 2, SKIP: 3, OUT: 4" +
            "});\n\nexport class Player {\n  " +
            fieldsToCpp() + "\n\n" +
            "  toString() {\n" +
            "    return `Player ${this.index + 1}`;\n" +
            "  }\n" +
            "}";
  }
}
