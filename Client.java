import java.util.*;

public class Client {

    static int[][] map = {
        {0,0,1,0,0,0,0,0,0,0,1},
        {0,0,1,0,0,0,0,0,0,0,1},
        {0,0,1,0,0,0,0,0,0,0,1},
        {3,0,1,0,0,0,3,0,0,0,1},
        {1,1,1,0,0,0,0,9,0,0,1},
        {0,0,9,0,0,0,0,0,0,0,1},
        {1,5,1,1,1,1,1,1,0,0,0}
    };

    public static void main(String[] args) {
        findAndPrintPath();                // ← only one line in main!
    }

    private static void findAndPrintPath() {
        List<Position> path = computePath(map);
        // format into ArrayList<String>
        ArrayList<String> answerList = new ArrayList<>();
        for (Position p : path) {
            answerList.add("A[" + p.r + "][" + p.c + "]");
        }
        System.out.println(answerList);
        // print the map showing only the path 1s
        printPathGrid(map.length, map[0].length, path);
    }

    // 1) Locate the single path‐component of 1s with exactly one 90° turn
    private static List<Position> computePath(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        boolean[][] seen = new boolean[rows][cols];

        // find connected components of 1s
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 1 && !seen[r][c]) {
                    List<Position> comp = new ArrayList<>();
                    dfs(r, c, grid, seen, comp);

                    // build adjacency within this component
                    Map<Position, List<Position>> adj = buildAdjacency(comp, grid);

                    // count endpoints (degree == 1) and ensure at least one turn
                    int ends = 0, mids = 0;
                    for (List<Position> nbrs : adj.values()) {
                        if (nbrs.size() == 1) ends++;
                        if (nbrs.size() == 2) mids++;
                    }
                    // a true L-shaped path: exactly 2 endpoints, at least one mid-segment
                    if (ends == 2 && mids >= 1 && comp.size() >= 3) {
                        return traversePath(adj);
                    }
                }
            }
        }
        return Collections.emptyList();  // should never happen per spec
    }

    // simple DFS to collect one component
    private static void dfs(int r, int c, int[][] grid, boolean[][] seen, List<Position> comp) {
        int rows = grid.length, cols = grid[0].length;
        if (r < 0|| c < 0|| r >= rows|| c >= cols) return;
        if (seen[r][c] || grid[r][c] != 1) return;
        seen[r][c] = true;
        Position p = new Position(r, c);
        comp.add(p);
        dfs(r+1, c, grid, seen, comp);
        dfs(r-1, c, grid, seen, comp);
        dfs(r, c+1, grid, seen, comp);
        dfs(r, c-1, grid, seen, comp);
    }

    // for each cell in the component, find its neighbors in the same comp
    private static Map<Position,List<Position>> buildAdjacency(
            List<Position> comp, int[][] grid) {

        Set<Position> set = new HashSet<>(comp);
        Map<Position,List<Position>> adj = new HashMap<>();
        int rows = grid.length, cols = grid[0].length;

        for (Position p : comp) {
            List<Position> nbrs = new ArrayList<>();
            int[][] d = {{1,0},{-1,0},{0,1},{0,-1}};
            for (int[] dir : d) {
                int nr = p.r + dir[0], nc = p.c + dir[1];
                Position q = new Position(nr,nc);
                if (nr>=0&&nc>=0&&nr<rows&&nc<cols
                    && grid[nr][nc]==1 && set.contains(q)) {
                    nbrs.add(q);
                }
            }
            adj.put(p, nbrs);
        }
        return adj;
    }

    // walk from one endpoint through the chain to the other
    private static List<Position> traversePath(Map<Position,List<Position>> adj) {
        Position start = null;
        for (Map.Entry<Position,List<Position>> e : adj.entrySet()) {
            if (e.getValue().size() == 1) { start = e.getKey(); break; }
        }
        List<Position> path = new ArrayList<>();
        Position prev = null, curr = start;
        while (true) {
            path.add(curr);
            List<Position> nbrs = adj.get(curr);
            // if we're at the other endpoint, stop
            if (nbrs.size()==1 && prev != null) break;
            // pick the neighbor that isn't where we just came from
            Position next = nbrs.get(0).equals(prev) && nbrs.size()>1
                            ? nbrs.get(1)
                            : nbrs.get(0);
            prev = curr;
            curr = next;
        }
        return path;
    }

    // print a grid with only the path 1s (all other spots blank)
    private static void printPathGrid(int rows, int cols, List<Position> path) {
        Set<Position> set = new HashSet<>(path);
        for (int r = 0; r < rows; r++) {
            System.out.print("[");
            for (int c = 0; c < cols; c++) {
                System.out.print(set.contains(new Position(r,c)) ? "1" : " ");
                if (c < cols-1) System.out.print(",");
            }
            System.out.println("]");
        }
    }

    // helper class for row/col pairs
    private static class Position {
        final int r, c;
        Position(int r, int c) { this.r = r; this.c = c; }
        @Override public boolean equals(Object o) {
            if (this==o) return true;
            if (!(o instanceof Position)) return false;
            Position p = (Position)o;
            return r==p.r && c==p.c;
        }
        @Override public int hashCode() {
            return Objects.hash(r,c);
        }
        @Override public String toString() {
            return "(" + r + "," + c + ")";
        }
    }
}
