package com.zikram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class LocalSearchAllGraph {
    public static void main(String[] args) throws IOException {
        FileWriter myWriter = new FileWriter("data.csv");
        myWriter.write("id,nodes,edges,nIter_randLocal,randLocal,nIter_grasp,grasp\n");
//        int nInputs = 4;
        int nInputs = 54;
        Solver solver;
        for(int ind = 1; ind <= nInputs; ind++){
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

            System.out.println("Local Random search");
            solver = new LocalSearchWithRandomStart(env);
            Solution randomLocalSearchSolution = solver.solve(50);
            double meanIterLocal = 0;
            for(Solution sol : ((LocalSearchWithRandomStart)solver).getSolutions()) meanIterLocal += sol.nIter;
            meanIterLocal /= 50;

            System.out.println("Grasp");
            // grasp
            solver = new GRASPSolver(env);
            Solution graspSolution = solver.solve(50);
            double meanIterGrasp = 0;
            for(Solution sol : ((GRASPSolver)solver).getSolutions()) meanIterGrasp += sol.nIter;
            meanIterGrasp /= 50;

            myWriter.write(ind + "," + nNodes + "," + nEdges + ",");
            myWriter.write(meanIterLocal + "," + randomLocalSearchSolution.cut + "," + meanIterGrasp + "," + graspSolution.cut+"\n");

        }

        myWriter.close();
    }
}
