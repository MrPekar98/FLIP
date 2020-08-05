package javacc.visitors.codegeneration.templates.js;

public class PileJs implements JsModuleFile {
  @Override
  public String getExportedNames() {
    return "Pile";
  }
  
  @Override
  public String getName() {
    return "pile.js";
  }
  
  @Override
  public String getContent() {
    return "export class Pile {\n" +
            "  constructor(faceUp, name, shortName) {\n" +
            "    this.faceUp = faceUp;\n" +
            "    this.cards = [];\n" +
            "    this.name = name;\n" +
            "    this.shortName = shortName || name;\n" +
            "    this.shortName = this.shortName.charAt(0).toUpperCase() + this.shortName.slice(1);\n" +
            "  }\n" +
            "  \n" +
            "  forEach(cb) {\n" +
            "\tif (typeof cb !== 'function') {\n" +
            "\t  cb = () => {};\n" +
            "\t}\t\n" +
            "    this.cards.forEach(cb);\n" +
            "  }\n" +
            "\n" +
            "  toString() {\n" +
            "    return String(this.name); \n" +
            "  }\n" +
            "}";
  }
}
