package clue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

//An enumerated type named DoorDirection that specifies the location of the door relative to the room. 
//Values will be UP, DOWN, LEFT, RIGHT and NONE.
//Door direction is explained in more detail below.

public class RoomCell extends BoardCell{
	 enum DoorDirection{
		UP, DOWN, LEFT, RIGHT, NONE
	};
	private DoorDirection direction;
	private int nameRow, nameColumn;
	private static Map<Character, String> roomNameMap;
	String roomName;

	//ArrayList<>
	public RoomCell(int row, int column, String C, String D){
		super(row, column);
		roomName = "";
		cellCode = C.charAt(0);
		decodeRoomInitial(C.charAt(0));
		decodeDirection(row, column, D);
	}
	
	public RoomCell() {
		
	}
	public DoorDirection getDirection(){
		return direction;
	}
	@Override
	public boolean checkRoom(){
		return true;
	}

	public String decodeRoomInitial(char code) {
		if (roomNameMap.containsKey(code)) {
			roomName = roomNameMap.get(code);
		} else {
			roomName = "No room name found";
		}
		return roomName;
	}
	
	public void decodeDirection(int row, int column, String D){
        char d = D.charAt(0);
		
		switch(d){
	
			case('U'): direction = DoorDirection.UP;
						break;
			case('L'): direction = DoorDirection.LEFT;
						break;
			case('R'): direction = DoorDirection.RIGHT;
						break;
			case('D'): direction = DoorDirection.DOWN;
						break;
			case('T'): nameRow = row;
						nameColumn = column;
						direction = DoorDirection.NONE;
						break;
			
			default: direction = DoorDirection.NONE;
					break;
				
		
		}
	}

	
//	A variable of type DoorDirection named doorDirection.
//	A variable of type char to hold the room initial
//	(we'll need this later, when drawing the room name on the screen).
//	Override the method isRoom to return true.
//	We will also override the draw method, when we do the GUI
	// DoorDirection doorDirection;

	@Override 
	public boolean isDoorway() {
		if (direction.equals(null) || direction.equals(DoorDirection.NONE)) {
			return false;
		}
		return true;
	}
	public boolean isRoom(char room){
		return true;
	}

	public char getInitial() {
		return cellCode;
	}
	
	public static void setRoomNameMap(Map<Character, String> roomNameMap) { //set the map of room names to room initials
		RoomCell.roomNameMap = roomNameMap;
	}
	public String toString (){
		String roomCellInfo =super.toString();
		 roomCellInfo += " Room: ";
		if(!(this.direction==DoorDirection.NONE)){
			roomCellInfo += "Direction: " + this.direction;
		}
		return roomCellInfo;
			
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Rectangle rect = new Rectangle(getColumn()*25, getRow()*25, 25, 25);
		
		
		
		if (cellCode == 'O') g2.setColor(Color.RED);
		else g2.setColor(Color.LIGHT_GRAY);
		g2.fill(rect);
		g2.draw(rect);

		if (isDoorway()) {
			Rectangle door = null;
			switch (direction) {
				case UP:
					door = new Rectangle(getColumn()*25, getRow()*25, 25, 25/4);
					break;
				case DOWN:
					door = new Rectangle(getColumn()*25, getRow()*25+25*3/4, 25, 25/4);
					break;
				case RIGHT:
					door = new Rectangle(getColumn()*25+25*3/4, getRow()*25, 25/4, 25);
					break;
				case LEFT:
					door = new Rectangle(getColumn()*25, getRow()*25, 25/4, 25);
					break;
				case NONE:
					break;
			}
			g2.setColor(Color.MAGENTA);
			if (door!=null) g2.fill(door);
		}
		g.setColor(Color.BLACK);
		if (nameRow != 0 && nameColumn != 0 && roomName != "") g.drawString(roomName.toUpperCase(), nameColumn*25, nameRow*25);
	}
	

}