package javacc.visitors.codegeneration.templates.css;

import javacc.visitors.codegeneration.templates.js.CssFile;

public class StylesCss implements CssFile {
  @Override
  public String getName() {
    return "styles.css";
  }
  
  @Override
  public String getContent() {
    return ".modal-content {\n" +
            "  color: #ffffffdd;\n" +
            "  background-color: #00000055;\n" +
            "}\n" +
            "\n" +
            ".modal-header {\n" +
            "  border-bottom: 1px solid #ffffff47;\n" +
            "}\n" +
            "\n" +
            ".modal-footer {\n" +
            "  border-top: 1px solid #ffffff47;\n" +
            "}\n" +
            "\n" +
            ".modal-backdrop {\n" +
            "  background-color: #ffffff00;\n" +
            "}\n" +
            "\n" +
            "#select {\n" +
            "  background-color: #bebebe12;\n" +
            "  color: #ffffffdd;\n" +
            "}\n" +
            "\n" +
            "#select option {\n" +
            "  background-color: #326e6c;\n" +
            "}\n" +
            "\n" +
            "body {\n" +
            "  background-color: transparent;\n" +
            "}\n" +
            "\n" +
            "#title h1 {\n" +
            "  padding-top: 60px;\n" +
            "  text-align: center;\n" +
            "  color: #5fdfd98a;\n" +
            "}\n" +
            "\n" +
            ".card-red {\n" +
            "  color: #ff0000b3;\n" +
            "}\n" +
            "\n" +
            ".card-black {\n" +
            "  color: #000000b3;\n" +
            "}\n" +
            "\n" +
            "html { \n" +
            "  background: url(http://erlectionede.dk/bg.jpg);\n" +
            "  -webkit-background-size: cover;\n" +
            "  -moz-background-size: cover;\n" +
            "  -o-background-size: cover;\n" +
            "  background-size: cover;\n" +
            "}";
  }
}
