import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {

    // ===============================
    // TOKEN INFO CLASS
    // ===============================
    static class TokenInfo {
        String text;
        String type;
        int line;
        int column;

        TokenInfo(String text, String type, int line, int column) {
            this.text = text;
            this.type = type;
            this.line = line;
            this.column = column;
        }

        @Override
        public String toString() {
            return String.format("%s('%s') at [%d:%d]", type, text, line, column);
        }
    }

    public static void main(String[] args) throws Exception {

        // -------- TOKENIZE FILES --------
        List<TokenInfo> tokens1 = tokenize("input1.txt");
        List<TokenInfo> tokens2 = tokenize("input2.txt");

        System.out.println("\nTokens in file 1: " + tokens1.size());
        System.out.println("Tokens in file 2: " + tokens2.size());

        int dist = editDistance(tokens1, tokens2);
        int maxLen = Math.max(tokens1.size(), tokens2.size());

        double similarity = 1.0 - (double) dist / maxLen;
        double percent = similarity * 100.0;

        System.out.printf("\nToken-based Similarity: %.2f%%\n", percent);
        System.out.println("Decision: " + (percent >= 60 ? "Similar" : "Not Similar"));

        // -------- TOKEN-LEVEL VISUALIZATION --------
        printTokenComparison(tokens1, tokens2);

        // -------- Phase 2 -> AST --------
        ASTNode ast1 = buildAST("input1.txt");
        ASTNode ast2 = buildAST("input2.txt");

        double astSim = ASTComparator.similarity(ast1, ast2);
        System.out.printf("\nAST-based Similarity: %.2f%%\n", astSim * 100);

        System.out.println("\n===== AST STRUCTURAL DIFF =====");
        ASTDiffPrinter.print(ast1, ast2);

        printAST(ast1, 0);
        printAST(ast2, 0);
        // ===============================
        // Additional AST similarity metrics
        // ===============================
        double subtreeSim = ASTComparator.maxSubtreeSimilarity(ast1, ast2);
        System.out.printf("\nMax Subtree Similarity: %.2f%%\n", subtreeSim * 100);

        // Optional: print a "weighted similarity" using node weights
        double weightedSim = ASTComparator.similarity(ast1, ast2);
        System.out.printf("Weighted AST Similarity: %.2f%%\n", weightedSim * 100);

        // Optional: decision based on AST similarity
        System.out.println("AST Decision: " + (weightedSim >= 0.6 ? "Similar" : "Not Similar"));


        CFGBuilder builder = new CFGBuilder();
        CFG cfg1 = builder.build(ast1);
        CFG cfg2 = builder.build(ast2);

        CFGComparator comparator = new CFGComparator();
        double cfgScore = comparator.compare(cfg1, cfg2);

        System.out.println("CFG Similarity: " + cfgScore);

        System.out.println("CFG 1:");
        CFGPrinter.print(cfg1);

        System.out.println("\nCFG 2:");
        CFGPrinter.print(cfg2);

        






    }

    // ===============================
    // TOKENIZATION (NO NORMALIZATION)
    // ===============================
    static List<TokenInfo> tokenize(String filename) throws Exception {

        CharStream input = CharStreams.fromFileName(filename);
        ExprLexer lexer = new ExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();

        List<TokenInfo> result = new ArrayList<>();

        System.out.println("\n===== TOKENS (" + filename + ") =====");

        for (Token t : tokens.getTokens()) {
            if (t.getType() == Token.EOF) continue;

            String symbolic = ExprLexer.VOCABULARY.getSymbolicName(t.getType());
            String text = t.getText();

            if ("WS".equals(symbolic)) continue;

            System.out.printf(
                "%-15s -> '%s' [%d:%d]\n",
                symbolic, text, t.getLine(), t.getCharPositionInLine()
            );

            result.add(new TokenInfo(
                text,
                symbolic,
                t.getLine(),
                t.getCharPositionInLine()
            ));
        }

        return result;
    }

    // ===============================
    // TOKEN-LEVEL SIMILARITY VIEW
    // ===============================
    static void printTokenComparison(List<TokenInfo> a, List<TokenInfo> b) {

        int n = a.size(), m = b.size();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                TokenInfo ta = a.get(i - 1);
                TokenInfo tb = b.get(j - 1);

                if (ta.type.equals(tb.type) && ta.text.equals(tb.text)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j - 1],
                            Math.min(dp[i - 1][j], dp[i][j - 1])
                    );
                }
            }
        }

        int i = n, j = m;
        List<String> outA = new ArrayList<>();
        List<String> outB = new ArrayList<>();

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0) {
                TokenInfo ta = a.get(i - 1);
                TokenInfo tb = b.get(j - 1);

                if (ta.type.equals(tb.type) && ta.text.equals(tb.text)) {
                    outA.add("[MATCH] " + ta);
                    outB.add("[MATCH] " + tb);
                    i--; j--;
                    continue;
                }
            }

            if (i > 0 && (j == 0 || dp[i][j] == dp[i - 1][j] + 1)) {
                outA.add("[DIFF ] " + a.get(i - 1));
                i--;
            } else if (j > 0) {
                outB.add("[DIFF ] " + b.get(j - 1));
                j--;
            }
        }

        Collections.reverse(outA);
        Collections.reverse(outB);

        System.out.println("\n--- Token comparison (File 1) ---");
        outA.forEach(System.out::println);

        System.out.println("\n--- Token comparison (File 2) ---");
        outB.forEach(System.out::println);
    }

    // ===============================
    // EDIT DISTANCE (TYPE + TEXT)
    // ===============================
    static int editDistance(List<TokenInfo> a, List<TokenInfo> b) {

        int n = a.size();
        int m = b.size();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                TokenInfo ta = a.get(i - 1);
                TokenInfo tb = b.get(j - 1);

                if (ta.type.equals(tb.type) && ta.text.equals(tb.text)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j - 1],
                            Math.min(dp[i - 1][j], dp[i][j - 1])
                    );
                }
            }
        }

        return dp[n][m];
    }

    // ===============================
    // AST BUILDER ENTRY
    // ===============================
    static ASTNode buildAST(String filename) throws Exception {
        CharStream input = CharStreams.fromFileName(filename);
        ExprLexer lexer = new ExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);

        ParseTree tree = parser.prog();
        ASTBuilder builder = new ASTBuilder();
        ASTNode ast = builder.visit(tree);

        // NEW
        return ASTNormalizer.normalize(ast);
    }

    static void printAST(ASTNode node, int indent) {
        System.out.println("  ".repeat(indent) + node.type);
        for (ASTNode c : node.children)
            printAST(c, indent + 1);
    }

}
