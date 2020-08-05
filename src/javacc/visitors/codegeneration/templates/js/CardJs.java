package javacc.visitors.codegeneration.templates.js;

public class CardJs implements JsModuleFile {
  
  @Override
  public String getExportedNames() {
    return "Card, Suit, Rank";
  }
  
  @Override
  public String getName() {
    return "card.js";
  }
  
  @Override
  public String getContent() {
    return "export class Suit {\n" +
            "  static SPADES = new Suit(1, \"spades\");\n" +
            "  static CLUBS = new Suit(2, \"clubs\");\n" +
            "  static HEARTS = new Suit(3, \"hearts\");\n" +
            "  static DIAMONDS = new Suit(4, \"diamonds\");\n" +
            "\n" +
            "  constructor(value, name) {\n" +
            "    this.value = value;\n" +
            "    this.name = name;\n" +
            "  }\n" +
            "\n" +
            "  static fromValue(number) {\n" +
            "    switch (number) {\n" +
            "      case 1:\n" +
            "        return this.SPADES;\n" +
            "      case 2:\n" +
            "        return this.CLUBS;\n" +
            "      case 3:\n" +
            "        return this.HEARTS;\n" +
            "      case 4:\n" +
            "        return this.DIAMONDS;\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  static forEach(callback) {\n" +
            "   for (let i = 1; i < 4; i++) {\n" +
            "     callback(this.fromValue(i));\n" +
            "   }\n" +
            "  }\n" +
            "\n" +
            "  valueOf() {\n" +
            "    return this.value;\n" +
            "  }\n" +
            "\n" +
            "  toString() {\n" +
            "    return this.name;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "export class Rank {\n" +
            "  static ACE = new Rank(1, \"ace\");\n" +
            "  static TWO = new Rank(2, \"two\");\n" +
            "  static THREE = new Rank(3, \"three\");\n" +
            "  static FOUR = new Rank(4, \"four\");\n" +
            "  static FIVE = new Rank(5, \"five\");\n" +
            "  static SIX = new Rank(6, \"six\");\n" +
            "  static SEVEN = new Rank(7, \"seven\");\n" +
            "  static EIGHT = new Rank(8, \"eight\");\n" +
            "  static NINE = new Rank(9, \"nine\");\n" +
            "  static TEN = new Rank(10, \"ten\");\n" +
            "  static JACK = new Rank(11, \"jack\");\n" +
            "  static QUEEN = new Rank(12, \"queen\");\n" +
            "  static KING = new Rank(13, \"king\");\n" +
            "\n" +
            "  constructor(value, name) {\n" +
            "    this.value = value;\n" +
            "    this.name = name;\n" +
            "  }\n" +
            "\n" +
            "  static fromValue(number) {\n" +
            "    switch (number) {\n" +
            "      case 1:\n" +
            "        return this.ACE;\n" +
            "      case 2:\n" +
            "        return this.TWO;\n" +
            "      case 3:\n" +
            "        return this.THREE;\n" +
            "      case 4:\n" +
            "        return this.FOUR;\n" +
            "      case 5:\n" +
            "        return this.FIVE;\n" +
            "      case 6:\n" +
            "        return this.SIX;\n" +
            "      case 7:\n" +
            "        return this.SEVEN;\n" +
            "      case 8:\n" +
            "        return this.EIGHT;\n" +
            "      case 9:\n" +
            "        return this.NINE;\n" +
            "      case 10:\n" +
            "        return this.TEN;\n" +
            "      case 11:\n" +
            "        return this.JACK;\n" +
            "      case 12:\n" +
            "        return this.QUEEN;\n" +
            "      case 13:\n" +
            "        return this.KING;\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  static forEach(callback) {\n" +
            "    for (let i = 1; i < 14; i++) {\n" +
            "      callback(this.fromValue(i));\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  valueOf() {\n" +
            "    return this.value;\n" +
            "  }\n" +
            "\n" +
            "  toString() {\n" +
            "   return this.name;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "export class Card {\n" +
            "  constructor(rank, suit) {\n" +
            "    this.rank = rank;\n" +
            "    this.suit = suit;\n" +
            "  }\n" +
            "\n" +
            "  toString() {\n" +
            "    return `${this.rank.toString()} of ${this.suit.toString()}`;\n" +
            "  }\n" +
            "}";
  }
}
