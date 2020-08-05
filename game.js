import { Player, State } from "./player.js";
import { Utils } from "./utils.js";
import { UI, FormElement } from "./ui.js";
import { Card, Suit, Rank } from "./card.js";
import { Pile } from "./pile.js";

import { Renderer } from "./rendering.js";

export class Game {
  constructor(playerCount) {
    this.$deck$ = Utils.generateDeck();
    this.$piles$ = new Array(1).fill().map((x, index) => new Pile(true, `Pile ${index + 1}`));
    this.$players$ = new Array(playerCount).fill().map((x, index) => new Player(index));
    this.$winner$;
    this.$renderer$ = new Renderer(this.$deck$, this.$piles$, this.$players$);
    this.winner;
    this.$stages$ = [{preCode: this.$pre_stage_dealStage$.bind(this), code: this.$stage_dealStage$.bind(this), nextStage: 1}, {preCode: this.$pre_stage_gameStage$.bind(this), code: this.$stage_gameStage$.bind(this), nextStage: 1}];
    this.$play$();
  }

  async $play$() {
    let $currentPlayer$ = 0;
    let $currentStage$ = 0;

    // Execute pre code
    this.$stages$[0].preCode();
    this.$renderer$.setCurrentPlayer(0);  
    while (!this.$winner$) {
      this.$renderer$.hideHands();
      await UI.createDialog({}, `${this.$players$[$currentPlayer$].toString()}'s turn`, { buttonOk: "Start turn" });
      this.$renderer$.setCurrentPlayer($currentPlayer$);
      switch (this.$players$[$currentPlayer$].state) {
        case State.SKIP:
          this.$players$[$currentPlayer$].state = State.NEXT;
        case State.DONE:
        case State.OUT:
          await UI.createDialog({}, `${this.$players$[$currentPlayer$].toString()} does not have a turn`, { buttonOk: "Ok" });
          $currentPlayer$ = Utils.nextPlayer($currentPlayer$, this.$players$);
          break;
        case State.NEXT:
          // Run stage code for this player.
          await this.$stages$[$currentStage$].code($currentPlayer$);
          await UI.createDialog({}, "End my turn", { buttonOk: "Ok" });
          if (this.$winner$) {
            break;
          }

          if (Utils.stageDone(this.$players$)) {
            // Change done to next.
            Utils.setReadyPlayers(this.$players$);

            // Set current stage.
            $currentStage$ = this.$stages$[$currentStage$].nextStage;

            // Set the first player of the next stage.
            $currentPlayer$ = Utils.firstReadyPlayer(this.$players$);

            // Execute pre stage code.
            this.$stages$[$currentStage$].preCode();
            if (this.$winner$) {
              break;
            }
          } else if (!this.$winner$) {
            $currentPlayer$ = Utils.nextPlayer($currentPlayer$, this.$players$);
          }

          break;
        default:
          break;
      }
    }
    await UI.createDialog({}, `${this.$winner$.toString()} has won the game!`, { buttonOk: "Ok"});
  }

  $pre_stage_dealStage$() {
}

  async $stage_dealStage$($currentPlayer$) {
Utils.deal(this.$deck$, (this.$players$[$currentPlayer$].hand), 5);
this.$players$[$currentPlayer$].state = State.DONE;
return;
}

  $pre_stage_gameStage$() {
Utils.deal(this.$deck$, this.$piles$[0], 1);
}

  async $stage_gameStage$($currentPlayer$) {
if (((this.topCard(this.$piles$[0]).rank) == Rank.ACE)) {
    for (let $i_0$ = 0; $i_0$ < 1; $i_0$++) {
    let $selectFrom_0$ = {};
    
    Utils.allCards.forEach(c => {
      if(((Utils.contains((this.$players$[$currentPlayer$].hand), c)) && ((c.rank) == Rank.ACE))) {
        $selectFrom_0$[c.toString()] = new FormElement(c, c.toString());
      }
    });
    
    if (Object.keys($selectFrom_0$).length) {
      let $result$ = await UI.createDialog($selectFrom_0$, "Put ace or be skipped", { buttonOk: "Select", buttonCancel: "Skip" });
      if (!$result$ || ($result$ && $result$.result === false)) {
        break;
      }
      let c = $result$.value;
      
      // Option block begins here
            Utils.move((this.$players$[$currentPlayer$].hand), this.$piles$[0], c);
// Option block ends here
    } else {
      break;
    }
  }
  this.$players$[$currentPlayer$].state = State.NEXT;
  return;
}
let mustDraw = this.checkPass(this.$players$[$currentPlayer$]);
if ((mustDraw == true)) {
    Utils.deal(this.$deck$, (this.$players$[$currentPlayer$].hand), 1);
  this.$players$[$currentPlayer$].state = State.NEXT;
  return;
}
mustDraw = true;
for (let $i_0$ = 0; $i_0$ < 1; $i_0$++) {
  let $selectFrom_1$ = {};
  
  Utils.allCards.forEach(card => {
    if(((Utils.contains((this.$players$[$currentPlayer$].hand), card)) && this.playableCard(card, this.topCard(this.$piles$[0])))) {
      $selectFrom_1$[card.toString()] = new FormElement(card, card.toString());
    }
  });
  
  if (Object.keys($selectFrom_1$).length) {
    let $result$ = await UI.createDialog($selectFrom_1$, "Select card to place", { buttonOk: "Select", buttonCancel: "Skip" });
    if (!$result$ || ($result$ && $result$.result === false)) {
      break;
    }
    let card = $result$.value;
    
    // Option block begins here
        mustDraw = false;
if (((((    this.topCard(this.$piles$[0]).rank) == Rank.SEVEN) || (this.topCard(this.$piles$[1]) == new Card(Rank.KING, Suit.SPADES))) && ((!(card == new Card(Rank.KING, Suit.SPADES))) || (!((card.rank) == Rank.SEVEN))))) {
            Utils.deal(this.$deck$, (this.$players$[$currentPlayer$].hand), this.amountGiven(this.$piles$[0]));
}
    Utils.move((this.$players$[$currentPlayer$].hand), this.$piles$[0], card);
// Option block ends here
  } else {
    break;
  }
}
if ((mustDraw == true)) {
    Utils.deal(this.$deck$, (this.$players$[$currentPlayer$].hand), 1);
} else if (this.isWinner(this.$players$[$currentPlayer$])) {
    winner = this.$players$[$currentPlayer$];
}
}

  playableCard(playerCard, topCard) {
    let result = false;
if ((((playerCard.rank) == Rank.QUEEN) || (((playerCard.suit) == (topCard.suit)) || ((playerCard.rank) == (topCard.rank))))) {
    result = true;
}
return result;
  }

  topCard(somePile) {
    let result;
somePile.forEach(c => {
    result = c;
});
return result;
  }

  isWinner(somePlayer) {
    let result = false;
if (((!((this.topCard(this.$piles$[0]).rank) == Rank.SEVEN)) && ((!((this.topCard(this.$piles$[0]).rank) == Rank.KING)) && (!((this.topCard(this.$piles$[0]).suit) == Suit.SPADES))) && (((somePlayer.hand).cards.length) == 0))) {
    result = true;
}
return result;
  }

  checkPass(somePlayer) {
    let played = true;
(somePlayer.hand).forEach(c => {
    if (this.playableCard(c, this.topCard(this.$piles$[0]))) {
        played = false;
}
});
return played;
  }

  amountGiven(playPile) {
    let count = 0;
playPile.forEach(c => {
    if (((c.rank) == Rank.SEVEN)) {
        count = (count + 2);
} else if ((c == new Card(Rank.KING, Suit.SPADES))) {
        count = (count + 5);
}
  else {
        count = 0;
}
});
return count;
  }
}