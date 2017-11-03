package com.example;

import java.util.*;

class Sat2Solver {

    private LinkedList<Integer>[] adj; //adjacency list implementation of graph
    private LinkedList<Integer>[] adjInv; //adjacency list implementation of inverse graph

    private boolean[] explored; //to check if node in graph has been visited or not
    private boolean[] exploredInv; //to check if node in inverse graph has been visited or not

    private Deque<Integer> stk = new ArrayDeque<>(); //stack to get final output after topological sort

    private int[] scc; //stores the Strongly Connected Component (SCC) a node is part of.
    private int sccCounter = 1; //maintains number of scc

    private ArrayList<Integer> popped; //final output of stack for processing of boolean variable assignment

    private boolean[] output; //stores boolean values assigned to variable if satisfiable
    private String op =""; //output form 1
    private String op1=""; //output form 1

    //initializing all data structures
    //ignore unchecked assignment warning of adj and adjInv
    Sat2Solver(int n){
        final int SIZE= 2*n+1; //size of all data structures. Must have space for node and its inverse so 2n but staring from var1 thus +1
        adj = new LinkedList[SIZE];
        adjInv = new LinkedList[SIZE];
        for(int i=0; i<SIZE; i++) {
            adj[i] = new LinkedList();
            adjInv[i] = new LinkedList();
        }
        popped = new ArrayList<>();
        output = new boolean[((SIZE-1)/2)+1];
        explored = new boolean[SIZE];
        exploredInv = new boolean[SIZE];
        scc = new int[SIZE]; //store the scc that the particular node belongs to
        Arrays.fill(output,true); //setting all output to be true by default. assigned false later if inv node occurs after node after topological sort
    }

    private void addEdges(int a, int b){ //original graph edges
        adj[a].add(b);
    } //adding edges in graph

    private void addEdgesInverse(Integer a, Integer b){ //adding edges in inverse graph
        adjInv[b].add(a);
    }

    // first step of Kosaraju's algorithm
    private void dfsFirst(int u){
        if(!explored[u]){
            explored[u]=true;
            for(int i=0; i<adj[u].size(); i++){
                dfsFirst(adj[u].get(i));
            }
            stk.push(u);
        }
    }

    // second step of Kosaraju's algorithm
    private void dfsSecond(int u){
        if(!exploredInv[u]){
            exploredInv[u]=true;
            for(int i=0; i<adjInv
                    [u].size(); i++){
                dfsSecond(adjInv[u].get(i));
            }
            scc[u]= sccCounter;
        }
    }

    // satifiability check method
    void satisfiable(int n, int m, int[] a, int[] b){
        //add edges to graph
        for(int i=0; i<m; i++){
            // for a[i] or b[i], addEdges -a[i] -> b[i] AND -b[i] -> a[i]
            // variable x is mapped to x
            // variable -x is mapped to n+x = n-(-x)
            if (a[i] > 0 && b[i] > 0) {
                addEdges( a[i]+n, b[i] );
                addEdgesInverse( a[i]+n, b[i] );
                addEdges(b[i]+n, a[i]);
                addEdgesInverse(b[i]+n, a[i]);
            }
            else if(a[i]>0 && b[i]<0){
                addEdges(a[i]+n, n-b[i]);
                addEdgesInverse(a[i]+n, n-b[i]);
                addEdges(-b[i], a[i]);
                addEdgesInverse(-b[i], a[i]);
            }
            else if (a[i] < 0 && b[i]>0) {
                addEdges(-a[i], b[i]);
                addEdgesInverse(-a[i], b[i]);
                addEdges(b[i] + n, n - a[i]);
                addEdgesInverse(b[i] + n, n - a[i]);
            }
            else {
                addEdges(-a[i], n - b[i]);
                addEdgesInverse(-a[i], n - b[i]);
                addEdges(-b[i], n - a[i]);
                addEdgesInverse(-b[i], n - a[i]);
            }
        }

        //step 1 traversing graph
        for(int i=1; i<=2*n; i++){
            if(!explored[i]){
                dfsFirst(i);
            }
        }

        //step 2 traversing inverse graph
        while(!stk.isEmpty()){
            int o = stk.peek();
            popped.add(stk.pop()); //store popped element from stack after topological sorting for assigning boolean values
            if(!exploredInv[o]){
                this.sccCounter +=1;
                dfsSecond(o);
            }
        }


        for(int i=0; i<=n;i++){
            if(scc[i]==scc[i+n]){ //checking if a node and it's inverse lie in the same scc
                System.out.println("FORMULA UNSATISFIABLE");
                return;
            }
        }

        //node and inverse don't lie in same scc
        System.out.println("FORMULA SATISFIABLE");
        //using output of topological sort stored in popped to determine which boolean value to assign to variables
        for(int i=1; i<=popped.size()/2; i++) {
            if(popped.indexOf(i)<popped.indexOf(i+n)){ //if inverse node appears after node in topological sort output
                output[i]=false; //set false value for variable represented by node
            }
        }

        //prit solution in 2 forms
        System.out.println("\nSolution: ");
        for(int i=1; i<output.length; i++){
            op1+=("x"+i+"="+output[i]+" "); //easily readable output format
            if(output[i]){
                op +=("1 "); //required output format
            }
            else if(!output[i]){
                op +=("0 "); //required output format
            }
        }

        System.out.println(op); //print output in required format
        System.out.println(op1); //print output in easy to read format
    }
}
