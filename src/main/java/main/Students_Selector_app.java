package main;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Students_Selector_app {
    static String FILEPATH = System.getProperty("user.dir");
    static String backupPath = FILEPATH + File.separator + "backup.txt";
    static String filePath = FILEPATH + File.separator + "file.txt";

    public static void main(String[] args) throws IOException, RuntimeException {
        try {
            if (args[0] != null) {
                FILEPATH = args[0];// Set the filepath if provided as a command line argument
                backupPath = FILEPATH + File.separator + "backup.txt";
                filePath = FILEPATH + File.separator + "file.txt";
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        List<Student> students = new ArrayList<>(); // Create a list to store student objects
        String isReseted = "";
        String ask1 = JOptionPane.showInputDialog("Do you want to reset the list of students?\n\tY/N");
        if (Objects.equals(ask1.toLowerCase(), "y")) {// Check if user wants to reset the student list
            String ask2 = JOptionPane.showInputDialog("Are you sure?");
            if (Objects.equals(ask2.toLowerCase(), "y")) {
                isReseted = Create_the_list.create();// Reset the list if confirmed by the user
            }
        }

        BufferedReader reader = new BufferedReader(new FileReader(filePath));// Open the file for reading

        Set<String> ResultSet;
        String Result;
        String line;
        int n;
        // Read student data from the file and create Student objects
        while ((line = reader.readLine()) != null) {
            String[] temp = line.split(" ");
            students.add(new Student(Integer.parseInt(temp[0]), temp[1], Integer.parseInt(temp[2])));
        }
        reader.close(); // Close the file
        // Prompt the user to input the number of students to select
        do {
            n = Integer.parseInt(JOptionPane.showInputDialog(isReseted + "\nInput how many students will answer today: "));
        } while (n <= 0 || n > students.size());
        do {
            ResultSet = pick_students(students, n);  // Pick students randomly
            do {
                Result = JOptionPane.showInputDialog(ResultSet + "\nAre You Satisfied with result?" +
                        "\n\tY(yes)/N(no)/Q(quit)");
                if (Objects.equals(Result.toLowerCase(), "q")) System.exit(0);// Exit the program if user wants to quit
            } while (!Objects.equals(Result.toLowerCase(), "y") && !Objects.equals(Result.toLowerCase(), "n"));

        } while (!Objects.equals(Result.toLowerCase(), "y"));// Repeat until user is satisfied
        // Update the number of times each student has been selected
        for (Student student : students) {
            if (ResultSet.contains(student.getName())) {
                student.setAnswers(student.getAnswers() + 1);
            }
        }
        students.sort(Comparator.comparing(Student::getId));// Sort the students by ID
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));// Open the file for writing
        // Write the updated student data back to the file
        for (int i = 0; i < students.size(); i++) {
            writer.write(i + 1 + " " + students.get(i).getName() + " " + students.get(i).getAnswers());
            writer.newLine();
        }

        writer.close();// Close the file

    }

    // Method to pick students based on the number of times they have been selected
    public static Set<String> pick_students(List<Student> students, int n) throws NullPointerException {
        Set<String> names;
        int min_answers = students.stream().mapToInt(Student::getAnswers).min().orElseThrow(
                RuntimeException::new
        );

        List<Student> students_left = students.stream().filter(
                student -> student.getAnswers() == min_answers
        ).toList();

        List<Student> students_removed;
        students_removed = students.stream().filter(
                student -> student.getAnswers() == min_answers + 1
        ).toList();


        int size_of_left = students_left.size();
        if (size_of_left == n) {
            names = new HashSet<>(students_left.stream().map(Student::getName).toList()
            );
        } else if (size_of_left > n) {
            names = random_pick(students_left, n);
        } else {
            //if number of pick is larger than updated students
            n -= size_of_left;
            names = new HashSet<>(random_pick(students_removed, n));
            names.addAll(students_left.stream().map(Student::getName).toList());
        }
        return names;
    }

    // Method to randomly select students
    public static Set<String> random_pick(List<Student> students, int n) {
        List<String> students_names = new ArrayList<>(students.stream().map(
                Student::getName
        ).toList());
        Collections.shuffle(students_names);
        Set<String> names = new HashSet<>();
        for (int i = 0; i < n; i++) {
            names.add(students_names.get(i));
        }
        return names;
    }
}