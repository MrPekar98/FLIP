import { Card, Suit } from "./card.js";

export class UI {

  static createDialog(selectMap, title, options) {
    return new Promise(async resolve => {
      const result = await this.createModal(selectMap, title, options);
      if (result === "cancel") {
        resolve({ result: false });
      } else if (selectMap && Object.keys(selectMap).length) {
        // Has result
        resolve({
          result: true,
          value: selectMap[result].value
        });
      } else if (!selectMap || !Object.keys(selectMap).length) {
        // Pressed ok button, but no select value.
        resolve({ result: true });
      } else {
        console.error("How did we get here?");
        resolve({ result: false });
      }
    });
  }

  static createModal(selectMap, title, options) {
    let modal = `<div class="modal fade" id="ui-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalCenterTitle">${title}</h5>
      </div>
      <div class="modal-body">	
    ${this.createOptionsFromMap(selectMap)}
	 </div>
      <div class="modal-footer">
        ${this.createButtons(options)}
      </div>
    </div>
  </div>
</div>`;
    $("#ui").html(modal);
    $('#ui-modal').modal({ show: true, backdrop: "static" });

    return new Promise((resolve, reject) => {
      $("#ui-ok").click(() => {
        this.closeModal();
        resolve($("select#select").val());
      });
      $("#ui-cancel").click(() => {
        this.closeModal();
        resolve("cancel");
      });
    });
  }

  static createOptionsFromMap(selectMap) {
    if (!selectMap || !Object.keys(selectMap).length) {
      return "";
    }

    let result = `<form>
    <select id="select" class="form-control form-control-sm">`

    for (let key in selectMap) {
      if (selectMap.hasOwnProperty(key)) {
        result += selectMap[key].getHtml() + "\n";
      }
    }

    result += `</select>
    </form>`;

    return result;
  }

  static closeModal() {
    $("#ui-modal").modal("hide");
    $(".modal-backdrop").remove();
    $("body").removeClass("modal-open");
  }

  static createButtons(options) {
    let buttonHtml = "";

    if (options["buttonCancel"]) {
      buttonHtml += `<button id="ui-cancel" type="button" class="btn btn-secondary" data-dismiss="modal">${options["buttonCancel"]}</button>`;
    }

    buttonHtml += `<button id="ui-ok" type="button" class="btn btn-primary" data-dismiss="modal">${options["buttonOk"]}</button>`;

    return buttonHtml;
  }
}

export class FormElement {
  constructor(value, name) {
    this.value = value;
    this.name = name;

    if (value instanceof Card) {
      this.class = (value.suit == Suit.HEARTS || value.suit == Suit.DIAMONDS) ? "card-red" : "card-black";

      switch (value.suit) {
        case Suit.HEARTS:
          this.suffix = " ♥";
          break;
        case Suit.DIAMONDS:
          this.suffix = " ♦";
          break;
        case Suit.SPADES:
          this.suffix = " ♠";
          break;
        case Suit.CLUBS:
          this.suffix = " ♣";
          break;
      }
    }
  }

  getHtml() {
    return `<option class="${this.class || ""}" value="${this.name}">${this.name}${this.suffix || ""}</option>`;
  }
}