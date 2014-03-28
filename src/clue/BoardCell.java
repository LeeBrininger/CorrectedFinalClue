package clue;

import java.awt.Graphics;

public abstract class BoardCell {
	private int row;
	private int column;
	protected char cellCode;
	
	public BoardCell (int row, int column){
		this.row = row;
		this.column = column;
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


	
}