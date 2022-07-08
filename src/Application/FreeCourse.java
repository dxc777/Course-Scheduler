package Application;

import java.util.ArrayList;

public class FreeCourse
{
	private State type;
	
	private int vertex;
	
	private ArrayList<Integer> corequisites;

	public FreeCourse(State type, int vertex, ArrayList<Integer> corequisites)
	{
		this.type = type;
		this.vertex = vertex;
		this.corequisites = corequisites;
	}
	
	public boolean isConditional() 
	{
		return type == State.CONDITIONAL;
	}

	public State getType()
	{
		return type;
	}

	public void setType(State type)
	{
		this.type = type;
	}

	public int getVertex()
	{
		return vertex;
	}

	public void setVertex(int vertex)
	{
		this.vertex = vertex;
	}

	public ArrayList<Integer> getCorequisites()
	{
		return corequisites;
	}

	public void setCorequisites(ArrayList<Integer> corequisites)
	{
		this.corequisites = corequisites;
	}
}
