package com.minmax;

public class Node {
    private final long state;
    private  int gScore = Integer.MAX_VALUE;
    private final int fScore;
    private final Node parent;
    private boolean expanded = false;

    public Node(long state, int gScore, int fScore, Node parent){
        this.state = state;
        this.gScore = gScore;
        this.fScore = fScore;
        this.parent = parent;
    }

    public int getFScore(){
        return fScore;
    }

    // public void setFScore(int fscore){
    //     this.fScore = fscore;
    // }

    public int getGScore(){
        return gScore;
    }

    public void setGScore(int gScore){
        this.gScore = gScore;
    }

    public long getState(){
        return state;
    }

    public Node getParent(){
        return parent;
    }

    public boolean isExpanded(){
        return expanded;
    }

    public void setExpanded(){
        expanded = true;
    }
}
