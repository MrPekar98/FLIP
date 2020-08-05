package javacc.visitors.codegeneration.templates.js;

public class UtilsJs implements JsModuleFile {
  @Override
  public String getExportedNames() {
    return "Utils";
  }
  
  @Override
  public String getName() {
    return "utils.js";
  }
  
  @Override
  public String getContent() {
    return "import { Card, Rank, Suit } from \"./card.js\";\n" +
            "import { Pile } from \"./pile.js\";\n" +
            "import { Player, State } from \"./player.js\";\n" +
            "\n" +
            "export class Utils {\n" +
            "  static contains(array, element) {\n" +
            "    if (this.indexOf(array, element) !== -1) {\n" +
            "      return true;\n" +
            "    }\n" +
            "\n" +
            "    return false;\n" +
            "  }\n" +
            "\n" +
            "  static sequenceUnion(array, element) {\n" +
            "    if (!Array.isArray(array)) {\n" +
            "      if (Array.isArray(element)) {\n" +
            "        return [...element];\n" +
            "      }\n" +
            "\n" +
            "      return [element];\n" +
            "    }\n" +
            "\n" +
            "    if (Array.isArray(element)) {\n" +
            "      return [...array, ...element];\n" +
            "    }\n" +
            "\n" +
            "    return [...array, element];\n" +
            "  }\n" +
            "\n" +
            "  static sequenceComplement(array, element) {\n" +
            "    let newArray = [];\n" +
            "\n" +
            "    array.forEach((e) => {\n" +
            "      if (e != element) {\n" +
            "        newArray.push(e);\n" +
            "        return;\n" +
            "      }\n" +
            "    });\n" +
            "\n" +
            "    return newArray;\n" +
            "  }\n" +
            "\n" +
            "  static isEqual(value, other) {\n" +
            "    // Get the value type\n" +
            "    let type = Object.prototype.toString.call(value);\n" +
            "\n" +
            "    // If the two objects are not the same type, return false\n" +
            "    if (type !== Object.prototype.toString.call(other)) return false;\n" +
            "\n" +
            "    // If completely equal.\n" +
            "    if (value === other) return true;\n" +
            "\n" +
            "    // If items are not an object or array, return false\n" +
            "    if (['[object Array]', '[object Object]'].indexOf(type) < 0) return false;\n" +
            "\n" +
            "    // Compare the length of the length of the two items\n" +
            "    let valueLen = type === '[object Array]' ? value.length : Object.keys(value).length;\n" +
            "    let otherLen = type === '[object Array]' ? other.length : Object.keys(other).length;\n" +
            "    if (valueLen !== otherLen) return false;\n" +
            "\n" +
            "    // Compare two items\n" +
            "    let compare = function (item1, item2) {\n" +
            "      // Get the object type\n" +
            "      let itemType = Object.prototype.toString.call(item1);\n" +
            "\n" +
            "      // If an object or array, compare recursively\n" +
            "      if (['[object Array]', '[object Object]'].indexOf(itemType) >= 0) {\n" +
            "        if (!isEqual(item1, item2)) return false;\n" +
            "      }\n" +
            "\n" +
            "      // Otherwise, do a simple comparison\n" +
            "      else {\n" +
            "\n" +
            "        // If the two items are not the same type, return false\n" +
            "        if (itemType !== Object.prototype.toString.call(item2)) return false;\n" +
            "\n" +
            "        // Else if it's a function, convert to a string and compare\n" +
            "        // Otherwise, just compare\n" +
            "        if (itemType === '[object Function]') {\n" +
            "          if (item1.toString() !== item2.toString()) return false;\n" +
            "        } else {\n" +
            "          if (item1 !== item2) return false;\n" +
            "        }\n" +
            "      }\n" +
            "    };\n" +
            "\n" +
            "    // Compare properties\n" +
            "    if (type === '[object Array]') {\n" +
            "      for (let i = 0; i < valueLen; i++) {\n" +
            "        if (compare(value[i], other[i]) === false) return false;\n" +
            "      }\n" +
            "    } else {\n" +
            "      for (let key in value) {\n" +
            "        if (value.hasOwnProperty(key)) {\n" +
            "          if (compare(value[key], other[key]) === false) return false;\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    // If nothing failed, return true\n" +
            "    return true;\n" +
            "  }\n" +
            "\n" +
            "  static indexOf(array, element) {\n" +
            "    let lookIn = array;\n" +
            "\n" +
            "    if (array instanceof Pile) {\n" +
            "      lookIn = array.cards;\n" +
            "    }\n" +
            "\n" +
            "    for (let i = 0; i < lookIn.length; i++) {\n" +
            "      if (this.objectEquals(element, lookIn[i])) {\n" +
            "        return i;\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    return -1;\n" +
            "  }\n" +
            "\n" +
            "  static objectEquals(o1, o2) {\n" +
            "    // TODO: Make all of these\n" +
            "    // Also recursive for Sequence<Sequence<Card>>\n" +
            "    if (o1 instanceof Card) {\n" +
            "      return this.cardEquals(o1, o2);\n" +
            "    } else if (o1 instanceof Player) {\n" +
            "      return this.playerEquals(o1, o2);\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  static cardEquals(c1, c2) {\n" +
            "    if (c1.rank == c2.rank && c1.suit == c2.suit) {\n" +
            "      return true;\n" +
            "    }\n" +
            "    return false;\n" +
            "  }\n" +
            "\n" +
            "  static move(from, to, cards) {\n" +
            "    let cardArray = [];\n" +
            "\n" +
            "    if (cards instanceof Pile) {\n" +
            "      cardArray = [...cards.cards];\n" +
            "    } else if (Array.isArray(cards)) {\n" +
            "      cardArray = [...cards];\n" +
            "    } else {\n" +
            "      cardArray = [cards];\n" +
            "    }\n" +
            "\n" +
            "    for (let i = 0; i < cardArray.length; i++) {\n" +
            "      let index = Utils.indexOf(from, cardArray[i]);\n" +
            "      if (index === -1) {\n" +
            "        throw `\"From\" pile does not contain the specified card`;\n" +
            "      }\n" +
            "\n" +
            "      to.cards.splice(to.cards.length, 0, from.cards[index]);\n" +
            "      from.cards.splice(index, 1);\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  static deal(deck, to, count) {\n" +
            "    if (deck.cards.length < count) {\n" +
            "      throw `Deck has ${deck.cards.length} cards, but ${count} was requested`;\n" +
            "    }\n" +
            "\n" +
            "    for (let i = 0; i < count; i++) {\n" +
            "      to.cards.splice(to.cards.length, 0, deck.cards[deck.cards.length - 1]);\n" +
            "      deck.cards.splice(deck.cards.length - 1, 1);\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  static play(hand, to, cards) {\n" +
            "    let move = cards;\n" +
            "    if (cards instanceof Card) {\n" +
            "      move = [cards];\n" +
            "    } else if (cards instanceof Pile) {\n" +
            "      move = cards.cards;\n" +
            "    }\n" +
            "\n" +
            "    let removeIndices = [];\n" +
            "\n" +
            "    for (let i = 0; i < move.length; i++) {\n" +
            "      let index = this.indexOf(hand, move[i]);\n" +
            "\n" +
            "      if (index === -1) {\n" +
            "        throw `The current player's hand does not contain ${move[i].toString()}`;\n" +
            "      }\n" +
            "\n" +
            "      removeIndices.push(index);\n" +
            "    }\n" +
            "\n" +
            "    removeIndices.sort((a, b) => b - a);\n" +
            "\n" +
            "    // Remove cards from hand.\n" +
            "    for (let i = 0; i < removeIndices.length; i++) {\n" +
            "      hand.cards.splice(removeIndices[i], 1);\n" +
            "    }\n" +
            "\n" +
            "    // Move cards to pile.\n" +
            "    to.cards.splice(to.cards.length, 0, ...move);\n" +
            "  }\n" +
            "\n" +
            "  static shuffle(array) {\n" +
            "    let temp, j;\n" +
            "\n" +
            "    for (let i = array.length - 1; i > 0; i--) {\n" +
            "      j = Math.floor(Math.random() * (i + 1));\n" +
            "\n" +
            "      temp = array[j];\n" +
            "      array[j] = array[i];\n" +
            "      array[i] = temp;\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  static stageDone(players) {\n" +
            "    for (let i = 0; i < players.length; i++) {\n" +
            "      if (players[i].state != State.OUT && players[i].state != State.DONE) {\n" +
            "        return false;\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    return true;\n" +
            "  }\n" +
            "\n" +
            "  static nextPlayer(currentPlayer, players) {\n" +
            "    if (currentPlayer < players.length - 1) {\n" +
            "      return currentPlayer + 1;\n" +
            "    }\n" +
            "\n" +
            "    return 0;\n" +
            "  }\n" +
            "\n" +
            "  static setReadyPlayers(players) {\n" +
            "    for (let i = 0; i < players.length; i++) {\n" +
            "      if (players[i].state == State.DONE) {\n" +
            "        players[i].state = State.NEXT;\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  static firstReadyPlayer(players) {\n" +
            "    for (let i = 0; i < players.length; i++) {\n" +
            "      if (players[i].state == State.NEXT) {\n" +
            "        return i;\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    return -1;\n" +
            "  }\n" +
            "\n" +
            "\n" +
            "  static generateDeck() {\n" +
            "    let deck = new Pile(false, \"Deck\");\n" +
            "\n" +
            "    deck.cards = this.generateAllCards();\n" +
            "\n" +
            "    Utils.shuffle(deck.cards);\n" +
            "    return deck;\n" +
            "  }\n" +
            "\n" +
            "  static generateAllCards() {\n" +
            "    let cards = [];\n" +
            "\n" +
            "    for (let s = 1; s < 5; s++) {\n" +
            "      for (let r = 1; r < 14; r++) {\n" +
            "        cards.push(new Card(Rank.fromValue(r), Suit.fromValue(s)));\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    return cards;\n" +
            "  }\n" +
            "\n" +
            "\n" +
            "  static allSuits = Object.freeze(Suit.fromValue(1), Suit.fromValue(2), Suit.fromValue(3), Suit.fromValue(4));\n" +
            "  static allRanks = Object.freeze(Suit.fromValue(1), Suit.fromValue(2), Suit.fromValue(3), Suit.fromValue(4));\n" +
            "  static allCards = Object.freeze(Utils.generateAllCards());\n" +
            "}";
  }
}
