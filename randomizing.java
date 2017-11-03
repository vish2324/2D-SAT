package com.example;

import java.util.*;

class randomizing {

    ArrayList<Clause> check;
    randomizing(int[] a, int[] b){
        check = createClauses(a,b);
    }

    /**
     * Create clauses after taking 2 int array inputs
     */
    private static ArrayList<Clause> createClauses( int[] a, int[] b ) {
        ArrayList<Clause> clauses = new ArrayList<>();
        for ( int i = 0; i < a.length; i++ ) {
            clauses.add( new Clause( a[i], b[i] ) );
        }
        return clauses;
    }

    /**
     * Get all unsatisfied clauses in random order
     */
    private static Clause genRandFalse( ArrayList<Clause> clauses, ArrayList<Clause> clausesFalse, ArrayList<Boolean> randContents) {
        Clause randomClause = null;
        clausesFalse.clear(); //resets clausesFalse array

        for ( Clause c : clauses ) {
            if( !c.evaluate( randContents.get( Math.abs( c.getA() ) ), randContents.get( Math.abs( c.getB() ) ) ) ) { //evaluates contents of randContents array
                clausesFalse.add( c );  //if false, add particular clause into clauseFalse
            }
        }

        if( !clausesFalse.isEmpty() ) { //if after processing, clauseFalse is not empty, shuffle contents and set output as clausesFalse0
            Collections.shuffle( clausesFalse );
            randomClause = clausesFalse.get( 0 );
        }
        return randomClause;
    }

    void rand2SAT(ArrayList<Clause> clauses ) {
        ArrayList<Clause> clausesFalse = new ArrayList<>();
        ArrayList<Boolean> randContents = new ArrayList<>();

        for ( int i = 0; i < clauses.size(); i++ ) {
            randContents.add(true); //arbitrary initialization of all variable values as true
        }

        int n = clauses.size();
        int count = 1;
        boolean done = false;

        while ( count <= Math.log( n ) / Math.log( 2 ) ) { //run as long as count less than log2(clause size)
            int count2 = 1;

            Collections.shuffle( randContents ); //shuffling contents of array so as to pick arbitrary variable

            while ( count2 <= (double) 2*n*n ) { //run 2n^2 time
                //both while loop conditions help decrease error probability

                Clause c = genRandFalse(clauses, clausesFalse, randContents); //get all unsatisfied clauses

                if ( c == null ) { //if no unsatisfied clauses
                    done = true; //looping done
                    break;
                }
                else {
                    int r = new Random().nextInt( 2 ) + 1; //choosing which vairable to choose form unsatisfied clause
                    
                    if ( r == 1 ) {
                        Boolean b = randContents.get( Math.abs( c.getA() ) );
                        randContents.set( Math.abs( c.getA() ), !b ); //invert boolean value and put back
                    }
                    else {
                        Boolean b = randContents.get( Math.abs( c.getB() ) );
                        randContents.set( Math.abs( c.getB() ), !b ); //invert boolean value and put back
                    }
                }
                count2++;
            }
            if ( done ) { //if done then exit loop
                break;
            }
            count++;
        }
        if(done){ //check if solution found or not nad is satifsiable or not
            System.out.println("\nRandomizer algo output: Satisfiable\n");
        }
        else
            System.out.println("\nRandomizer algo output: Unsatisfiable\n");
    }
}

/**
 * Represents a single clause
 */
class Clause {

    private int a, b; //literals in a clause

    Clause(int a, int b){
        this.a = a;
        this.b = b;
    }

    int getA() {
        return a;
    }

    int getB() {
        return b;
    }

    boolean evaluate(boolean a1, boolean b1) {
        if(a < 0){
            a1 = !a1;
        }
        if(b < 0){
            b1 = !b1;
        }
        return a1 || b1;
    }
}

