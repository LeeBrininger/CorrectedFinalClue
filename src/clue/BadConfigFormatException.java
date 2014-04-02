package clue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//
//You should throw a BadConfigFormatException with an appropriate message
//if you uncover any issues with the configuration file. For example, 
//if all rows don't have the same number of columns, or if a row of the legend file doesn't have exactly 2 items, 
//or a room has an initial that is not a valid room, etc.
//Extra credit: There should be a reason to create your own
//exception class, rather than just throwing an Exception.
//If a BadConfigFormatException is thrown, write a message to a
//log file (you might think of this as a log file someone could submit to you,
//if you distributed the code and a user encountered a problem).
@SuppressWarnings("serial")
public class BadConfigFormatException extends RuntimeException{
public BadConfigFormatException(String string) throws IOException{
	    	String fileName ="logfile.txt";
	    
	    	File file = new File(fileName);
	    	 
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw;
		
				fw = new FileWriter(file.getAbsoluteFile());
			
		
			BufferedWriter bw = new BufferedWriter(fw);
		
				bw.write(toString());
		
		
				bw.close();
		
	    	
	    
	    }

	    public String toString (){
	    	return "There was an error in the configuration!";
	    	
	    }

}
