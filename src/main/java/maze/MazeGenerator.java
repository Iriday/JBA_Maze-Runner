package maze;

import java.util.*;
import java.util.stream.Collectors;

public class MazeGenerator {
    public static List<StringBuilder> generateMaze(int rows, int cols) {
        if (rows < 3 || cols < 3) throw new IllegalArgumentException("rows/cols should be >= 3");
        if (rows % 2 != 1 || cols % 2 != 1) throw new IllegalArgumentException("rows/cols should be odd");

        int[][] graph = numerateNodes(new int[rows / 2 + 1][cols / 2 + 1]);
        List<int[]> weights = createAdjacencyListAndConnectNodesHorizontallyAndVerticallyWithRandWeights(graph);
        makeBordersSolid(graph, weights);
        makeRandEntrance(graph, weights);

        int[] startNode = weights.get(0); // todo impl random border node chooser
        List<int[]> minSpanTree = new ArrayList<>(List.of(startNode));
        minimumSpanningTreeOf(weights, minSpanTree);
        makeRandExit(graph, minSpanTree);

        return buildMaze(graph, minSpanTree);
    }

    private static int[][] numerateNodes(int[][] graph) {
        int counter = 0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                graph[i][j] = ++counter;
            }
        }
        return graph;
    }

    /**
     * modified adjacency list. int[] contains 3 values: vertexA, weight between, vertexB.
     */
    private static List<int[]> createAdjacencyListAndConnectNodesHorizontallyAndVerticallyWithRandWeights(int[][] graph) {
        Random rand = new Random();
        int rows = graph.length;
        int cols = graph[0].length;
        List<int[]> adj = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (j + 1 != cols) {
                    int randWeight = rand.nextInt(10) + 1;
                    adj.add(new int[]{graph[i][j], randWeight, graph[i][j + 1]});
                }
                if (i + 1 != rows) {
                    int randWeight = rand.nextInt(10) + 1;
                    adj.add(new int[]{graph[i][j], randWeight, graph[i + 1][j]});
                }
            }
        }
        return adj;
    }

    /**
     * Changes values of border weights to zeros.
     */
    private static void makeBordersSolid(int[][] graph, List<int[]> adjList) {
        List<Integer> topBorder = Arrays.stream(graph[0]).boxed().collect(Collectors.toList());
        List<Integer> bottomBorder = Arrays.stream(graph[graph.length - 1]).boxed().collect(Collectors.toList());
        List<Integer> rightBorder = Arrays.stream(graph).map(row -> row[row.length - 1]).collect(Collectors.toList());
        List<Integer> leftBorder = Arrays.stream(graph).map(row -> row[0]).collect(Collectors.toList());

        for (int[] three : adjList) {
            if (topBorder.contains(three[0]) && topBorder.contains(three[2])) three[1] = 0;
            else if (rightBorder.contains(three[0]) && rightBorder.contains(three[2])) three[1] = 0;
            else if (bottomBorder.contains(three[0]) && bottomBorder.contains(three[2])) three[1] = 0;
            else if (leftBorder.contains(three[0]) && leftBorder.contains(three[2])) three[1] = 0;
        }
    }

    /**
     * Changes value of rand left border weight to 10
     */
    private static void makeRandEntrance(int[][] graph, List<int[]> adjList) {
        int randEntrance = graph[new Random().nextInt(graph.length - 1)][0];
        for (int[] three : adjList) {
            if (three[0] == randEntrance && three[2] == randEntrance + graph[0].length) {
                three[1] = 10;
                break;
            }
        }
    }

    /**
     * Changes value of rand right border weight to 10
     */
    private static void makeRandExit(int[][] graph, List<int[]> adjList) {
        int randRow = new Random().nextInt(graph.length - 1);
        removeWeightFromList(adjList, graph[randRow][graph[0].length - 1], graph[randRow + 1][graph[0].length - 1]);
    }

    // Prim's algorithm
    private static void minimumSpanningTreeOf(List<int[]> weights, List<int[]> minSpanTree) {
        List<int[]> data = weights.stream()
                .filter(w -> minSpanTree.stream()
                        .anyMatch(w2 -> w2[0] == w[0] && w2[2] != w[2] || w2[2] == w[2] && w2[0] != w[0]
                                || w2[0] == w[2] && w2[2] != w[0] || w2[2] == w[0] && w2[0] != w[2]))
                .collect(Collectors.toList());
        data.removeAll(minSpanTree);

        // prevent a loop in a graph
        data = data.stream().filter(w -> !(containsNode(minSpanTree, w[0]) && containsNode(minSpanTree, w[2])))
                .sorted((a, b) -> Integer.compare(a[1], b[1])).collect(Collectors.toList());

        if (data.size() == 0) return;

        minSpanTree.add(data.get(0));
        minimumSpanningTreeOf(weights, minSpanTree);
    }

    private static boolean containsNode(List<int[]> data, int val) {
        for (int[] three : data) {
            if (three[0] == val || three[2] == val) return true;
        }
        return false;
    }

    private static void removeWeightFromList(List<int[]> adjList, int nodeA, int nodeB) {
        for (int[] three : adjList) {
            if (three[0] == nodeA && three[2] == nodeB || three[0] == nodeB && three[2] == nodeA) {
                adjList.remove(three);
                break;
            }
        }
    }

    private static List<StringBuilder> buildMaze(int[][] graph, List<int[]> minSpanTree) {
        List<StringBuilder> mazeBuilder = new ArrayList<>();
        for (int i = 0; i < graph.length; i++) {
            var firstRow = new StringBuilder();
            var secondRow = new StringBuilder();
            for (int j = 0; j < graph[i].length; j++) {
                firstRow.append("N");

                if (j + 1 != graph[i].length) {
                    Optional<Integer> weight = getWeightBetweenNodes(minSpanTree, graph[i][j], graph[i][j + 1]);
                    firstRow.append(weight.isPresent() ? weight.orElseThrow() : " ");
                }
                if (i + 1 != graph.length) {
                    Optional<Integer> weight = getWeightBetweenNodes(minSpanTree, graph[i][j], graph[i + 1][j]);
                    secondRow.append(weight.isPresent() ? weight.orElseThrow() : " ");
                    if (j + 1 != graph[i].length)
                        secondRow.append(" ");
                }
            }
            mazeBuilder.add(firstRow);
            mazeBuilder.add(secondRow);
        }
        return mazeBuilder;
    }

    private static Optional<Integer> getWeightBetweenNodes(List<int[]> weights, int nodeA, int nodeB) {
        for (int[] three : weights) {
            if (nodeA == three[0] && nodeB == three[2] || nodeA == three[2] && nodeB == three[0]) {
                return Optional.of(three[1]);
            }
        }
        return Optional.empty();
    }
}
