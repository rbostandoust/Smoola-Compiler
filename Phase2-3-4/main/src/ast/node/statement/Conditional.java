package ast.node.statement;

import ast.Visitor;
import ast.node.expression.ArrayCall;
import ast.node.expression.Expression;

import java.util.ArrayList;

public class Conditional extends Statement {
    private Expression expression;
    private Statement consequenceBody;
    private Statement alternativeBody;
    private static int lable_index = 0;

    public Conditional(Expression expression, Statement consequenceBody, Statement alternativeBody) {
        this.expression = expression;
        this.consequenceBody = consequenceBody;
        this.alternativeBody = alternativeBody;
    }
    public Conditional(Expression expression, Statement consequenceBody) {
        this.expression = expression;
        this.consequenceBody = consequenceBody;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Statement getConsequenceBody() {
        return consequenceBody;
    }

    public void setConsequenceBody(Statement consequenceBody) {
        this.consequenceBody = consequenceBody;
    }

    public Statement getAlternativeBody() {
        return alternativeBody;
    }

    public void setAlternativeBody(Statement alternativeBody) {
        this.alternativeBody = alternativeBody;
    }

    @Override
    public ArrayList<String> to_byte_code() {
        ArrayList<String> byte_code = new ArrayList<String>(expression.to_byte_code());
        byte_code.add("ifeq ELSE_IF_" + Integer.toString(lable_index));
        byte_code.addAll(consequenceBody.to_byte_code());
        if(alternativeBody != null) {
            byte_code.add("goto END_IF_" + Integer.toString(lable_index));
            byte_code.add("ELSE_IF_" + Integer.toString(lable_index) + " :");
            byte_code.addAll(alternativeBody.to_byte_code());
            byte_code.add("END_IF_" + Integer.toString(lable_index) + " :");
        }else{
            byte_code.add("ELSE_IF_" + Integer.toString(lable_index) + " :");
        }
        lable_index++;
        return byte_code;
    }

    @Override
    public String toString() {
        return "Conditional";
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
