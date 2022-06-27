package Application;

import java.util.*;
import GraphFiles.*;

public class Scheduler
{
	private ArrayList<Course> courseList;
	
	private ArrayList<Semester> schedule;
	
	private LinkedList<Integer> freeClasses;
	
	private LinkedList<Integer> classesFreeNextSemester;
	
	private Graph prereqGraph;
	
	private Graph requirementGraph;
	
	public Scheduler(Parser parsedFile) 
	{
		courseList = parsedFile.getCourseList();
		prereqGraph = parsedFile.getPrereqGraph();
		requirementGraph = parsedFile.getRequirementGraph();
		
		schedule = new ArrayList<>();
		freeClasses = new LinkedList<>();
		classesFreeNextSemester = new LinkedList<>();
	}
	
	
}
