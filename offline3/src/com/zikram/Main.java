package com.zikram;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        FileWriter myWriter = new FileWriter("data.csv");
        myWriter.write("id,nodes,edges,rand,greedy,semiGreedy,grasp\n");
        int nInputs = 54;
//        int nInputs = 23;
        Solver solver;
        for(int ind = 23; ind <= nInputs; ind++){
            System.out.println("Testing input " + ind);
            // write your code here
            String fileName = "set1/g" + ind +".rud";
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            int nNodes = scanner.nextInt();
            int nEdges = scanner.nextInt();

//            System.out.println("Nodes : " + nNodes + " Edges : " + nEdges);
            GraphEnvironment env = new GraphEnvironment(nNodes);
            for(int i = 0; i < nEdges; i++){
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                int w = scanner.nextInt();
                env.addEdge(u, v, w, true);
            }
            System.out.println("Random");
            // random
            solver = new RandomSolver(env);
            Solution randSolution = solver.solve(20);

            System.out.println("Greedy");
            // greedy
            solver = new GreedySolver(env);
            Solution greedySolution = solver.solve(20);

            System.out.println("Semi Greedy");
            // semi-greedy
            solver = new SemiGreedySolver(env);
            Solution semiGreedySolution = solver.solve(20);

            System.out.println("Grasp");
            // grasp
            solver = new GRASPSolver(env);
            Solution graspSolution = solver.solve(20);
            myWriter.write(ind + "," + nNodes + "," + nEdges + ",");
            myWriter.write(randSolution.cut + "," + greedySolution.cut + "," + semiGreedySolution.cut + "," + graspSolution.cut+"\n");
        }

        myWriter.close();
    }
}
