package com.example.ihorko.sapr.controller;

import com.example.ihorko.sapr.finder.BaseFinder;
import com.example.ihorko.sapr.finder.RayPathFinder;
import com.example.ihorko.sapr.finder.WavePathFinder;
import com.example.ihorko.sapr.finder.data.Connection;
import com.example.ihorko.sapr.finder.data.Element;
import com.example.ihorko.sapr.finder.data.Point;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class BasePageController {

    @GetMapping("/wavePath")
    public String wavePath(Model model) {
        findPath(model, new WavePathFinder());
        return "waveAlg";
    }

    @GetMapping("/rayPath")
    public String rayPath(Model model) {
        findPath(model, new RayPathFinder());
        return "rayAlg";
    }

    private void findPath(Model model, BaseFinder finder) {
        StopWatch timer = new StopWatch();
        timer.start();

        char[][] startMap = new char[11][9];
        for (char[] row : startMap) {
            Arrays.fill(row, '#');
        }
        char[][] currentMap = new char[11][9];
        for (char[] row : currentMap) {
            Arrays.fill(row, '#');
        }

        Element elementA = new Element('A', new Point(3, 4));
        Element elementB = new Element('B', new Point(5, 2));
        Element elementC = new Element('C', new Point(1, 1));
        Element elementD = new Element('D', new Point(6, 8));
        Element elementE = new Element('E', new Point(1, 9));
        Element elementF = new Element('F', new Point(7, 5));
        Element elementH = new Element('H', new Point(2, 6));

        List<Element> availableElements = new ArrayList<>(List.of(elementA, elementB, elementC, elementD, elementE, elementF, elementH));

        List<Connection> connections = new ArrayList<>(List.of(new Connection(elementA, elementC, 1),
                new Connection(elementB, elementC, 2),
                new Connection(elementB, elementE, 2),
                new Connection(elementE, elementH, 1),
                new Connection(elementE, elementD, 1),
                new Connection(elementA, elementH, 1),
                new Connection(elementH, elementF, 2),
                new Connection(elementF, elementC, 1)));
        for (Element element : availableElements) {
            startMap[element.getPoint().y][element.getPoint().x] = element.getValue();
            currentMap[element.getPoint().y][element.getPoint().x] = element.getValue();
        }

        int currentIndex = 0;
        List<char[][]> listMaps = new ArrayList<>();
        int countFlow = 1;
        while (!connections.isEmpty()) {
            if (currentIndex == connections.size()) {
                currentMap = Arrays.stream(startMap).map(char[]::clone).toArray(char[][]::new);
                currentIndex = 0;
                countFlow++;
                finder.countStage = 1;
                listMaps.add(null);
            }
            Connection connection = connections.get(currentIndex);
            char[][] tempResult = finder.findRoute(currentMap, connection.getElementFrom(), connection.getElementTo());
            if (tempResult == null) {
                currentIndex++;
                continue;
            }
            if (connection.getCountConnections() == 1) {
                if (!Arrays.deepEquals(tempResult, currentMap)) {
                    currentMap = tempResult;
                    connections.remove(connection);
                }
            } else {
                if (!Arrays.deepEquals(tempResult, currentMap)) {
                    currentMap = tempResult;
                    connection.setCountConnections(connection.getCountConnections() - 1);
                }
            }
            listMaps.add(currentMap);
        }
        timer.stop();
        model.addAttribute("listMaps", listMaps);
        model.addAttribute("countFlow", countFlow);
        model.addAttribute("time", timer.getTotalTimeNanos());
        model.addAttribute("countConductors", finder.countConductors);

    }
}
