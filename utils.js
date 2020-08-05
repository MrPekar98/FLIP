import { Card, Rank, Suit } from "./card.js";
import { Pile } from "./pile.js";
import { Player, State } from "./player.js";

export class Utils {
  static contains(array, element) {
    if (this.indexOf(array, element) !== -1) {
      return true;
    }

    return false;
  }

  static sequenceUnion(array, element) {
    if (!Array.isArray(array)) {
      if (Array.isArray(element)) {
        return [...element];
      }

      return [element];
    }

    if (Array.isArray(element)) {
      return [...array, ...element];
    }

    return [...array, element];
  }

  static sequenceComplement(array, element) {
    let newArray = [];

    array.forEach((e) => {
      if (e != element) {
        newArray.push(e);
        return;
      }
    });

    return newArray;
  }

  static isEqual(value, other) {
    // Get the value type
    let type = Object.prototype.toString.call(value);

    // If the two objects are not the same type, return false
    if (type !== Object.prototype.toString.call(other)) return false;

    // If completely equal.
    if (value === other) return true;

    // If items are not an object or array, return false
    if (['[object Array]', '[object Object]'].indexOf(type) < 0) return false;

    // Compare the length of the length of the two items
    let valueLen = type === '[object Array]' ? value.length : Object.keys(value).length;
    let otherLen = type === '[object Array]' ? other.length : Object.keys(other).length;
    if (valueLen !== otherLen) return false;

    // Compare two items
    let compare = function (item1, item2) {
      // Get the object type
      let itemType = Object.prototype.toString.call(item1);

      // If an object or array, compare recursively
      if (['[object Array]', '[object Object]'].indexOf(itemType) >= 0) {
        if (!isEqual(item1, item2)) return false;
      }

      // Otherwise, do a simple comparison
      else {

        // If the two items are not the same type, return false
        if (itemType !== Object.prototype.toString.call(item2)) return false;

        // Else if it's a function, convert to a string and compare
        // Otherwise, just compare
        if (itemType === '[object Function]') {
          if (item1.toString() !== item2.toString()) return false;
        } else {
          if (item1 !== item2) return false;
        }
      }
    };

    // Compare properties
    if (type === '[object Array]') {
      for (let i = 0; i < valueLen; i++) {
        if (compare(value[i], other[i]) === false) return false;
      }
    } else {
      for (let key in value) {
        if (value.hasOwnProperty(key)) {
          if (compare(value[key], other[key]) === false) return false;
        }
      }
    }

    // If nothing failed, return true
    return true;
  }

  static indexOf(array, element) {
    let lookIn = array;

    if (array instanceof Pile) {
      lookIn = array.cards;
    }

    for (let i = 0; i < lookIn.length; i++) {
      if (this.objectEquals(element, lookIn[i])) {
        return i;
      }
    }

    return -1;
  }

  static objectEquals(o1, o2) {
    // TODO: Make all of these
    // Also recursive for Sequence<Sequence<Card>>
    if (o1 instanceof Card) {
      return this.cardEquals(o1, o2);
    } else if (o1 instanceof Player) {
      return this.playerEquals(o1, o2);
    }
  }

  static cardEquals(c1, c2) {
    if (c1.rank == c2.rank && c1.suit == c2.suit) {
      return true;
    }
    return false;
  }

  static move(from, to, cards) {
    let cardArray = [];

    if (cards instanceof Pile) {
      cardArray = [...cards.cards];
    } else if (Array.isArray(cards)) {
      cardArray = [...cards];
    } else {
      cardArray = [cards];
    }

    for (let i = 0; i < cardArray.length; i++) {
      let index = Utils.indexOf(from, cardArray[i]);
      if (index === -1) {
        throw `"From" pile does not contain the specified card`;
      }

      to.cards.splice(to.cards.length, 0, from.cards[index]);
      from.cards.splice(index, 1);
    }
  }

  static deal(deck, to, count) {
    if (deck.cards.length < count) {
      throw `Deck has ${deck.cards.length} cards, but ${count} was requested`;
    }

    for (let i = 0; i < count; i++) {
      to.cards.splice(to.cards.length, 0, deck.cards[deck.cards.length - 1]);
      deck.cards.splice(deck.cards.length - 1, 1);
    }
  }

  static play(hand, to, cards) {
    let move = cards;
    if (cards instanceof Card) {
      move = [cards];
    } else if (cards instanceof Pile) {
      move = cards.cards;
    }

    let removeIndices = [];

    for (let i = 0; i < move.length; i++) {
      let index = this.indexOf(hand, move[i]);

      if (index === -1) {
        throw `The current player's hand does not contain ${move[i].toString()}`;
      }

      removeIndices.push(index);
    }

    removeIndices.sort((a, b) => b - a);

    // Remove cards from hand.
    for (let i = 0; i < removeIndices.length; i++) {
      hand.cards.splice(removeIndices[i], 1);
    }

    // Move cards to pile.
    to.cards.splice(to.cards.length, 0, ...move);
  }

  static shuffle(array) {
    let temp, j;

    for (let i = array.length - 1; i > 0; i--) {
      j = Math.floor(Math.random() * (i + 1));

      temp = array[j];
      array[j] = array[i];
      array[i] = temp;
    }
  }

  static stageDone(players) {
    for (let i = 0; i < players.length; i++) {
      if (players[i].state != State.OUT && players[i].state != State.DONE) {
        return false;
      }
    }

    return true;
  }

  static nextPlayer(currentPlayer, players) {
    if (currentPlayer < players.length - 1) {
      return currentPlayer + 1;
    }

    return 0;
  }

  static setReadyPlayers(players) {
    for (let i = 0; i < players.length; i++) {
      if (players[i].state == State.DONE) {
        players[i].state = State.NEXT;
      }
    }
  }

  static firstReadyPlayer(players) {
    for (let i = 0; i < players.length; i++) {
      if (players[i].state == State.NEXT) {
        return i;
      }
    }

    return -1;
  }


  static generateDeck() {
    let deck = new Pile(false, "Deck");

    deck.cards = this.generateAllCards();

    Utils.shuffle(deck.cards);
    return deck;
  }

  static generateAllCards() {
    let cards = [];

    for (let s = 1; s < 5; s++) {
      for (let r = 1; r < 14; r++) {
        cards.push(new Card(Rank.fromValue(r), Suit.fromValue(s)));
      }
    }

    return cards;
  }


  static allSuits = Object.freeze(Suit.fromValue(1), Suit.fromValue(2), Suit.fromValue(3), Suit.fromValue(4));
  static allRanks = Object.freeze(Suit.fromValue(1), Suit.fromValue(2), Suit.fromValue(3), Suit.fromValue(4));
  static allCards = Object.freeze(Utils.generateAllCards());
}