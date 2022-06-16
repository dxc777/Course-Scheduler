package Application;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;



public class Main
{
	static Scanner kb = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		System.out.println(StringDepot.intro);
		System.out.println(StringDepot.filePrompt);
		
		System.out.print(StringDepot.inputIndicator);
		String filePath = kb.nextLine();
		Scanner file = openFile(filePath);
		
		System.out.println(file);
		System.out.println(kb);
	}
	

	public static Scanner openFile(String filePath) 
	{
		
		Scanner s = null;
		try 
		{
			s = new Scanner(new File(filePath));
		}
		catch(FileNotFoundException e) 
		{
			System.out.println("The entered file cannot be found");
			System.exit(0);
		}
		return s;
		
	}
}
