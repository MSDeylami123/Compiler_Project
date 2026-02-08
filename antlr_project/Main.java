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

        // -------- PARSE TREE DEMO --------
        //System.out.println("\n===== PARSE TREE (input1.txt) =====");
        //parseAndPrintTree("input1.txt");
        //System.out.println("\n===== PARSE TREE (input2.txt) =====");
        //parseAndPrintTree("input2.txt");



        // -------- Phase 2 -> AST --------
        ASTNode ast1 = buildAST("input1.txt");
        ASTNode ast2 = buildAST("input2.txt");

        double astSim = ASTComparator.similarity(ast1, ast2);
        System.out.printf("\nAST-based Similarity: %.2f%%\n", astSim * 100);

    }

    // ===============================
    // TOKENIZATION WITH POSITIONS
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

            System.out.printf(
                "%-15s -> '%s' [%d:%d]\n",
                symbolic, text, t.getLine(), t.getCharPositionInLine()
            );

            // Normalize tokens for similarity
            String normalized;
            if ("ID".equals(symbolic)) {
                normalized = "ID";
            } else if ("NUMBER".equals(symbolic) || "FLOAT".equals(symbolic)) {
                normalized = "NUM";
            } else if (!"WS".equals(symbolic)) {
                normalized = symbolic;
            } else {
                continue;
            }

            result.add(new TokenInfo(
                text,
                normalized,
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
                if (a.get(i - 1).type.equals(b.get(j - 1).type)) {
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
            if (i > 0 && j > 0 && a.get(i - 1).type.equals(b.get(j - 1).type)) {
                outA.add("[MATCH] " + a.get(i - 1));
                outB.add("[MATCH] " + b.get(j - 1));
                i--; j--;
            } else if (i > 0 && (j == 0 || dp[i][j] == dp[i - 1][j] + 1)) {
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
    // PARSE TREE OUTPUT
    // ===============================
    static void parseAndPrintTree(String filename) throws Exception {

        CharStream input = CharStreams.fromFileName(filename);
        ExprLexer lexer = new ExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);

        ParseTree tree = parser.prog();
        System.out.println(tree.toStringTree(parser));
    }

    // ===============================
    // EDIT DISTANCE (TOKEN TYPES)
    // ===============================
    static int editDistance(List<TokenInfo> a, List<TokenInfo> b) {

        int n = a.size();
        int m = b.size();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.get(i - 1).type.equals(b.get(j - 1).type)) {
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

    static ASTNode buildAST(String filename) throws Exception {
        CharStream input = CharStreams.fromFileName(filename);
        ExprLexer lexer = new ExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);

        ParseTree tree = parser.prog();
        ASTBuilder builder = new ASTBuilder();
        return builder.visit(tree);
    }

}
