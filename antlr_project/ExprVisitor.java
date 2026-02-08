// Generated from Expr.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ExprParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ExprVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ExprParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(ExprParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#externalDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternalDecl(ExprParser.ExternalDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#functionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDecl(ExprParser.FunctionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#paramList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamList(ExprParser.ParamListContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(ExprParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStat(ExprParser.StatContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(ExprParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(ExprParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#assignStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStat(ExprParser.AssignStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#returnStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStat(ExprParser.ReturnStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#ifStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStat(ExprParser.IfStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#whileStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStat(ExprParser.WhileStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#forUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForUpdate(ExprParser.ForUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#forStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStat(ExprParser.ForStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(ExprParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#exprStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprStat(ExprParser.ExprStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(ExprParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#assignmentExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentExpr(ExprParser.AssignmentExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#logicalOrExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOrExpr(ExprParser.LogicalOrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#logicalAndExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndExpr(ExprParser.LogicalAndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#equalityExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpr(ExprParser.EqualityExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#relationalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpr(ExprParser.RelationalExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#additiveExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpr(ExprParser.AdditiveExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#multiplicativeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpr(ExprParser.MultiplicativeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#unaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr(ExprParser.UnaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#postfixExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixExpr(ExprParser.PostfixExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpr(ExprParser.PrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(ExprParser.ArgumentListContext ctx);
}