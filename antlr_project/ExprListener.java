// Generated from Expr.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExprParser}.
 */
public interface ExprListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ExprParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(ExprParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(ExprParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#externalDecl}.
	 * @param ctx the parse tree
	 */
	void enterExternalDecl(ExprParser.ExternalDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#externalDecl}.
	 * @param ctx the parse tree
	 */
	void exitExternalDecl(ExprParser.ExternalDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#functionDecl}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDecl(ExprParser.FunctionDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#functionDecl}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDecl(ExprParser.FunctionDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamList(ExprParser.ParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamList(ExprParser.ParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(ExprParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(ExprParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStat(ExprParser.StatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStat(ExprParser.StatContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(ExprParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(ExprParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(ExprParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(ExprParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#assignStat}.
	 * @param ctx the parse tree
	 */
	void enterAssignStat(ExprParser.AssignStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#assignStat}.
	 * @param ctx the parse tree
	 */
	void exitAssignStat(ExprParser.AssignStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#returnStat}.
	 * @param ctx the parse tree
	 */
	void enterReturnStat(ExprParser.ReturnStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#returnStat}.
	 * @param ctx the parse tree
	 */
	void exitReturnStat(ExprParser.ReturnStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#ifStat}.
	 * @param ctx the parse tree
	 */
	void enterIfStat(ExprParser.IfStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#ifStat}.
	 * @param ctx the parse tree
	 */
	void exitIfStat(ExprParser.IfStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#whileStat}.
	 * @param ctx the parse tree
	 */
	void enterWhileStat(ExprParser.WhileStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#whileStat}.
	 * @param ctx the parse tree
	 */
	void exitWhileStat(ExprParser.WhileStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void enterForUpdate(ExprParser.ForUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void exitForUpdate(ExprParser.ForUpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#forStat}.
	 * @param ctx the parse tree
	 */
	void enterForStat(ExprParser.ForStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#forStat}.
	 * @param ctx the parse tree
	 */
	void exitForStat(ExprParser.ForStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(ExprParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(ExprParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#exprStat}.
	 * @param ctx the parse tree
	 */
	void enterExprStat(ExprParser.ExprStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#exprStat}.
	 * @param ctx the parse tree
	 */
	void exitExprStat(ExprParser.ExprStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(ExprParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(ExprParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#assignmentExpr}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentExpr(ExprParser.AssignmentExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#assignmentExpr}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentExpr(ExprParser.AssignmentExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#logicalOrExpr}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOrExpr(ExprParser.LogicalOrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#logicalOrExpr}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOrExpr(ExprParser.LogicalOrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#logicalAndExpr}.
	 * @param ctx the parse tree
	 */
	void enterLogicalAndExpr(ExprParser.LogicalAndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#logicalAndExpr}.
	 * @param ctx the parse tree
	 */
	void exitLogicalAndExpr(ExprParser.LogicalAndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#equalityExpr}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpr(ExprParser.EqualityExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#equalityExpr}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpr(ExprParser.EqualityExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#relationalExpr}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpr(ExprParser.RelationalExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#relationalExpr}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpr(ExprParser.RelationalExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#additiveExpr}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpr(ExprParser.AdditiveExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#additiveExpr}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpr(ExprParser.AdditiveExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#multiplicativeExpr}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpr(ExprParser.MultiplicativeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#multiplicativeExpr}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpr(ExprParser.MultiplicativeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#unaryExpr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(ExprParser.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#unaryExpr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(ExprParser.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#postfixExpr}.
	 * @param ctx the parse tree
	 */
	void enterPostfixExpr(ExprParser.PostfixExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#postfixExpr}.
	 * @param ctx the parse tree
	 */
	void exitPostfixExpr(ExprParser.PostfixExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#primaryExpr}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpr(ExprParser.PrimaryExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#primaryExpr}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpr(ExprParser.PrimaryExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(ExprParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(ExprParser.ArgumentListContext ctx);
}