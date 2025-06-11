package com.minmax;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.PriorityQueue;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongIntHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.set.hash.TLongHashSet;

public class Solver {
    // public static Comparator<Pair> pairComparator = (pair1, pair2) -> {
    //     if (pair1.getPriority() == pair2.getPriority()) {
    //         // return pair1.getItem().compareTo(pair2.getItem());
    //         return Long.compare(pair1.getItem(), pair2.getItem());
    //     }
    //     else{
    //         return Integer.compare(pair1.getPriority(), pair2.getPriority());
    //     }
    // };


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
    
        PriorityQueue<Node> openSet = new PriorityQueue<>(nodeComparator);
        TLongObjectMap<Node> allNodes = new TLongObjectHashMap<>();

        // openSet.add(new Node(start, 0, manhattanDistance(start), null));
        Node startNode = new Node(start, 0, manhattanDistance(start), null);
        openSet.add(startNode);
        allNodes.put(start, startNode);
    
        // HashMap<Long, Long> cameFrom = new HashMap<>();
        // HashMap<Long, Integer> gScore = new HashMap<>();
        // TLongIntHashMap gScore = new TLongIntHashMap();
        // gScore.put(start, 0);
    
        // HashMap<Long, Integer> fScore = new HashMap<>();
        // fScore.put(start, manhattanDistance(start));
    
        // HashSet<Long> closedSet = new HashSet<>();
        // TLongHashSet closedSet = new TLongHashSet();

    
        int expanded = 0;
    
        while (!openSet.isEmpty()) {
            // Pair found = openSet.poll();
            // long current = found.getItem();
            Node current = openSet.poll();
            long state = current.getState();

            if (current.isExpanded()) {
                continue;
            }
            current.setExpanded();
    
            if (state == goal) {

                System.out.println("Goal reached in " + expanded + " expansions.");
                return reconstructPath(current);
            }
    
            expanded++;
            if (expanded % 10000 == 0) {
                System.out.println("Expanded: " + expanded);
            }
    
            for (long neighbor : StateGenerator.getNeighbors(state)) {
                Node existing = allNodes.get(neighbor);
                if (existing != null && existing.isExpanded()) {
                    continue;
                }
    
                int tentativeG;
                if (current.getGScore() == Integer.MAX_VALUE) {
                    tentativeG = Integer.MAX_VALUE;
                }
                else{
                    tentativeG = current.getGScore() + 1;
                }
                
                
    
                if (existing == null || tentativeG < existing.getGScore()) {
                    // cameFrom.put(neighbor, current);
                    // gScore.put(neighbor, tentativeG);
    
                    int h = manhattanDistance(neighbor);
                    // int f = tentativeG + h;
                    // fScore.put(neighbor, f);

                    int f;
                    if (tentativeG >= Integer.MAX_VALUE - h) {
                        f = Integer.MAX_VALUE;
                    }
                    else{
                        f = tentativeG + h;
                    }

                    Node neighborNode = new Node(neighbor, tentativeG, f, current);
                    openSet.add(neighborNode);
                    allNodes.put(neighbor, neighborNode);
    
                    // openSet.add(new Pair(neighbor, f));
                }
            }
        }
    
        System.out.println("No solution found after expanding " + expanded + " states.");
        return null;
    }
    

    // as described on wikipedia
    // private static ArrayList<Long> reconstructPath(HashMap<Long, Long> cameFrom, long current){
    //     ArrayList<Long> totalPath = new ArrayList<>();
    //     totalPath.add(current);
        
    //     while (cameFrom.containsKey(current)) {
    //         current = cameFrom.get(current);
    //         totalPath.add(0, current);
    //     }

    //     return totalPath;
    // }

    private static ArrayList<Long> reconstructPath(Node node) {
        Deque<Long> path = new ArrayDeque<>();
        while (node != null) {
            path.addFirst(node.getState());
            node = node.getParent();
        }
        return new ArrayList<>(path);
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
            int tile = (int)((state >>> ((15 - i) * 4)) & 0xF);
            if (tile != 0) {
                int currRow = i / 4;
                int currCol = i % 4;
                int targetRow = (tile - 1) / 4;
                int targetCol = (tile - 1) % 4;
                distance += Math.abs(currRow - targetRow) + Math.abs(currCol - targetCol);
            }
        }

        // for (int i = 0; i < 16; i++) {
        //     for (int j = 0; j < 16; j++) {
        //         // shift by 4 times the current tile to the right, and & with 1111 to isolate the last 4 bits
        //         int tile = (int) ((state >>> (i * 4))) & 0xF;

        //         if (tile != 0) {
        //             // tiles 1, 2, 3 and 4 should be in row 0, 5, 6, 7, and 8 in row 1 etc.
        //             int targetRow = (tile - 1) / 16;
        //             int targetColumn = (tile - 1) % 16;
        //             distance += Math.abs(i - targetRow) + Math.abs(j - targetColumn);
        //         }
        //     }
        // }
        return distance;
    }
    
}
