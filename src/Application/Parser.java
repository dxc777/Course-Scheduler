package Application;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import DS.Graph;
import DS.AdjList;

public class Parser
{
	private Scanner file;
	
	private ArrayList<Course> courses;
	
	private HashMap<String,Integer> nameToIndex;
	
	private Queue<LinkedList<String>> edges;
	
	private Graph g;
	
	
	public Parser(Scanner file) 
	{
		courses = new ArrayList<Course>();
		nameToIndex = new HashMap<>();
		edges = new LinkedList();
		this.file = file;
		parseFile();
	}
	
	private void parseFile() 
	{
		
	}
}
