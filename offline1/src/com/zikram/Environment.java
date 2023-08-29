package com.zikram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Environment implements Comparable<Environment>{
    private int[][] grid;
    private int[][] goal;
    private int n;
    private int emptyRow, emptyCol;

    public Environment(int n, int[][] grid) {
        this.n = n;
        this.grid = grid;
        getEmptySlot();
        setupGoal();
    }

    private void getEmptySlot() {
        int emptySlot = -1;
        for(int i = 0; i < n*n; i++) if(grid[i/n][i%n] == -1) emptySlot = i;

        emptyRow = emptySlot / n;
        emptyCol = emptySlot % n;
    }

    public int[][] getGoal() {
        return goal;
    }

    private void setupGoal() {
        goal = new int[n][n];
        for(int i = 0; i < n*n; i++){
            goal[i / n][i % n] = i + 1;
            if(i == n*n - 1) goal[i/n][i%n] = -1; // empty is -1
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getN() {
        return n;
    }

    public static Environment getRandomEnvironment(int n){
        List<Integer> numbers = new ArrayList<>();
        for(int i = 0; i < n*n - 1; i++) numbers.add(i + 1); numbers.add(-1);
        Collections.shuffle(numbers);
        int[][] grid = new int[n][n];
        for(int i = 0; i < numbers.size(); i++) grid[i / n][i % n] = numbers.get(i);
        return new Environment(n, grid);
    }

    public static Environment getEnvironmentByShuffling(int n, int nShuffle){
        List<Integer> numbers = new ArrayList<>();
        for(int i = 0; i < n*n - 1; i++) numbers.add(i + 1); numbers.add(-1);
        int[][] grid = new int[n][n];
        for(int i = 0; i < numbers.size(); i++) grid[i / n][i % n] = numbers.get(i);
        Environment env = new Environment(n, grid);
        for(int i = 0; i < nShuffle; i++) env = env.shuffle();
        return env;
    }

    private Environment shuffle(){
        List<Environment> neighbors = getActions();
        return neighbors.get(new Random().nextInt(neighbors.size()));
    }

    public void printGrid(){
        for(int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++){
                if(grid[i][j] == -1) {
                    System.out.print("  ");
                    continue;
                }
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printGoal(){
        for(int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++){
                if(goal[i][j] == -1) {
                    System.out.print("  ");
                    continue;
                }
                System.out.print(goal[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int hammingDistance(){
        int distance = 0;
        for(int i = 0; i < n*n - 1; i++) if(grid[i/n][i%n] != goal[i/n][i%n]) distance++;
        return distance;
    }

    public int manhattanDistance(){
        int distance = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(grid[i][j] == -1) continue;
                int good_row = (grid[i][j] - 1) / n;
                int good_col = (grid[i][j] - 1) % n;
                distance += Math.abs(good_row - i);
                distance += Math.abs(good_col - j);
            }
        }
        return distance;
    }

    @Override
    public int compareTo(Environment o) {
        return Integer.compare(this.hammingDistance(), o.hammingDistance());
    }

    public List<Environment> getActions() {
        List<Environment> actions = new ArrayList<>();
        int[] dxs = {-1, 1, 0, 0};
        int[] dys = {0, 0, -1, 1};
        for (int i = 0; i < 4; i++) {
            int dx = dxs[i]; int dy = dys[i];
            if (isValidPosition(emptyRow + dx, emptyCol + dy)){
                int[][] newGrid = new int[n][n];
                for(int j = 0; j < n; j++) newGrid[j] = grid[j].clone();
                newGrid[emptyRow][emptyCol] = grid[emptyRow + dx][emptyCol + dy];
                newGrid[emptyRow + dx][emptyCol + dy] = -1;
                actions.add(new Environment(n, newGrid));
            }

        }
        return actions;
    }

    private boolean isValidPosition(int x, int y){
        return (x >= 0 && x < n && y >= 0 && y < n);
    }

    public int getEmptyRow() {
        return emptyRow;
    }

    public int getEmptyCol() {
        return emptyCol;
    }

    public int getNumberOfInversions(){
        int nInversions = 0;
        for(int i = 0; i < n*n; i++){
            for(int j = i + 1; j < n*n; j++){
                if(grid[i/n][i%n] == -1 || grid[j/n][j%n] == -1) continue;
                if(grid[i/n][i%n] > grid[j/n][j%n]) nInversions++;
            }
        }
        return nInversions;
    }

    public boolean isSolvable(){
        int nInversions = getNumberOfInversions();
        return (n % 2 != 0 && nInversions % 2 == 0) || (n % 2 == 0 && emptyRow % 2  != nInversions % 2 );
    }
}
