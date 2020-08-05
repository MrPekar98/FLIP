package javacc.visitors.codegeneration.templates.js;

public class UiJs implements JsModuleFile {
  
  @Override
  public String getExportedNames() {
    return "UI, FormElement";
  }
  
  @Override
  public String getName() {
    return "ui.js";
  }
  
  @Override
  public String getContent() {
    return "import { Card, Suit } from \"./card.js\";\n" +
            "\n" +
            "export class UI {\n" +
            "\n" +
            "  static createDialog(selectMap, title, options) {\n" +
            "    return new Promise(async resolve => {\n" +
            "      const result = await this.createModal(selectMap, title, options);\n" +
            "      if (result === \"cancel\") {\n" +
            "        resolve({ result: false });\n" +
            "      } else if (selectMap && Object.keys(selectMap).length) {\n" +
            "        // Has result\n" +
            "        resolve({\n" +
            "          result: true,\n" +
            "          value: selectMap[result].value\n" +
            "        });\n" +
            "      } else if (!selectMap || !Object.keys(selectMap).length) {\n" +
            "        // Pressed ok button, but no select value.\n" +
            "        resolve({ result: true });\n" +
            "      } else {\n" +
            "        console.error(\"How did we get here?\");\n" +
            "        resolve({ result: false });\n" +
            "      }\n" +
            "    });\n" +
            "  }\n" +
            "\n" +
            "  static createModal(selectMap, title, options) {\n" +
            "    let modal = `<div class=\"modal fade\" id=\"ui-modal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalCenterTitle\" aria-hidden=\"true\">\n" +
            "  <div class=\"modal-dialog modal-dialog-centered\" role=\"document\">\n" +
            "    <div class=\"modal-content\">\n" +
            "      <div class=\"modal-header\">\n" +
            "        <h5 class=\"modal-title\" id=\"exampleModalCenterTitle\">${title}</h5>\n" +
            "      </div>\n" +
            "      <div class=\"modal-body\">\t\n" +
            "    ${this.createOptionsFromMap(selectMap)}\n" +
            "\t </div>\n" +
            "      <div class=\"modal-footer\">\n" +
            "        ${this.createButtons(options)}\n" +
            "      </div>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>`;\n" +
            "    $(\"#ui\").html(modal);\n" +
            "    $('#ui-modal').modal({ show: true, backdrop: \"static\" });\n" +
            "\n" +
            "    return new Promise((resolve, reject) => {\n" +
            "      $(\"#ui-ok\").click(() => {\n" +
            "        this.closeModal();\n" +
            "        resolve($(\"select#select\").val());\n" +
            "      });\n" +
            "      $(\"#ui-cancel\").click(() => {\n" +
            "        this.closeModal();\n" +
            "        resolve(\"cancel\");\n" +
            "      });\n" +
            "    });\n" +
            "  }\n" +
            "\n" +
            "  static createOptionsFromMap(selectMap) {\n" +
            "    if (!selectMap || !Object.keys(selectMap).length) {\n" +
            "      return \"\";\n" +
            "    }\n" +
            "\n" +
            "    let result = `<form>\n" +
            "    <select id=\"select\" class=\"form-control form-control-sm\">`\n" +
            "\n" +
            "    for (let key in selectMap) {\n" +
            "      if (selectMap.hasOwnProperty(key)) {\n" +
            "        result += selectMap[key].getHtml() + \"\\n\";\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    result += `</select>\n" +
            "    </form>`;\n" +
            "\n" +
            "    return result;\n" +
            "  }\n" +
            "\n" +
            "  static closeModal() {\n" +
            "    $(\"#ui-modal\").modal(\"hide\");\n" +
            "    $(\".modal-backdrop\").remove();\n" +
            "    $(\"body\").removeClass(\"modal-open\");\n" +
            "  }\n" +
            "\n" +
            "  static createButtons(options) {\n" +
            "    let buttonHtml = \"\";\n" +
            "\n" +
            "    if (options[\"buttonCancel\"]) {\n" +
            "      buttonHtml += `<button id=\"ui-cancel\" type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">${options[\"buttonCancel\"]}</button>`;\n" +
            "    }\n" +
            "\n" +
            "    buttonHtml += `<button id=\"ui-ok\" type=\"button\" class=\"btn btn-primary\" data-dismiss=\"modal\">${options[\"buttonOk\"]}</button>`;\n" +
            "\n" +
            "    return buttonHtml;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "export class FormElement {\n" +
            "  constructor(value, name) {\n" +
            "    this.value = value;\n" +
            "    this.name = name;\n" +
            "\n" +
            "    if (value instanceof Card) {\n" +
            "      this.class = (value.suit == Suit.HEARTS || value.suit == Suit.DIAMONDS) ? \"card-red\" : \"card-black\";\n" +
            "\n" +
            "      switch (value.suit) {\n" +
            "        case Suit.HEARTS:\n" +
            "          this.suffix = \" ♥\";\n" +
            "          break;\n" +
            "        case Suit.DIAMONDS:\n" +
            "          this.suffix = \" ♦\";\n" +
            "          break;\n" +
            "        case Suit.SPADES:\n" +
            "          this.suffix = \" ♠\";\n" +
            "          break;\n" +
            "        case Suit.CLUBS:\n" +
            "          this.suffix = \" ♣\";\n" +
            "          break;\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  getHtml() {\n" +
            "    return `<option class=\"${this.class || \"\"}\" value=\"${this.name}\">${this.name}${this.suffix || \"\"}</option>`;\n" +
            "  }\n" +
            "}";
  }
}
