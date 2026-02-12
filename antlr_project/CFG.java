import java.util.ArrayList;
import java.util.List;

public class CFG {
    private CFGNode entry;
    private CFGNode exit;
    private List<CFGNode> nodes;

    public CFG() {
        nodes = new ArrayList<>();
    }

    public void setEntry(CFGNode entry) {
        this.entry = entry;
    }

    public void setExit(CFGNode exit) {
        this.exit = exit;
    }

    public CFGNode getEntry() {
        return entry;
    }

    public CFGNode getExit() {
        return exit;
    }

    public void addNode(CFGNode node) {
        nodes.add(node);
    }

    public List<CFGNode> getNodes() {
        return nodes;
    }
}

