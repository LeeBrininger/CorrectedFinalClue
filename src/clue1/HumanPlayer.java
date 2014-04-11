package clue1;

import javax.swing.JOptionPane;

public class HumanPlayer extends Player {

	public HumanPlayer(String name, String color, int startLocation) {
		super(name, color, startLocation);
	}


	@Override
	public void handleTurn(ClueGame game, int roll) {
		// TODO Auto-generated method stub
		game.setHumanTurnFinished(false);
		game.getBoard().calcTargets(getCurrentLocation().getRow(), getCurrentLocation().getColumn(), roll);
		game.getBoard().setHighlightTargets(true);
		game.getBoard().repaint();
	}
	
	public void makeMove(Board board, int row, int col) {
		setCurrentLocation(board.getCellAt(board.calcIndex(row, col)));
		board.repaint();
	}

	public void makeguess(){
		
	}

}
