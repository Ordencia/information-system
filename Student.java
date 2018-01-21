import java.util.*;
import java.io.*;

//Guest user class. Extends User
public class Student extends User{

	private Scanner sc = new Scanner(System.in);

	private final String MENU = "user_menus/Student_Menu.txt";
	private String firstName;
	private String lastName;
	private String oen;
	private String studentNumber;
	private CourseTracker courseTracker;
	private boolean acceptedToUni;
	private int numMenuOptions;
	private String[] menuOptions;

	//admin constructor. takes in username, password, and admin number as strings
	//respectively
	public Student(String username, String password, String firstName, String lastName, String oen, String studentNumber, CourseTracker courseTracker, boolean accepted){
		super(username, password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.oen = oen;
		this.studentNumber = studentNumber;
		this.courseTracker = courseTracker;
		acceptedToUni = accepted;
		loadMenu();
	}

	//accessors
	public String getFirstName(){
		return firstName;
	}

	public String getLastName(){
		return lastName;
	}

	public String getOEN(){
		return oen;
	}

	public String getStudentNumber(){
		return studentNumber;
	}

	public CourseTracker getCourseTracker(){
		return courseTracker;
	}

	public boolean getAcceptedToUni(){
		return acceptedToUni;
	}

	//mutators
	public void setFirstName(String s){
		firstName = s;
	}

	public void setLastName(String s){
		lastName = s;
	}

	public void setOEN(String s){
		oen = s;
	}

	public void setStudentNumber(String s){
		studentNumber = s;
	}

	public void setCourseTracker(CourseTracker c){
		courseTracker = c;
	}

	public void setAcceptedToUni(boolean b){
		acceptedToUni = b;
	}

	public boolean setPassword (String oldPass, String newPass) {
		if (oldPass.equals(password)) {
			password = newPass;
			return true;
		}
		return false;
	}

	public boolean setUsername (String oldPass, String newUsername) {
		if (oldPass.equals(password)) {
			username = newUsername;
			return true;
		}
		return false;
	}

	public void loadMenu () {
		try {
			BufferedReader in = new BufferedReader(new FileReader(MENU));
			numMenuOptions = Integer.parseInt(in.readLine());
			in.readLine();

			menuOptions = new String[numMenuOptions];
			for (int i = 0; i < numMenuOptions; i++) {
				menuOptions[i] = in.readLine();
			}

			in.close();
		}
		catch(IOException iox){
			System.out.println("Error reading student menu from file.");
		}
	}

	//displays the menu for admin users
	public void displayMainMenu() {
		Scanner sc = new Scanner(System.in);
		int choice = 0;
		boolean keepOnGoing = true;

		while (keepOnGoing) {

			System.out.println("\n--- Student Menu (Logged In As " + username + ") ---");
			for (int i = 0; i < numMenuOptions; i++) {
				System.out.println(menuOptions[i]);
			}

			while (!(choice > 0 && choice <= numMenuOptions)) {
				try {
						System.out.print("Enter an option: ");
						choice = sc.nextInt();
						sc.nextLine();
					} catch (InputMismatchException ime) {
						sc.nextLine();
						System.out.print("Invalid input. ");
				}
			}

			//executes the choice number
			switch (choice){
				case 1:
				   	QNAStudent.initQNA();
					QNAStudent.displayMenu();
				 	break;
				case 2:
					//call program list method
					break;
				case 3:
					viewCourses();
					break;
				case 4:
					addCourse();
					break;
				case 5:
					deleteCourse();
					break;
				case 6:
					updateCourseMark();
					break;
				case 7:
					keepOnGoing = false;
					break;
			}
			choice = 0;
		}
	}

	private void displayCourseList () {
		ArrayList<ActiveCourse> courseList = courseTracker.getCourseList();

		System.out.printf("%n--- %s %s's Courses ---%n", firstName, lastName);
		for (int i = 0; i < courseList.size(); i++) {
			ActiveCourse course = courseList.get(i);
			System.out.printf("%d - %s (%.1f)%n", i+1, course.getCourseCode(), course.getMark());
		}
	}

	public void viewCourses () {
		displayCourseList();
		System.out.println("Enter anything to return to the previous menu: ");
		sc.nextLine();
	}

	// READ OVER THIS METHOD
	public void addCourse () {
		final int EXIT = -1;
		String courseCode;
		int mark = -1;

		System.out.println("\n--- Add Course ---");

		if (courseTracker.getNumCourses() >= CourseTracker.MAX_COURSES) {
			System.out.println("A student can only take 8 courses at a time! Please drop an existing course");
			System.out.println("and try again. Enter anything to return to the previous menu.");
			sc.nextLine();
			return;
		} else {
			System.out.print("Course Code: ");
			courseCode = sc.nextLine();
			while (!CourseTracker.isValidCourse(courseCode)) {
				System.out.println("That course is not offered at AY Jackson.");
				System.out.print("Enter a new course (enter " + EXIT + " to cancel): ");
				courseCode = sc.nextLine();
				try {
					if (Integer.parseInt(courseCode) == EXIT) {
						return;
					}
				} catch (NumberFormatException nfe) {
				};
			}
			while (courseTracker.courseExists(courseCode)) {
				System.out.println("Already taking this course!");
				System.out.print("Enter a new course (enter " + EXIT + " to cancel): ");
				courseCode = sc.nextLine();
				try {
					if (Integer.parseInt(courseCode) == EXIT) {
						return;
					}
				} catch (NumberFormatException nfe) {
				};
			}

			do {
				try {
					System.out.print("Mark: ");
					mark = sc.nextInt();
					sc.nextLine();
				} catch (InputMismatchException ime) {
					sc.nextLine();
				}
				if (!(mark >= ActiveCourse.MIN_MARK && mark <= ActiveCourse.MAX_MARK)) {
					System.out.println("Invalid input.");
					mark = -1;
				}
			} while (mark == -1);

			ActiveCourse course = new ActiveCourse(courseCode, mark);
			courseTracker.addCourse(course);
			System.out.println("Course added successfully!");
		}
	}

	public void deleteCourse () {
		final int EXIT = -1;
		int choice = 0;
		ArrayList<ActiveCourse> courseList = courseTracker.getCourseList();
		displayCourseList();

		System.out.println("Enter the number associated with the course you wish to delete (or enter " + EXIT + " to cancel):");
		while (choice == 0) {
			try {
				choice = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException ime) {
				sc.nextLine();
			}
			
			if (choice == EXIT) {
				return;
			} else if (!(choice >= 1 && choice <= courseList.size())) {
				System.out.print("Invalid input! Please try again: ");
				choice = 0;
			}
		}

		ActiveCourse course = courseList.get(choice - 1);
		courseTracker.dropCourse(course);
		System.out.println("Course successfully droppped.");
	}

	//student update mark
	public void updateCourseMark () {
		final int EXIT = -1;
		int choice = 0;
		double mark = Double.NaN;
		ArrayList<ActiveCourse> courseList = courseTracker.getCourseList();
		displayCourseList();

		System.out.println("Enter the number associated with the course you wish to update (or enter " + EXIT + " to cancel):");
		while (choice == 0) {
			try {
				choice = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException ime) {
				sc.nextLine();
			}
			
			if (choice == EXIT) {
				return;
			} else if (!(choice >= 1 && choice <= courseList.size())) {
				System.out.print("Invalid input! Please try again: ");
				choice = 0;
			}
		}

		ActiveCourse course = courseList.get(choice - 1);

		System.out.printf("Enter a new mark for %s (or enter %d to cancel): ", course.getCourseCode(), EXIT);
		do {
			try {
				mark = sc.nextDouble();
				sc.nextLine();
			} catch (InputMismatchException ime) {
				sc.nextLine();
				System.out.println("Invalid input. Please try again: ");
			}
			if (mark == EXIT) {
				return;
			} else if (!course.updateMark(mark)) {
				mark = Double.NaN;
				System.out.println("Mark must be between " + ActiveCourse.MIN_MARK + " and " + ActiveCourse.MAX_MARK + ". Please try again: ");
			}
		} while (mark == Double.NaN);

		System.out.println("Mark updated successfully!");
	}
}
