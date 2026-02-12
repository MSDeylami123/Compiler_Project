import java.util.List;

public class CFGBuilder {

    private int nodeCounter = 0;

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

    // ========================================
    // MAIN DISPATCH
    // ========================================
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

            case FOR:
                return buildFor(node, cfg);   // ✅ NEW

            case ASSIGN:
            case RETURN:
            case FUNCTION_CALL:
            case VAR_DECL:
                return buildSimple(node, cfg);

            default:
                return buildSimple(node, cfg);
        }
    }

    // ========================================
    // SIMPLE STATEMENT
    // ========================================
    private CFGFragment buildSimple(ASTNode node, CFG cfg) {

        CFGNode stmtNode = new CFGNode(nextId(), node.type.toString());
        cfg.addNode(stmtNode);

        return new CFGFragment(stmtNode, stmtNode);
    }

    // ========================================
    // BLOCK / SEQUENCE
    // ========================================
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

    // ========================================
    // IF
    // ========================================
    private CFGFragment buildIf(ASTNode node, CFG cfg) {

        CFGNode conditionNode = new CFGNode(nextId(), "IF");
        cfg.addNode(conditionNode);

        ASTNode thenNode = node.children.get(1);
        ASTNode elseNode = node.children.size() > 2 ? node.children.get(2) : null;

        CFGFragment thenFrag = buildFragment(thenNode, cfg);
        CFGFragment elseFrag = null;

        if (elseNode != null)
            elseFrag = buildFragment(elseNode, cfg);

        CFGNode mergeNode = new CFGNode(nextId(), "MERGE");
        cfg.addNode(mergeNode);

        conditionNode.addSuccessor(thenFrag.entry);
        thenFrag.exit.addSuccessor(mergeNode);

        if (elseFrag != null) {
            conditionNode.addSuccessor(elseFrag.entry);
            elseFrag.exit.addSuccessor(mergeNode);
        } else {
            conditionNode.addSuccessor(mergeNode);
        }

        return new CFGFragment(conditionNode, mergeNode);
    }

    // ========================================
    // WHILE
    // ========================================
    private CFGFragment buildWhile(ASTNode node, CFG cfg) {

        CFGNode conditionNode = new CFGNode(nextId(), "LOOP_COND");
        cfg.addNode(conditionNode);

        ASTNode bodyNode = node.children.get(1);
        CFGFragment bodyFrag = buildFragment(bodyNode, cfg);

        CFGNode exitNode = new CFGNode(nextId(), "LOOP_EXIT");
        cfg.addNode(exitNode);

        // cond -> body
        conditionNode.addSuccessor(bodyFrag.entry);

        // body -> cond
        bodyFrag.exit.addSuccessor(conditionNode);

        // cond -> exit
        conditionNode.addSuccessor(exitNode);

        return new CFGFragment(conditionNode, exitNode);
    }

    // ========================================
    // ✅ FOR (Normalized to WHILE)
    // ========================================
    private CFGFragment buildFor(ASTNode node, CFG cfg) {

        // Expected:
        // [0] init
        // [1] condition
        // [2] update
        // [3] body (optional in malformed cases)

        ASTNode initNode = node.children.size() > 0 ? node.children.get(0) : null;
        ASTNode condNode = node.children.size() > 1 ? node.children.get(1) : null;
        ASTNode updateNode = node.children.size() > 2 ? node.children.get(2) : null;
        ASTNode bodyNode = node.children.size() > 3 ? node.children.get(3) : null;

        // 1️⃣ INIT
        CFGFragment initFrag = initNode != null
                ? buildFragment(initNode, cfg)
                : buildSimple(new ASTNode(ASTType.EMPTY), cfg);

        // 2️⃣ LOOP CONDITION
        CFGNode conditionNode = new CFGNode(nextId(), "LOOP_COND");
        cfg.addNode(conditionNode);

        initFrag.exit.addSuccessor(conditionNode);

        // 3️⃣ BODY
        CFGFragment bodyFrag = bodyNode != null
                ? buildFragment(bodyNode, cfg)
                : buildSimple(new ASTNode(ASTType.EMPTY), cfg);

        // 4️⃣ UPDATE
        CFGFragment updateFrag = updateNode != null
                ? buildFragment(updateNode, cfg)
                : buildSimple(new ASTNode(ASTType.EMPTY), cfg);

        // 5️⃣ EXIT
        CFGNode exitNode = new CFGNode(nextId(), "LOOP_EXIT");
        cfg.addNode(exitNode);

        // cond -> body
        conditionNode.addSuccessor(bodyFrag.entry);

        // body -> update
        bodyFrag.exit.addSuccessor(updateFrag.entry);

        // update -> cond
        updateFrag.exit.addSuccessor(conditionNode);

        // cond -> exit
        conditionNode.addSuccessor(exitNode);

        return new CFGFragment(initFrag.entry, exitNode);
    }

    // ========================================
    private int nextId() {
        return nodeCounter++;
    }
}
