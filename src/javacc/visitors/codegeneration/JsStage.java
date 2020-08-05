package javacc.visitors.codegeneration;

public class JsStage {
  private String fromStage;
  private String fromStagePre;
  private String toStage;
  private String preCode;
  private String code;
  
  public JsStage(String fromStage, String toStage, String preCode, String code) {
    this.fromStage = "stage_" + fromStage;
    this.fromStagePre = "pre_stage_" + fromStage;
    this.toStage = "stage_" + toStage;
    this.preCode = preCode;
    this.code = code;
  }
  
  public String getFromStage() {
    return fromStage;
  }
  
  public String getFromStagePre() {
    return fromStagePre;
  }
  
  public String getToStage() {
    return toStage;
  }
  
  public String getPreCode() {
    return preCode;
  }
  
  public String getCode() {
    return code;
  }
}
