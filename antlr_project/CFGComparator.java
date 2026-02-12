import java.util.*;

public class CFGComparator {

    public double compare(CFG cfg1, CFG cfg2) {

        double nodeScore = compareNodeCount(cfg1, cfg2);
        double edgeScore = compareEdgeCount(cfg1, cfg2);
        double labelScore = compareLabelFrequency(cfg1, cfg2);
        double cycleScore = compareCycles(cfg1, cfg2);
        double pathScore = comparePaths(cfg1, cfg2); // NEW

        // Behavioral similarity weighted highest
        return (
                nodeScore * 0.15 +
                edgeScore * 0.15 +
                labelScore * 0.2 +
                cycleScore * 0.1 +
                pathScore * 0.4
        );
    }

    // =========================================================
    // 1️⃣ Node Count Similarity
    // =========================================================
    private double compareNodeCount(CFG c1, CFG c2) {
        int n1 = c1.getNodes().size();
        int n2 = c2.getNodes().size();

        if (Math.max(n1, n2) == 0) return 1.0;

        return 1.0 - (Math.abs(n1 - n2) / (double) Math.max(n1, n2));
    }

    // =========================================================
    // 2️⃣ Edge Count Similarity
    // =========================================================
    private double compareEdgeCount(CFG c1, CFG c2) {
        int e1 = countEdges(c1);
        int e2 = countEdges(c2);

        if (Math.max(e1, e2) == 0) return 1.0;

        return 1.0 - (Math.abs(e1 - e2) / (double) Math.max(e1, e2));
    }

    private int countEdges(CFG cfg) {
        int count = 0;
        for (CFGNode node : cfg.getNodes())
            count += node.getSuccessors().size();
        return count;
    }

    // =========================================================
    // 3️⃣ Label Frequency Similarity
    // =========================================================
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

    // =========================================================
    // 4️⃣ Cycle Detection
    // =========================================================
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

    // =========================================================
    // 5️⃣ NEW: Execution Path Similarity (Behavioral)
    // =========================================================
    private double comparePaths(CFG c1, CFG c2) {

        Set<String> paths1 = extractPaths(c1);
        Set<String> paths2 = extractPaths(c2);

        if (paths1.isEmpty() && paths2.isEmpty())
            return 1.0;

        Set<String> intersection = new HashSet<>(paths1);
        intersection.retainAll(paths2);

        Set<String> union = new HashSet<>(paths1);
        union.addAll(paths2);

        return intersection.size() / (double) union.size();
    }

    private Set<String> extractPaths(CFG cfg) {
        Set<String> paths = new HashSet<>();
        List<String> currentPath = new ArrayList<>();

        dfsPaths(cfg.getEntry(),
                 cfg.getExit(),
                 currentPath,
                 paths,
                 0);

        return paths;
    }

    private void dfsPaths(CFGNode node,
                          CFGNode exit,
                          List<String> currentPath,
                          Set<String> paths,
                          int depth) {

        // Prevent infinite loop explosion
        if (depth > 20) return;

        currentPath.add(node.getLabel());

        if (node == exit) {
            paths.add(String.join("->", currentPath));
            return;
        }

        for (CFGNode succ : node.getSuccessors()) {
            dfsPaths(succ,
                     exit,
                     new ArrayList<>(currentPath),
                     paths,
                     depth + 1);
        }
    }
}
