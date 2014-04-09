package clue;

public class Card {
	public enum CardType {ROOM, WEAPON, PLAYER};
	
	public String name;
	private CardType type;
	
	
	public Card(CardType type, String name) {
		this.name = name;
		this.type = type;
	}
	
	public CardType getCardType() {
		return type;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Card)) return false;
		Card c = (Card) o;
		if (this.name.equals(c.getName()) && this.type == (c.getCardType())) return true;
		else return false;
	}

	@Override
	public String toString() {
		return "Name: " + name + ", Type: " + type;
	}
	
}
