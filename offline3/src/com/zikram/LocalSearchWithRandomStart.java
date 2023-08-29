package com.zikram;

import java.util.*;

public class LocalSearchWithRandomStart implements Solver{
    GraphEnvironment env;
    private final long INF = 1000000000;
    private List<Solution> solutions;
    public LocalSearchWithRandomStart(GraphEnvironment env) {
        this.env = env;
        this.solutions = new ArrayList<>();
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public Solution solve(int nEpochs){
        Solution bestSolution = new Solution(new HashSet<>(), new HashSet<>());
        bestSolution.cut = -INF;
        for(int i = 0; i < nEpochs; i++) {
            Solution solution = randomCut();

            solution = localSearchMaxcut(solution);
            solution.cut = env.getCut(solution.s, solution.sPrime);
            solutions.add(solution);
            if (solution.cut > bestSolution.cut) bestSolution = solution;
        }
        return bestSolution;
    }

    private Solution randomCut() {
        Solution solution = new Solution(new HashSet<>(), new HashSet<>());
        for(int j = 0; j < env.getnNodes(); j++){
            if((new Random()).nextFloat() < 0.5) solution.s.add(j);
            else solution.sPrime.add(j);
        }
        return solution;
    }

    private Solution localSearchMaxcut(Solution sol) {
        boolean change = true;
        long cnt = 0;
        while(change){
            cnt += 1;
            change = false;
            for(int i = 0; i < env.getnNodes(); i++){
                long sigmaS = getSigma(i, sol.sPrime);
                long sigmaSPrime = getSigma(i, sol.s);
                if(sol.s.contains(i) && sigmaSPrime > sigmaS){
                    sol.s.remove(i);
                    sol.sPrime.add(i);
                    change = true;
                }
                else if(sol.sPrime.contains(i) && sigmaS > sigmaSPrime){
                    sol.sPrime.remove(i);
                    sol.s.add(i);
                    change = true;
                }
            }
        }
        sol.cut = env.getCut(sol.s, sol.sPrime);
        sol.nIter = cnt;
        return sol;
    }

    private long getSigma(int v, Set<Integer> other) {
        Set<Integer> vSet = new HashSet<>();
        vSet.add(v);
        return env.getCut(vSet, other);
    }
}
