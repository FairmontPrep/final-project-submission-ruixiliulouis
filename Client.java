import java.util.*;

public class Client {
    static final List<List<Integer>> map = Arrays.asList(
        Arrays.asList(1, 0, 0, 1, 0, 0, 0, 0),
        Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0),
        Arrays.asList(0, 0, 0, 1, 0, 0, 1, 0),
        Arrays.asList(9, 0, 0, 1, 0, 0, 0, 0),
        Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0),
        Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0),
        Arrays.asList(0, 0, 0, 1, 2, 0, 0, 0),
        Arrays.asList(1, 0, 0, 1, 1, 1, 1, 1)
    );

    public static void main(String[] args) {
        displaySolution(map);
    }

    static void displaySolution(List<List<Integer>> map) {
        ArrayList<String> pathCoords = findPath(map);
        System.out.println("Path Coordinates:");
        for (String c : pathCoords) {
            System.out.println(c);
        }
        System.out.println("\nPath Map (only 1s on the path):");
        printPathOnly(pathCoords, map);
    }

    static ArrayList<String> findPath(List<List<Integer>> map) {
        int R = map.size(), C = map.get(0).size();

        int[] turn = findTurnCell(map);
        boolean[][] seen = new boolean[R][C];
        List<int[]> comp = new ArrayList<>();
        Deque<int[]> stk = new ArrayDeque<>();
        stk.push(turn);
        seen[turn[0]][turn[1]] = true;
        comp.add(turn);

        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        while (!stk.isEmpty()) {
            int[] p = stk.pop();
            for (int[] d : dirs) {
                int nr = p[0]+d[0], nc = p[1]+d[1];
                if (nr>=0 && nr<R && nc>=0 && nc<C
                 && !seen[nr][nc]
                 && (map.get(nr).get(nc)==1 || map.get(nr).get(nc)==2)) {
                    seen[nr][nc] = true;
                    comp.add(new int[]{nr,nc});
                    stk.push(new int[]{nr,nc});
                }
            }
        }

        List<int[]> ends = new ArrayList<>();
        for (int[] p : comp) {
            int r = p[0], c = p[1];
            if (r==0||r==R-1||c==0||c==C-1) {
                int deg = 0;
                for (int[] d : dirs) {
                    int rr = r+d[0], cc = c+d[1];
                    if (rr>=0&&rr<R&&cc>=0&&cc<C
                     && (map.get(rr).get(cc)==1||map.get(rr).get(cc)==2)) {
                        deg++;
                    }
                }
                if (deg==1) ends.add(p);
            }
        }

        int[] start = ends.get(0), goal = ends.get(1);

        boolean[][] vis2 = new boolean[R][C];
        Map<String,String> parent = new HashMap<>();
        Queue<int[]> q = new ArrayDeque<>();
        q.add(start);
        vis2[start[0]][start[1]] = true;

        while (!q.isEmpty()) {
            int[] p = q.poll();
            if (p[0]==goal[0] && p[1]==goal[1]) break;
            for (int[] d : dirs) {
                int nr = p[0]+d[0], nc = p[1]+d[1];
                String key = nr + "," + nc;
                if (nr>=0&&nr<R&&nc>=0&&nc<C
                 && !vis2[nr][nc]
                 && (map.get(nr).get(nc)==1||map.get(nr).get(nc)==2)) {
                    vis2[nr][nc] = true;
                    parent.put(key, p[0]+","+p[1]);
                    q.add(new int[]{nr,nc});
                }
            }
        }

        List<int[]> rawPath = new ArrayList<>();
        String cur = goal[0]+","+goal[1];
        while (true) {
            String[] sp = cur.split(",");
            rawPath.add(new int[]{
                Integer.parseInt(sp[0]),
                Integer.parseInt(sp[1])
            });
            if (cur.equals(start[0]+","+start[1])) break;
            cur = parent.get(cur);
        }
        Collections.reverse(rawPath);

        ArrayList<String> answer = new ArrayList<>();
        for (int[] p : rawPath) {
            answer.add("A[" + p[0] + "][" + p[1] + "]");
        }
        return answer;
    }

    static void printPathOnly(ArrayList<String> path, List<List<Integer>> map) {
        int R = map.size(), C = map.get(0).size();
        Set<String> onPath = new HashSet<>(path);
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                String key = "A[" + i + "][" + j + "]";
                System.out.print(onPath.contains(key) ? "1 " : "  ");
            }
            System.out.println();
        }
    }

    static int[] findTurnCell(List<List<Integer>> map) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) == 2) {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalStateException("No '2' in the map!");
    }
}
