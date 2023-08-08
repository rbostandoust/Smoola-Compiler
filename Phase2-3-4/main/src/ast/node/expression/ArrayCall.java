package ast.node.expression;

import ast.Visitor;

import java.util.ArrayList;

public class ArrayCall extends Expression {
    private Expression instance;
    private Expression index;

    public ArrayCall(Expression instance, Expression index) {
        this.instance = instance;
        this.index = index;
    }
    public ArrayCall(Expression instance, Expression index, int line) {
        this.instance = instance;
        this.index = index;
        this.line = line;
    }


    public Expression getInstance() {
        return instance;
    }

    public void setInstance(Expression instance) {
        this.instance = instance;
    }

    public Expression getIndex() {
        return index;
    }

    public void setIndex(Expression index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "ArrayCall";
    }

    @Override
    public ArrayList<String> to_byte_code() {
        return to_byte_code("right");
    }

    public ArrayList<String> to_byte_code(String right_or_left) {
        ArrayList<String> byte_code = new ArrayList<String>();
        byte_code.addAll(instance.to_byte_code());
        byte_code.addAll(index.to_byte_code());
        if(right_or_left.equals("right"))
            byte_code.add("iaload");
        else { }
        return byte_code;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
