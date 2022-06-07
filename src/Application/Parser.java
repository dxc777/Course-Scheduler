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
	private ArrayList<Course> courseNames;
	
	private HashMap<String, Integer> nameToIndex;
	
	private Queue<LinkedList<Course>> edgeToBeDetermined; 
	
	private Graph g;
	
	private final char SEPERATOR = ',';
	
	public Parser(Scanner filePointer) 
	{
		courseNames = new ArrayList<>();
		nameToIndex = new HashMap<>();
		edgeToBeDetermined = new LinkedList<>();
	}
	
	
	
}
