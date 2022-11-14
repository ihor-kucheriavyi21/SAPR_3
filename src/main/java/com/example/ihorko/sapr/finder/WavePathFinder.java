package com.example.ihorko.sapr.finder;

import com.example.ihorko.sapr.finder.data.Connection;
import com.example.ihorko.sapr.finder.data.Element;
import com.example.ihorko.sapr.finder.data.Point;
import com.example.ihorko.sapr.finder.data.Result;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class WavePathFinder  extends BaseFinder{

    public void printResults(List<Connection> connections, char[][] currentMap, char[][] startMap){
        int currentIndex = 0;
        while (!connections.isEmpty()) {
            if (currentIndex == connections.size()) {
                currentMap = Arrays.stream(startMap).map(char[]::clone).toArray(char[][]::new);
                currentIndex = 0;
                countStage = 1;
                System.out.println("===============================================================");
            }
            Connection connection = connections.get(currentIndex);
            char[][] tempResult = findRoute(currentMap, connection.getElementFrom(), connection.getElementTo());
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

            printArray(tempResult);

        }
    }

    public char[][] findRoute(char[][] map, Element startElement, Element endElement) {
        int K = map.length;
        int L = map[0].length;
        char[][] doneMap;

        Point[] Points = getStartEndPoints(map, K, L, startElement, endElement);
        Point startPoint = Points[0];
        Point endPoint = Points[1];

        int[][] intMap = convertMapToInt(map, K, L, endElement);
        Result result = wavePropagation(intMap, startPoint, endPoint, K, L);
        if (!result.isReached()) {
            doneMap = null;
        } else {
            int[][] recoverPathMap = pathRecovery(result.getField(), startPoint, endPoint, result.getCounter(), K, L);
            doneMap = clearField(recoverPathMap, startPoint, endPoint, K, L, startElement, endElement, map);
            countStage++;
            countConductors+= result.getCounter()-1;
        }
        return doneMap;
    }


    private int[][] convertMapToInt(char[][] map, int K, int L, Element endElement) {
        int[][] intMap = new int[K][L];

        for (int i = 0; i < K; i++) {
            for (int j = 0; j < L; j++) {
                if (map[i][j] == endElement.getValue()) {
                    intMap[i][j] = -10;
                } else if (map[i][j] >= '1') {
                    intMap[i][j] = -1;
                }
            }
        }

        return intMap;
    }


    private Point[] getStartEndPoints(char[][] map, int K, int L, Element startElement, Element endElement) {
        Point startPoint = new Point(startElement.getPoint().y, startElement.getPoint().x);
        Point endPoint = new Point(endElement.getPoint().y, endElement.getPoint().x);

        Point[] both = {startPoint, endPoint};

        return both;
    }


    private Result wavePropagation(int[][] map, Point startPoint, Point endPoint, int K, int L) {
        HashSet<Point> seenPoints = new HashSet<>();
        ArrayDeque<Point> pointsQueue = new ArrayDeque<>();
        Result result = null;

        int allPoints = K * L;
        int counter = 0;

        seenPoints.add(startPoint);
        pointsQueue.addLast(startPoint);

        outer:
        while (allPoints > 0) {
            if ((pointsQueue.size() == 0) && map[endPoint.x][endPoint.y] == -10) {
                result = new Result(false, map, counter);
                break;
            }

            // маркировка одноуровневых клеток
            for (Point point : pointsQueue) {
                if (point.equals(startPoint)) {
                    map[point.x][point.y] = counter;
                } else if (map[point.x][point.y] == 0) {
                    map[point.x][point.y] = counter;
                } else if (map[point.x][point.y] == -1) {
                    ;
                } else if (point.equals(endPoint)) {
                    map[point.x][point.y] = counter;
                    result = new Result(true, map, counter);
                    break outer;
                }
            }

            counter++;

            // поиск соседних клеток
            int lenQueue = pointsQueue.size();
            while (lenQueue > 0) {
                Point thisPoint = pointsQueue.pollFirst();
                Point[] neighborPoints = getNeighbors(thisPoint, K, L);

                for (Point point : neighborPoints) {
                    if (point == null) {
                        ;
                    } else if (map[point.x][point.y] == -1) {
                        ;
                    } else {
                        if (!seenPoints.contains(point)) {
                            seenPoints.add(point);
                            pointsQueue.addLast(point);
                        }
                    }
                }
                lenQueue--;
            }

            allPoints--;
        }

        return result;
    }


    private int[][] pathRecovery(int[][] map, Point startPoint, Point endPoint, int counter, int K, int L) {
        ArrayDeque<Point> nextPointsQueue = new ArrayDeque<>();
        nextPointsQueue.addLast(endPoint);

        while (counter > 0) {
            Point[] neighborPoints = getNeighbors(nextPointsQueue.pollFirst(), K, L);
            for (Point point : neighborPoints) {
                if (point == null) {
                    ;
                } else if (map[point.x][point.y] == (counter - 1)) {
                    map[point.x][point.y] = -2;
                    nextPointsQueue.addLast(point);
                    break;
                }
            }
            counter--;
        }

        return map;
    }


    private char[][] clearField(int[][] map, Point startPoint, Point endPoint, int K, int L, Element startElement, Element endElement, char[][] startMap) {
        char[][] doneMap = new char[K][L];

        for (int i = 0; i < K; i++) {
            for (int j = 0; j < L; j++) {
                if ((i == startPoint.x) && (j == startPoint.y)) {
                    doneMap[i][j] = startElement.getValue();
                    map[i][j] = -1;
                } else if ((i == endPoint.x) && (j == endPoint.y)) {
                    doneMap[i][j] = endElement.getValue();
                    map[i][j] = -1;
                } else if (map[i][j] == -2) {
                    doneMap[i][j] = (char) (countStage + '0');
                } else if (map[i][j] == -1) {
                    doneMap[i][j] = startMap[i][j];
                }
            }
        }
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < L; j++) {
                if (map[i][j] >= 0 && map[i][j] <= '1') {
                    doneMap[i][j] = '#';
                }
            }
        }

        return doneMap;
    }


    private Point[] getNeighbors(Point point, int K, int L) {
        Point[] neighbors = new Point[4];
        neighbors[0] = point.x - 1 < 0 ? null : point.getUpperPoint();
        neighbors[1] = point.x + 1 > K - 1 ? null : point.getLowerPoint();
        neighbors[2] = point.y - 1 < 0 ? null : point.getLeftPoint();
        neighbors[3] = point.y + 1 > L - 1 ? null : point.getRightPoint();

        return neighbors;
    }

    public void printArray(char[][] mapElements) {
        for (char[] arr : mapElements)
            System.out.println(Arrays.toString(arr));
    }
}
