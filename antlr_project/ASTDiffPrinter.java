public class ASTDiffPrinter {

    public static void print(ASTNode a, ASTNode b) {
        print(a, b, 0);
    }

    private static void print(ASTNode a, ASTNode b, int indent) {
        String pad = "  ".repeat(indent);

        // Both nodes exist
        if (a != null && b != null) {
            if (a.type == b.type) {
                System.out.println(pad + "[MATCH] " + a.type);
            } else {
                System.out.println(pad + "[DIFF ] " +
                        a.type + " vs " + b.type);
            }

            int max = Math.max(
                a.children.size(),
                b.children.size()
            );

            for (int i = 0; i < max; i++) {
                ASTNode ca = i < a.children.size()
                        ? a.children.get(i)
                        : null;
                ASTNode cb = i < b.children.size()
                        ? b.children.get(i)
                        : null;

                print(ca, cb, indent + 1);
            }
        }
        // Node deleted
        else if (a != null) {
            System.out.println(pad + "[DELETE] " + a.type);
            for (ASTNode c : a.children)
                print(c, null, indent + 1);
        }
        // Node inserted
        else if (b != null) {
            System.out.println(pad + "[INSERT] " + b.type);
            for (ASTNode c : b.children)
                print(null, c, indent + 1);
        }
    }
}
