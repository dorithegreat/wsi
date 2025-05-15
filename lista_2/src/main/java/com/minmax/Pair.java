package com.minmax;

// used for priority queue entry
public class Pair {
    private final long item;
    private final int priority;


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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        return ((Pair) obj).item == this.item;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(item);
    }
}
