package Application;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;



public class Main
{
	static Scanner kb = new Scanner(System.in);
	
	static Parser fileParser;
	
	static Scheduler scheduler;
	
	static String fileName;
	
	static final String INPUT_INDICATOR = "$";
	
	static ArrayList<Action> actions = new ArrayList<>();
	
	
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
		System.out.println(INPUT_INDICATOR);
		String fileName = kb.nextLine();
		System.out.println("Enter the max units you plan to take a semester");
		System.out.println(INPUT_INDICATOR);
		int maxUnits = kb.nextInt();
		kb.nextLine();
		fileParser = new Parser(openFile(fileName));
		scheduler = new Scheduler(fileParser,maxUnits);
	}
	
	/**
	 * ====================BELOW ARE LAMBDAS FROM THE ACTION INTERFACE========================
	 */
	
	public static Action showStatus = () ->
 	{
 		System.out.println(scheduler.getScheduleStr());
 		System.out.println(scheduler.getTakeableStr());
 		System.out.println(scheduler.getFreedStr());
	};
	
	public static Action pickClass = () ->
	{
		System.out.println(scheduler.getTakeableStr());
		System.out.println("Enter the number next to the class you want to take");
		System.out.print(INPUT_INDICATOR);
		int choice = kb.nextInt();
		kb.nextLine();
	};
	
	public static Action moveSemester = () -> 
	{
		System.out.println(scheduler.getScheduleStr());
		System.out.print("Enter the semester you want to move to:");
		System.out.println(INPUT_INDICATOR);
		int semester = kb.nextInt();
		kb.nextLine();
		scheduler.moveToSemester(semester);
	};
	
	public static Action endSemester = () ->
	{
		scheduler.endSemester();
	};
	/**
	 * ==========================================================================================
	 */
	
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
