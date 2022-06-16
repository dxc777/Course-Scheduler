package Application;
import java.util.LinkedList;
import java.util.ArrayList;
import DS.*;


public class CourseScheduler
{
	private Graph graph;
	
	private ArrayList<Course> courses;
	
	private int[] prereqs;
	
	private int maxUnits;
	
	private int currUnits;
	
	private ArrayList<ArrayList<Integer>> schedule;
	
	private ArrayList<Integer> freeClasses;
	
	private LinkedList<Integer> classesFreeNextSemester;
	
	private final String blockSeperator = "===========================================\n";
	
	public static int MAX_UNITS_REACHED = 2;
	
	public static int FREE_CLASSES_EMPTY = 1;
	
	public static int CAN_CONTINUE_PICKING = 0;
	
	public CourseScheduler(Graph graph, ArrayList<Course> courses, int maxUnits) 
	{
		this.graph = graph;
		this.courses = courses;
		this.maxUnits = maxUnits;
		prereqs = new int[graph.nodeCount()];
		schedule = new ArrayList<>();
		freeClasses = new ArrayList<>();
		classesFreeNextSemester = new LinkedList<>();
		
		fillPrereqCount();
		initFreeClasses();
		schedule.add(new ArrayList<>());
	}
	
	
	private void initFreeClasses()
	{
		for(int i = 0; i < prereqs.length; i++) 
		{
			if(prereqs[i] == 0) freeClasses.add(i);
		}
	}


	private void fillPrereqCount() 
	{
		for(int i = 0; i < graph.nodeCount(); i++) 
		{
			int[] neighbors = graph.neighbors(i);
			for(int j = 0; j < neighbors.length; j++) 
			{
				prereqs[neighbors[j]]++;
			}
		}
	}
	
	
	//TODO: fix the can take same semester case
	public int overridePrereqs(int vertex) throws MaxUnitsReachedException
	{
		Course course = courses.get(vertex);
		if(course.units() + currUnits > maxUnits) 
		{
			throw new MaxUnitsReachedException(maxUnits,currUnits,course.units());
		}
		
		currUnits = course.units() + currUnits;
		schedule.get(schedule.size() - 1).add(vertex);
		overideDecrementPrereqCount(vertex);
		
		if(currUnits == maxUnits)
		{
			return MAX_UNITS_REACHED;
		}
		else if(freeClasses.size() == 0) 
		{
			return FREE_CLASSES_EMPTY;
		}
		else 
		{
			return CAN_CONTINUE_PICKING;
		}
	}
	
	//here you are passing in the actual vertex not the index 
	private void overideDecrementPrereqCount(int vertex)
	{
		int[] neighbors = graph.neighbors(vertex);
		for(int i = 0; i < neighbors.length; i++) 
		{
			prereqs[neighbors[i]]--;
		}
		prereqs[vertex] = -1;
	}


	//picking the index of a vertex in the list
	//decrement count
	//add to schedule
	//remove from list
	//update next free classes
	public int pick(int index) throws MaxUnitsReachedException
	{
		Course course = courses.get(freeClasses.get(index));
		if(course.units() + currUnits > maxUnits) 
		{
			throw new MaxUnitsReachedException(maxUnits,currUnits,course.units());
		}
		
		currUnits = course.units() + currUnits;
		schedule.get(schedule.size() - 1).add(freeClasses.get(index));
		decrementPrereqCount(index);
		freeClasses.remove(index);
		
		if(currUnits == maxUnits)
		{
			return MAX_UNITS_REACHED;
		}
		else if(freeClasses.size() == 0) 
		{
			return FREE_CLASSES_EMPTY;
		}
		else 
		{
			return CAN_CONTINUE_PICKING;
		}
		
	}
	
	
	
	private void decrementPrereqCount(int index)
	{
		int[] neighbors = graph.neighbors(freeClasses.get(index));
		for(int i = 0; i < neighbors.length; i++) 
		{
			prereqs[neighbors[i]]--;
			if(prereqs[neighbors[i]] == 0) 
			{
				classesFreeNextSemester.add(neighbors[i]);
			}
		}
	}


	public void endSemester() 
	{
		schedule.add(new ArrayList<>());
		while(classesFreeNextSemester.isEmpty() == false) 
		{
			freeClasses.add(classesFreeNextSemester.removeFirst());
		}
	}


	public ArrayList<ArrayList<Integer>> getSchedule()
	{
		return schedule;
	}


	public ArrayList<Integer> getFreeClasses()
	{
		return freeClasses;
	}


	public LinkedList<Integer> getClassesFreeNextSemester()
	{
		return classesFreeNextSemester;
	}
	
	public String currentState() 
	{
		StringBuilder s = new StringBuilder();
		int semesterNum = 1;
		int classNum = 1;
		s.append(blockSeperator);
		s.append("The current shcedule that has been built:\n");
		for(ArrayList<Integer> indexes : schedule) 
		{
			s.append("Semester #" + semesterNum + "\n");
			for(Integer i : indexes) 
			{
				s.append(classNum + ") " + courses.get(i) + "\n");
				classNum++;
			}
			semesterNum++;
		}
		s.append(blockSeperator);
		s.append("Classes that are free to pick:\n");
		
		classNum = 1;
		for(Integer i : freeClasses) 
		{
			s.append(classNum + ") " + courses.get(i) + "\n");
			classNum++;
		}
		
		s.append(blockSeperator);
		
		s.append("Classes that are free to pick next semester\n");
		classNum = 1;
		for(Integer i : classesFreeNextSemester) 
		{
			s.append(classNum + ") " + courses.get(i)  + "\n" );
			classNum++;
		}
		s.append(blockSeperator);
		return s.toString();
	}
	
	
	
	
	
}
