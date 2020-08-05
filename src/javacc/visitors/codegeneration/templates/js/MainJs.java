package javacc.visitors.codegeneration.templates.js;

public class MainJs implements JsModuleFile {
  
  int minPlayerCount;
  int maxPlayerCount;
  
  public MainJs(int minPlayerCount, int maxPlayerCount) {
    this.minPlayerCount = minPlayerCount;
    this.maxPlayerCount = maxPlayerCount;
  }
  
  @Override
  public String getExportedNames() {
    return "";
  }
  
  @Override
  public String getContent() {
    return "import { Game } from \"./game.js\";\n" +
            "import { UI, FormElement } from \"./ui.js\";\n" +
            "\n" +
            "startGame();\n" +
            "\n" +
            "async function startGame() {\n" +
            "  let selectMap = {};\n" +
            "\n" +
            "  for (let i = " + minPlayerCount + "; i < " + (maxPlayerCount + 1) + "; i++) {\n" +
            "    selectMap[i] = new FormElement(i, i);\n" +
            "  }\n" +
            "\n" +
            "  let result = await UI.createDialog(selectMap, \"Select number of players\", { buttonOk: \"Start Game\" });\n" +
            "  let title = document.getElementById(\"title\");\n" +
            "  title.parentNode.removeChild(title);" +
            "  let game = new Game(result.value);\n" +
            "}";
  }
  
  @Override
  public String getName() {
    return "main.js";
  }
}
