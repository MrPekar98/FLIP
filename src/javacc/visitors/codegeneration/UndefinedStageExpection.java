package javacc.visitors.codegeneration;

public class UndefinedStageExpection extends RuntimeException {
  public UndefinedStageExpection() {
    super("Undefined stage");
  }
  
  public UndefinedStageExpection(String stage) {
    super("Stage " + stage + " does not exist");
  }
}
