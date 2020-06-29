package maze;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MazeSolver {
    private boolean exitFound = false;

    public List<StringBuilder> findPathFromEntranceToExit(List<StringBuilder> maze) {
        List<StringBuilder> mazeCopy = maze.stream().map(StringBuilder::new).collect(Collectors.toList());

        int[] entranceCoords = findEntranceCoords(mazeCopy);
        int[] exitCoords = findExitCoords(mazeCopy);

        exitFound = false;
        findPathFromEntranceToExitRecursively(mazeCopy, entranceCoords[0], entranceCoords[1], exitCoords[0], exitCoords[1], new ArrayList<>());

        return mazeCopy;
    }

    private int[] findEntranceCoords(List<StringBuilder> maze) {
        for (int i = 0; i < maze.size(); i++) {
            if (maze.get(i).charAt(0) == ' ') {
                return new int[]{i, 0};
            }
        }
        return null;
    }

    private int[] findExitCoords(List<StringBuilder> maze) {
        for (int i = 0; i < maze.size(); i++) {
            if (maze.get(i).charAt(maze.get(i).length() - 1) == ' ') {
                return new int[]{i, maze.get(i).length() - 1};
            }
        }
        return null;
    }

    private void findPathFromEntranceToExitRecursively(List<StringBuilder> maze, int iCurrentCoords, int jCurrentCoords, int iExitCoords, int jExitCoords, List<Tuple<Integer, Integer>> tempCoords) {
        Tuple<Integer, Integer> currentCoords = new Tuple<>(iCurrentCoords, jCurrentCoords);

        if (tempCoords.contains(currentCoords)) {
            return;
        }
        tempCoords.add(currentCoords);

        if (iCurrentCoords == iExitCoords && jCurrentCoords == jExitCoords) {
            exitFound = true;
        }

        // top
        if (!exitFound && iCurrentCoords - 1 != -1 && maze.get(iCurrentCoords - 1).charAt(jCurrentCoords) == ' ') {
            findPathFromEntranceToExitRecursively(maze, iCurrentCoords - 1, jCurrentCoords, iExitCoords, jExitCoords, tempCoords);
        }
        // right
        if (!exitFound && jCurrentCoords + 1 != maze.get(0).length() && maze.get(iCurrentCoords).charAt(jCurrentCoords + 1) == ' ') {
            findPathFromEntranceToExitRecursively(maze, iCurrentCoords, jCurrentCoords + 1, iExitCoords, jExitCoords, tempCoords);
        }
        // bottom
        if (!exitFound && iCurrentCoords + 1 != maze.size() && maze.get(iCurrentCoords + 1).charAt(jCurrentCoords) == ' ') {
            findPathFromEntranceToExitRecursively(maze, iCurrentCoords + 1, jCurrentCoords, iExitCoords, jExitCoords, tempCoords);
        }
        // left
        if (!exitFound && jCurrentCoords - 1 != -1 && maze.get(iCurrentCoords).charAt(jCurrentCoords - 1) == ' ') {
            findPathFromEntranceToExitRecursively(maze, iCurrentCoords, jCurrentCoords - 1, iExitCoords, jExitCoords, tempCoords);
        }

        if (exitFound) {
            maze.get(iCurrentCoords).setCharAt(jCurrentCoords, '/');
        }
    }
}
