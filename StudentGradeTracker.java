
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class Student {

    String name;
    int marks;

    Student(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }

    String calculateGrade() {

        if (marks >= 90) {
            return "A+";
        } else if (marks >= 80) {
            return "A";
        } else if (marks >= 70) {
            return "B";
        } else if (marks >= 60) {
            return "C";
        } else if (marks >= 40) {
            return "D";
        } else {
            return "F";
        }
    }
}

public class StudentGradeTracker {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Student> students = new ArrayList<>();

    static final String FILE_NAME = "students.txt";

    // Read integer safely
    static int readInt(String message) {

        while (true) {

            System.out.print(message);

            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    // Read marks between 0 and 100
    static int readMarks() {

        while (true) {

            int marks = readInt("Enter marks (0-100): ");

            if (marks >= 0 && marks <= 100) {
                return marks;
            }

            System.out.println("Marks must be between 0 and 100!");
        }
    }

    // Find student by name
    static Student findStudent(String name) {

        for (Student s : students) {

            if (s.name.equalsIgnoreCase(name)) {
                return s;
            }
        }

        return null;
    }

    // Save data in file
    static void saveData() {

        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Student s : students) {

                writer.write(s.name + "\t" + s.marks);
                writer.newLine();
            }

            System.out.println("Data saved successfully!");

        } catch (IOException e) {

            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load data from file
    static void loadData() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\t", 2);

                if (data.length != 2) {
                    continue;
                }

                try {

                    String name = data[0].trim();
                    int marks = Integer.parseInt(data[1].trim());

                    if (!name.isEmpty()
                            && marks >= 0
                            && marks <= 100
                            && findStudent(name) == null) {

                        students.add(new Student(name, marks));
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid record.");
                }
            }

            System.out.println("Student data loaded successfully!");

        } catch (IOException e) {

            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // Add student
    static void addStudent() {

        System.out.print("Enter student name: ");

        String name = sc.nextLine().trim();

        if (name.isEmpty() || name.contains("\t")) {
            System.out.println("Invalid student name!");
            return;
        }

        // Duplicate name validation
        if (findStudent(name) != null) {

            System.out.println("Student already exists!");
            return;
        }

        int marks = readMarks();

        students.add(new Student(name, marks));

        saveData();

        System.out.println("Student added successfully!");
    }

    // View all students
    static void viewStudents() {

        if (students.isEmpty()) {

            System.out.println("No student records found!");
            return;
        }

        System.out.println("\n========== STUDENT REPORT ==========");

        System.out.printf("%-15s %-10s %-10s%n",
                "Name", "Marks", "Grade");

        System.out.println("------------------------------------");

        for (Student s : students) {

            System.out.printf("%-15s %-10d %-10s%n",
                    s.name, s.marks, s.calculateGrade());
        }
    }

    // Search student
    static void searchStudent() {

        System.out.print("Enter student name to search: ");

        String name = sc.nextLine().trim();

        Student s = findStudent(name);

        if (s != null) {

            System.out.println("\nStudent Found!");

            System.out.println("Name: " + s.name);
            System.out.println("Marks: " + s.marks);
            System.out.println("Grade: " + s.calculateGrade());

        } else {

            System.out.println("Student not found!");
        }
    }

    // Update student marks
    static void updateStudent() {

        System.out.print("Enter student name to update: ");

        String name = sc.nextLine().trim();

        Student s = findStudent(name);

        if (s != null) {

            System.out.println("Current Marks: " + s.marks);

            s.marks = readMarks();

            saveData();

            System.out.println("Marks updated successfully!");

        } else {

            System.out.println("Student not found!");
        }
    }

    // Delete student
    static void deleteStudent() {

        System.out.print("Enter student name to delete: ");

        String name = sc.nextLine().trim();

        Student s = findStudent(name);

        if (s != null) {

            students.remove(s);

            saveData();

            System.out.println("Student deleted successfully!");

        } else {

            System.out.println("Student not found!");
        }
    }

    // Show summary
    static void showSummary() {

        if (students.isEmpty()) {

            System.out.println("No student records available!");
            return;
        }

        int total = 0;
        int highest = students.get(0).marks;
        int lowest = students.get(0).marks;

        for (Student s : students) {

            total += s.marks;

            if (s.marks > highest) {
                highest = s.marks;
            }

            if (s.marks < lowest) {
                lowest = s.marks;
            }
        }

        double average = (double) total / students.size();

        System.out.println("\n========== CLASS SUMMARY ==========");

        System.out.println("Total Students: " + students.size());

        System.out.printf("Average Marks: %.2f%n", average);

        System.out.println("Highest Marks: " + highest);

        System.out.println("Lowest Marks: " + lowest);
    }

    // Main method
    public static void main(String[] args) {

        loadData();

        while (true) {

            System.out.println("\n========== STUDENT GRADE TRACKER ==========");

            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Marks");
            System.out.println("5. Delete Student");
            System.out.println("6. Show Summary");
            System.out.println("7. Exit");

            int choice = readInt("\nEnter your choice: ");

            switch (choice) {

                case 1:
                    addStudent();
                    break;

                case 2:
                    viewStudents();
                    break;

                case 3:
                    searchStudent();
                    break;

                case 4:
                    updateStudent();
                    break;

                case 5:
                    deleteStudent();
                    break;

                case 6:
                    showSummary();
                    break;

                case 7:
                    System.out.println("Thank you for using Student Grade Tracker!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice! Please select 1-7.");
            }
        }
    }
}
