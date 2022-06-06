package Application;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;



public class Main
{
	public static void main(String[] args)
	{
		Scanner filePointer = openFile(args);
		System.out.println(filePointer.toString());
		
	}
	
	public static Scanner openFile(String[] args) 
	{
		if(args.length == 0) 
		{
			System.out.println("No file has been provided for the program");
			System.exit(0);
		}
		
		Scanner s = null;
		try 
		{
			s = new Scanner(new File(args[0]));
		}
		catch(FileNotFoundException e) 
		{
			System.out.println("The entered file cannot be found");
			System.exit(0);
		}
		return s;
		
	}
}
