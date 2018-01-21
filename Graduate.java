/**
* Graduate.java
* Stores all information about a single Graduate.
*/

public class Graduate {
	private String graduateID;
	private String firstName;
	private String lastName;
	private Course[] topSixCourses;
	private double topSixAverage;
	private String university;
	private int programID;
	private int yearGraduated;

	public Graduate (String graduateID, String firstName, String lastName, Course[] topSixCourses, double topSixAverage, String university, int programID, int yearGraduated) {
		this.graduateID = graduateID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.topSixCourses = topSixCourses;
		this.topSixAverage = topSixAverage;
		this.university = university;
		this.programID = programID;
		this.yearGraduated = yearGraduated;
	}

	public String getGraduateID () {
		return graduateID;
	}

	public String getFirstName () {
		return firstName;
	}

	public String getLastName () {
		return lastName;
	}

	public Course[] getTopSixCourses () {
		return topSixCourses;
	}

	public double getTopSixAverage () {
		return topSixAverage;
	}

	public String getUniversity () {
		return university;
	}

	public int getProgramID () {
		return programID;
	}

	public int getYearGraduated () {
		return yearGraduated;
	}

	public double compareToGraduate (Graduate other) {
		return topSixAverage - other.topSixAverage;
	}
}