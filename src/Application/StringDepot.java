package Application;

public class StringDepot
{
	public static String inputIndicator = ">>";
	
	public static String intro = "This program is designed to allow students to plan their "
			+ "entire course schedule.\n"
			+ "To use this program a text file is needed that contains all the courses a student plans to take.\n"
			+ "Each line in the text file is used to give information about a single class.\n"
			+ "Each line should follow  this format [class identifier],[class name],[class unit worth],[class prerequisite(s)]\n"
			+ "The difference between a class identifier and a class name is that the class identifier is used to identify a certain class"
			+ "and is meant to be short.\n"
			+ "A class name is meant to ease the experience of the user by allowing them to name aa class whatever they want so long as they can recognize the name.\n"
			+ "An example of a class identifier is CSC121 while a class name example is \"Intro to Programming I\"\n"
			+ "The class prerequisites list is a list of class identifiers that are comma seperated.\n"
			+ "If a class has no prerequisites then dont enter anything after the class units.\n\n"
			+ "And example input can be this:\n"
			+ "Say we have three classes \"Intro to programming\", \"Discrete Mathematics\", and \"Data Structures\"\n"
			+ "Discrete Mathematics and Intro to programming have no prerequisites but data structures requires both of those classes.\n"
			+ "The corresponding input file would look like this:\n"
			+ "CSC121,Intro to programming,3\n"
			+ "MAT281,Discrete Mathematics,4\n"
			+ "CSC311,Data Strucutres,3,CSC121,Mat 281\n\n"
			+ "Notice that the identifiers in the prerequisite list for CSC311 are not case sensitive and it doesnt not matter if there is extra spacing\n.";

	public static String filePrompt = "Enter the text file path: ";
	
}
