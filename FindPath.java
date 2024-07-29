package ShortestPath;

import java.util.*;

/*
 Cara ini menggunakan algoritma Dijkstra
*/

public class FindPath {
    public static void main(String[] args) {
        String[][] graph = readGraphFromInput();
        List<String> result = shortestPathWithInstructions(graph);
        if (result != null) {
            for (String line : result) {
                System.out.println(line);
            }
        } else {
            System.out.println("No path found.");
        }
    }

    private static String[][] readGraphFromInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            ArrayList<String> rows = new ArrayList<>();

            System.out.println("Masukkan graph (ketik 'OK' pada baris baru untuk selesai):");

            while (true) {
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("OK")) {
                    break;
                }
                rows.add(line);
            }

            if (rows.isEmpty()) {
                return null;
            }

            int rowCount = rows.size();
            int colCount = rows.get(0).length();
            String[][] graph = new String[rowCount][colCount];

            for (int i = 0; i < rowCount; i++) {
                String row = rows.get(i);
                if (row.length() != colCount) {
                    System.out.println("Error: Semua baris harus memiliki panjang yang sama");
                    return null;
                }
                graph[i] = row.split("");
            }

            return graph;
        }
    }

    static class Node implements Comparable<Node> {
        int x, y, dist;

        Node(int x, int y, int dist) {
            this.x = x;
            this.y = y;
            this.dist = dist;
        }

        public int compareTo(Node other) {
            return Integer.compare(dist, other.dist);
        }
    }

    static int[] dx = { -1, 0, 1, 0 };
    static int[] dy = { 0, 1, 0, -1 }; // Directions: up, right, down, left

    public static List<String> shortestPathWithInstructions(String[][] graph) {
        int rows = graph.length, cols = graph[0].length;
        int[][] distance = new int[rows][cols];
        boolean[][] visited = new boolean[rows][cols];
        Node[][] parent = new Node[rows][cols];
        for (int[] row : distance)
            Arrays.fill(row, Integer.MAX_VALUE);
    
        PriorityQueue<Node> pq = new PriorityQueue<>();
    
        // Find start and end nodes
        Node start = null, end = null;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (graph[i][j].equals("^"))
                    start = new Node(i, j, 0);
                if (graph[i][j].equals("*"))
                    end = new Node(i, j, 0);
            }
        }
        distance[start.x][start.y] = 0;
        pq.offer(start);
    
        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            if (visited[curr.x][curr.y])
                continue;
            visited[curr.x][curr.y] = true;
    
            if (curr.x == end.x && curr.y == end.y) {
                // Reconstruct and format the path
                return buildPathInstructions(graph, parent, start, end);
            }
    
            for (int i = 0; i < 4; i++) {
                int nx = curr.x + dx[i], ny = curr.y + dy[i];
                if (nx < 0 || nx >= rows || ny < 0 || ny >= cols || graph[nx][ny].equals("#"))
                    continue;
    
                int newDist = curr.dist + 1;
                if (newDist < distance[nx][ny]) {
                    distance[nx][ny] = newDist;
                    parent[nx][ny] = curr;
                    pq.offer(new Node(nx, ny, newDist));
                }
            }
        }
        return null; // End unreachable
    }

    private static List<String> buildPathInstructions(String[][] graph, Node[][] parent, Node start, Node end) {
        List<String> instructions = new ArrayList<>();
        Node curr = end;
        int totalSteps = 0;
        
        // Create a copy of the graph to draw the path
        String[][] pathGraph = new String[graph.length][];
        for (int i = 0; i < graph.length; i++) {
            pathGraph[i] = Arrays.copyOf(graph[i], graph[i].length);
        }
    
        while (curr != start) {
            Node prev = parent[curr.x][curr.y];
            int steps = 0;
            String direction = "";
            if (curr.x < prev.x) {
                steps = prev.x - curr.x;
                direction = "atas";
                for (int i = curr.x; i < prev.x; i++) pathGraph[i][curr.y] = "X";
            } else if (curr.x > prev.x) {
                steps = curr.x - prev.x;
                direction = "bawah";
                for (int i = prev.x + 1; i <= curr.x; i++) pathGraph[i][curr.y] = "X";
            } else if (curr.y < prev.y) {
                steps = prev.y - curr.y;
                direction = "kiri";
                for (int j = curr.y; j < prev.y; j++) pathGraph[curr.x][j] = "X";
            } else if (curr.y > prev.y) {
                steps = curr.y - prev.y;
                direction = "kanan";
                for (int j = prev.y + 1; j <= curr.y; j++) pathGraph[curr.x][j] = "X";
            }
            instructions.add(0, steps + " " + direction);
            totalSteps += steps;
            curr = prev;
        }
    
        instructions.add("Total: " + totalSteps + " langkah");
        
        // Add the graph with the path drawn to the instructions
        instructions.add("\nPath in graph:");
        for (String[] row : pathGraph) {
            instructions.add(String.join("", row));
        }
    
        return instructions;
    }
}
