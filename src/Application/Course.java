package Application;

public record Course(String courseName, int units)
{
	public Course
	{
		if(units < 0) 
		{
			throw new IllegalArgumentException();
		}
	}
	
	public String toString() 
	{
		return courseName + " - " + units + " units";
	}
}
