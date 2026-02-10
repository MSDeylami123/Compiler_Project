import java.util.*;

public class ASTComparator {

    public static int treeEditDistance(ASTNode a, ASTNode b) {

        if (a == null && b == null) return 0;
        if (a == null) return b.size();
        if (b == null) return a.size();

        int cost = a.type == b.type ? 0 : 1;

        List<ASTNode> A = a.children;
        List<ASTNode> B = b.children;

        // ORDER-INSENSITIVE MATCHING
        if (a.isUnordered()) {
            return cost + unorderedDistance(A, B);
        }

        // ORDER-SENSITIVE MATCHING
        return cost + orderedDistance(A, B);
    }

    private static int orderedDistance(List<ASTNode> A, List<ASTNode> B) {
        int m = A.size(), n = B.size();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = Math.min(
                    dp[i - 1][j] + A.get(i - 1).size(),
                    Math.min(
                        dp[i][j - 1] + B.get(j - 1).size(),
                        dp[i - 1][j - 1] +
                            treeEditDistance(A.get(i - 1), B.get(j - 1))
                    )
                );
            }
        }

        return dp[m][n];
    }

    private static int unorderedDistance(List<ASTNode> A, List<ASTNode> B) {

        boolean[] used = new boolean[B.size()];
        int cost = 0;

        for (ASTNode a : A) {
            int best = Integer.MAX_VALUE;
            int bestIdx = -1;

            for (int i = 0; i < B.size(); i++) {
                if (used[i]) continue;

                int d = treeEditDistance(a, B.get(i));
                if (d < best) {
                    best = d;
                    bestIdx = i;
                }
            }

            if (bestIdx != -1) {
                used[bestIdx] = true;
                cost += best;
            } else {
                cost += a.size();
            }
        }

        // unmatched nodes in B
        for (int i = 0; i < B.size(); i++) {
            if (!used[i])
                cost += B.get(i).size();
        }

        return cost;
    }

    public static double similarity(ASTNode a, ASTNode b) {
        int dist = treeEditDistance(a, b);
        int max = Math.max(a.size(), b.size());
        return 1.0 - (double) dist / max;
    }
}
