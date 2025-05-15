package com.minmax;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class Solver {
    public static Comparator<Pair> pairComparator = (pair1, pair2) -> {
        if (pair1.getPriority() == pair2.getPriority()) {
            // return pair1.getItem().compareTo(pair2.getItem());
            return Long.compare(pair1.getItem(), pair2.getItem());
        }
        else{
            return Integer.compare(pair1.getPriority(), pair2.getPriority());
        }
    };


    public static Comparator<Node> nodeComparator = (node1, node2) -> {
        if (node1.getFScore() == node2.getFScore()) {
            return Long.compare(node1.getState(), node2.getState());
        }
        else{
            return Integer.compare(node1.getFScore(), node2.getFScore());
        }
    };

    public static ArrayList<Long> solve(long start) {
        long goal = 0x123456789ABCDEF0L; // Goal state: tiles 1–15 in order, 0 (blank) at the end
    
        PriorityQueue<Pair> openSet = new PriorityQueue<>(pairComparator);
        openSet.add(new Pair(start, manhattanDistance(start)));
    
        HashMap<Long, Long> cameFrom = new HashMap<>();
        HashMap<Long, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);
    
        HashMap<Long, Integer> fScore = new HashMap<>();
        fScore.put(start, manhattanDistance(start));
    
        HashSet<Long> closedSet = new HashSet<>();
    
        int expanded = 0;
    
        while (!openSet.isEmpty()) {
            Pair found = openSet.poll();
            long current = found.getItem();
    
            if (closedSet.contains(current)) {
                continue;
            }
            closedSet.add(current);
    
            if (current == goal) {
                System.out.println("Goal reached in " + expanded + " expansions.");
                return reconstructPath(cameFrom, current);
            }
    
            expanded++;
            if (expanded % 10000 == 0) {
                System.out.println("Expanded: " + expanded);
            }
    
            for (long neighbor : StateGenerator.getNeighbors(current)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
    
                int tentativeG = gScore.get(current) + 1;
    
                if (!gScore.containsKey(neighbor) || tentativeG < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
    
                    int h = manhattanDistance(neighbor);
                    int f = tentativeG + h;
                    fScore.put(neighbor, f);
    
                    openSet.add(new Pair(neighbor, f));
                }
            }
        }
    
        System.out.println("No solution found after expanding " + expanded + " states.");
        return null;
    }
    

    // as described on wikipedia
    private static ArrayList<Long> reconstructPath(HashMap<Long, Long> cameFrom, long current){
        ArrayList<Long> totalPath = new ArrayList<>();
        totalPath.add(current);
        
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(0, current);
        }

        return totalPath;
    }

    // private int heuristic(long state){
    //     // TODO: implement a heuristic
    //     return 1;
    // }

    public static int misplacedTiles(long state) {
        int misplaced = 0;
        for (int i = 0; i < 16; i++) {
            long tile = (state >>> (i * 4)) & 0xF;
            if (tile != 0 && tile != i + 1) {
                misplaced++;
            }
        }
        return misplaced;
    }

    public static int manhattanDistance(long state) {
        int distance = 0;
        for (int i = 0; i < 16; i++) {
            long tile = (state >>> (i * 4)) & 0xF;
            if (tile != 0) {
                int goalIndex = (int)(tile - 1);
                int currRow = i / 4, currCol = i % 4;
                int goalRow = goalIndex / 4, goalCol = goalIndex % 4;
                distance += Math.abs(currRow - goalRow) + Math.abs(currCol - goalCol);
            }
        }
        return distance;
    }
}
