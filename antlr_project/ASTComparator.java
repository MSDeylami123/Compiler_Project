import java.util.*;

public class ASTComparator {

    // ===============================
    // TREE EDIT DISTANCE WITH WEIGHTS
    // ===============================
    public static int treeEditDistance(ASTNode a, ASTNode b) {

        if (a == null && b == null) return 0;
        if (a == null) return b.weightedSize();
        if (b == null) return a.weightedSize();

        // Weighted mismatch cost
        int cost = a.type == b.type ? 0 : Math.max(a.weight(), b.weight());

        List<ASTNode> A = a.children;
        List<ASTNode> B = b.children;

        // ORDER-INSENSITIVE MATCHING
        if (a.isUnordered()) {
            return cost + unorderedDistance(A, B);
        }

        // ORDER-SENSITIVE MATCHING
        return cost + orderedDistance(A, B);
    }

    // ===============================
    // ORDERED CHILDREN DISTANCE
    // ===============================
    private static int orderedDistance(List<ASTNode> A, List<ASTNode> B) {
        int m = A.size(), n = B.size();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i == 0 ? 0 : dp[i - 1][0] + A.get(i - 1).weightedSize();
        for (int j = 0; j <= n; j++) dp[0][j] = j == 0 ? 0 : dp[0][j - 1] + B.get(j - 1).weightedSize();

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = Math.min(
                    dp[i - 1][j] + A.get(i - 1).weightedSize(),
                    Math.min(
                        dp[i][j - 1] + B.get(j - 1).weightedSize(),
                        dp[i - 1][j - 1] + treeEditDistance(A.get(i - 1), B.get(j - 1))
                    )
                );
            }
        }

        return dp[m][n];
    }

    // ===============================
    // UNORDERED CHILDREN DISTANCE
    // ===============================
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
                cost += a.weightedSize();
            }
        }

        // unmatched nodes in B
        for (int i = 0; i < B.size(); i++) {
            if (!used[i])
                cost += B.get(i).weightedSize();
        }

        return cost;
    }

    // ===============================
    // MAIN SIMILARITY FUNCTION
    // ===============================
    public static double similarity(ASTNode a, ASTNode b) {
        int dist = treeEditDistance(a, b);
        int max = Math.max(a.weightedSize(), b.weightedSize());
        return 1.0 - (double) dist / max;
    }

    // ===============================
    // MAX SUBTREE SIMILARITY
    // ===============================
    public static double maxSubtreeSimilarity(ASTNode A, ASTNode B) {
        double maxSim = 0;

        List<ASTNode> subA = A.getAllSubtrees();
        List<ASTNode> subB = B.getAllSubtrees();

        for (ASTNode aSub : subA) {
            for (ASTNode bSub : subB) {
                double sim = similarity(aSub, bSub);
                if (sim > maxSim)
                    maxSim = sim;
            }
        }

        return maxSim;
    }
}
