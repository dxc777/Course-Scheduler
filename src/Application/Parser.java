package Application;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import DS.Graph;
import DS.AdjList;
import DS.AdjMatrix;

public class Parser
{
	private Scanner file;
	
	private ArrayList<Course> courses;
	
	private HashMap<String,Integer> nameToIndex;
	
	private LinkedList<LinkedList<String>> edges;
	
	private Graph graph;
	
	private final char[] illegalCharacters = {' ','\t'};
	
	private final String INCOMPLETE_DATA = "Error this line does not contain all the data that is required: ";
	
	private final String CANNOT_PARSE_NUMBER = "The units entered cannot be parsed. ";
	
	private final String CONCURRENT_FLAG = "-C";
	
	private final String SEPERATOR = ",";
	
	private final byte MINIMUM_DATA_LENGTH = 3;
	
	private final byte COURSE_IDENTIFIER_INDEX = 0;

	private final byte COURSE_NAME_INDEX = 1;
	
	private final byte COURSE_UNITS_INDEX = 2;
	
	private final byte COURSE_PREREQS_INDEX = 3;
	
	private final byte CONCURENT_WEIGHT = 2;
	
	private final byte NORMAL_WEIGHT = 1;
	
	public Parser(Scanner file) 
	{
		courses = new ArrayList<Course>();
		nameToIndex = new HashMap<>();
		edges = new LinkedList<>();
		this.file = file;
		parseFile();
		buildGraph();
		System.out.println(graph);
	}
	
	private void buildGraph()
	{
		graph = new AdjMatrix(courses.size());
		int i = 0;
		for(LinkedList<String> vertexes : edges)
		{
			for(String e : vertexes) 
			{
				if(e.endsWith(CONCURRENT_FLAG)) 
				{
					e = e.substring(0, e.length()-2);
					graph.addEdge(nameToIndex.get(e), i, CONCURENT_WEIGHT);
				}
				else 
				{
					graph.addEdge(nameToIndex.get(e), i,NORMAL_WEIGHT);
				}
			}
			i++;
		}
	}

	private void parseFile() 
	{
		while(file.hasNextLine()) 
		{
			String line = file.nextLine();
			if(line.isEmpty() || line.isBlank()) 
			{
				continue;
			}
			String[] data = line.split(SEPERATOR);
			
			if(data.length < MINIMUM_DATA_LENGTH) 
			{
				System.out.println(INCOMPLETE_DATA + Arrays.toString(data));
				System.exit(0);
			}
			
			formatData(data);
			
			addData(data);
			
		}
	}

	private void addData(String[] data)
	{
		String courseName = data[COURSE_NAME_INDEX];
		int courseUnits = -1;
		
		try 
		{
			courseUnits = (int) Double.parseDouble(data[COURSE_UNITS_INDEX]);
		}
		catch(NumberFormatException e) 
		{
			System.out.println(CANNOT_PARSE_NUMBER  + Arrays.toString(data));
			System.exit(0);
		}
		
		courses.add(new Course(courseName,courseUnits));
		nameToIndex.put(data[COURSE_IDENTIFIER_INDEX], courses.size() - 1);
		edges.add(new LinkedList<>());
		
		for(int i = COURSE_PREREQS_INDEX; i < data.length; i++) 
		{		
			
			if(data[i] == null) continue;
			edges.getLast().add(data[i]);
		}
		
	}

	private void formatData(String[] data)
	{
		data[COURSE_IDENTIFIER_INDEX] = stripAllIllegalCharacters(data[COURSE_IDENTIFIER_INDEX]);
		if(data[COURSE_IDENTIFIER_INDEX] != null)
			data[COURSE_IDENTIFIER_INDEX] = data[COURSE_IDENTIFIER_INDEX].toUpperCase();
		
		data[COURSE_NAME_INDEX] = data[COURSE_NAME_INDEX].strip();
		
		//Starting at units index and moving down all prereq list
		for(int i = COURSE_PREREQS_INDEX; i < data.length; i++) 
		{
			data[i] = stripAllIllegalCharacters(data[i]);
			if(data[i] != null) data[i] = data[i].toUpperCase();
		}
	}

	private String stripAllIllegalCharacters(String str)
	{
		StringBuilder s = new StringBuilder();
		for(char c : str.toCharArray()) 
		{
			if(isIllegalCharacter(c)) continue;
			s.append(c);
		}
		if(s.isEmpty()) return null;
		return s.toString();
	}
	
	private boolean isIllegalCharacter(char c) 
	{
		for(char i : illegalCharacters) 
		{
			if(c == i) 
			{
				return true;
			}
		}
		return false;
	}
	
	
	public Graph getGraph() 
	{
		return graph;
	}
	
	public ArrayList<Course> getCourseList()
	{
		return courses;
	}
}
