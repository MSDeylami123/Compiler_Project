import java.util.ArrayList;
import java.util.List;
public class CFGNode {
    private int id;
    private String label;
    private List<CFGNode> successors;
    private List<CFGNode> predecessors;

    public CFGNode(int id, String label) {
        this.id = id;
        this.label = label;
        this.successors = new ArrayList<>();
        this.predecessors = new ArrayList<>();
    }

    public void addSuccessor(CFGNode node) {
        successors.add(node);
        node.predecessors.add(this);
    }

    public List<CFGNode> getSuccessors() {
        return successors;
    }

    public List<CFGNode> getPredecessors() {
        return predecessors;
    }

    public String getLabel() {
        return label;
    }

    public int getId() {
        return id;
    }
}

