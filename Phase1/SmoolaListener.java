// Generated from Smoola.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SmoolaParser}.
 */
public interface SmoolaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(SmoolaParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(SmoolaParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#returnExpression}.
	 * @param ctx the parse tree
	 */
	void enterReturnExpression(SmoolaParser.ReturnExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#returnExpression}.
	 * @param ctx the parse tree
	 */
	void exitReturnExpression(SmoolaParser.ReturnExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#returning}.
	 * @param ctx the parse tree
	 */
	void enterReturning(SmoolaParser.ReturningContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#returning}.
	 * @param ctx the parse tree
	 */
	void exitReturning(SmoolaParser.ReturningContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#method}.
	 * @param ctx the parse tree
	 */
	void enterMethod(SmoolaParser.MethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#method}.
	 * @param ctx the parse tree
	 */
	void exitMethod(SmoolaParser.MethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(SmoolaParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(SmoolaParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(SmoolaParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(SmoolaParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#classes}.
	 * @param ctx the parse tree
	 */
	void enterClasses(SmoolaParser.ClassesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#classes}.
	 * @param ctx the parse tree
	 */
	void exitClasses(SmoolaParser.ClassesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#loop}.
	 * @param ctx the parse tree
	 */
	void enterLoop(SmoolaParser.LoopContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#loop}.
	 * @param ctx the parse tree
	 */
	void exitLoop(SmoolaParser.LoopContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#if_then}.
	 * @param ctx the parse tree
	 */
	void enterIf_then(SmoolaParser.If_thenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#if_then}.
	 * @param ctx the parse tree
	 */
	void exitIf_then(SmoolaParser.If_thenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(SmoolaParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(SmoolaParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(SmoolaParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(SmoolaParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#leftMostEXP}.
	 * @param ctx the parse tree
	 */
	void enterLeftMostEXP(SmoolaParser.LeftMostEXPContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#leftMostEXP}.
	 * @param ctx the parse tree
	 */
	void exitLeftMostEXP(SmoolaParser.LeftMostEXPContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#var_def}.
	 * @param ctx the parse tree
	 */
	void enterVar_def(SmoolaParser.Var_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#var_def}.
	 * @param ctx the parse tree
	 */
	void exitVar_def(SmoolaParser.Var_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#var_assign}.
	 * @param ctx the parse tree
	 */
	void enterVar_assign(SmoolaParser.Var_assignContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#var_assign}.
	 * @param ctx the parse tree
	 */
	void exitVar_assign(SmoolaParser.Var_assignContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#primitive}.
	 * @param ctx the parse tree
	 */
	void enterPrimitive(SmoolaParser.PrimitiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#primitive}.
	 * @param ctx the parse tree
	 */
	void exitPrimitive(SmoolaParser.PrimitiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link SmoolaParser#nonprimitive}.
	 * @param ctx the parse tree
	 */
	void enterNonprimitive(SmoolaParser.NonprimitiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link SmoolaParser#nonprimitive}.
	 * @param ctx the parse tree
	 */
	void exitNonprimitive(SmoolaParser.NonprimitiveContext ctx);
}