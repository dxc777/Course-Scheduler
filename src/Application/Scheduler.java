package Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import GraphFiles.*;

/**
 * The scheduler class will handle the logic of when a class is available and 
 * adding and removing classes to a schedule. It runs based on a modified version of Khan's algorithm for 
 * topological sort. The main difference being that the user will chose what vertex is chosen from the queue
 * and the array that would have the number of incoming edges is no replaced by a graph. I chose to use another graph
 * instead of an array of integers as the graph can provide more information about a class and it prerequisites.
 * And it also solved the problem of figuring out if a class with co requisites can be taken. However this does 
 * change the time complexity for certain parts of the algorithm.
 * @author J
 *
 */
public class Scheduler
{
	private ArrayList<Course> courseList;
		
	/**
	 * true = class taken 
	 * false = class not taken
	 */
	private HashMap<Integer,Integer> completedClasses;
	
	private int maxUnits;
	
	private int currSemester;
	
	private int coursesProcessed;
	
	private ArrayList<Semester> fullSchedule;
	
	/**
	 * Takeable classes are classes that have all prerequisites met or can be taken with
	 * co requisites
	 */
	private LinkedList<FreeCourse> takeableClasses;
	
	/**
	 * FreedClasses is meant to hold classes whose prerequisites were all completed during a semester but cannot be taken
	 * until the next semester. When a semester ends then the free classes list is emptied into takeable classes
	 */
	private LinkedList<FreeCourse> freedClasses;
	
	/**
	 * Required by X as in for vertex a each edge X requires a as a prerequisite.
	 * This is the graph that would be used in a normal implementation of topological sort
	 */
	private AdjList requiredByXGraph;
	
	/**
	 * For each vertex in this graph each edge is a prerequisite to that vertex.
	 * The meaning of each edge weight is listed below. This graph would be the integer array that holds the incoming 
	 * edges.
	 */
	private AdjList classPrereqGraph;
	
	private static final int CONCURRENT_WEIGHT = 2;
	
	private static final int UNCOMPLETED_WEIGHT = 1;
	
	private static final int COMPLETED_WEIGHT = 3;
		
	private static final int UNCOMPLETED = -1;
	
	private static final int DEFAULT_SCHEDULE_LENGTH = 24;
	
	/**
	 * The constructor takes the parser object that has been instantiated with a text file. From that it 
	 * will pull all the information it needs.
	 * @param parsedFile
	 * @param maxUnits
	 */
	//TODO have input file has section for maxUnits
	public Scheduler(Parser parsedFile, int maxUnits) 
	{
		courseList = parsedFile.getCourseList();
		requiredByXGraph = parsedFile.getRequiredByGraph();
		classPrereqGraph = parsedFile.getClassPrereqGraph();
		
		this.maxUnits = maxUnits;
		currSemester = 0;
		fullSchedule = new ArrayList<>();
		takeableClasses = new LinkedList<>();
		freedClasses = new LinkedList<>();
		completedClasses = new HashMap<>(courseList.size());
		
		initializeFreeList();
		for(int i = 0; i < DEFAULT_SCHEDULE_LENGTH; i++) 
		{
			fullSchedule.add(new Semester());
		}
		
	}
	
	/**
	 * This methods find classes that have no prerequisites or can be conditionally added. 
	 * This method will be called only in the constructor. This is equivalent to finding vertexes that have 
	 * had all incoming edges processed in Khans algorithm.
	 */
	private void initializeFreeList() 
	{
		for(int i = 0; i < classPrereqGraph.nodeCount(); i++) 
		{
			State courseState = courseState(i);
			if(courseState == State.CONDITIONAL) 
			{
				addConditionalClass(i,takeableClasses);
			}
			else if(courseState == State.ALL_PREREQ_DONE) 
			{
				takeableClasses.add(new FreeCourse(State.ALL_PREREQ_DONE,i,null));
			}
		}
	}
	
	/**
	 * If a class needs to be added and needs co-requisites as well then its vertex needs to be saved as well as all the
	 * co-requisites it needs
	 * Note: there is an issue when a conditional class is added as if the co - requisite is taken then that 
	 * conditional no longer exists need a solution to remove stale data
	 * @param vertex
	 */
	private void addConditionalClass(int vertex,LinkedList<FreeCourse> list) 
	{
		ArrayList<Integer> coreqs = new ArrayList<>();
		Edge curr = classPrereqGraph.getHeadOfVertex(vertex).next;
		while(curr != null) 
		{
			if(curr.weight == CONCURRENT_WEIGHT) 
			{
				coreqs.add(curr.adjVertex);
			}
			curr = curr.next;
		}
		list.add(new FreeCourse(State.CONDITIONAL,vertex,coreqs));
	}
	
	/**
	 * Classes that can be taken concurrently with other classes is optional and not required. So if a students decides
	 * not to take a class without it corequisites and said class has all corequisites completed then 
	 * it is just a class with all prereqs completed. However there is no way for freedClasses to tell that is has been completed
	 * so it needs to check for that specific case.
	 * @param vertexTaken
	 */
	private void cleanUpStaleData() 
	{
		Iterator<FreeCourse> courseIter = takeableClasses.iterator();
		while(courseIter.hasNext()) 
		{
			FreeCourse course = courseIter.next();
			if(course.isConditional()) 
			{
				Iterator<Integer> coreqIter = course.getCorequisites().iterator();
				while(coreqIter.hasNext()) 
				{
					if(completedClasses.getOrDefault(coreqIter.next(), UNCOMPLETED) != UNCOMPLETED) 
					{
						coreqIter.remove();
					}
				}
				
				if(course.getCorequisites().isEmpty()) 
				{
					courseIter.remove();
				}
			}
		}
	}
	
	/**
	 * Pick course is called when a student adds a course to their schedule and will handle the logic of adding it to the schedule 
	 * and adding classes that can be taken to the freedlist
	 * @param listIndex
	 * @return
	 */
	//TODO: Add logic so that it moves linearly up or down not just one direction (in this case only up)
	public State pickCourse(int listIndex) 
	{
		FreeCourse vertex = takeableClasses.get(listIndex);
		Course course = courseList.get(vertex.getVertex());
		Semester semesterData = fullSchedule.get(this.currSemester);
		if(course.units() + semesterData.getUnitTotal() > maxUnits) 
		{
			return State.UNIT_EXCEEDED;
		}
		
		if(vertex.isConditional()) 
		{
			if(containsCoReqs(vertex) == false) 
			{
				return State.COREQ_NOT_PRESENT;
			}
		}
		
		semesterData.getCourseLoad().add(vertex.getVertex());
		completedClasses.put(vertex.getVertex(), this.currSemester);
		semesterData.setUnitTotal(semesterData.getUnitTotal() + course.units());
		coursesProcessed++;
		updateState(vertex.getVertex());
		takeableClasses.remove(vertex);
		
		if(semesterData.getUnitTotal() == maxUnits) 
		{
			return State.UNIT_MAX;
		}
		else if(takeableClasses.isEmpty()) 
		{
			return State.NO_CLASSES_LEFT;
		}
		else 
		{
			return State.UNIT_NOT_FULL;
		}
		
	}
	
	public void removeCourse(int semester, int vertex) 
	{
		
	}
	//TODO add logic to handle case of when semester ends
	public void endSemester() 
	{		
		cleanUpStaleData();
		
		while(freedClasses.isEmpty() == false) 
		{
			takeableClasses.add(freedClasses.removeFirst());
		}
	}
	
	/**
	 * Update state updates the edge weight of a vertex and if it cna be taken it will be added to the feedClasses list
	 * 
	 * @param vertex
	 */
	private void updateState(int vertex)
	{
		Edge curr = requiredByXGraph.getHeadOfVertex(vertex).next;
		while(curr != null) 
		{
			classPrereqGraph.addEdge(curr.adjVertex, vertex, COMPLETED_WEIGHT);
			State courseState = courseState(curr.adjVertex);
			if(courseState == State.CONDITIONAL) 
			{
				addConditionalClass(curr.adjVertex,freedClasses);
			}
			else if(courseState == State.ALL_PREREQ_DONE) 
			{
				freedClasses.add(new FreeCourse(State.ALL_PREREQ_DONE,curr.adjVertex,null));
			}
			curr = curr.next;
		}
	}

	private boolean containsCoReqs(FreeCourse vertex)
	{
		ArrayList<Integer> coreqs = vertex.getCorequisites();
		for(Integer course : coreqs) 
		{
			if(completedClasses.getOrDefault(course, UNCOMPLETED_WEIGHT) == UNCOMPLETED_WEIGHT) 
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Given the vertex of a class this function will return if this class can be taken, cannot be taken,
	 * or can be taken with certain co-requisite
	 * @param vertex
	 * @return the state of the course
	 */
	private State courseState(int vertex) 
	{
		boolean containsConditional = false;
		Edge curr = classPrereqGraph.getHeadOfVertex(vertex).next;
		while(curr != null) 
		{
			if(curr.weight == CONCURRENT_WEIGHT) 
			{
				containsConditional = true;
			}
			else if(curr.weight == UNCOMPLETED_WEIGHT)	
			{
				return State.CANNOT_TAKE;
			}
			curr = curr.next;
		}
		
		if(containsConditional) 
		{
			return State.CONDITIONAL;
		}
		else 
		{
			return State.ALL_PREREQ_DONE;
		}
	}
	
	
	public boolean finshedPlanning() 
	{
		return courseList.size() == coursesProcessed;
	}
	
	//=============All methods below relate to creating strings that describe the state of the class========
	public String getScheduleStr() 
	{
		StringBuilder s = new StringBuilder();
		s.append("Current Schedule that has been built\n");
		for(int i = 0; i < fullSchedule.size(); i++) 
		{
			s.append("Semester #" + (i + 1));
			s.append('\n');
			ArrayList<Integer> courseLoad = fullSchedule.get(i).getCourseLoad();
			translateList(s, courseLoad, courseList);
		}
		return s.toString();
	}
	
	public String getTakeableStr() 
	{
		return translateFreeCourse("Classes that can be added to your schedule now:", takeableClasses);
	}

	
	
	public String getFreedStr() 
	{
		return translateFreeCourse("Classes that are available next semester:", freedClasses);
	}
	
	private void translateList(StringBuilder s, List<Integer> vertexes, List<?> sourceList) 
	{
		Iterator<Integer> it = vertexes.iterator();
		int i = 1;
		while(it.hasNext()) 
		{
			s.append(i + ") " + sourceList.get(it.next()) + "\n");
			i++;
		}
	}
	
	public String translateFreeCourse(String header, LinkedList<FreeCourse> list) 
	{
		StringBuilder s = new StringBuilder();
		s.append(header);
		s.append('\n');
		Iterator<FreeCourse> it = list.iterator();
		int i = 1;
		while(it.hasNext()) 
		{
			s.append(i);
			s.append(')');
			s.append(' ');
			appendFreeCourse(s, it.next());
			i++;
		}
		return s.toString();
	}
	
	private void appendFreeCourse(StringBuilder s, FreeCourse course)
	{
		if(course.getType() == State.ALL_PREREQ_DONE) 
		{
			s.append(courseList.get(course.getVertex()) + " ===All prerequisites complete===\n");
		}
		else 
		{
			s.append(courseList.get(course.getVertex()) + " ===Can be taken but the following classes need to be"
					+ " in the same schedule:===\n");
			translateList(s, course.getCorequisites(), courseList);
		}
	}
	
	public boolean moveToSemester(int semester) 
	{
		if(semester < 0 || semester >= fullSchedule.size()) 
		{
			return false;
		}
		currSemester = semester;
		return true;
	}
}
