package Application;

public class MaxUnitsReachedException extends Exception
{
	public MaxUnitsReachedException(int maxUnits, int currUnits, int classUnits) 
	{
		super("Cannot add another class to this schedule as it currently holds " + currUnits
			+ " untis and adding a class worth " + classUnits 
			+ " would exceed the maximum units allowed");
	}
}
