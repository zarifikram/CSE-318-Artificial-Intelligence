package com.zikram;

import java.util.*;

public class GRASPSolver implements Solver{
    GraphEnvironment env;
    private final long INF = 1000000000;
    private List<Solution> solutions;
    public GRASPSolver(GraphEnvironment env) {
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
            Solution solution = semiGreedyMaxcut();

            solution = localSearchMaxcut(solution);
            solution.cut = env.getCut(solution.s, solution.sPrime);
            solutions.add(solution);
            if (solution.cut > bestSolution.cut) bestSolution = solution;
        }
        return bestSolution;
    }

    private Solution semiGreedyMaxcut() {
        float alpha = (new Random()).nextFloat();
        long minWeight = env.getMinWeight();
        long maxWeight = env.getMaxWeight();

        float gamma = minWeight + alpha * (maxWeight - minWeight);

        ArrayList<Edge> RCL = env.getEdgesGreaterThan(gamma);
        Edge edge = getRandomEdge(RCL);
        Set<Integer> X = new HashSet<>();
        Set<Integer> Y = new HashSet<>();
        X.add(edge.u);
        Y.add(edge.v);

        Set<Integer> vPrime = initializeVPrime();
        while(!vPrime.isEmpty()){
            updateVPrime(vPrime, X, Y);
            if(vPrime.isEmpty()) break;
            long[] xSigmas = getSigmas(vPrime, Y);
            long[] ySigmas = getSigmas(vPrime, X);
//            System.out.println(vPrime);
            minWeight = getMinWeight(vPrime, xSigmas, ySigmas);
            maxWeight = getMaxWeight(vPrime, xSigmas, ySigmas);
            gamma = minWeight + alpha * (maxWeight - minWeight);

            List<Integer> vertexRCL = getRCL(vPrime, xSigmas, ySigmas, gamma);

            int vStar = vertexRCL.get((new Random()).nextInt(vertexRCL.size()));

            updateXAndY(vStar, X, Y, xSigmas, ySigmas);

        }
        Solution solution = new Solution(X, Y, env.getCut(X, Y));
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

    private void updateXAndY(int vStar, Set<Integer> X, Set<Integer> Y, long[] xSigmas, long[] ySigmas) {
        if(xSigmas[vStar] > ySigmas[vStar]) X.add(vStar);
        else Y.add(vStar);
    }

    private List<Integer> getRCL(Set<Integer> vPrime, long[] xSigmas, long[] ySigmas, float gamma) {
        List<Integer> vertexRCL = new ArrayList<>();
        for(int v : vPrime){
            if(xSigmas[v] >= gamma || ySigmas[v] >= gamma) vertexRCL.add(v);
        }
        return vertexRCL;
    }

    private long getMaxWeight(Set<Integer> vPrime, long[] xSigmas, long[] ySigmas) {
        long maxWeight = -INF;

        for(int v : vPrime) if(xSigmas[v] > maxWeight) maxWeight = xSigmas[v];
        for(int v : vPrime) if(ySigmas[v] > maxWeight) maxWeight = ySigmas[v];

        return maxWeight;
    }

    private long getMinWeight(Set<Integer> vPrime, long[] xSigmas, long[] ySigmas) {
        long minWeight = INF;

        for(int v : vPrime) if(xSigmas[v] < minWeight) minWeight = xSigmas[v];
        for(int v : vPrime) if(ySigmas[v] < minWeight) minWeight = ySigmas[v];

        return minWeight;
    }
    

    private long[] getSigmas(Set<Integer> vPrime, Set<Integer> other) {
        long[] sigmas = new long[env.getnNodes()];
        for(int v : vPrime){
            sigmas[v] = getSigma(v, other);
        }
        return sigmas;
    }

    private long getSigma(int v, Set<Integer> other) {
        Set<Integer> vSet = new HashSet<>();
        vSet.add(v);
        return env.getCut(vSet, other);
    }

    private void updateVPrime(Set<Integer> vPrime, Set<Integer> x, Set<Integer> y) {
        vPrime.removeAll(x);
        vPrime.removeAll(y);
    }

    private Set<Integer> initializeVPrime() {
        Set<Integer> vPrime = new HashSet<>();
        for(int i = 0; i < env.getnNodes(); i++) vPrime.add(i);
        return vPrime;
    }

    private Edge getRandomEdge(ArrayList<Edge> rcl) {
        return rcl.get((new Random()).nextInt(rcl.size()));
    }
}


class Solution{
    public Set<Integer> s, sPrime;
    public Long cut;
    public Long nIter;
    public Solution(Set<Integer> s, Set<Integer> sPrime) {
        this.s = s;
        this.sPrime = sPrime;
        this.nIter = Long.valueOf(0);
    }

    public Solution(Set<Integer> s, Set<Integer> sPrime, Long cut) {
        this.s = s;
        this.sPrime = sPrime;
        this.cut = cut;
    }
}