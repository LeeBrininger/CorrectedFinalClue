package clue;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ControlFrame extends JFrame {
	
	private TurnPanel turnPanel;
	private FeedbackPanel rollPanel, guessPanel, resultPanel;
	
	public ControlFrame() {
		
		setSize(600,200);
		
		turnPanel = new TurnPanel();
		rollPanel = new FeedbackPanel("Die", "Roll", "short");
		guessPanel = new FeedbackPanel("Guess", "Guess", "long");
		resultPanel = new FeedbackPanel("Guess Result", "Feedback", "long");
		 
		add(turnPanel, BorderLayout.NORTH);
		add(rollPanel, BorderLayout.WEST);
		add(guessPanel, BorderLayout.CENTER);
		add(resultPanel, BorderLayout.EAST);
		turnPanel.setPreferredSize(new Dimension(300,100));
	}
	
	
	public static void main(String[] args) {
		ControlFrame controls = new ControlFrame();
		controls.setDefaultCloseOperation(EXIT_ON_CLOSE);
		controls.setVisible(true);
	}
	

}
