package gameTests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import clue1.*;
import clue1.Card.CardType;


public class GameSetupTests {
	
	static ClueGame game;
	
	// Makes a new game and loads configuration files
	@BeforeClass
	public static void setup(){ 
		game = new ClueGame("componentConfig.csv");
	}
	
	/*
	 * Tests loading the people into the game. It first finds the human player
	 * and stores that in a list, and then takes two other random players from
	 * the list and tests whether or not the name, color, and starting location
	 * is correct.
	 */
	@Test
	public void testLoadPeople() {
		assertEquals(6, game.getPlayers().size());
		
		ArrayList<Player> testList = new ArrayList<Player>();
		
		// Adds the human player to the list of players to be tested
		testList.add(game.getPlayers().get(game.getHumanPlayerIndex()));
		
		// Randomly selects two different computer players and adds them to the list
		Random rand = new Random();
		int x = -1;
		while (x==game.getHumanPlayerIndex() || x == -1) x = rand.nextInt(game.getPlayers().size());
		testList.add(game.getPlayers().get(x));
		x = -1;
		while (x==game.getHumanPlayerIndex() || x == -1) x = rand.nextInt(game.getPlayers().size());
		testList.add(game.getPlayers().get(x));
		
		// For each of the players to be tested, verifies that the name, color, and
		// starting location is correct for each of them.
		for (Player player : testList) {
			switch (game.getPlayers().indexOf(player)) {
				case 0:
					assertEquals("Frodo", player.getName());
					assertEquals(new Color(102, 0, 102), player.getColor());
					assertEquals(92, player.getStartLocation());
					break;
				case 1:
					assertEquals("Sam", player.getName());
					assertEquals(Color.GREEN, player.getColor());
					assertEquals(253, player.getStartLocation());
					break;
				case 2:
					assertEquals("Aragorn", player.getName());
					assertEquals(Color.RED, player.getColor());
					assertEquals(190, player.getStartLocation());
					break;
				case 3:
					assertEquals("Gandalf", player.getName());
					assertEquals(Color.WHITE, player.getColor());
					assertEquals(10, player.getStartLocation());
					break;
				case 4:
					assertEquals("Legolas", player.getName());
					assertEquals(Color.YELLOW, player.getColor());
					assertEquals(229, player.getStartLocation());
					break;
				case 5:
					assertEquals("Gollum", player.getName());
					assertEquals(Color.BLACK, player.getColor());
					assertEquals(457, player.getStartLocation());
					break;
			}
		}
		
		
	}
	
	
	/*
	 * Tests that the deck has the right amount of cards, the right amount of
	 * each type of card, and if a selection of each type of card is in the deck
	 */
	@Test
	public void testLoadCards() {
		assertEquals(20, game.getCards().size());
		int players = 0;
		int weapons = 0;
		int rooms = 0;
		
		// Goes through the deck and counts each type of card, then verifies that
		// there is the correct amount of each of them.
		for (Card c : game.getCards()) {
			switch (c.getCardType()) {
				case WEAPON:
					weapons++;
					break;
				case ROOM:
					rooms++;
					break;
				case PLAYER: 
					players++;
					break;
			}
		}
		assertEquals(8, rooms);
		assertEquals(6, players);
		assertEquals(6, weapons);
		
		// Makes a new card of each type that should be in the deck and
		// checks if they are actually present in the deck
		Card player = new Card(CardType.PLAYER, "Sam");
		Card weapon = new Card(CardType.WEAPON, "Bow");
		Card room = new Card(CardType.ROOM, "Rivendell");
		assertTrue(game.getCards().contains(player));
		assertTrue(game.getCards().contains(weapon));
		assertTrue(game.getCards().contains(room));
	}
	
	/*
	 * Tests that there are 17 cards dealt (the other 3 are in the solution),
	 * that a card is not dealt twice, and that each player has either 2 or 3
	 * cards
	 */
	@Test
	public void testDealCards() {
		game.clearDeal();
		game.deal();
		ArrayList<Card> existing = new ArrayList<Card>();
		int cardsDealt = 0;
		
		for (Player p : game.getPlayers()) {
			ArrayList<Card> cards = p.getCards();
			
			// Makes sure the player has either 2 or 3 cards
			assertTrue(cards.size() == 3 || cards.size() == 2);
			
			// For each of the players cards, makes sure that each of them have
			// not been dealt to another player, and then adds them to the list
			// of cards that have already been seen.
			for (int j = 0; j < cards.size(); j++) {
				assertFalse(existing.contains(cards.get(j)));
				existing.add(cards.get(j));
			}
			cardsDealt += cards.size();
		}
		
		// Makes sure that there are only 17 cards dealt to the players (the rest
		// are in the solution to the game).
		assertTrue(cardsDealt == 17);
	}

}
