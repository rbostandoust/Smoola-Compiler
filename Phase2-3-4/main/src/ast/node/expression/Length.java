package ast.node.expression;

import ast.Visitor;

import java.util.ArrayList;

public class Length extends Expression {
    private Expression expression;

    public Length(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "Length";
    }

    @Override
    public ArrayList<String> to_byte_code() {
        ArrayList<String> byte_code = new ArrayList<String>();
        byte_code.addAll(expression.to_byte_code());
        byte_code.add("arraylength");
        return byte_code;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
