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

    public boolean isUnordered() {
        return type == ASTType.PROGRAM
            || type == ASTType.BLOCK
            || type == ASTType.FUNCTION;
    }

    public int weight() {
        switch (type) {
            case FUNCTION: return 6;
            case IF:
            case WHILE:
            case FOR: return 5;
            case ASSIGN:
            case FUNCTION_CALL: return 4;
            case RETURN: return 3;
            case BLOCK: return 2;
            case IDENTIFIER:
            case CONSTANT: return 1;
            default: return 2;
        }
    }

    public int weightedSize() {
        int sum = weight();
        for (ASTNode c : children)
            sum += c.weightedSize();
        return sum;
    }

    public List<ASTNode> getAllSubtrees() {
        List<ASTNode> list = new ArrayList<>();
        collect(this, list);
        return list;
    }

    private void collect(ASTNode node, List<ASTNode> list) {
        list.add(node);
        for (ASTNode c : node.children)
            collect(c, list);
    }

    




}
