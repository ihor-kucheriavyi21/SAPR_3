package com.example.ihorko.sapr.finder;

import com.example.ihorko.sapr.finder.data.Element;

public abstract class BaseFinder {

    public int countStage = 1;

    public int countConductors = 0;
    public abstract char[][] findRoute(char[][] map, Element startElement, Element endElement);
}
