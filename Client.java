import java.util.*;

public class Client {

    static List<List<Integer>> map = new ArrayList<>(List.of(
        List.of(0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0),
        List.of(0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1),
        List.of(0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0),
        List.of(3, 0, 1, 0, 0, 3, 0, 1, 1, 1, 1),
        List.of(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        List.of(0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 1),
        List.of(1, 5, 1, 1, 1, 1, 1, 0, 0, 1, 0)
    ));

    public static void main(String[] args) {
        findAndPrintPath();
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
        printPathGrid(map.size(), map.get(0).size(), path);
    }

    // ─── compute the L-shaped path on a List<List<Integer>> ────────────────
    private static List<Position> computePath(List<List<Integer>> grid) {
        int rows = grid.size(), cols = grid.get(0).size();
        boolean[][] seen = new boolean[rows][cols];

        // find connected components of 1s
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid.get(r).get(c) == 1 && !seen[r][c]) {
                    List<Position> comp = new ArrayList<>();
                    dfs(r, c, grid, seen, comp);

                    // build adjacency within this component
                    Map<Position, List<Position>> adj = buildAdjacency(comp, grid);

                    // count endpoints & the one true corner
                    int ends = 0, cornerCount = 0;
                    for (var e : adj.entrySet()) {
                        List<Position> nbrs = e.getValue();
                        if (nbrs.size() == 1) ends++;

                        // detect a 90° corner: exactly two neighbors, one horiz + one vert
                        if (nbrs.size() == 2) {
                            Position p = e.getKey(), a = nbrs.get(0), b = nbrs.get(1);
                            boolean isCorner =
                                (a.r == p.r && b.c == p.c) ||
                                (b.r == p.r && a.c == p.c);
                            if (isCorner) cornerCount++;
                        }
                    }

                    // exactly two ends + exactly one corner → that’s our L
                    if (ends == 2 && cornerCount == 1) {
                        return traversePath(adj);
                    }
                }
            }
        }

        return Collections.emptyList(); // per spec, should never happen
    }

    // DFS over only the 1’s
    private static void dfs(int r, int c,
                            List<List<Integer>> grid,
                            boolean[][] seen,
                            List<Position> comp) {
        int rows = grid.size(), cols = grid.get(0).size();
        if (r < 0 || c < 0 || r >= rows || c >= cols) return;
        if (seen[r][c] || grid.get(r).get(c) != 1) return;

        seen[r][c] = true;
        comp.add(new Position(r, c));

        dfs(r+1, c, grid, seen, comp);
        dfs(r-1, c, grid, seen, comp);
        dfs(r, c+1, grid, seen, comp);
        dfs(r, c-1, grid, seen, comp);
    }

    // Build adjacency list for the positions in comp
    private static Map<Position,List<Position>> buildAdjacency(
            List<Position> comp,
            List<List<Integer>> grid) {

        Set<Position> set = new HashSet<>(comp);
        Map<Position,List<Position>> adj = new HashMap<>();
        int rows = grid.size(), cols = grid.get(0).size();
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        for (Position p : comp) {
            List<Position> nbrs = new ArrayList<>();
            for (int[] d : dirs) {
                int nr = p.r + d[0], nc = p.c + d[1];
                Position q = new Position(nr, nc);
                if (nr >= 0 && nc >= 0 && nr < rows && nc < cols
                 && grid.get(nr).get(nc) == 1
                 && set.contains(q)) {
                    nbrs.add(q);
                }
            }
            adj.put(p, nbrs);
        }
        return adj;
    }

    // Walk from one endpoint to the other
    private static List<Position> traversePath(
            Map<Position,List<Position>> adj) {

        Position start = adj.entrySet()
                            .stream()
                            .filter(e -> e.getValue().size() == 1)
                            .findFirst()
                            .get()
                            .getKey();

        List<Position> path = new ArrayList<>();
        Position prev = null, curr = start;

        while (true) {
            path.add(curr);
            List<Position> nbrs = adj.get(curr);

            // reached the far end?
            if (nbrs.size() == 1 && prev != null) break;

            // pick the neighbor that isn’t where we came from
            Position next = nbrs.get(0).equals(prev) && nbrs.size() > 1
                            ? nbrs.get(1)
                            : nbrs.get(0);

            prev = curr;
            curr = next;
        }

        return path;
    }

    // Print a grid showing only the path 1’s
    private static void printPathGrid(int rows, int cols, List<Position> path) {
        Set<Position> set = new HashSet<>(path);
        for (int r = 0; r < rows; r++) {
            System.out.print("[");
            for (int c = 0; c < cols; c++) {
                System.out.print(set.contains(new Position(r,c)) ? "1" : " ");
                if (c < cols - 1) System.out.print(",");
            }
            System.out.println("]");
        }
    }

    // Helper class
    private static class Position {
        final int r, c;
        Position(int r, int c) { this.r = r; this.c = c; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Position)) return false;
            Position p = (Position)o;
            return r == p.r && c == p.c;
        }
        @Override public int hashCode() {
            return Objects.hash(r, c);
        }
    }
}
