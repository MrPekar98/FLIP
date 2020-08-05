package javacc.visitors.codegeneration.templates.html;

import javacc.visitors.codegeneration.CodeFile;
import javacc.visitors.codegeneration.templates.js.CssFile;
import javacc.visitors.codegeneration.templates.js.JsFile;
import javacc.visitors.codegeneration.templates.js.JsModuleFile;

import java.util.List;

public class MainHtml implements CodeFile {
  
  public String imports = "";
  public String headImports = "";
  
  public void addImports(List<CodeFile> files) {
    for (CodeFile file : files) {
      if (file instanceof JsModuleFile) {
        imports += "  <script type=\"module\" src=\"" + file.getName() + "\"></script>\n";
      } else if (file instanceof JsFile){
        imports += "  <script type=\"application/javascript\" src=\"" + file.getName() + "\"></script>\n";
      } else if (file instanceof CssFile) {
        headImports += "  <link rel=\"stylesheet\" href=\"" + file.getName() + "\">\n";
      }
    }
  }
  
  @Override
  public String getName() {
    return "main.html";
  }
  
  public String getContent() {
    return "<!doctype html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "  <meta charset=\"utf-8\" />\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
            "  <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">\n" +
            headImports + "\n" +
            "</head>\n" +
            "<body>\n" +
            "  <div id=\"title\">\n" +
            "    <h1>A FLIP game</h1>\n" +
            "  </div>\n" +
            "  <div id=\"ui\"></div>\n" +
            "  <canvas id=\"bg-canvas\" height=\"540\" width=\"800\" style=\"position: absolute; z-index: 0\"></canvas>\n" +
            "  <canvas id=\"pile-canvas\" height=\"540\" width=\"800\" style=\"position: absolute; z-index: 1\"></canvas>\n" +
            "  <canvas id=\"ui-canvas\" height=\"540\" width=\"800\" style=\"position: absolute; z-index: 2\"></canvas>\n" +
            "  <script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\" crossorigin=\"anonymous\"></script>\n" +
            "  <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\" integrity=\"sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1\" crossorigin=\"anonymous\"></script>\n" +
            "  <script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\" integrity=\"sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM\" crossorigin=\"anonymous\"></script>\n" +
            imports +
            "</body>\n" +
            "</html>";
  }
}
