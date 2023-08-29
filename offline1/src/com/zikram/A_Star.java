package com.zikram;
import java.util.*;
class Node implements Comparable<Node>{
    public Environment env;
    public int g, h, f;

    public Node(Environment env, int g, boolean isManhattan) {
        this.env = env;
        this.g = g;
        if(isManhattan) this.h = env.manhattanDistance();
        else this.h = env.hammingDistance();
        this.f = g + h;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.f, o.f);
    }
}
public class A_Star {
    private Environment env;
    private PriorityQueue<Node> queue;
    private Environment goal;
    private List<Environment> results;
    Map<Environment, Environment> childToParent;
    boolean isManhattan;
    private int nExploredNodes = 0;

    public A_Star(Environment env, boolean isManhattan) {
        this.env = env;
        this.isManhattan = isManhattan;
        queue = new PriorityQueue<>();
        queue.add(new Node(env, 0, isManhattan));
        childToParent = new HashMap<>();
        results = new ArrayList<>();
    }


    public void solve() throws InterruptedException {
        if(!env.isSolvable()){
            System.out.println("Unsolvable!");
            return;
        }
        Set<Node> closedSet = new HashSet<>();

        while(!queue.isEmpty()){

            Node currentNode = queue.poll();
            Environment current = currentNode.env;
            if(current.hammingDistance() == 0) {goal = current; break;}
            closedSet.add(currentNode);
            for(Environment neighbor : current.getActions()){
                Node neighborNode = new Node(neighbor, currentNode.g + 1, isManhattan);
                if(closedSet.contains(neighbor)) continue;
                nExploredNodes++;
                queue.add(neighborNode);
                childToParent.put(neighbor, current);
            }
        }
        buildResult();
    }

    private void buildResult(){
        for(Environment cur = goal; childToParent.containsKey(cur); cur = childToParent.get(cur)){
            results.add(cur);
        }
        results.add(env);
        Collections.reverse(results);
    }

    public void showResult(){
        System.out.println("Explored Nodes : " + nExploredNodes);
        if(results.size() > 0) System.out.println("Total Steps : " + (results.size() - 1));
        for(Environment cur : results) {
            cur.printGrid();
            System.out.println();
        }
    }


}
