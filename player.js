import { Pile } from "./pile.js";

export let State = Object.freeze({NEXT: 1, DONE: 2, SKIP: 3, OUT: 4});

export class Player {
  constructor(index) {
    // Default fields
    this.index = index
    this.state = State.NEXT;
    this.hand = new Pile(true, `Player ${index + 1}'s hand`, "hand");
  }

  toString() {
    return `Player ${this.index + 1}`;
  }
}