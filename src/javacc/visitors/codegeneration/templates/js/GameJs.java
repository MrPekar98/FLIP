package javacc.visitors.codegeneration.templates.js;

import javacc.visitors.codegeneration.JsFunction;
import javacc.visitors.codegeneration.JsStage;
import javacc.visitors.codegeneration.UndefinedStageExpection;

import java.util.ArrayList;
import java.util.List;

public class GameJs implements JsModuleFile {
  private List<JsStage> stages = new ArrayList<>();
  private List<JsFunction> functions = new ArrayList<>();
  private String globals;
  private int pileCount;
  
  @Override
  public String getContent() {
    return "import { Player, State } from \"./player.js\";\n" +
            "import { Utils } from \"./utils.js\";\n" +
            "import { UI, FormElement } from \"./ui.js\";\n" +
            "import { Card, Suit, Rank } from \"./card.js\";\n" +
            "import { Pile } from \"./pile.js\";\n\n" +
            "import { Renderer } from \"./rendering.js\";\n\n" +
            "export class Game {\n" +
            "  constructor(playerCount) {\n" +
            "    this.$deck$ = Utils.generateDeck();\n" +
            "    this.$piles$ = new Array(" + pileCount + ").fill().map((x, index) => new Pile(true, `Pile ${index + 1}`));\n" +
            "    this.$players$ = new Array(playerCount).fill().map((x, index) => new Player(index));\n" +
            "    this.$winner$;\n" +
            "    this.$renderer$ = new Renderer(this.$deck$, this.$piles$, this.$players$);\n" +
            "    " + globals +
            "    " + generateStageArray() + "\n" +
            "    this.$play$();\n" +
            "  }\n\n" +
            getPlayMethod() +
            "  }" +
            generateStages() +
            generateFunctions() + "\n" +
            "}";
  }
  
  public void setGlobals(String globals) {
    this.globals = globals;
  }
  
  private String generateStageArray() {
    String result = "this.$stages$ = [";
    
    
    for (int i = 0; i < stages.size(); i++) {
      if (i > 0) {
        result += ", ";
      }
      
      result += "{preCode: this.$" + stages.get(i).getFromStagePre() + "$.bind(this)" +
              ", code: this.$" + stages.get(i).getFromStage() + "$.bind(this)" +
              ", nextStage: " + getStageIndex(stages.get(i).getToStage()) + "}";
      
    }
    
    result += "];";
    
    return result;
  }
  
  private int getStageIndex(String stageName) {
    for (int i = 0; i < stages.size(); i++) {
      if (stages.get(i).getFromStage().equalsIgnoreCase(stageName)) {
        return i;
      }
    }
    
    throw new UndefinedStageExpection(stageName);
  }
  
  private String generateStages() {
    String result = "";
    
    for (JsStage stage : stages) {
      result += "\n\n  $" + stage.getFromStagePre() + "$() {\n";
      result += stage.getPreCode() + "}";
      
      result += "\n\n  async $" + stage.getFromStage() + "$($currentPlayer$) {\n";
      result += stage.getCode() + "}";
    }
    
    return result;
  }
  
  private String generateFunctions() {
    String result = "";
    
    for (JsFunction function : functions) {
      result += "\n\n  " + function.getSignature() + " {\n";
      result += "    " + function.getContent();
      result += "  }";
    }
    
    return result;
  }
  
  @Override
  public String getName() {
    return "game.js";
  }
  
  @Override
  public String getExportedNames() {
    return "Game";
  }
  
  public void addStages(List<JsStage> stages) {
    this.stages.addAll(stages);
  }
  
  public void addFunctions(List<JsFunction> functions) {
    this.functions.addAll(functions);
  }
  
  public void setPileCount(int pileCount) {
    this.pileCount = pileCount;
  }
  
  private String getPlayMethod() {
    return "  async $play$() {\n" +
            "    let $currentPlayer$ = 0;\n" +
            "    let $currentStage$ = 0;\n" +
            "\n" +
            "    // Execute pre code\n" +
            "    this.$stages$[0].preCode();\n" +
            "    this.$renderer$.setCurrentPlayer(0);" +
            "  \n" +
            "    while (!this.$winner$) {\n" +
            "      this.$renderer$.hideHands();\n" +
            "      await UI.createDialog({}, `${this.$players$[$currentPlayer$].toString()}'s turn`, { buttonOk: \"Start turn\" });\n" +
            "      this.$renderer$.setCurrentPlayer($currentPlayer$);\n" +
            "      switch (this.$players$[$currentPlayer$].state) {\n" +
            "        case State.SKIP:\n" +
            "          this.$players$[$currentPlayer$].state = State.NEXT;\n" +
            "        case State.DONE:\n" +
            "        case State.OUT:\n" +
            "          await UI.createDialog({}, `${this.$players$[$currentPlayer$].toString()} does not have a turn`, { buttonOk: \"Ok\" });\n" +
            "          $currentPlayer$ = Utils.nextPlayer($currentPlayer$, this.$players$);\n" +
            "          break;\n" +
            "        case State.NEXT:\n" +
            "          // Run stage code for this player.\n" +
            "          await this.$stages$[$currentStage$].code($currentPlayer$);\n" +
            "          await UI.createDialog({}, \"End my turn\", { buttonOk: \"Ok\" });\n" +
            "          if (this.$winner$) {\n" +
            "            break;\n" +
            "          }\n" +
            "\n" +
            "          if (Utils.stageDone(this.$players$)) {\n" +
            "            // Change done to next.\n" +
            "            Utils.setReadyPlayers(this.$players$);\n" +
            "\n" +
            "            // Set current stage.\n" +
            "            $currentStage$ = this.$stages$[$currentStage$].nextStage;\n" +
            "\n" +
            "            // Set the first player of the next stage.\n" +
            "            $currentPlayer$ = Utils.firstReadyPlayer(this.$players$);\n" +
            "\n" +
            "            // Execute pre stage code.\n" +
            "            this.$stages$[$currentStage$].preCode();\n" +
            "            if (this.$winner$) {\n" +
            "              break;\n" +
            "            }\n" +
            "          } else if (!this.$winner$) {\n" +
            "            $currentPlayer$ = Utils.nextPlayer($currentPlayer$, this.$players$);\n" +
            "          }\n" +
            "\n" +
            "          break;\n" +
            "        default:\n" +
            "          break;\n" +
            "      }\n" +
            "    }\n" +
            "    await UI.createDialog({}, `${this.$winner$.toString()} has won the game!`, { buttonOk: \"Ok\"});\n";
  }
}
