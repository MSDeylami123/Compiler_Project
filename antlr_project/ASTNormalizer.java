import java.util.*;

public class ASTNormalizer {

    public static ASTNode normalize(ASTNode node) {
        if (node == null) return null;

        List<ASTNode> normalizedChildren = new ArrayList<>();

        for (ASTNode c : node.children) {
            ASTNode nc = normalize(c);
            if (nc != null) {
                // Flatten any child BLOCKs by merging their children
                if (nc.type == ASTType.BLOCK) {
                    normalizedChildren.addAll(nc.children);
                } else {
                    normalizedChildren.add(nc);
                }
            }
        }

        node.children = normalizedChildren;

        // Flatten trivial BLOCKs (single-child)
        if (node.type == ASTType.BLOCK && node.children.size() == 1) {
            return node.children.get(0);
        }

        return node;
    }
}
