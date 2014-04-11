package clue1;

import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
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
	
	public SuggestionDialog(ArrayList<Card> deck, ClueGame game, String room) {
		setTitle("Make Suggestion");
		setSize(500, 500);
		
		setLayout(new GridLayout(4,2));
		playerCombobox = new JComboBox<String>();
		weaponCombobox = new JComboBox<String>();

		//create comboboxes based on cards in deck
		for (Card x : deck) { 
			if (x.getCardType().equals(Card.CardType.PLAYER)) {
				playerCombobox.addItem(x.getName()); 
			}
			if (x.getCardType().equals(Card.CardType.WEAPON)) {
				weaponCombobox.addItem(x.getName()); 
			}
		}
		
		add(new Label("Your Room"));
		add(new Label(room));
		add(new Label("Person"));
		add(playerCombobox);
		add(new Label("Weapon"));
		add(weaponCombobox);
		JButton submit = new JButton("ok");
		add(submit);
		add(new Button("Cancel"));
		
	}
}
