package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class cnfread {

    static int N, M; //(N, M) = number of (literals, clauses)

    public static void main(String[] args) {

        try{
            int lineNum=-1; //to count line numbers for checking if file is empty or not
            boolean pFound = false; // to check if p statement exists in file
            System.out.println("What is the file name?"); //take user input of file name along with entire file path
            Scanner input = new Scanner(System.in); // scans user input and takes in name of file
            File file = new File(input.nextLine()); //opens file from input

            input = new Scanner(file); //reuse input to read specified file

            while(input.hasNext()){ //as long as input has something next and not end of file
                String line = input.nextLine(); //takes in individual lines endign with \n from file
                lineNum++; //increment line number

                String[] statement = line.split(" "); //splitting line input based on spaces for ease of obtaining data
                if(!pFound) { // do following only if p statement not encountered, assuming only 1 p statement per file
                    if (statement[0].equalsIgnoreCase("p")) { // checking first 'word' of statement to see if it is p rather than c or undefined case
                        pFound = true; //found p statement in file
                        if (statement[1].equalsIgnoreCase("cnf")) { //checking if file is of cnf format
                            N = Integer.parseInt(statement[2]); //take in number of literals, assuming p statement is of valid format
                            M = Integer.parseInt(statement[3]); //take in number of clauses, assuming p statement is of valid format
                        }
                        else {
                            System.out.println("Wrong file format!"); //file is not of cnf format
                            break;
                        }
                    }
                }
                else {
                    //continue processing
                    // obtain literals and clauses
                }
            }
            System.out.println("Number of statements: "+lineNum); //fun data!
            if (lineNum==-1){ //empty file case
                System.out.println("File empty");
            }
            else if (!pFound){ //if p was not found. will print if file not empty
                System.out.println("No Problem line found");
            }
            input.close(); //important to close input
        }
        catch (ArrayIndexOutOfBoundsException a){ //if p statement doesn't have number of literals or clauses specified. Assuming if specified is of correct format
            System.out.println("Invalid statement");
            a.printStackTrace();
        }
        catch (FileNotFoundException e) { //checking if file exists or not after taking user input
            System.out.println("File not found!");
            e.printStackTrace();
        }
    }
}
