import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class rp {
    private static int nbrNodes;
    private static int nbrEdges;
    private static int nbrStudents;
    private static int nbrRoutes;
    private static int[][] capacityMatrix; // capacity for each route
    private static ArrayList<Integer[]> removeable; // routes to remove
    private static ArrayList<Integer> gointToRemove; // index of routes to remove
    private static int current; // current nbr of removed routes
    private static int oldFlow;
    private static ArrayList<Integer> currentPath; // current path used

    public void handleData() {
        Scanner scan = null;
        scan = new Scanner(System.in);
        nbrNodes = scan.nextInt();
        nbrEdges = scan.nextInt();
        nbrStudents = scan.nextInt();
        nbrRoutes = scan.nextInt();
        capacityMatrix = new int[nbrNodes][nbrNodes];
        removeable = new ArrayList<Integer[]>();
        gointToRemove = new ArrayList<Integer>();
        for (int i = 0; i < nbrEdges; i++) {
            int edge1 = scan.nextInt();
            int edge2 = scan.nextInt();
            int capacity = scan.nextInt();
            Integer[] route = new Integer[3];
            route[0] = edge1;
            route[1] = edge2;
            route[2] = capacity;
            removeable.add(route);
            capacityMatrix[edge1][edge2] = capacity;
            capacityMatrix[edge2][edge1] = capacity;
        }

        for (int i = 0; i < nbrRoutes; i++) {
            gointToRemove.add(scan.nextInt());
        }
        current = gointToRemove.size();
        scan.close();
    }

    public boolean BFS(int graph[][], int src, int dest, Integer pred[]) {
        boolean visited[] = new boolean[nbrNodes];
        for (int i = 0; i < nbrNodes; i++) {
            visited[i] = false;
        }
        LinkedList<Integer> queue = new LinkedList<>();

        visited[src] = true;
        queue.add(src);
        pred[src] = -1;

        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < nbrNodes; i++) {
                if (visited[i] == false && graph[u][i] > 0) {
                    visited[i] = true;
                    pred[i] = u;
                    queue.add(i);
                    if (i == dest) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    public int fordFulk(int resMatrix[][], int flow) {
        Integer[] path = new Integer[nbrNodes];

        // System.out.println(Arrays.deepToString(resMatrix)+"
        // "+Arrays.deepToString(capacityMatrix));
        while (BFS(resMatrix, 0, nbrNodes - 1, path)) {
            int pathFlow = Integer.MAX_VALUE;

            for (int i = nbrNodes - 1; i != 0; i = path[i]) {
                int j = path[i];
                pathFlow = Math.min(pathFlow, resMatrix[j][i]);

            }

            for (int i = nbrNodes - 1; i != 0; i = path[i]) {
                int j = path[i];
                resMatrix[j][i] -= pathFlow;
                resMatrix[i][j] += pathFlow;
            }

            flow += pathFlow;
        }

        // for (int i = 0; i < path.length; i++) {
        // currentPath.add(path[i]);
        // }
        return flow;
    }

    public static void main(String[] args) {
        // currentPath = new ArrayList<Integer>();
        rp rail = new rp();
        rail.handleData();

        for (int i = 0; i < gointToRemove.size(); i++) {
            Integer test = gointToRemove.get(i);
            Integer[] route = removeable.get(test);
            capacityMatrix[route[0]][route[1]] -= route[2];
            capacityMatrix[route[1]][route[0]] -= route[2];
        }

        int[][] resMatrix = new int[nbrNodes][nbrNodes];
        for (int i = 0; i <= nbrNodes - 1; i++) {
            for (int j = 0; j <= nbrNodes - 1; j++) {
                resMatrix[i][j] = capacityMatrix[i][j];
            }
        }
        oldFlow = 0;
        while (oldFlow < nbrStudents) {
            current--;
            Integer test = gointToRemove.remove(gointToRemove.size() - 1);
            Integer[] route = removeable.get(test);
            resMatrix[route[0]][route[1]] += route[2];
            resMatrix[route[1]][route[0]] += route[2];
            oldFlow = rail.fordFulk(resMatrix, oldFlow);
        }

        System.out.println(current + " " + oldFlow);

    }

}

// Om flödet ut från första noden är C, max C bfs sökningar , om bfs O(m+n) så
// blir F-F O(C*m) (m>n).
// Bipartit matchning
// Max-Flow-Min-Cut
// Edge disjoint paths
// Om kapaciteterna är stora vill man unvika små ökningar av flödet, inför gräns
// över vilka kanter som är intressanta
// Så x=2^k>=C, sen halvera x och kör igen.