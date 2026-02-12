public class CFGPrinter {

    public static void print(CFG cfg) {

        for (CFGNode node : cfg.getNodes()) {
            System.out.print("Node " + node.getId() + " (" + node.getLabel() + ") -> ");

            for (CFGNode succ : node.getSuccessors()) {
                System.out.print(succ.getId() + " ");
            }

            System.out.println();
        }
    }
}
