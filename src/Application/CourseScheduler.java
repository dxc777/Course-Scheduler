package Application;
import java.util.LinkedList;
import java.util.List;

import GraphFiles.*;

import java.util.ArrayList;
import java.util.Iterator;


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
	public State overridePrereqs(int vertex)
	{
		Course course = courses.get(vertex);
		if(course.units() + currUnits > maxUnits) 
		{
			return State.SEMESTER_UNITS_EXCEEDED;
		}
		
		currUnits = course.units() + currUnits;
		schedule.get(schedule.size() - 1).add(vertex);
		overideDecrementPrereqCount(vertex);
		
		if(currUnits == maxUnits)
		{
			return State.MAX_UNITS_REACHED;
		}
		else if(freeClasses.size() == 0) 
		{
			return State.FREE_CLASSES_EMPTY;
		}
		else 
		{
			return State.CAN_CONTINUE_PICKING;
		}
	}
	
	//Here you are passing in the actual vertex not the index 
	//TODO determine what weight should be
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
	public State pick(int index)
	{
		Course course = courses.get(freeClasses.get(index));
		if(course.units() + currUnits > maxUnits) 
		{
			return State.SEMESTER_UNITS_EXCEEDED;
		}
		
		currUnits = course.units() + currUnits;
		schedule.get(schedule.size() - 1).add(freeClasses.get(index));
		decrementPrereqCount(index);
		freeClasses.remove(index);
		
		if(currUnits == maxUnits)
		{
			return State.MAX_UNITS_REACHED;
		}
		else if(freeClasses.size() == 0) 
		{
			return State.FREE_CLASSES_EMPTY;
		}
		else 
		{
			return State.CAN_CONTINUE_PICKING;
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
	
	private final String blockSeperator = "===========================================\n";
	
	public String currentState() 
	{
		StringBuilder s = new StringBuilder();
		
		int semesterNum = 1;
		s.append(blockSeperator);
		s.append("The current shcedule that has been built:\n");
		for(ArrayList<Integer> indexes : schedule) 
		{
			s.append("Semester #" + semesterNum + "\n");
			if(indexes.isEmpty()) 
			{
				s.append("<EMPTY>\n");
			}
			else 
			{
				s.append(getNumberedList(indexes));
			}
			semesterNum++;
		}
		
		s.append(blockSeperator);
		s.append("Classes that are free to pick:\n");
		s.append(getNumberedList(freeClasses));
		s.append(blockSeperator);
		
		s.append("Classes that are free to pick next semester\n");
		s.append(getNumberedList(classesFreeNextSemester));
		s.append(blockSeperator);
		return s.toString();
	}
	
	//Meant to be called with a list of indexes that will 
	//be used with the get() function from the courses arraylist
	public String getNumberedList(List<Integer> indexes) 
	{
		StringBuilder s = new StringBuilder();
		
		int numberLabel = 1;
		Iterator<Integer> iter = indexes.iterator();
		while(iter.hasNext()) 
		{
			s.append(numberLabel + ") ");
			s.append(courses.get(iter.next()) + "\n");
			numberLabel++;
		}
		
		return s.toString();
	}
	
	
	
	
	
}
