package clue;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class FeedbackPanel extends JPanel {
	
	private JTextField text;
	
	public FeedbackPanel(String title, String name, String length) {
		
		setBorder(new TitledBorder(new EtchedBorder(), title));
		JLabel label = new JLabel(name);
		text = new JTextField();
		
		switch(length) {
			case "short":
				text.setPreferredSize(new Dimension(25,20));
				break;
			case "long":
				text.setPreferredSize(new Dimension(75, 20));
				break;
			case "humongous":
				text.setPreferredSize(new Dimension(275,20));
				break;
			default:
				break;
		}
		
		add(label);
		add(text);
		
	}

	public JTextField getTextField() {
		return text;
	}

}
