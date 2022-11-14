package com.example.ihorko.sapr.finder.data;

public class Element {

    private char value;
    private Point point;

    public char getValue() {
        return value;
    }

    public Element(char value, Point point) {
        this.value = value;
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return "Елемент{" +
                "" + value +
                '}';
    }
}
