import java.util.*;

public class ASTNode {
    public ASTType type;
    public String value;               // operator, type, etc.
    public List<ASTNode> children;

    public ASTNode(ASTType type) {
        this.type = type;
        this.children = new ArrayList<>();
    }

    public ASTNode(ASTType type, String value) {
        this(type);
        this.value = value;
    }

    public void add(ASTNode child) {
        if (child != null)
            children.add(child);
    }

    public int size() {
        int s = 1;
        for (ASTNode c : children)
            s += c.size();
        return s;
    }
}
