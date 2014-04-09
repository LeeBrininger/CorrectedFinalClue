package clue1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class BoardCell {
	private int row;
	private int column;
	protected char cellCode;
	private boolean isHighlighted;
	
	public BoardCell (int row, int column){
		this.row = row;
		this.column = column;
		isHighlighted = false;
	}

	public BoardCell() {
		
	}
	
	/**
	 * Based on the char W
	 * @param walkway
	 * @return
	 */
	public char getCellCode(){
		return cellCode;
	}
	
	public static boolean isWalkway(String walkway){
		if (walkway.equals("X") || walkway.equals("W")){
			return true;
		}
		else {
			return false;
		}
	}
	public static boolean isRoom(String room){
		if (!(room.equals("X") || room.equals("W"))&&room.length()<2){//to do: change doorway names to D
			return true;
		}
		else {
			return false;
		}
	}
	public boolean checkRoom(){
		return false;
	}
	public boolean isDoorway(){ //overridden in RoomCell
		return false;
	}
	public int getRow (){
		return row;
	}
	public int getColumn (){
		return column;
	}
	public void setRow(int row){
		this.row = row;
	}
	public void setCol(int column){
		this.column = column;
	}
	public String toString (){
		return "Row: " + row + " Col: " + column;
	}
	public int [] getRowsCols(){
		int [] rowsCols = new int [2];
		rowsCols[0] = row;
		rowsCols[1] = column;
		return rowsCols;
	}

	@Override
	public boolean equals (Object o){
		if (this == o) return true;
		if (!(o instanceof BoardCell)) return false;
		BoardCell b = (BoardCell) o;
		if (this.row == b.row && this.column == b.column) return true;
		else return false;
	}
	
	public abstract void draw(Graphics g);
	
	public void setHighlighted(boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}
	
	public boolean isHighlighted() {
		return isHighlighted;
	}
	
	public void drawHighlighted(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Rectangle rect = new Rectangle(getColumn()*25, getRow()*25, 25, 25);
		g2.setColor(Color.BLUE);
		g2.fill(rect);
		g2.setColor(Color.BLACK);
		g2.draw(rect);
	}
	
	
}