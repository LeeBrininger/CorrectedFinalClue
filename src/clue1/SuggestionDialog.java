package clue1;

import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clue1.DetectiveNotesDialog.CheckboxPanel;

public class SuggestionDialog extends JDialog {
	
	private JComboBox<String> playerCombobox, weaponCombobox;
	private Label roomNameLabel;
	private JButton submit, cancel;
	private ClueGame game;
	private String room;
	
	public SuggestionDialog(ArrayList<Card> deck, ClueGame game, String room) {
		this.room = room;
		this.game = game;
		setTitle("Make Suggestion");
		setSize(500, 500);
		
		setLayout(new GridLayout(4,2));
		playerCombobox = new JComboBox<String>();
		weaponCombobox = new JComboBox<String>();
		submit = new JButton("ok");
		cancel = new JButton("Cancel");
		cancel.addActionListener(new SuggestionDialogButtonListener());
		submit.addActionListener(new SuggestionDialogButtonListener());

		//create comboboxes based on cards in deck
		for (Card x : deck) { 
			if (x.getCardType().equals(Card.CardType.PLAYER)) {
				playerCombobox.addItem(x.getName()); 
			}
			if (x.getCardType().equals(Card.CardType.WEAPON)) {
				weaponCombobox.addItem(x.getName()); 
			}
		}
		roomNameLabel = new Label();
		
		add(new Label("Your Room"));
		add(roomNameLabel);
		add(new Label("Person"));
		add(playerCombobox);
		add(new Label("Weapon"));
		add(weaponCombobox);
		add(submit);
		add(cancel);
		
	}
	
	public void setRoomName(String roomName) {
		roomNameLabel.setText(roomName);
		room = roomName;
	}
	
	public JButton getSubmitButton() {
		return submit;
	}

	public JButton getCancelButton() {
		return cancel;
	}
	
	public void closeWindow() {
		this.dispose();
	}
	
	public void makeSuggestion() {
		Solution suggestion = new Solution((String) playerCombobox.getSelectedItem(), 
				(String) weaponCombobox.getSelectedItem(), room);
		Card feedback = game.handleSuggestion(suggestion, game.getHumanPlayer());
		game.getControlPanel().displayGuess(suggestion.toOutputString(), feedback);
		this.dispose();
	}
	
	private class SuggestionDialogButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (((JButton) e.getSource()) == cancel) closeWindow();
			else if (((JButton) e.getSource()) == submit) makeSuggestion();
		}
		
		
	}
}
