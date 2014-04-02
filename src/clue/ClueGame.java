package clue;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import clue.Card.CardType;


@SuppressWarnings("serial")
public class ClueGame extends JFrame {
	private ArrayList<Card> cards;
	private ArrayList<Player> players;
	private int humanPlayerIndex;
	private Solution solution;
	private Board board;
	private File componentConfig;
	private DetectiveNotesDialog detectiveNotes;
	private ControlFrame controls;
	private static final String defaultLayoutFile = "ClueLayout";
	private static final String defaultLegendFile = "ClueLegend";
	
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
		
		board.setPlayers(players);
		
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
       	
       	add(board, BorderLayout.CENTER);
       	
       	controls = new ControlFrame();
       	
       	add(controls.getContentPane(), BorderLayout.SOUTH);
       	
       	setSize((board.getNumColumns()+1)*board.getCellLength(),(board.getNumRows()+1)*board.getCellLength() + controls.getHeight());
	}
	
	public void paintComponent(Graphics g) {
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
			Map <Character, String> roomNameMap = new HashMap<Character, String>();
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
	
	public void makePlayer(String name, String color, int startLoc) {
		
	}
	
	public void selectAnswer() {
		
	}
	
	public Card handleSuggestion(String person, String weapon, String room, Player accusingPerson) {
		for (Player p : players) {
			if (!p.equals(accusingPerson)) {
				ArrayList<Card> sameCards = new ArrayList<Card>();
				Card playerCard = new Card(CardType.PLAYER, person);
				Card weaponCard = new Card(CardType.WEAPON, weapon);
				Card roomCard = new Card(CardType.ROOM, room);
				
				if (p.getCards().contains(playerCard)) sameCards.add(playerCard);
				if (p.getCards().contains(weaponCard)) sameCards.add(weaponCard);
				if (p.getCards().contains(roomCard)) sameCards.add(roomCard);
				
				if (sameCards.size() == 1) return sameCards.get(0);
				else if (sameCards.size() > 1) {
					Random rand = new Random();
					return sameCards.get(rand.nextInt(sameCards.size()));
				}
			}
		}
		return null;
	}
	
	public boolean checkAccusation(Solution solution) {
		if (this.solution.equals(solution)) return true;
		else return false;
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
	
	public static void main(String[] args) {
		ClueGame game = new ClueGame("componentConfig2.csv");
		
		game.setDefaultCloseOperation(EXIT_ON_CLOSE);
		game.setVisible(true);
	}
}
