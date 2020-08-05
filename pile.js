export class Pile {
  constructor(faceUp, name, shortName) {
    this.faceUp = faceUp;
    this.cards = [];
    this.name = name;
    this.shortName = shortName || name;
    this.shortName = this.shortName.charAt(0).toUpperCase() + this.shortName.slice(1);
  }
  
  forEach(cb) {
	if (typeof cb !== 'function') {
	  cb = () => {};
	}	
    this.cards.forEach(cb);
  }

  toString() {
    return String(this.name); 
  }
}