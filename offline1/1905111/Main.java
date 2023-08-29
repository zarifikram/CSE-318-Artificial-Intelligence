package com.zikram;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws InterruptedException, IOException {
	    Environment env = Environment.getEnvironmentByShuffling(4, 70);
//        Environment env = getEnvFromInput();
        System.out.println("The Environment:");
        env.printGrid();
        System.out.println("\nSolution With Manhattan:");
        A_Star manAgent = new A_Star(env, true);
        manAgent.solve();
        manAgent.showResult();
        System.out.println("\nSolution With Hamming:");
        A_Star hamAgent = new A_Star(env, false);
        hamAgent.solve();
        hamAgent.showResult();

    }

    private static Environment getEnvFromInput() throws IOException {
        System.out.println("Enter the grid dimention: ");
        int n = Integer.parseInt(reader.readLine());
        System.out.println("Enter the grid");
        int[][] grid = new int[n][n];
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++){
                String inp = reader.readLine();
                if(inp.equalsIgnoreCase("*"))  grid[i][j] = -1;
                else{
                    grid[i][j] = Integer.parseInt(inp);
                }
            }
        return new Environment(n, grid);
    }
}
