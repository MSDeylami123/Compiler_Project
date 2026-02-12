import java.util.*;

public class CFGComparator {

    public double compare(CFG cfg1, CFG cfg2) {

        double nodeScore = compareNodeCount(cfg1, cfg2);
        double edgeScore = compareEdgeCount(cfg1, cfg2);
        double labelScore = compareLabelFrequency(cfg1, cfg2);
        double cycleScore = compareCycles(cfg1, cfg2);

        // Weighted average
        return (nodeScore * 0.2 +
                edgeScore * 0.2 +
                labelScore * 0.3 +
                cycleScore * 0.3);
    }

    // --------------------------
    private double compareNodeCount(CFG c1, CFG c2) {
        int n1 = c1.getNodes().size();
        int n2 = c2.getNodes().size();
        return 1.0 - (Math.abs(n1 - n2) / (double)Math.max(n1, n2));
    }

    // --------------------------
    private double compareEdgeCount(CFG c1, CFG c2) {
        int e1 = countEdges(c1);
        int e2 = countEdges(c2);
        return 1.0 - (Math.abs(e1 - e2) / (double)Math.max(e1, e2));
    }

    private int countEdges(CFG cfg) {
        int count = 0;
        for (CFGNode node : cfg.getNodes())
            count += node.getSuccessors().size();
        return count;
    }

    // --------------------------
    private double compareLabelFrequency(CFG c1, CFG c2) {

        Map<String, Integer> freq1 = labelFrequency(c1);
        Map<String, Integer> freq2 = labelFrequency(c2);

        Set<String> allLabels = new HashSet<>();
        allLabels.addAll(freq1.keySet());
        allLabels.addAll(freq2.keySet());

        int totalDiff = 0;
        int totalMax = 0;

        for (String label : allLabels) {
            int f1 = freq1.getOrDefault(label, 0);
            int f2 = freq2.getOrDefault(label, 0);

            totalDiff += Math.abs(f1 - f2);
            totalMax += Math.max(f1, f2);
        }

        if (totalMax == 0) return 1.0;

        return 1.0 - (totalDiff / (double) totalMax);
    }

    private Map<String, Integer> labelFrequency(CFG cfg) {
        Map<String, Integer> map = new HashMap<>();
        for (CFGNode node : cfg.getNodes()) {
            map.put(node.getLabel(),
                    map.getOrDefault(node.getLabel(), 0) + 1);
        }
        return map;
    }

    // --------------------------
    private double compareCycles(CFG c1, CFG c2) {
        boolean hasCycle1 = hasCycle(c1);
        boolean hasCycle2 = hasCycle(c2);

        return (hasCycle1 == hasCycle2) ? 1.0 : 0.0;
    }

    private boolean hasCycle(CFG cfg) {
        Set<CFGNode> visited = new HashSet<>();
        Set<CFGNode> stack = new HashSet<>();

        for (CFGNode node : cfg.getNodes()) {
            if (dfsCycle(node, visited, stack))
                return true;
        }
        return false;
    }

    private boolean dfsCycle(CFGNode node,
                             Set<CFGNode> visited,
                             Set<CFGNode> stack) {

        if (stack.contains(node))
            return true;

        if (visited.contains(node))
            return false;

        visited.add(node);
        stack.add(node);

        for (CFGNode succ : node.getSuccessors()) {
            if (dfsCycle(succ, visited, stack))
                return true;
        }

        stack.remove(node);
        return false;
    }
}
