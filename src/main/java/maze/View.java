package maze;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class View {
    private List<StringBuilder> lastMaze;

    public void run() {
        var scn = new Scanner(System.in);
        mainMenu(scn);
    }

    private void mainMenu(Scanner scn) {
        boolean mazeInitialized = false;
        while (true) {
            System.out.println("== Menu ==\n"
                    + "1. Generate a new maze\n"
                    + "2. Load a maze");
            if (mazeInitialized) {
                System.out.println("3. Save the maze\n"
                        + "4. Display the maze");
            }
            System.out.println("0. Exit");

            String option = scn.next().trim();
            if (!mazeInitialized && !(option.equals("1") || option.equals("2") || option.equals("0"))) {
                System.out.println("Incorrect option, please try again.\n");
                continue;
            }
            switch (option) {
                case "1" -> {
                    lastMaze = generateNewMaze(scn);
                    printMaze(lastMaze);
                    mazeInitialized = true;
                }
                case "2" -> {
                    try {
                        lastMaze = loadMazeFromFile(getFilePathFromConsole(scn));
                        mazeInitialized = true;
                    } catch (IOException e) {
                        System.out.println("Error, something went wrong!");
                    }
                }
                case "3" -> {
                    try {
                        saveMazeToFile(lastMaze, getFilePathFromConsole(scn));
                    } catch (IOException e) {
                        System.out.println("Error, something went wrong!");
                    }
                }
                case "4" -> printMaze(lastMaze);
                case "0" -> {
                    return;
                }
                default -> System.out.println("Incorrect option, please try again.");
            }
            System.out.println();
        }
    }

    private List<StringBuilder> generateNewMaze(Scanner scn) {
        int[] size = getMazeSizeFromConsole(scn);
        return formatMaze(MazeGenerator.generateMaze(size[0], size[1]));
    }

    public static void saveMazeToFile(List<StringBuilder> maze, String filePath) throws IOException {
        var mazeStr = new StringBuilder();
        maze.forEach(row -> mazeStr.append(row).append("\n"));
        Files.writeString(Path.of(filePath), mazeStr.toString().trim());
    }

    public static List<StringBuilder> loadMazeFromFile(String path) throws IOException {
        return Files.readAllLines(Path.of(path))
                .stream()
                .map(StringBuilder::new)
                .collect(Collectors.toList());
    }

    public static String getFilePathFromConsole(Scanner scn) {
        System.out.println("Enter filepath:");
        return scn.next().trim();
    }

    public static List<StringBuilder> formatMaze(List<StringBuilder> maze) {
        List<StringBuilder> formattedMaze = new ArrayList<>(maze.size());
        for (StringBuilder row : maze) {
            var sbRow = new StringBuilder();
            for (char elem : row.toString().toCharArray()) {
                if (elem == ' ') {
                    sbRow.append("  ");
                } else {
                    sbRow.append("##"); // or change to - "\u2588\u2588"
                }
            }
            formattedMaze.add(sbRow);
        }
        return formattedMaze;
    }

    public static void printMaze(List<StringBuilder> maze) {
        maze.forEach(System.out::println);
    }

    public static int[] getMazeSizeFromConsole(Scanner scn) {
        System.out.println("Please, enter the size of a maze:");
        while (true) {
            int rows = scn.nextInt();
            int cols = scn.nextInt();
            if (rows < 3 || cols < 3) {
                System.out.println("Incorrect input, maze size should be at least 3x3, please try again.");
            } else {
                return new int[]{rows, cols};
            }
        }
    }
}
