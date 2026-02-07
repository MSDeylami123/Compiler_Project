import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {

    public static void main(String[] args) throws Exception {

        // -------- TOKENIZE FILES --------
        List<String> tokens1 = tokenize("input1.txt");
        List<String> tokens2 = tokenize("input2.txt");

        System.out.println("Tokens in file 1: " + tokens1.size());
        System.out.println("Tokens in file 2: " + tokens2.size());

        int dist = editDistance(tokens1, tokens2);
        int maxLen = Math.max(tokens1.size(), tokens2.size());

        double similarity = 1.0 - (double) dist / maxLen;
        double percent = similarity * 100.0;

        System.out.printf("Token-based Similarity: %.2f%%\n", percent);
        System.out.println("Decision: " + (percent >= 60 ? "Similar" : "Not Similar"));

        // -------- PARSE TREE DEMO --------
        System.out.println("\n===== PARSE TREE (input1.txt) =====");
        parseAndPrintTree("input1.txt");
    }

    // ===============================
    // TOKENIZATION (CLEAN VERSION)
    // ===============================
    static List<String> tokenize(String filename) throws Exception {

        CharStream input = CharStreams.fromFileName(filename);
        ExprLexer lexer = new ExprLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();

        List<String> result = new ArrayList<>();

        System.out.println("\n===== TOKENS (" + filename + ") =====");

        for (Token t : tokens.getTokens()) {
            if (t.getType() == Token.EOF) continue;

            String symbolic = ExprLexer.VOCABULARY.getSymbolicName(t.getType());
            String text = t.getText();

            System.out.printf("%-15s -> '%s'\n", symbolic, text);

            // Normalization for similarity
            if ("ID".equals(symbolic)) {
                result.add("ID");
            } else if ("NUMBER".equals(symbolic) || "FLOAT".equals(symbolic)) {
                result.add("NUM");
            } else if (!"WS".equals(symbolic)) {
                result.add(symbolic);
            }
        }

        return result;
    }

    // ===============================
    // PARSE TREE OUTPUT
    // ===============================
    static void parseAndPrintTree(String filename) throws Exception {

        CharStream input = CharStreams.fromFileName(filename);
        ExprLexer lexer = new ExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);

        parser.setBuildParseTree(true);

        ParseTree tree = parser.prog();
        System.out.println(tree.toStringTree(parser));
    }

    // ===============================
    // EDIT DISTANCE (UNCHANGED)
    // ===============================
    static int editDistance(List<String> a, List<String> b) {

        int n = a.size();
        int m = b.size();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.get(i - 1).equals(b.get(j - 1))) {
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
}
