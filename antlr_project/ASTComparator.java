public class ASTComparator {

    public static int treeEditDistance(ASTNode a, ASTNode b) {
        if (a == null && b == null) return 0;
        if (a == null) return b.size();
        if (b == null) return a.size();

        int cost = a.type == b.type ? 0 : 1;

        int m = a.children.size();
        int n = b.children.size();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = Math.min(
                    dp[i - 1][j] + 1,
                    Math.min(
                        dp[i][j - 1] + 1,
                        dp[i - 1][j - 1] +
                            (a.children.get(i - 1).type ==
                             b.children.get(j - 1).type ? 0 : 1)
                    )
                );
            }
        }

        return cost + dp[m][n];
    }

    public static double similarity(ASTNode a, ASTNode b) {
        int dist = treeEditDistance(a, b);
        int max = Math.max(a.size(), b.size());
        return 1.0 - (double) dist / max;
    }
}
