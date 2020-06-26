package maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class View {
    private List<StringBuilder> lastMaze;

    public void run() {
        var scn = new Scanner(System.in);
        initialMainMenu(scn);
        mainMenu(scn);
    }

    private void initialMainMenu(Scanner scn) {
        while (true) {
            System.out.println("=== Menu ===\n"
                    + "1. Generate a new maze\n"
                    + "2. Load a maze\n"
                    + "0. Exit");

            switch (scn.next().trim()) {
                case "1" -> {
                    lastMaze = generateNewMaze(scn);
                    printMaze(lastMaze);
                    System.out.println();
                    return;
                }
                case "2" -> System.out.println("Not implemented yet, coming soon.");
                case "0" -> System.exit(0);
                default -> System.out.println("Incorrect option, please try again.");
            }
            System.out.println();
        }
    }

    private void mainMenu(Scanner scn) {
        while (true) {
            System.out.println("== Menu ==\n"
                    + "1. Generate a new maze\n"
                    + "2. Load a maze\n"
                    + "3. Save the maze\n"
                    + "4. Display the maze\n"
                    + "0. Exit");

            switch (scn.next().trim()) {
                case "1" -> {
                    lastMaze = generateNewMaze(scn);
                    printMaze(lastMaze);
                }
                case "2", "3" -> System.out.println("Not implemented yet, coming soon.");
                case "4" -> printMaze(lastMaze);
                case "0" -> {
                    return;
                }
                default -> System.out.println("Incorrect option, please try again.");
            }
            System.out.println();
        }
    }

    public static List<StringBuilder> generateNewMaze(Scanner scn) {
        int[] size = getMazeSizeFromConsole(scn);
        return formatMaze(MazeGenerator.generateMaze(size[0], size[1]));
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
