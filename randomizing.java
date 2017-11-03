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
     * Generates a random false
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
            randContents.add(true);
        }

        int n = clauses.size();
        int count = 1;
        boolean done = false;

        while ( count <= Math.log( n ) / Math.log( 2 ) ) { //2^count <= clause.size
            int count2 = 1;

            Collections.shuffle( randContents ); //shuffling contents of array

            while ( count2 <= (double) 2*n*n ) { //count2 <= 2(clause.size^2)

                Clause c = genRandFalse(clauses, clausesFalse, randContents);

                if ( c == null ) {
                    done = true;
                    break;
                }
                else {
                    int r = new Random().nextInt( 2 ) + 1;

                    if ( r == 1 ) {
                        Boolean b = randContents.get( Math.abs( c.getA() ) );
                        randContents.set( Math.abs( c.getA() ), !b );
                    }
                    else {
                        Boolean b = randContents.get( Math.abs( c.getB() ) );
                        randContents.set( Math.abs( c.getB() ), !b );
                    }
                }
                count2++;
            }
            if ( done ) {
                break;
            }
            count++;
        }
        if(done){
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

