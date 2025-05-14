package com.minmax;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

    public ArrayList<Long> solve(long start){

        // String goal = "12345678x";
        long goal = 0;

        TreeSet<Pair> openSet = new TreeSet<>(pairComparator);
        openSet.add(new Pair(start, heuristic(start)));


        HashMap<Long, Long> cameFrom = new HashMap<>();

        HashMap<Long, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        HashMap<Long, Integer> fScore = new HashMap<>();
        fScore.put(start, heuristic(start));

        while (! openSet.isEmpty()) {
            Pair found = openSet.first();
            Long current = found.getItem();
            if (current == goal) {
                return reconstructPath(cameFrom, current);
            }

            openSet.remove(found);
            for (long neighbor : StateGenerator.getNeighbors(current)) {
                int tentativeGScore = gScore.get(current) + 1; // TODO:  replace the 1 with the distane between current and neighbor
                if (tentativeGScore < gScore.get(neighbor)) {
                    int heuristic = heuristic(neighbor);
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic);

                    Pair pair = new Pair(neighbor, fScore.get(neighbor));
                    if (! openSet.contains(pair)) {
                        openSet.add(pair);
                    }
                }
            }
        }

        return null;
    }

    // as described on wikipedia
    private ArrayList<Long> reconstructPath(HashMap<Long, Long> cameFrom, long current){
        ArrayList<Long> totalPath = new ArrayList<>();
        totalPath.add(current);
        

        return totalPath;
    }

    private int heuristic(long state){
        // TODO: implement a heuristic
        return 1;
    }
}
