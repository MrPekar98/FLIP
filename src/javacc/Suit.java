package javacc.generated;

public enum Suit {
  SPADES("spades"),
  CLUBS("clubs"),
  HEARTS("hearts"),
  DIAMONDS("diamonds");
  
  private String text;
  
  Suit(String text) {
    this.text = text;
  }
  
  public String getText() {
    return this.text;
  }
  
  public static Suit fromString(String text) {
    for (Suit suit : Suit.values()) {
      if (suit.text.equalsIgnoreCase(text)) {
        return suit;
      }
    }
    
    throw new IllegalArgumentException("No suit with text: " + text + " found");
  }
}
