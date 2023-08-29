package com.zikram;

import java.util.*;

public class RandomSolver implements Solver{
    GraphEnvironment env;
    private final long INF = 1000000000;

    public RandomSolver(GraphEnvironment env) {
        this.env = env;
    }

    @Override
    public Solution solve(int nEpochs) {
        Solution bestSolution = new Solution(new HashSet<>(), new HashSet<>());
        bestSolution.cut = -INF;
        long totalCut = 0;
        for(int i = 0; i < nEpochs; i++){
            List<Integer> nodes = new ArrayList<>();
            for(int j = 0; j < env.getnNodes(); j++) nodes.add(j);
            Collections.shuffle(nodes);

            Solution solution = new Solution(new HashSet<>(), new HashSet<>());
            for(int j = 0; j < env.getnNodes(); j++){
                if((new Random()).nextFloat() < 0.5) solution.s.add(j);
                else solution.sPrime.add(j);
            }

            solution.cut = env.getCut(solution.s, solution.sPrime);
            totalCut += solution.cut;
            if (solution.cut > bestSolution.cut) bestSolution = solution;
        }
        bestSolution.cut = totalCut / nEpochs;
        return bestSolution;
    }
}
