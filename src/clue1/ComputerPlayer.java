package clue1;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import clue1.Card.CardType;


public class ComputerPlayer extends Player {

	private ArrayList<Card> unseenCards;
	private char lastRoomVisited;
	private BoardCell target;
	
	public ComputerPlayer(String name, String color, int startLocation) {
		super(name, color, startLocation);
		unseenCards = new ArrayList<Card>();
	}
	
	
	@SuppressWarnings("static-access")
	public BoardCell pickLocation(Set<BoardCell> targets) {

		for (BoardCell i : targets) {
			if (i.isRoom("" + i.getCellCode())) {
				if (lastRoomVisited != i.getCellCode()) {
					lastRoomVisited=i.getCellCode();
					target = i;
					return target;
				}
			}
			
		}
		
		Random random = new Random();
		target = new RoomCell();
		while (target instanceof RoomCell) {
			int randomTarget = random.nextInt(targets.size());
			int i =0;
			for (BoardCell b : targets) {
				if (i == randomTarget) target = b;
				i++;
			}
		}
		
		return target;
	}
	
	public void setLastRoomVisited(char c) {
		lastRoomVisited = c;
	}
	
	public char getLastRoomVisited () {
		return lastRoomVisited;
	}
	
	public Solution createSuggestion() {
		Random rand = new Random();
		Card player = unseenCards.get(rand.nextInt(unseenCards.size()));
		while (player.getCardType() != CardType.PLAYER) player = unseenCards.get(rand.nextInt(unseenCards.size()));
		Card weapon = unseenCards.get(rand.nextInt(unseenCards.size()));
		while (weapon.getCardType() != CardType.WEAPON) weapon = unseenCards.get(rand.nextInt(unseenCards.size()));
		return new Solution(player.getName(), weapon.getName(), ((RoomCell) getCurrentLocation()).decodeRoomInitial(((RoomCell) getCurrentLocation()).getInitial()));
	}

	@Override
	public void updateSeen(Card seen, ArrayList<Card> deck) {
		if (unseenCards.size() == 0) {
			unseenCards = new ArrayList<Card>(deck);
			//removes the rooms from the deck
			for (int i = 0; i<8; i++) unseenCards.remove(unseenCards.size()-1);
		}
		unseenCards.remove(seen);
	}
	
	@Override
	public void clearCards() {
		super.clearCards();
		unseenCards.clear();
	}


	@Override
	public void handleTurn(ClueGame game, int roll) {
		game.getBoard().setHighlightTargets(false);
		game.getBoard().calcTargets(getCurrentLocation().getRow(), getCurrentLocation().getColumn(), roll);
		setCurrentLocation(pickLocation(game.getBoard().getTargets()));
		game.getBoard().repaint();
		if(currentLocation.checkRoom()){
			Solution solution = createSuggestion();
			Card feedback = game.handleSuggestion(solution, this);

			game.getControlPanel().displayGuess(solution.toOutputString(), feedback.getName());
			
		}

	}
	
}
