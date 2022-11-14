package com.example.ihorko.sapr.finder.data;

public class Connection {
    private Element elementFrom;
    private Element elementTo;
    private int countConnections;

    public Connection(Element elementFrom, Element elementTo, int countConnections) {
        this.elementFrom = elementFrom;
        this.elementTo = elementTo;
        this.countConnections = countConnections;
    }

    public Element getElementFrom() {
        return elementFrom;
    }

    public Element getElementTo() {
        return elementTo;
    }

    public int getCountConnections() {
        return countConnections;
    }

    public void setCountConnections(int countConnections) {
        this.countConnections = countConnections;
    }
}
