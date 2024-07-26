package ShortestPath;

public class FindPath {
    public static void main(String[] args) {
        String graphCoba[][] = new String[][] {
            { "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#" },
            { "#", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", " ", " ", " ", " ", "*", "#" },
            { "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", " ", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#" },
            { "#", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", "#", "#", " ", " ", " ", " ", " ", " ", " ", "#" },
            { "#", "#", "#", "#", "#", "#", "#", "#", "#", " ", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#" },
            { "#", " ", " ", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "#" },
            { "#", " ", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#" },
            { "#", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "^", " ", " ", " ", "#" },
            { "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#" } };
        caraBlind(graphCoba);
    }

    // Untuk masalah ini, kita bisa cek untuk setiap obstacle disekitar index
    public static void caraBlind(String[][] graph) {
        // Cari Index dari ^
        Integer rowIndex = -999;
        Integer colIndex = -999;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j].equals("^")) {
                    rowIndex = i;
                    colIndex = j;
                    break;
                }
            }
        }
        boolean flagBuntu = false;
        int total = 0;
        // while (flagBuntu == false && !graph[rowIndex][colIndex].equals("*")) {
            // Cari kondisi gerakan ke kanan kiri atas bawah
            System.out.println(cekSekitar(graph, rowIndex, colIndex, total));
        // }
    }

    public static int cekSekitar(String[][] graph, int rowIndex, int colIndex, int tanda) {
        int counter = 0;
        // Kanan
        int i = colIndex;
        do {
            // cek atas bawah
            if (!graph[rowIndex - 1][i].equals("#") || !graph[rowIndex + 1][i].equals("#")) {
                counter += 1;
            }
            i+=1;
        } while (!graph[rowIndex][i].equals("#"));
        // Kiri
        i = colIndex;
        do {
            // cek atas bawah
            if (!graph[rowIndex - 1][i].equals("#") || !graph[rowIndex + 1][i].equals("#")) {
                counter += 1;
            }
            i-=1;
        } while (!graph[rowIndex][i].equals("#"));
        // Atas
        int j = rowIndex;
        do {
            // cek kiri kanan
            if (!graph[j][colIndex - 1].equals("#") || !graph[j][colIndex + 1].equals("#")) {
                counter += 1;
            }
            j-=1;
        } while (!graph[j][colIndex].equals("#"));
        // Bawah
        j = rowIndex;
        do {
            // cek kiri kanan
            if (!graph[j][colIndex - 1].equals("#") || !graph[j][colIndex + 1].equals("#")) {
                counter += 1;
            }
            j+=1;
        } while (!graph[j][colIndex].equals("#"));

        return counter;
    }
}
