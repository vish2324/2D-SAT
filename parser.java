package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class parser {

    private static int N, M; //(N, M) = number of (literals, clauses)
    private static ArrayList<Integer> a = new ArrayList<>(); //set of 1st literals from each clause
    private static ArrayList<Integer> b = new ArrayList<>(); //set of 2nd literals from each clause
    private static int literalCounter=1; //to ensure appropriate literal assignment
    private static int sat2Counter=0; //to ensure only valid numbers are stored in set of literals

    //function to convert Integer ArrayList to int array
    private static int[] convertToInt(List<Integer> integers){
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for(int i=0; i<ret.length; i++){
            ret[i] = iterator.next();
        }
        return ret;
    }

    public static void main(String[] args) {
        /*String again; //to open multiple files one after the other
        do {*/
            try {
                int lineNum = -1; //to count line numbers for checking if file is empty or not

                boolean pFound = false; // to check if p statement exists in file

                System.out.println("\nWhat is the file name?"); //take user input of file name along with entire file path
                Scanner input = new Scanner(System.in); // scans user input and takes in name of file
                File file = new File(input.nextLine()); //opens file from input

                input = new Scanner(file); //reuse input to read specified file

                while (input.hasNext()) { //as long as input has something lined up and not end of file

                    String line = input.nextLine(); //takes in individual lines ending with \n from file
                    lineNum++; //increment line number

                    String[] statement = line.split(" "); //splitting line input based on spaces for ease of obtaining data

                    if (!pFound) { // do following only if p statement not encountered, assuming only 1 p statement per file
                        if (statement[0].equals("p")) { // checking first 'word' of statement to see if it is p rather than c or undefined case
                            pFound = true; //found p statement in file

                            if (statement[1].equals("cnf")) { //checking if file is of cnf format
                                N = Integer.parseInt(statement[2]); //take in number of literals, assuming p statement is of valid format
                                M = Integer.parseInt(statement[3]); //take in number of clauses, assuming p statement is of valid format
                            } else {
                                System.out.println("Wrong internal format!"); //file is not of cnf format
                                input.close();
                                return;
                            }
                        }
                    } else {
                        for (String s : statement) {
                            if(s.equals("0")){
                                sat2Counter = 0; //reset sat2counter when 0 is encountered from cnf file
                            }
                            if(sat2Counter==2){ //checking if number of literals per clause doesn't exceed 2. Otherwise not 2SAT anymore
                                System.out.println("File not 2SAT format");
                                input.close();
                                return;
                            }
                            else if (!s.equals("0") && !s.equalsIgnoreCase(" ") && sat2Counter!=2) { //ensuring only valid inputs are passed. Also covers case when extra spacing or tabs are encountered
                                if (literalCounter == 1) {
                                    a.add(Integer.parseInt(s));
                                    literalCounter = 2; //move to array b assignment
                                    sat2Counter = 1; //1st literal of clause encountered
                                }
                                else if (literalCounter == 2) {
                                    b.add(Integer.parseInt(s));
                                    literalCounter = 1; //go back to array a assignment
                                    sat2Counter = 2; //2nd literal of clause encountered
                                }
                            }
                        }
                    }
                }

                if (lineNum == -1) { //empty file case
                    System.out.println("File empty!");
                    input.close();
                    return;
                }
                else if (!pFound) { //will print if file not empty and no p found
                    System.out.println("No Problem line found!");
                    System.out.println("here4");
                    input.close();
                    return;
                }

                input.close();
            }
            catch (ArrayIndexOutOfBoundsException a) { //if p statement doesn't have number of literals or clauses specified. Assuming if specified is of correct format
                System.out.println("Invalid statement!");
                return;
            }
            catch (FileNotFoundException e) { //checking if file exists or not after taking user input
                System.out.println("File not found!");
                return;
            }

            //extra messages to ensure smooth running
            System.out.println("\nFile found");
            System.out.println("File not empty!");
            System.out.println("Problem line found!\n");

            int[] arrA = convertToInt(a);
            int[] arrB = convertToInt(b);

            Sat2Solver solve = new Sat2Solver(N);

            long startGraph = System.nanoTime();
            solve.satisfiable(N, M, arrA, arrB); //calling Sat2Solver class with satisfiable method
            long endGraph = System.nanoTime();

            randomizing lol = new randomizing(arrA, arrB);

            long startRand = System.nanoTime();
            lol.rand2SAT(lol.check);
            long endRand = System.nanoTime();

            long graphTime = endGraph - startGraph;
            long randTime = endRand - startRand;
            System.out.println("Graph Time:" + graphTime/1000000.0 + "ms");
            System.out.println("Randm Time:" + randTime/1000000.0 + "ms");

            double efficiency = ( (randTime/1000000.0) - (graphTime/1000000.0) ) / (graphTime/1000000.0) * 100.0;
            System.out.println("Implication graph algo is "+efficiency+"% faster than randomized algo");
/*
            //to read another cnf file in case of multiple input
            System.out.println("\n\nRead another file? ");
            System.out.println("'y' or 'n'");
            Scanner cont = new Scanner(System.in);
            again = cont.nextLine();
        } while(again.equalsIgnoreCase("y"));
        */
    }
}
