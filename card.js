export class Suit {
  static SPADES = new Suit(1, "spades");
  static CLUBS = new Suit(2, "clubs");
  static HEARTS = new Suit(3, "hearts");
  static DIAMONDS = new Suit(4, "diamonds");

  constructor(value, name) {
    this.value = value;
    this.name = name;
  }

  static fromValue(number) {
    switch (number) {
      case 1:
        return this.SPADES;
      case 2:
        return this.CLUBS;
      case 3:
        return this.HEARTS;
      case 4:
        return this.DIAMONDS;
    }
  }

  static forEach(callback) {
   for (let i = 1; i < 4; i++) {
     callback(this.fromValue(i));
   }
  }

  valueOf() {
    return this.value;
  }

  toString() {
    return this.name;
  }
}

export class Rank {
  static ACE = new Rank(1, "ace");
  static TWO = new Rank(2, "two");
  static THREE = new Rank(3, "three");
  static FOUR = new Rank(4, "four");
  static FIVE = new Rank(5, "five");
  static SIX = new Rank(6, "six");
  static SEVEN = new Rank(7, "seven");
  static EIGHT = new Rank(8, "eight");
  static NINE = new Rank(9, "nine");
  static TEN = new Rank(10, "ten");
  static JACK = new Rank(11, "jack");
  static QUEEN = new Rank(12, "queen");
  static KING = new Rank(13, "king");

  constructor(value, name) {
    this.value = value;
    this.name = name;
  }

  static fromValue(number) {
    switch (number) {
      case 1:
        return this.ACE;
      case 2:
        return this.TWO;
      case 3:
        return this.THREE;
      case 4:
        return this.FOUR;
      case 5:
        return this.FIVE;
      case 6:
        return this.SIX;
      case 7:
        return this.SEVEN;
      case 8:
        return this.EIGHT;
      case 9:
        return this.NINE;
      case 10:
        return this.TEN;
      case 11:
        return this.JACK;
      case 12:
        return this.QUEEN;
      case 13:
        return this.KING;
    }
  }

  static forEach(callback) {
    for (let i = 1; i < 14; i++) {
      callback(this.fromValue(i));
    }
  }

  valueOf() {
    return this.value;
  }

  toString() {
   return this.name;
  }
}

export class Card {
  constructor(rank, suit) {
    this.rank = rank;
    this.suit = suit;
  }

  toString() {
    return `${this.rank.toString()} of ${this.suit.toString()}`;
  }
}