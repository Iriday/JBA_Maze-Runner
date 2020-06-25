package maze;

import java.util.List;
import java.util.Scanner;

public class View {
    public static void main(String[] args) {
        int[] size = getMazeSizeFromConsole();
        formatAndPrintMaze(MazeGenerator.generateMaze(size[0], size[1]));
    }

    public static void formatAndPrintMaze(List<StringBuilder> maze) {
        for (StringBuilder row : maze) {
            for (char elem : row.toString().toCharArray()) {
                if (elem == ' ') {
                    System.out.print("  ");
                } else {
                    System.out.print("##"); // or change to - "\u2588\u2588"
                }
            }
            System.out.println();
        }
    }

    public static int[] getMazeSizeFromConsole() {
        var scn = new Scanner(System.in);
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
