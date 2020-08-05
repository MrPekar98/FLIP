import { Game } from "./game.js";
import { UI, FormElement } from "./ui.js";

startGame();

async function startGame() {
  let selectMap = {};

  for (let i = 2; i < 9; i++) {
    selectMap[i] = new FormElement(i, i);
  }

  let result = await UI.createDialog(selectMap, "Select number of players", { buttonOk: "Start Game" });
  let title = document.getElementById("title");
  title.parentNode.removeChild(title);  let game = new Game(result.value);
}