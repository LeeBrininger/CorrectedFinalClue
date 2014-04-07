package clue;
import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import clue.BoardCell;

@SuppressWarnings("serial")
public class Board extends JPanel {
	
	private final int CELL_LENGTH = 25;

	private ArrayList<BoardCell> cells;//contains the board layout
	private Set<BoardCell> targets;
    private BoardCell target;
	private ArrayList<Boolean> visited;
	private Map<Character, String> rooms;//contains the legend 
	private int numRows;
	private int numColumns;
	private String boardName;
	private String legendName;
 	private Map<Integer, LinkedList<Integer>> adjCells;
	private BoardCell previousTarget; //for determining validity of next target
	private ArrayList<Player> players;

	//constructor that requires input locations is known
	public Board(String fileLayout, String fileLegend) {
		cells = new ArrayList<BoardCell> ();
		targets = new HashSet <BoardCell>();
		rooms = new HashMap <Character, String> ();
		players = new ArrayList<Player>();
		this.boardName = fileLayout;
		this.legendName = fileLegend;
        adjCells = new HashMap<Integer, LinkedList<Integer>>();

	}
    public Board() {
        cells = new ArrayList<BoardCell> ();
        targets = new HashSet <BoardCell>();
        rooms = new HashMap <Character, String> ();
        this.boardName = "ClueLayout";
        this.legendName = "ClueLegend";
        adjCells = new HashMap<Integer, LinkedList<Integer>>();

    }
    
    public void paintComponent(Graphics g) {
    	for (BoardCell cell : cells) cell.draw(g);
    	for (Player p : players) p.draw(g);
    }
    
    public void setPlayers(ArrayList<Player> players)  {
    	this.players = players;
    }
    
    public int getNumRows() {
    	return numRows;
    }
    
    public int getNumColumns() {
    	return numColumns;
    }

    //simple getter function for all cell type, so walkpaths and rooms
	public ArrayList<BoardCell> getCells(){
		return cells;
	}
	//getter for the legend data structure
	public Map<Character, String> getRooms(){
		return rooms;
	}
	//returns the targets
	public Set<BoardCell> getTargets(){

		return targets;
	}
	//-------------------------------------------------SET-UP METHODS-----------------------------------------------//
	/**
	 * fills in the cells from the given files 
	 * throws FileNotFoundException
	 * @throws IOException 
	 */
	public void loadConfigFiles() throws IOException {
        loadLegend();
		loadBoard();
		visited = new ArrayList<Boolean>();
	}
	/*
	 * Loads only the the board layout and sets numRow/numCol/size
	 */
	public void loadBoard () throws BadConfigFormatException, FileNotFoundException{
		Scanner scan = new Scanner (new File (boardName+".csv"));
		int countRows =0;
		while(scan.hasNext()){
			String row = scan.nextLine();

			//all cellCode, so walkways, rooms, doorways are included
			String [] cellCodes = row.split(",");
            if(countRows==0)
		    	numColumns = cellCodes.length;
            else if(cellCodes.length!=numColumns){
                try {
                    throw new BadConfigFormatException("The length of your columns are off.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            //classify the cellCode types
			for(int i = 0; i<cellCodes.length; i++){
                if(!(isValidCellCode(cellCodes[i]))){
                    try{
                        throw new BadConfigFormatException("There was an unidentifiable room.");
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
				if(BoardCell.isRoom(cellCodes[i])){

					cells.add(new RoomCell (countRows, i, cellCodes[i], "N"));
				}
				else if(BoardCell.isWalkway(cellCodes[i])){

					cells.add(new WalkwayCell (countRows, i));}

				else{

					cells.add( new RoomCell(countRows, i, cellCodes[i], cellCodes[i].substring(1)));
				}

			}
			countRows++;
		}
		numRows = countRows;
   		scan.close();
   		startTargets();
	}
	public void loadLegend() throws FileNotFoundException{
		Scanner scan = new Scanner (new File (legendName +".txt"));

		while(scan.hasNext()){
   			String cellWithName = scan.nextLine();

			char cellCode = cellWithName.charAt(0);
			Character cellCodeB = new Character(cellCode);

			int posSpace = cellWithName.indexOf(" ");

			String name = cellWithName.substring(posSpace);
			name = name.trim();
			
			rooms.put(cellCodeB, name);
		}

		RoomCell.setRoomNameMap(rooms); //allow RoomCell to use this map to decode room initials
		scan.close();

	}
    public boolean isValidCellCode(String code){
       return rooms.containsKey(code.charAt(0));
    }
	public void calcAdjacencies(){
   		for (int i = 0; i < cells.size(); i++){
 			adjCells.put(i, calcAdjList(i));
		}
	}

//	//All cells are set to NOT visited initially
	public void startTargets(){

			visited = new ArrayList<Boolean>();
			for(int i = 0; i<cells.size(); i++){
				visited.add(i, false);
			}


	}
	//------------------------------------------END OF SET-UP METHODS----------------------------------------//

	//---------------------------------------------CALCULATION-METHODS---------------------------------------//
    public void calcTargets (int row, int column, int numSteps){
        startTargets();

        int position = calcIndex(row, column);
        visited.set(position, true);
        target= getCellAt(position);

        targets = new HashSet<BoardCell>();//clear out targets
        findTargets(target, numSteps);
    }

	//recursive method that stores the targets of a given cell that is within a certain number of steps
	public void findTargets(BoardCell thisCell, int numSteps){
  		int referenceSteps =  numSteps;
		previousTarget = thisCell;
		LinkedList<Integer> intsTargets = getAdjList(calcIndex(thisCell.getRow(), thisCell.getColumn()));

		ArrayList <BoardCell> adjacentTargets = new ArrayList<BoardCell>();
   		for(Integer i : intsTargets){
           if(!(visited.get(i))) //if the cell has not been visited
			adjacentTargets.add(getCellAt(i));

		}

		for(BoardCell AT: adjacentTargets){
            visited.set(calcIndex(AT.getRow(), AT.getColumn()), true);

            if(AT instanceof RoomCell)
                    if(((RoomCell) AT).getDirection()!=RoomCell.DoorDirection.NONE)//shorten path if Door found
                        numSteps =1;

			if ((numSteps == 1) ){
                targets.add(AT);

            }else {
				findTargets(AT, --numSteps);
			}

			//reset numSteps
			numSteps = referenceSteps;
			visited.set(calcIndex(AT.getRow(), AT.getColumn()), false);

		}
    }

	public BoardCell getCellAt (int position){
        return cells.get(position);
	}
	
	public int getCellLength() {
		return CELL_LENGTH;
	}

	/*
	 * Criteria for validity: walkway, doorway in direction of path
	 */
	public boolean validTarget(int position){

        BoardCell nextTarget = getCellAt(position);
        if(nextTarget.getRow()==target.getRow() && nextTarget.getColumn()==target.getColumn()){
            return false;
        }

		if(nextTarget instanceof WalkwayCell){

			return true;
		}

		else if(nextTarget instanceof RoomCell){
			if(isValidDoorWay((RoomCell)nextTarget))
    			return true;
		}
		return false;

	}
	public boolean isValidDoorWay (RoomCell cell){

		//is the RoomCell even a door?
   		if(cell.getDirection().equals(RoomCell.DoorDirection.NONE)){

			return false;
        }

        if(previousTarget instanceof RoomCell){//cannot move from doorway to doorway
            return false;
        }
		else{
			//is the door Up and is that okay
			if(cell.getDirection().equals(RoomCell.DoorDirection.UP)&&(previousTarget.getRow()==cell.getRow()-1)){
				return true;

			}
			//is the door Down and okay
			else if (cell.getDirection().equals(RoomCell.DoorDirection.DOWN)&&(previousTarget.getRow()==cell.getRow()+1)){
				return true;

			}
			//is the door Left and alright
			else if(cell.getDirection().equals(RoomCell.DoorDirection.LEFT)&&(previousTarget.getColumn()==cell.getColumn()-1)){
				return true;
			}
			//is the door Right and not in need of a therapist
			else if(cell.getDirection().equals(RoomCell.DoorDirection.RIGHT)&&(previousTarget.getColumn()==cell.getColumn()+1)) {
				return true;
	    	}
		}
		return false;

	}
	//returns a list of adjacent cells given a particular "base" cell
	public LinkedList<Integer> calcAdjList(int position){

        LinkedList<Integer> adjList;

        BoardCell baseCell = getCellAt(position);
        previousTarget = baseCell;//used for calculating validity
        target = baseCell; //same as above
        if((baseCell instanceof RoomCell)){
            if(((RoomCell) baseCell).getDirection().equals( RoomCell.DoorDirection.NONE)){

                adjList = new LinkedList<Integer>();

                return adjList;
            }
        }
        adjList = checkDirections(position);
        return  adjList;
    }
    public LinkedList<Integer> getAdjList(int position){
        return adjCells.get(position);
    }

	//every direction that could be adjacent to a given position is checked
	public LinkedList<Integer> checkDirections (int position){
        //dissect into row and column

        int [] rowCols = calcRowCol(position);
        int r = rowCols[0];
        int c = rowCols[1];
		LinkedList<Integer> l = new LinkedList<Integer>();
		int adjPos;
		if ((isValidLocation(r+1, c)) && validTarget(calcIndex(r+1, c))){
			adjPos =calcIndex(r+1, c);
			l.add(adjPos);
		}
		if ((isValidLocation(r-1, c))&& (validTarget(calcIndex(r-1, c)))){
			adjPos = calcIndex (r-1, c);
			l.add(adjPos);
		}
		if((isValidLocation(r, c+1))&& (validTarget(calcIndex(r, c+1)))){
			adjPos = calcIndex(r, c+1);
			l.add(adjPos);
		}
		if((isValidLocation(r, c-1))&& (validTarget(calcIndex(r, c-1)))){
			adjPos = calcIndex(r, c-1);
			l.add(adjPos);
		}
		return l;
	}

	//checks to see if the location is within bounds of the maps
	public boolean isValidLocation(int r, int c){
		if(r<0)
			return false;
		if(r>=numRows)
			return false;
		if(c<0)
			return false;
		if(c>=numColumns)
			return false;
		return true;
	}	
	/*
	 * translates the row and column location format
	 * to a single location number
	 */
	public int calcIndex(int row, int col) {
		return row*numColumns + col;
	}
	/*
	 * translates position to row and column
	 */
	public int [] calcRowCol(int position){
		int [] rowCol = new int [2];
		rowCol[0] = position/numColumns;// the row
		rowCol[1] = position%numColumns;// the column
		return rowCol;

	}
	//------------------------------------END OF CALCULATION METHODS-----------------------------//

	//---------------------------------------PRINTER METHODS-------------------------------------//
	public void printCells(){
		for(BoardCell b :cells){
			System.out.println(b);
		}
	}
	public void printRooms(){
		Set<Character> cellCodes  = rooms.keySet();
		for(Character cell : cellCodes){
			System.out.println(cell+ ": "+ rooms.get(cell));
		}

	}

	public void printTarget(){
		System.out.println("Targets: ");
		int count = 0;
		for(BoardCell t : targets){
			System.out.println(t);
			count++;
		}
		System.out.println(count);
	}
	//--------PRINTER METHODS----------//


}//Board






