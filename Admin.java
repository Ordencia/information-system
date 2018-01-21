import java.util.*;
import java.io.*;

//Guest user class. Extends User
public class Admin extends User{

	private final String MAIN_MENU = "user_menus/Admin_Menu.txt";
	private final String UPDATE_STUDENT_INFO_MENU = "user_menus/Admin_Menu_UpdateStudentInfo.txt";
	private String adminNumber;
	private Scanner sc;
	private ArrayList<Student> studentList;
	private ProgramDatabase programDatabase;
	private int numMenuOptions;
	private String[] menuOptions;
	private int numUpdateCourseMenuOptions;
	private String[] updateCourseMenuOptions;

	//admin constructor. takes in username, password, and admin number as strings
	//respectively
	public Admin(String username, String password, String adminNumber, ArrayList<Student> studentList, ProgramDatabase programDatabase){
		super(username, password);
		this.adminNumber = adminNumber;
		this.studentList = studentList;
		this.programDatabase = programDatabase;
		sc = new Scanner(System.in);
		loadMainMenu();
		loadUpdateCourseMenu();
	}

	public String getUsername(){
		return username;
	}

	public String getPassword(){
		return password;
	}

	public String getAdminNumber(){
		return adminNumber;
	}

	public void setUsername(String user){
		username = user;
	}

	public void setPassword(String pass){
		password = pass;
	}

	public void setAdminNumber(String num){
		adminNumber = num;
	}

	public void loadMainMenu () {
		try {
			BufferedReader in = new BufferedReader(new FileReader(MAIN_MENU));
			numMenuOptions = Integer.parseInt(in.readLine());
			in.readLine();

			menuOptions = new String[numMenuOptions];
			for (int i = 0; i < numMenuOptions; i++) {
				menuOptions[i] = in.readLine();
			}

			in.close();
		} catch(IOException iox){
			System.out.println("Error reading admin menu from file.");
		}
	}

	public void loadUpdateCourseMenu () {
		try {
			BufferedReader in = new BufferedReader(new FileReader(UPDATE_STUDENT_INFO_MENU));
			numUpdateCourseMenuOptions = Integer.parseInt(in.readLine());
			in.readLine();

			updateCourseMenuOptions = new String[numUpdateCourseMenuOptions];
			for (int i = 0; i < numUpdateCourseMenuOptions; i++) {
				updateCourseMenuOptions[i] = in.readLine();
			}

			in.close();
		} catch(IOException iox){
			System.out.println("Error reading admin's \"update student course menu\" from file.");
		}
	}

	//displays the menu for admin users
	public void displayMainMenu(){
		int choice = 0;
		boolean keepOnGoing = true;

		while(keepOnGoing){	   
			System.out.println("\n--- Admin Menu (Logged In As " + username + ") ---");
			for (int i = 0; i < numMenuOptions; i++) {
				System.out.println(menuOptions[i]);
			}

			while (choice == 0) {
				try {
					System.out.print("Enter your choice: ");
					choice = sc.nextInt();
					sc.nextLine();
				} catch (InputMismatchException ime) {
					sc.nextLine();
				}

				if (!(choice > 0 && choice <= numMenuOptions)) {
					System.out.println("Invalid input!");
					choice = 0;
				}
			}

			//executes the choice number				
			switch (choice){
				case 1:
					QNAAdmin.initQNA();
					QNAAdmin.displayMenu();
					break;
				case 2:
					//search programs
					break;
				case 3:
					// add program
					break;
				case 4: 
					//delete program
					break;
				case 5:
					Student student = selectStudentByOEN();
					if (student != null) {
						displayUpdateCourseMenu(student);
					}
					break;
				case 6:
					keepOnGoing = false;
					break;
			}
			choice = 0;
		}
	}

	private Student selectStudentByOEN () {
		final int EXIT = -1;
		boolean markUpdated = false;
		String studentNumber, courseCode;
		double newMark = -1;
		Student student;
		ActiveCourse course;

		System.out.println("\n--- Select Student ---");
		System.out.print("Student OEN: ");
		studentNumber = sc.nextLine();
		student = searchStudentByOEN(studentNumber);
		while (student == null) {
			System.out.println("That student number does not exist. Enter " +  EXIT + " to cancel.");
			System.out.print("Student OEN: ");
			studentNumber = sc.nextLine();
			try {
				if (Integer.parseInt(studentNumber) == EXIT) {
					return null;
				}
			} catch (NumberFormatException nfe) {};
			student = searchStudentByOEN(studentNumber);
		}

		return student;
	}

	private void displayUpdateCourseMenu (Student student) {
		final int EXIT = -1;
		int choice = 0;
		boolean keepOnGoing = true;

		do {
			System.out.println("\n--- Update Student Courses ---");
			for (int i = 0; i < updateCourseMenuOptions.length; i++) {
				System.out.println(updateCourseMenuOptions[i]);
			}

			while (choice == 0) {
				try {
					System.out.print("Enter your choice (or enter " + EXIT + " to cancel): ");
					choice = sc.nextInt();
					sc.nextLine();
				} catch (InputMismatchException ime) {
					sc.nextLine();
				}

				if (choice == EXIT) {
					return;
				} else if (!(choice > 0 && choice <= numUpdateCourseMenuOptions)) {
					System.out.println("Invalid input!");
					choice = 0;
				}
			}

			switch (choice){
				case 1:
					student.viewCourses();
					break;
				case 2:
					student.addCourse();
					break;
				case 3:
					student.deleteCourse();
					break;
				case 4: 
					student.updateCourseMark();
					break;
			}
			choice = 0;
		} while (keepOnGoing);
	}

	private Student searchStudentByOEN (String OEN) {
		for(Student student: studentList){
			if(student.getOEN().equals(OEN)){
				return student;
			}
		}
		return null;
	}
}
