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

public class GuessDialog extends JDialog {
	
	private JComboBox<String> playerCombobox, weaponCombobox, roomCombobox;
	private JButton submit, cancel;
	
	public GuessDialog(ArrayList<Card> deck, ClueGame game) {
		setTitle("Make Suggestion");
		setSize(500, 500);
		
		setLayout(new GridLayout(4,2));
		playerCombobox = new JComboBox<String>();
		weaponCombobox = new JComboBox<String>();
		roomCombobox = new JComboBox<String>();
		//create comboboxes based on cards in deck
		for (Card x : deck) { 
			if (x.getCardType().equals(Card.CardType.PLAYER)) {
				playerCombobox.addItem(x.getName()); 
			}
			if (x.getCardType().equals(Card.CardType.WEAPON)) {
				weaponCombobox.addItem(x.getName()); 
			}
			if (x.getCardType().equals(Card.CardType.ROOM)) {
				roomCombobox.addItem(x.getName()); 
			}
		}
		submit = new JButton("ok");
		cancel = new JButton("Cancel");
		cancel.addActionListener(new GuessDialogButtonListener());
		submit.addActionListener(new GuessDialogButtonListener());
		add(new Label("Your Room"));
		add((roomCombobox));
		add(new Label("Person"));
		add(playerCombobox);
		add(new Label("Weapon"));
		add(weaponCombobox);
		add(submit);
		add(cancel);
		
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

	private class GuessDialogButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (((JButton) e.getSource()) == cancel) closeWindow();
			//else if (((JButton) e.getSource()) == turnPanel.getMakeAccusationButton()) accusation();
		}
		
		
	}
}
