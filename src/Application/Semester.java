package Application;

import java.util.ArrayList;

public class Semester
{
	private int unitTotal;

	private ArrayList<Integer> courseLoad;

	public Semester() 
	{
		this(0);
	}

	public Semester(int unitTotal)
	{
		super();
		this.unitTotal = unitTotal;
		this.courseLoad = new ArrayList<>();
	}
	
	public int getUnitTotal()
	{
		return unitTotal;
	}

	public void setUnitTotal(int unitTotal)
	{
		this.unitTotal = unitTotal;
	}

	public ArrayList<Integer> getCourseLoad()
	{
		return courseLoad;
	}

	public void setCourseLoad(ArrayList<Integer> courseLoad)
	{
		this.courseLoad = courseLoad;
	}
	
}
