package maze;

import java.util.List;

public class View {
    public static void main(String[] args) {
        formatAndPrintMaze(MazeGenerator.generateMaze(5, 15));
    }

    public static void formatAndPrintMaze(List<StringBuilder> maze) {
        for (StringBuilder row : maze) {
            for (char elem : row.toString().toCharArray()) {
                if (elem == ' ') {
                    System.out.print("  ");
                } else {
                    System.out.print("##");
                }
            }
            System.out.println();
        }
    }
}
