package com.example.ihorko.sapr.finder.data;

import java.util.Objects;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point getUpperPoint() {
        return new Point(x-1, y);
    }

    public Point getLowerPoint() {
        return new Point(x+1, y);
    }

    public Point getLeftPoint() {
        return new Point(x, y-1);
    }

    public Point getRightPoint() {
        return new Point(x, y+1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
