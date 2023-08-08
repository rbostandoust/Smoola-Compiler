package ast.node.expression;

import ast.Visitor;

import java.util.ArrayList;

public class UnaryExpression extends Expression {

    private UnaryOperator unaryOperator;
    private Expression value;

    public UnaryExpression(UnaryOperator unaryOperator, Expression value) {
        this.unaryOperator = unaryOperator;
        this.value = value;
    }

    public Expression getValue() {
        return value;
    }

    public void setValue(Expression value) {
        this.value = value;
    }

    public UnaryOperator getUnaryOperator() {
        return unaryOperator;
    }

    public void setUnaryOperator(UnaryOperator unaryOperator) {
        this.unaryOperator = unaryOperator;
    }

    @Override
    public String toString() {
        return "UnaryExpression " + unaryOperator.name();
    }

    @Override
    public ArrayList<String> to_byte_code() {
        ArrayList<String> byte_code = new ArrayList<String>();

        if(unaryOperator.equals(UnaryOperator.minus)) {
            byte_code.addAll(value.to_byte_code());
            byte_code.add("ineg");
        }else if(unaryOperator.equals(UnaryOperator.not)){
            byte_code.add("ldc 1");
            byte_code.addAll(value.to_byte_code());
            byte_code.add("isub");
        }
        return byte_code;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

