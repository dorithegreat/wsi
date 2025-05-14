package com.minmax;

// used for priority queue entry
public class Pair {
    private long item;
    private int priority;

    public Pair(){

    }

    public Pair(long item, int priority){
        this.item = item;
        this.priority = priority;
    }

    public long getItem(){
        return item;
    }

    public int getPriority(){
        return priority;
    }
}
