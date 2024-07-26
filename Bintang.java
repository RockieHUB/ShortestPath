package ShortestPath;

import java.util.*;

/*
 Cara ini menggunakan algoritma A Star
*/

public class Bintang {
    static class Node {
        int x, y;
        int g, h, f;
        Node parent;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int calculateH(Node goal) { // Manhattan distance dalam bentuk heuristic
            return Math.abs(x - goal.x) + Math.abs(y - goal.y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Node node = (Node) o;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static int[] dx = { -1, 0, 1, 0 };
    static int[] dy = { 0, 1, 0, -1 };

    public static List<Node> findShortestPath(String[][] graph) {
        int rows = graph.length;
        int cols = graph[0].length;
        Node start = findStartNode(graph);
        Node goal = findGoalNode(graph);
    
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> node.f));
        Set<Node> closedSet = new HashSet<>();
        Map<Node, Node> cameFrom = new HashMap<>();
        Map<Node, Integer> gScore = new HashMap<>();
        Map<Node, Integer> fScore = new HashMap<>();
    
        gScore.put(start, 0);
        fScore.put(start, start.calculateH(goal));
        openSet.offer(start);
    
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
    
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }
    
            closedSet.add(current);
    
            for (int i = 0; i < 4; i++) {
                int nx = current.x + dx[i];
                int ny = current.y + dy[i];
    
                if (isValid(nx, ny, rows, cols) && !graph[nx][ny].equals("#")) {
                    Node neighbor = new Node(nx, ny);
                    if (closedSet.contains(neighbor)) {
                        continue;
                    }
    
                    int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1;
    
                    if (!openSet.contains(neighbor)) {
                        openSet.offer(neighbor);
                    } else if (tentativeGScore >= gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                        continue;
                    }
    
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, gScore.get(neighbor) + neighbor.calculateH(goal));
    
                    // Perbarui tetangga di dalam openset
                    openSet.remove(neighbor);
                    openSet.offer(neighbor);
                }
            }
        }
    
        return null;// Jalan Buntu
    }

    private static List<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {
        List<Node> path = new ArrayList<>();
        path.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(0, current);
        }
        return path;
    }

    private static Node findStartNode(String[][] graph) {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                if (graph[i][j].equals("^")) {
                    return new Node(i, j);
                }
            }
        }
        throw new IllegalArgumentException("Start node tidak ditemukan");
    }

    private static Node findGoalNode(String[][] graph) {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                if (graph[i][j].equals("*")) {
                    return new Node(i, j);
                }
            }
        }
        throw new IllegalArgumentException("Finish node tidak ditemukan");
    }

    private static boolean isValid(int x, int y, int rows, int cols) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    private static void printPath(String[][] graph, List<Node> path) {
        String[][] copy = new String[graph.length][graph[0].length];
        for (int i = 0; i < graph.length; i++) {
            copy[i] = Arrays.copyOf(graph[i], graph[i].length);
        }

        for (Node node : path) {
            if (!copy[node.x][node.y].equals("^") && !copy[node.x][node.y].equals("*")) {
                copy[node.x][node.y] = "o";
            }
        }

        for (String[] row : copy) {
            System.out.println(String.join("", row));
        }

        // Hitung langkah dan Print
        List<String> movements = calculateMovements(path);
        int totalSteps = 0;
        System.out.println("");
        for (String movement : movements) {
            System.out.println(movement);
            totalSteps += Integer.parseInt(movement.split(" ")[0]);
        }
        System.out.println(totalSteps + " langkah");
    }

    private static List<String> calculateMovements(List<Node> path) {
        List<String> movements = new ArrayList<>();
        String currentDirection = "";
        int count = 0;

        for (int i = 1; i < path.size(); i++) {
            Node current = path.get(i);
            Node previous = path.get(i - 1);

            String direction;
            if (current.x < previous.x) {
                direction = "atas";
            } else if (current.x > previous.x) {
                direction = "bawah";
            } else if (current.y < previous.y) {
                direction = "kiri";
            } else if (current.y > previous.y) {
                direction = "kanan";
            } else {
                continue; // Tidak ada gerakan
            }

            if (direction.equals(currentDirection)) {
                count++;
            } else {
                if (!currentDirection.isEmpty()) {
                    movements.add(count + " " + currentDirection);
                }
                currentDirection = direction;
                count = 1;
            }
        }

        // Tambah gerakan terakhir
        if (count > 0) {
            movements.add(count + " " + currentDirection);
        }

        return movements;
    }

    public static void main(String[] args) {
        String graph[][] = new String[][] {
            { "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#" },
            { "#", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "*", "#" },
            { "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", " ", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#" },
            { "#", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", " ", " ", "#" },
            { "#", " ", "#", "#", "#", "#", "#", "#", "#", " ", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#" },
            { "#", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "#" },
            { "#", " ", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#" },
            { "#", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "^", " ", " ", " ", "#" },
            { "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#" } };

        // String graph[][] = new String[][] {
        //     { "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#" },
        //     { "#", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "*", "#" },
        //     { "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", " ", "#", "#", "#", "#", "#", "#", "#", "#", " ", "#" },
        //     { "#", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", " ", " ", "#" },
        //     { "#", " ", "#", "#", "#", "#", "#", "#", "#", " ", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", " ", "#" },
        //     { "#", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "#" },
        //     { "#", " ", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", " ", "#" },
        //     { "#", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "^", " ", " ", " ", "#" },
        //     { "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#" } };

        // String graph[][] = new String[][] {{ "#", "*", "#", "^", "#" }};

        List<Node> path = findShortestPath(graph);
        if (path != null) {
            printPath(graph, path);
        } else {
            System.out.println("tidak ada jalan");
        }
    }
}
