package clue1;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clue1.Card.CardType;


@SuppressWarnings("serial")
public class ClueGame extends JFrame {
	private ArrayList<Card> cards;
	private ArrayList<Card> cardsNotShuffled; //used in testing
	private ArrayList<Player> players;
	private boolean humanTurnFinished;
	private int humanPlayerIndex, currentPlayerIndex;
	private Solution solution;
	private Board board;
	private File componentConfig;
	private DetectiveNotesDialog detectiveNotes;
	private ControlFrame controls;
	private MyCardsFrame humanCards;
	private static final String defaultLayoutFile = "ClueLayout";
	private static final String defaultLegendFile = "ClueLegend";
	private SuggestionDialog suggestionDialog;
	private GuessDialog GuessDialog;

	public ClueGame(String componentFile) {
		this(componentFile, defaultLayoutFile, defaultLegendFile);
	}

	public ClueGame(String componentFile, String layoutFile, String legendFile) {
		componentConfig = new File(componentFile);
		cards = new ArrayList<Card>();
		players = new ArrayList<Player>();
		board = new Board(layoutFile, legendFile);

		try {
			board.loadConfigFiles();
			board.loadBoard();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		board.calcAdjacencies();

		loadConfig();
		cardsNotShuffled = new ArrayList(cards);
		deal();

		board.setPlayers(players);
		board.setGame(this);
		// JFrame setup
		setTitle("Clue");

		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem showNotes = new JMenuItem("Show Detective Notes");
		JMenuItem exit = new JMenuItem("Exit");
		file.add(showNotes);
		file.add(exit);
		menu.add(file);

		showNotes.addActionListener(new MenuListener());
		exit.addActionListener(new MenuListener());
		setJMenuBar(menu);

		detectiveNotes = new DetectiveNotesDialog(cards);
		suggestionDialog = new SuggestionDialog(cards, this, "testing");


		add(board, BorderLayout.CENTER);

		controls = new ControlFrame(this);

		add(controls.getContentPane(), BorderLayout.SOUTH);

		humanCards = new MyCardsFrame((HumanPlayer) players.get(humanPlayerIndex));

		add(humanCards.getContentPane(), BorderLayout.EAST);

		setSize((board.getNumColumns()+1)*board.getCellLength() + humanCards.getWidth(),(board.getNumRows()+1)*board.getCellLength() + controls.getHeight());

		for(Player x: players){
			x.updateSeen(null, cards);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		for (Player p : players) p.draw(g);
	}

	public void deal() {
		Collections.shuffle(cards);
		int nextCard = 0, nextPlayer = 0;
		Random rand = new Random();
		String room = "", weapon = "", person = "";
		int roomInd, weaponInd, personInd;
		int r = rand.nextInt(cards.size());
		while (cards.get(r).getCardType() != CardType.PLAYER) r=rand.nextInt(cards.size());
		person = cards.get(r).name;
		personInd=r;
		while (cards.get(r).getCardType() != CardType.ROOM) r=rand.nextInt(cards.size());
		room = cards.get(r).name;
		roomInd = r;
		while (cards.get(r).getCardType() != CardType.WEAPON) r=rand.nextInt(cards.size());
		weapon = cards.get(r).name;
		weaponInd = r;

		setSolution(person, weapon, room);

		while (nextCard < cards.size()) {
			if (nextCard != roomInd && nextCard != weaponInd && nextCard != personInd) {
				players.get(nextPlayer).giveCard(cards.get(nextCard));

				if (nextPlayer == players.size()-1) nextPlayer = 0;
				else nextPlayer++;
			}
			nextCard++;
		}
	}

	public void loadConfig() {
		try {
			Scanner scan = new Scanner(componentConfig);
			Random rand = new Random();
			int character = rand.nextInt(6);
			for (int i=0; scan.hasNextLine(); i++) {
				String next = scan.nextLine();
				String[] separated = next.split(",");
				if (separated[0].equals("PLAYER")) {
					int startIndex;
					if (separated.length == 5) { //file has coordinates for player starting position
						startIndex = board.calcIndex(Integer.parseInt(separated[3]), Integer.parseInt(separated[4]));
					} else { //file has straight index
						startIndex = Integer.parseInt(separated[3]);
					}
					if (i == character) {
						players.add(new HumanPlayer (separated[1], separated[2],startIndex));
						humanPlayerIndex = i;
						currentPlayerIndex = i-1;
					}
					else players.add(new ComputerPlayer (separated[1], separated[2],startIndex));
					players.get(players.size()-1).setCurrentLocation(board.getCellAt(startIndex));
				}
				cards.add(new Card(CardType.valueOf(separated[0]),separated[1]));
			}

			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void setSolution(String person, String weapon, String room) {
		solution = new Solution(person, weapon, room);
	}
	
	//used to help set up tests
	public void clearDeal() {
		cards = cardsNotShuffled;
		for (Player x: players) {
			x.clearCards();
		}
	}

/*

	public void makePlayer(String name, String color, int startLoc) {

	}

	public void selectAnswer() {

	}
	
	
*/
	//display the accusation and end game if it is correct
	public void handleAccusation(Solution guess, Player accusingPerson){
		if (guess.equals(solution)) {
			String title = "Correct Accusation";
			if (accusingPerson == getHumanPlayer()) {
				title = "You win!";
			}
			JOptionPane.showMessageDialog(this, accusingPerson.getName() + " correctly guessed "
					+ solution.toOutputString(), title, JOptionPane.INFORMATION_MESSAGE);
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(this, accusingPerson.getName() + " incorrectly guessed "
					+ guess.toOutputString(), "Incorrect Accusation", JOptionPane.INFORMATION_MESSAGE);
			setHumanTurnFinished(true);
			controls.nextPlayer();
		}
	}
	
	public Card handleSuggestion(Solution guesses, Player accusingPerson){
		return handleSuggestion(guesses.getPerson(), guesses.getWeapon() ,guesses.getRoom(), accusingPerson);
		
	}

	@SuppressWarnings("unused")
	public Card handleSuggestion(String person, String weapon, String room, Player accusingPerson) {
		Card response = null;
		//move accused player to room
		for (Player p: players) {
			if (p.getName().equals(person)) {
				p.setCurrentLocation(accusingPerson.getCurrentLocation());
			}
		}
		board.repaint(); //show that player was moved
		
		for (Player p : players) {
			if (!p.equals(accusingPerson)) { //the accusing person cannot disprove 
				ArrayList<Card> sameCards = new ArrayList<Card>();
				Card playerCard = new Card(CardType.PLAYER, person);
				Card weaponCard = new Card(CardType.WEAPON, weapon);
				Card roomCard = new Card(CardType.ROOM, room);
				
				//set up the cards that can be used to disprove
				if (p.getCards().contains(playerCard)) sameCards.add(playerCard);
				if (p.getCards().contains(weaponCard)) sameCards.add(weaponCard);
				if (p.getCards().contains(roomCard)) sameCards.add(roomCard);
				
				if (sameCards.size() == 1) { 
					response = sameCards.get(0);
					break;
				
				}   else if (sameCards.size() > 1) {
					Random rand = new Random();
					response =  sameCards.get(rand.nextInt(sameCards.size()));
				}
			}
		}
		if (!(response == null)) { //if card found to disprove
			for (Player p: players) { //show card to all players
				p.updateSeen(response);
			}
			return response;
		}
		return null;
	}

	public boolean checkAccusation(Solution solution) {
		if (this.solution.equals(solution)) return true;
		else return false;
	}

	
	public boolean checkTurnComplete() {
		return humanTurnFinished;
	}
	

	
	public boolean isHumanTurn() {
		if (currentPlayerIndex == humanPlayerIndex) return true;
		else return false;
	}
	
	public void moveHuman(int row, int col) {
		((HumanPlayer) players.get(humanPlayerIndex)).makeMove(board, row, col);
		//promt for suggestion if human moved to room
		if (((HumanPlayer) players.get(humanPlayerIndex)).getCurrentLocation().checkRoom()) {
			String room = ((Player) players.get(humanPlayerIndex)).returnRoom();
			//suggestionDialog = new SuggestionDialog(cards, this, "testing");
			suggestionDialog.setRoomName(room);
			suggestionDialog.setVisible(true);
		}
		board.setHighlightTargets(false);
		humanTurnFinished = true;
	}

	public void humanaccusation(){
		GuessDialog = new GuessDialog(cards, this);
		GuessDialog.setVisible(true);
	}
	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}

	public void setCurrentPlayerIndex(int index) {
		currentPlayerIndex = index;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public int getHumanPlayerIndex() {
		return humanPlayerIndex;
	}
	
	public Player getHumanPlayer() {
		return players.get(getHumanPlayerIndex());
	}

	public void setHumanTurnFinished(boolean isFinished) {
		humanTurnFinished = isFinished;
	}

	public Solution getSolution() {
		return solution;
	}

	public Board getBoard() {
		return board;
	}

	class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (((JMenuItem) arg0.getSource()).getText().equals("Exit"))
				System.exit(0);
			else detectiveNotes.setVisible(!detectiveNotes.isVisible());
		}

	}

	class MyCardsFrame extends JFrame {

		public MyCardsFrame(HumanPlayer player) {
			setLayout(new GridLayout(4,0));
			setSize(100,100);
			JPanel playerCards = new JPanel(), weaponCards = new JPanel(), roomCards = new JPanel();

			for (Card c : player.getCards())  {
				CardType type = c.getCardType();
				JTextField j = new JTextField();
				j.setText(c.getName());
				if (type == CardType.PLAYER) playerCards.add(j);
				else if (type == CardType.WEAPON) weaponCards.add(j);
				else roomCards.add(j);
			}

			add(new JLabel("My Cards"));
			addPanel("Players", playerCards);
			addPanel("Weapons", weaponCards);
			addPanel("Rooms", roomCards);

		}

		public void addPanel(String name, JPanel p) {
			p.setBorder(new TitledBorder(new EtchedBorder(), name));
			p.setPreferredSize(new Dimension(100,100));
			add(p);
		}

	}

	public static void main(String[] args) {
		ClueGame game = new ClueGame("componentConfig2.csv");
		game.setDefaultCloseOperation(EXIT_ON_CLOSE);
		game.setVisible(true);

		JOptionPane.showMessageDialog(game, "You are " + game.players.get(game.humanPlayerIndex) + ". Press the Next Player button to start!",
				"Welcome to Clue!", JOptionPane.INFORMATION_MESSAGE);
	}

	public ControlFrame getControlPanel() {
		return controls;
	}

	public SuggestionDialog getSuggestionDialog() {
		return suggestionDialog;
	}

}
