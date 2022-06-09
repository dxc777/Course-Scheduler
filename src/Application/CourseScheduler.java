package Application;
import java.util.LinkedList;
import java.util.ArrayList;
import DS.*;


public class CourseScheduler
{
	private Graph graph;
	
	private int[] prereqs;
	
	private ArrayList<ArrayList<Integer>> schedule;
	
	private LinkedList<Integer> freeClasses;
	
	private LinkedList<Integer> classesFreeNextSemester;
	
	public CourseScheduler(Graph graph) 
	{
		this.graph = graph;
	}
	
	
	
}
