package com.example.ihorko.sapr.finder.data;

public class Result {
    private boolean reached;
    private int[][] field;
    private int counter;

    public Result(boolean reached, int[][] field, int counter) {
        this.reached = reached;
        this.field = field;
        this.counter = counter;
    }

    public boolean isReached() {
        return reached;
    }

    public int[][] getField() {
        return field;
    }

    public int getCounter() {
        return counter;
    }
}
