import org.antlr.v4.runtime.tree.*;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;

public class ASTBuilder extends ExprBaseVisitor<ASTNode> {

    // ===============================
    // PROGRAM
    // ===============================

    @Override
    public ASTNode visitProg(ExprParser.ProgContext ctx) {
        ASTNode root = new ASTNode(ASTType.PROGRAM);
        for (ParseTree child : ctx.children) {
            ASTNode n = visit(child);
            if (n != null) root.add(n);
        }
        return root;
    }

    // ===============================
    // FUNCTION
    // ===============================

    @Override
    public ASTNode visitFunctionDecl(ExprParser.FunctionDeclContext ctx) {
        ASTNode func = new ASTNode(ASTType.FUNCTION, "FUNC");

        if (ctx.paramList() != null) {
            for (ExprParser.ParamContext p : ctx.paramList().param()) {
                func.add(new ASTNode(ASTType.PARAM, "VAR"));
            }
        }

        func.add(visit(ctx.block()));
        return func;
    }

    @Override
    public ASTNode visitBlock(ExprParser.BlockContext ctx) {
        ASTNode block = new ASTNode(ASTType.BLOCK);
        for (ExprParser.StatContext s : ctx.stat()) {
            block.add(visit(s));
        }
        return block;
    }

    // ===============================
    // STATEMENTS
    // ===============================

    @Override
    public ASTNode visitVarDecl(ExprParser.VarDeclContext ctx) {
        ASTNode v = new ASTNode(ASTType.VAR_DECL);
        if (ctx.expr() != null) {
            v.add(visit(ctx.expr()));
        }
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
    public ASTNode visitReturnStat(ExprParser.ReturnStatContext ctx) {
        ASTNode r = new ASTNode(ASTType.RETURN);
        if (ctx.expr() != null)
            r.add(visit(ctx.expr()));
        return r;
    }

    @Override
    public ASTNode visitIfStat(ExprParser.IfStatContext ctx) {
        ASTNode i = new ASTNode(ASTType.IF);
        i.add(visit(ctx.expr()));          // condition
        i.add(visit(ctx.stat(0)));         // then
        if (ctx.stat().size() > 1)
            i.add(visit(ctx.stat(1)));     // else
        return i;
    }

    @Override
    public ASTNode visitWhileStat(ExprParser.WhileStatContext ctx) {
        ASTNode w = new ASTNode(ASTType.WHILE);
        w.add(visit(ctx.expr()));          // condition
        w.add(visit(ctx.stat()));          // body
        return w;
    }

    // ===============================
    // ✅ FIXED FOR STATEMENT
    // ===============================

    @Override
    public ASTNode visitForStat(ExprParser.ForStatContext ctx) {

        ASTNode f = new ASTNode(ASTType.FOR);

        // =========================
        // INIT
        // =========================

        ASTNode initNode;

        if (ctx.varDecl() != null) {
            initNode = visit(ctx.varDecl());
        }
        else if (ctx.assignStat() != null) {
            initNode = visit(ctx.assignStat());
        }
        else {
            initNode = new ASTNode(ASTType.EMPTY);
        }

        f.add(initNode);

        // =========================
        // CONDITION
        // =========================

        ASTNode conditionNode;

        if (ctx.expr() != null) {
            conditionNode = visit(ctx.expr());
        } else {
            conditionNode = new ASTNode(ASTType.CONSTANT, "TRUE");
        }

        f.add(conditionNode);

        // =========================
        // UPDATE
        // =========================

        ASTNode updateNode;

        if (ctx.forUpdate() != null) {
            updateNode = visit(ctx.forUpdate());
        } else {
            updateNode = new ASTNode(ASTType.EMPTY);
        }

        f.add(updateNode);

        // =========================
        // BODY
        // =========================

        f.add(visit(ctx.stat()));

        return f;
    }

    @Override
    public ASTNode visitForUpdate(ExprParser.ForUpdateContext ctx) {

        ASTNode assign = new ASTNode(ASTType.ASSIGN);

        // Left side (ID)
        assign.add(new ASTNode(ASTType.IDENTIFIER, "VAR"));

        // Right side (expression)
        assign.add(visit(ctx.expr()));

        return assign;
    }



    // ===============================
    // EXPRESSIONS
    // ===============================

    @Override
    public ASTNode visitAssignmentExpr(ExprParser.AssignmentExprContext ctx) {

        if (ctx.ASSIGN() != null) {
            ASTNode assign = new ASTNode(ASTType.ASSIGN);
            assign.add(new ASTNode(ASTType.IDENTIFIER, "VAR"));
            assign.add(visit(ctx.assignmentExpr()));
            return assign;
        }

        return visit(ctx.logicalOrExpr());
    }

    @Override
    public ASTNode visitLogicalOrExpr(ExprParser.LogicalOrExprContext ctx) {
        return buildLeftAssociative(
                ctx.logicalAndExpr(),
                ctx.OR()
        );
    }

    @Override
    public ASTNode visitLogicalAndExpr(ExprParser.LogicalAndExprContext ctx) {
        return buildLeftAssociative(
                ctx.equalityExpr(),
                ctx.AND()
        );
    }

    @Override
    public ASTNode visitEqualityExpr(ExprParser.EqualityExprContext ctx) {
        return buildLeftAssociative(
                ctx.relationalExpr(),
                ctx.EQ(),
                ctx.NEQ()
        );
    }

    @Override
    public ASTNode visitRelationalExpr(ExprParser.RelationalExprContext ctx) {
        return buildLeftAssociative(
                ctx.additiveExpr(),
                ctx.GT(),
                ctx.LT(),
                ctx.GE(),
                ctx.LE()
        );
    }

    @Override
    public ASTNode visitAdditiveExpr(ExprParser.AdditiveExprContext ctx) {
        return buildLeftAssociative(
                ctx.multiplicativeExpr(),
                ctx.PLUS(),
                ctx.MINUS()
        );
    }

    @Override
    public ASTNode visitMultiplicativeExpr(ExprParser.MultiplicativeExprContext ctx) {
        return buildLeftAssociative(
                ctx.unaryExpr(),
                ctx.STAR(),
                ctx.DIV()
        );
    }

    @Override
    public ASTNode visitUnaryExpr(ExprParser.UnaryExprContext ctx) {

        if (ctx.unaryExpr() != null) {
            ASTNode u = new ASTNode(ASTType.UNARY_OP,
                    ctx.getChild(0).getText());
            u.add(visit(ctx.unaryExpr()));
            return u;
        }

        return visit(ctx.postfixExpr());
    }

    @Override
    public ASTNode visitPrimaryExpr(ExprParser.PrimaryExprContext ctx) {

        if (ctx.ID() != null)
            return new ASTNode(ASTType.IDENTIFIER, "VAR");

        if (ctx.NUMBER() != null ||
            ctx.FLOAT() != null ||
            ctx.CHAR_LITERAL() != null)
            return new ASTNode(ASTType.CONSTANT, "CONST");

        return visit(ctx.expr());
    }

    // ===============================
    // HELPER
    // ===============================

    private ASTNode buildLeftAssociative(
            List<? extends ParserRuleContext> operands,
            List<? extends TerminalNode>... operators) {

        if (operands.size() == 1)
            return visit(operands.get(0));

        ASTNode left = visit(operands.get(0));
        int opIndex = 0;

        for (List<? extends TerminalNode> opList : operators) {
            for (TerminalNode op : opList) {

                ASTNode bin = new ASTNode(ASTType.BIN_OP, op.getText());
                bin.add(left);
                bin.add(visit(operands.get(++opIndex)));

                left = bin;
            }
        }

        return left;
    }

    @Override
    public ASTNode visitPostfixExpr(ExprParser.PostfixExprContext ctx) {

        if (ctx.LPAREN().isEmpty()) {
            return visit(ctx.primaryExpr());
        }

        ASTNode base = visit(ctx.primaryExpr());

        for (int i = 0; i < ctx.LPAREN().size(); i++) {

            ASTNode call = new ASTNode(ASTType.FUNCTION_CALL);
            call.add(base);

            if (i < ctx.argumentList().size()) {
                ExprParser.ArgumentListContext argListCtx = ctx.argumentList(i);
                for (ExprParser.ExprContext arg : argListCtx.expr()) {
                    call.add(visit(arg));
                }
            }

            base = call;
        }

        return base;
    }
}
