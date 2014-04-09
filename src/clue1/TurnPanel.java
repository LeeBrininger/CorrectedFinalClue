package clue1;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TurnPanel extends JPanel {
	
	private JTextField currentPlayerText;
	private JButton nextPlayerButton, makeAccusationButton;
	
	public TurnPanel() {
		JLabel whoseTurn = new JLabel("Whose turn is it?");
		currentPlayerText = new JTextField();
		nextPlayerButton = new JButton("Next Player");
		makeAccusationButton = new JButton("Make Accusation");
		setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		currentPlayerText.setPreferredSize(new Dimension(100, 20));
		nextPlayerButton.setPreferredSize(new Dimension(200,50));
		makeAccusationButton.setPreferredSize(new Dimension(200, 50));
		
		constraints.gridx = 1;
		constraints.gridy= 0;
		add(nextPlayerButton, constraints);
		
		constraints.gridx = 2;
		constraints.gridy= 0;
		add(makeAccusationButton, constraints);
		
		constraints.gridx = 0;
		constraints.gridy= 0;
		constraints.insets = new Insets(0,0,0,25);
		add(whoseTurn, constraints);
	
		
		constraints.gridx = 0;
		constraints.gridy= 1;
		add(currentPlayerText, constraints);
		
	}
	
	public JButton getNextPlayerButton() {
		return nextPlayerButton;
	}
	
	public JButton getMakeAccusationButton() {
		return makeAccusationButton;
	}
	
	public JTextField getCurrentPlayerText() {
		return currentPlayerText;
	}

}
