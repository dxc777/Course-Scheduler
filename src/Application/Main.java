package Application;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;



public class Main
{
	static final int MAX_UNITS = 15;
	public static void main(String[] args)
	{
		args = new String[]{"Test.txt"};
		Scanner filePointer = openFile(args);
		Parser p = new Parser(filePointer);
		
		ArrayList<Course> courses = p.getCourseList();
		CourseScheduler cs = new CourseScheduler(p.getGraph(),p.getCourseList(),MAX_UNITS);
		System.out.println(cs.currentState());
		cs.pick(0);
		System.out.println(cs.currentState());
		cs.pick(0);
		System.out.println(cs.currentState());
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
