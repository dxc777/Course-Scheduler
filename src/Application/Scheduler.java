package Application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import GraphFiles.*;

public class Scheduler
{
	/**
	 * This is where each list will get the information from
	 * The index of a certain course is used as a vertex
	 */
	private ArrayList<Course> courseList;
	
	private int maxUnits;
	
	private ArrayList<Semester> fullSchedule;
	
	/**
	 * Takeable classes are classes that can be added to the current semester 
	 */
	private LinkedList<FreeCourse> takeableClasses;
	
	/**
	 * freed classes are classes that have had their prerequisite count decremented
	 */
	private LinkedList<FreeCourse> freedClasses;
	
	private AdjList requiredByXGraph;
	
	private AdjList classPrereqGraph;
	
	private static final byte CONCURRENT_WEIGHT = 2;
	
	private static final byte UNCOMPLETED_WEIGHT = 1;
	
	private static final byte COMPLETED_WEIGHT = 3;
	
	//TODO have input file has section for maxUnits
	public Scheduler(Parser parsedFile, int maxUnits) 
	{
		courseList = parsedFile.getCourseList();
		requiredByXGraph = parsedFile.getRequiredByGraph();
		classPrereqGraph = parsedFile.getClassPrereqGraph();
		
		this.maxUnits = maxUnits;
		fullSchedule = new ArrayList<>();
		takeableClasses = new LinkedList<>();
		freedClasses = new LinkedList<>();
		
		initializeFreeList();
		fullSchedule.add(new Semester());
	}
	
	/**
	 * This methods find classes that have no prerequisites or can be conditionally added. 
	 * This method will be called only in the constructor
	 */
	private void initializeFreeList() 
	{
		for(int i = 0; i < classPrereqGraph.nodeCount(); i++) 
		{
			State courseState = courseState(i);
			if(courseState == State.CONDITIONAL) 
			{
				addConditionalClass(i);
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
	private void addConditionalClass(int vertex) 
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
		takeableClasses.add(new FreeCourse(State.CONDITIONAL,vertex,coreqs));
	}
	
	//TODO: temp fix very inefficient
	//To be used in pick a class function
	private void cleanUpStaleData(int vertexTaken) 
	{
		Iterator<FreeCourse> it = takeableClasses.iterator();
		LinkedList<Integer> removeList = new LinkedList<>();
		int i = 0;
		while(it.hasNext()) 
		{
			FreeCourse course = it.next();
			if(course.isConditional()) 
			{
				ArrayList<Integer> coreqs = course.getCorequisites();
				coreqs.remove(vertexTaken);
				if(coreqs.isEmpty()) 
				{
					removeList.add(i);
				}
			}
			i++;
		}
		
		while(removeList.isEmpty() == false) 
		{
			int index = removeList.removeFirst();
			takeableClasses.remove(index);
		}
	}
	
	//TODO: Add logic so that it moves linearly up or down not just one direction (in this case only up)
	public State pickCourse(int listIndex) 
	{
		FreeCourse vertex = takeableClasses.get(listIndex);
		Course course = courseList.get(vertex.getVertex());
		Semester currSemester = fullSchedule.get(fullSchedule.size() - 1);
		if(course.units() + currSemester.getUnitTotal() > maxUnits) 
		{
			return State.UNIT_EXCEEDED;
		}
		
		if(vertex.isConditional()) 
		{
			if(containsCoReqs(currSemester,vertex) == false) 
			{
				return State.COREQ_NOT_PRESENT;
			}
		}
		
		currSemester.getCourseLoad().add(vertex.getVertex());
		currSemester.setUnitTotal(currSemester.getUnitTotal() + course.units());
		
		cleanUpStaleData(vertex.getVertex());
		updateState(vertex.getVertex());
		takeableClasses.remove(vertex);
		
		if(currSemester.getUnitTotal() == maxUnits) 
		{
			return State.UNIT_MAX;
		}
		else 
		{
			return State.UNIT_NOT_FULL;
		}
	}
	private void updateState(int vertex)
	{
		
	}

	//O(nm)
	private boolean containsCoReqs(Semester currSemester, FreeCourse vertex)
	{
		ArrayList<Integer> currentClasses = currSemester.getCourseLoad();
		int count = 0;
		for(int i = 0; i < currentClasses.size(); i++) 
		{
			if(vertex.getCorequisites().contains(currentClasses.get(i)))
			{
				count++;
			}
		}
		return count == vertex.getCorequisites().size();
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
	
	//=============All methods below relate to creating strings that describe the state of the class
	public String getScheduleStr() 
	{
		StringBuilder s = new StringBuilder();
		s.append("Current Schedule that has been built\n");
		for(int i = 0; i < fullSchedule.size(); i++) 
		{
			s.append("Semester #" + (i + 1));
			ArrayList<Integer> courseLoad = fullSchedule.get(i).getCourseLoad();
			translateList(s, courseLoad, courseList);
		}
		return s.toString();
	}
	
	public String getTakeableStr() 
	{
		return translateFreeCourse("Classes that can be added to your schedule now", takeableClasses);
	}

	
	
	public String getFreedStr() 
	{
		return translateFreeCourse("Classes that are available next semester", freedClasses);
	}
	
	private void translateList(StringBuilder s, List<Integer> vertexes, List<?> sourceList) 
	{
		Iterator<Integer> it = vertexes.iterator();
		int i = 1;
		while(it.hasNext()) 
		{
			s.append(i + ") " + sourceList.get(it.next()) + "\n");
		}
	}
	
	public String translateFreeCourse(String header, LinkedList<FreeCourse> list) 
	{
		StringBuilder s = new StringBuilder();
		s.append(header);
		Iterator<FreeCourse> it = list.iterator();
		while(it.hasNext()) 
		{
			appendFreeCourse(s, it.next());
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
}
