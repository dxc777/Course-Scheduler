package Application;

public record Course(String courseName, int units)
{
	public Course
	{
		if(units < 0) 
		{
			throw new IllegalArgumentException("The units of a course cannot be a negative number");
		}
	}
	
	public String toString() 
	{
		return courseName + " - " + units + " units";
	}
}
