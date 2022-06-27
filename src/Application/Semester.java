package Application;

import java.util.ArrayList;

public class Semester
{
	private ArrayList<Integer> courseLoad;//course load will hold the vertex # 
	
	private final ArrayList<Course> courseList;
	
	private int totalSemesterUnits;
	
	public Semester(ArrayList<Course> courseList) 
	{
		this.courseLoad = new ArrayList<>();
		this.totalSemesterUnits = 0;
		this.courseList = courseList; //using a reference to already built list
	}

	public ArrayList<Integer> getCourseLoad()
	{
		return courseLoad;
	}

	public int getTotalSemesterUnits()
	{
		return totalSemesterUnits;
	}

	public String toString() 
	{
		StringBuilder s = new StringBuilder();
		for(int i : courseLoad) 
		{
			s.append(courseList.get(i) + "\n");
		}
		return new String(s);
	}
	
	
}
