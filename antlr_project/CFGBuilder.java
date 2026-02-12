import java.util.List;

public class CFGBuilder {

    private int nodeCounter = 0;

    // Helper class representing entry/exit pair
    private static class CFGFragment {
        CFGNode entry;
        CFGNode exit;

        CFGFragment(CFGNode entry, CFGNode exit) {
            this.entry = entry;
            this.exit = exit;
        }
    }

    public CFG build(ASTNode root) {
        CFG cfg = new CFG();

        CFGFragment fragment = buildFragment(root, cfg);

        cfg.setEntry(fragment.entry);
        cfg.setExit(fragment.exit);

        return cfg;
    }

    // Main recursive builder
    private CFGFragment buildFragment(ASTNode node, CFG cfg) {

        switch (node.type) {

            case PROGRAM:
            case BLOCK:
            case FUNCTION:
                return buildBlock(node, cfg);

            case IF:
                return buildIf(node, cfg);

            case WHILE:
                return buildWhile(node, cfg);

            case ASSIGN:
            case RETURN:
            case FUNCTION_CALL:
                return buildSimple(node, cfg);

            default:
                return buildSimple(node, cfg);
        }
    }

    // ----------------------------------------
    // SIMPLE STATEMENT
    // ----------------------------------------
    private CFGFragment buildSimple(ASTNode node, CFG cfg) {

        String label = node.type.toString();

        CFGNode stmtNode = new CFGNode(nextId(), label);
        cfg.addNode(stmtNode);

        return new CFGFragment(stmtNode, stmtNode);
    }

    // ----------------------------------------
    // BLOCK / SEQUENCE
    // ----------------------------------------
    private CFGFragment buildBlock(ASTNode node, CFG cfg) {

        if (node.children.isEmpty()) {
            CFGNode emptyNode = new CFGNode(nextId(), "EMPTY");
            cfg.addNode(emptyNode);
            return new CFGFragment(emptyNode, emptyNode);
        }

        CFGFragment first = null;
        CFGFragment prev = null;

        for (ASTNode child : node.children) {

            CFGFragment current = buildFragment(child, cfg);

            if (first == null)
                first = current;

            if (prev != null)
                prev.exit.addSuccessor(current.entry);

            prev = current;
        }

        return new CFGFragment(first.entry, prev.exit);
    }

    // ----------------------------------------
    // IF STATEMENT
    // ----------------------------------------
    private CFGFragment buildIf(ASTNode node, CFG cfg) {

        // Create condition node
        CFGNode conditionNode = new CFGNode(nextId(), "IF");
        cfg.addNode(conditionNode);

        // Expected structure:
        // children[0] = condition expression
        // children[1] = then block
        // children[2] = else block (optional)

        ASTNode thenNode = node.children.size() > 1 ? node.children.get(1) : null;
        ASTNode elseNode = node.children.size() > 2 ? node.children.get(2) : null;

        CFGFragment thenFrag = buildFragment(thenNode, cfg);

        CFGFragment elseFrag = null;
        if (elseNode != null) {
            elseFrag = buildFragment(elseNode, cfg);
        }

        CFGNode mergeNode = new CFGNode(nextId(), "MERGE");
        cfg.addNode(mergeNode);

        // condition -> then
        conditionNode.addSuccessor(thenFrag.entry);
        thenFrag.exit.addSuccessor(mergeNode);

        if (elseFrag != null) {
            // condition -> else
            conditionNode.addSuccessor(elseFrag.entry);
            elseFrag.exit.addSuccessor(mergeNode);
        } else {
            // no else
            conditionNode.addSuccessor(mergeNode);
        }

        return new CFGFragment(conditionNode, mergeNode);
    }

    // ----------------------------------------
    // WHILE LOOP
    // ----------------------------------------
    private CFGFragment buildWhile(ASTNode node, CFG cfg) {

        // children[0] = condition
        // children[1] = body

        CFGNode conditionNode = new CFGNode(nextId(), "WHILE");
        cfg.addNode(conditionNode);

        ASTNode bodyNode = node.children.size() > 1 ? node.children.get(1) : null;

        CFGFragment bodyFrag = buildFragment(bodyNode, cfg);

        CFGNode exitNode = new CFGNode(nextId(), "WHILE_EXIT");
        cfg.addNode(exitNode);

        // condition -> body
        conditionNode.addSuccessor(bodyFrag.entry);

        // body -> condition (loop back)
        bodyFrag.exit.addSuccessor(conditionNode);

        // condition -> exit
        conditionNode.addSuccessor(exitNode);

        return new CFGFragment(conditionNode, exitNode);
    }

    // ----------------------------------------
    // ID generator
    // ----------------------------------------
    private int nextId() {
        return nodeCounter++;
    }
}
