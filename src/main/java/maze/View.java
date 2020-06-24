package maze;

public class View {
    public static void main(String[] args) {
        MazeGenerator.generateMaze(5, 15).forEach(System.out::println);
    }
}
