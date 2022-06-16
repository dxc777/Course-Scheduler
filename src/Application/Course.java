package Application;

/**
 * I used a record here and that will generate getters and setters as well as 
 * make the class immutable
 * @author J
 *
 */
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
