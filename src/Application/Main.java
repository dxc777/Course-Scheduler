package Application;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;



public class Main
{
	static Scanner kb = new Scanner(System.in);
	
	static Parser fileParser;
	
	static Scheduler scheduler;
	
	static String fileName;
	
	static final String INPUT_INDICATOR = "$";
	
	public static void main(String[] args)
	{
		createScheduler();
		buildSchedule();
	}
	
	private static void buildSchedule()
	{
		while(scheduler.finshedPlanning() == false) 
		{
			
		}
	}

	public static void createScheduler() 
	{
		System.out.println("Enter the file name");
		String fileName = kb.nextLine();
		System.out.println("Enter the max units you plan to take a semester");
		int maxUnits = kb.nextInt();
		fileParser = new Parser(openFile(fileName));
		scheduler = new Scheduler(fileParser,maxUnits);
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
