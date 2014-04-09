package clue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class WalkwayCell extends BoardCell {
	
	public WalkwayCell(int row, int column){
		super(row, column);
		cellCode = 'W';
	}
	public static boolean isWalkway(char walkway){
		return true;
	}
	public String toString (){
		
		return super.toString()+" Walkway";
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Rectangle rect = new Rectangle(getColumn()*25, getRow()*25, 25, 25);
		g2.setColor(Color.YELLOW);
		g2.fill(rect);
		g2.setColor(Color.BLACK);
		g2.draw(rect);
	}

}