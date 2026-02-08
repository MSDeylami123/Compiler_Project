import org.antlr.v4.runtime.tree.*;

public class ASTBuilder extends ExprBaseVisitor<ASTNode> {

    @Override
    public ASTNode visitProg(ExprParser.ProgContext ctx) {
        ASTNode root = new ASTNode(ASTType.PROGRAM);
        for (ParseTree child : ctx.children) {
            ASTNode n = visit(child);
            if (n != null) root.add(n);
        }
        return root;
    }

    @Override
    public ASTNode visitFunctionDecl(ExprParser.FunctionDeclContext ctx) {
        ASTNode func = new ASTNode(ASTType.FUNCTION, "FUNC");

        if (ctx.paramList() != null) {
            for (ExprParser.ParamContext p : ctx.paramList().param())
                func.add(new ASTNode(ASTType.PARAM, "VAR"));
        }

        func.add(visit(ctx.block()));
        return func;
    }

    @Override
    public ASTNode visitBlock(ExprParser.BlockContext ctx) {
        ASTNode block = new ASTNode(ASTType.BLOCK);
        for (ExprParser.StatContext s : ctx.stat())
            block.add(visit(s));
        return block;
    }

    @Override
    public ASTNode visitVarDecl(ExprParser.VarDeclContext ctx) {
        ASTNode v = new ASTNode(ASTType.VAR_DECL);
        if (ctx.expr() != null)
            v.add(visit(ctx.expr()));
        return v;
    }

    @Override
    public ASTNode visitAssignStat(ExprParser.AssignStatContext ctx) {
        ASTNode a = new ASTNode(ASTType.ASSIGN);
        a.add(new ASTNode(ASTType.IDENTIFIER, "VAR"));
        a.add(visit(ctx.expr()));
        return a;
    }

    @Override
    public ASTNode visitIfStat(ExprParser.IfStatContext ctx) {
        ASTNode i = new ASTNode(ASTType.IF);
        i.add(visit(ctx.expr()));
        i.add(visit(ctx.stat(0)));
        if (ctx.stat().size() > 1)
            i.add(visit(ctx.stat(1)));
        return i;
    }

    @Override
    public ASTNode visitWhileStat(ExprParser.WhileStatContext ctx) {
        ASTNode w = new ASTNode(ASTType.WHILE);
        w.add(visit(ctx.expr()));
        w.add(visit(ctx.stat()));
        return w;
    }

    @Override
    public ASTNode visitForStat(ExprParser.ForStatContext ctx) {
        ASTNode f = new ASTNode(ASTType.FOR);
        if (ctx.expr() != null)
            f.add(visit(ctx.expr()));
        f.add(visit(ctx.stat()));
        return f;
    }

    @Override
    public ASTNode visitReturnStat(ExprParser.ReturnStatContext ctx) {
        ASTNode r = new ASTNode(ASTType.RETURN);
        if (ctx.expr() != null)
            r.add(visit(ctx.expr()));
        return r;
    }

    @Override
    public ASTNode visitPrimaryExpr(ExprParser.PrimaryExprContext ctx) {
        if (ctx.ID() != null)
            return new ASTNode(ASTType.IDENTIFIER, "VAR");
        return new ASTNode(ASTType.CONSTANT, "CONST");
    }
}
