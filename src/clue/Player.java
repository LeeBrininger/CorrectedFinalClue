package clue;
import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Field;
import java.util.ArrayList;



public abstract class Player {
	
	private final int PLAYER_DIAMETER = 25;
	
	private ArrayList<Card> myCards;

	private String name;
	private Color color;
	private int startLocation;
	private BoardCell currentLocation;
	
	public Player (String name, String color, int startLocation) {
		this.name = name;
		if (color.equals("PURPLE")) this.color = new Color(102,0,102);
		else this.color = convertColor(color);
		this.startLocation = startLocation;
		currentLocation = null;
		myCards = new ArrayList<Card>();
	}
	
	public Card disproveSuggestion(String person, String room, String weapon) {
		return null;
	}
	
	public void setCurrentLocation(BoardCell currentLocation) {
		this.currentLocation = currentLocation;
	}
	
	public BoardCell getCurrentLocation() {
		return currentLocation;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public ArrayList<Card> getCards() {
		return myCards;
	}
	
	public int getStartLocation() {
		return startLocation;
	}
	
	public void giveCard(Card newCard) {
		myCards.add(newCard);
	}
	
	public Color convertColor(String strColor) {
		Color color; 
		try {     
			// We can use reflection to convert the string to a color
			Field field = Class.forName("java.awt.Color").getField(strColor.trim());     
			color = (Color)field.get(null); } 
		catch (Exception e) {  
			color = null; // Not defined } 
		}
		return color;
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(currentLocation.getColumn()*PLAYER_DIAMETER, currentLocation.getRow()*PLAYER_DIAMETER, PLAYER_DIAMETER, PLAYER_DIAMETER);

		g.setColor(Color.BLACK);
		g.drawOval(currentLocation.getColumn()*PLAYER_DIAMETER, currentLocation.getRow()*PLAYER_DIAMETER, PLAYER_DIAMETER, PLAYER_DIAMETER);
	}	
	
	@Override
	public String toString() {
		return name;
	}

	public abstract void handleTurn(ClueGame game, int roll);
	
	
}
