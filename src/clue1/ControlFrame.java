package clue1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class ControlFrame extends JFrame {
	
	private TurnPanel turnPanel;
	private FeedbackPanel rollPanel, guessPanel, resultPanel;
	private ClueGame game;
	private GuessDialog GuessDialog;
	
	public ControlFrame(ClueGame game) {
		this.game = game;
		setSize(600,200);
		
		turnPanel = new TurnPanel();
		rollPanel = new FeedbackPanel("Die", "Roll", "short");
		guessPanel = new FeedbackPanel("Guess", "Guess", "humongous");
		resultPanel = new FeedbackPanel("Guess Result", "Feedback", "long");
		 
		add(turnPanel, BorderLayout.NORTH);
		add(rollPanel, BorderLayout.WEST);
		add(guessPanel, BorderLayout.CENTER);
		add(resultPanel, BorderLayout.EAST);
		turnPanel.setPreferredSize(new Dimension(300,100));
		
	  	turnPanel.getNextPlayerButton().addActionListener(new ButtonListener());
       	turnPanel.getMakeAccusationButton().addActionListener(new ButtonListener());
	}
	
	public void nextPlayer() {
		

		if (game.isHumanTurn() && !game.checkTurnComplete()) {
			JOptionPane.showMessageDialog(game, "The current turn hasn't been completed yet!",
	       			"ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(game.isHumanTurn()) game.setHumanTurnFinished(false);
		if (game.getCurrentPlayerIndex() == 5) game.setCurrentPlayerIndex(0);
		else game.setCurrentPlayerIndex(game.getCurrentPlayerIndex()+1);;
		Player currentPlayer = game.getPlayers().get(game.getCurrentPlayerIndex());
		turnPanel.getCurrentPlayerText().setText(currentPlayer.toString());

		int roll = new Random().nextInt(6)+1;
		rollPanel.getTextField().setText(Integer.toString(roll));
		currentPlayer.handleTurn(game, roll);	
	}
	
	public void accusation(){
		
		if(game.isHumanTurn() && game.checkTurnComplete()){
			JOptionPane.showMessageDialog(game, "You can only guess at the begining of your turn!",
	       			"ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		game.humanaccusation();
		
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (((JButton) e.getSource()) == turnPanel.getNextPlayerButton()) nextPlayer();
			else if (((JButton) e.getSource()) == turnPanel.getMakeAccusationButton()) accusation();
		}
		
		
	}
	
	public void displayGuess(String guess, String feedback){
		//guessPanel.getTextField().setText(guess);
		resultPanel.getTextField().setText(feedback);		
	}
	

}
