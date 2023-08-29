package com.zikram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class LocalSearch {
    public static void main(String[] args) throws IOException {
        FileWriter myWriter = new FileWriter("data.csv");
        myWriter.write("id,nodes,edges,iterID,nIter_randLocal,randLocal,nIter_GRASP,grasp\n");
//        int nInputs = 54;
        int nInputs = 10;
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
            Solution randomLocalSearchSolution = solver.solve(20);
            List<Solution> randomSolutions = ((LocalSearchWithRandomStart)solver).getSolutions();

            System.out.println("Grasp");
            // grasp
            solver = new GRASPSolver(env);
            Solution graspSolution = solver.solve(20);
            List<Solution> graspSolutions = ((GRASPSolver)solver).getSolutions();

            for(int nIter = 1; nIter <= 20; nIter++){
                myWriter.write(ind + "," + nNodes + "," + nEdges + ","+nIter+",");
                myWriter.write(randomSolutions.get(nIter-1).nIter+ "," + randomSolutions.get(nIter-1).cut + "," + graspSolutions.get(nIter-1).nIter + ","+ graspSolutions.get(nIter-1).cut+"\n");
            }
        }

        myWriter.close();
    }
}
