package com.minmax;

public class Node {
    private long state;
    private int gScore;
    private int fScore;

    public Node(long state, int gScore, int fScore){
        this.state = state;
        this.gScore = gScore = Integer.MAX_VALUE;
        this.fScore = fScore;
    }

    public int getFScore(){
        return fScore;
    }

    public void setFScore(int fscore){
        this.fScore = fscore;
    }

    public int getGScore(){
        return gScore;
    }

    public void setGScore(int gScore){
        this.gScore = gScore;
    }

    public long getState(){
        return state;
    }
}
