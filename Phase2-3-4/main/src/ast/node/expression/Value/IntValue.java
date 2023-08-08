package ast.node.expression.Value;

import ast.Type.Type;
import ast.Visitor;

import java.util.ArrayList;

public class IntValue extends Value {
    private int constant;

    public IntValue(int constant, Type type) {
        this.constant = constant;
        this.type = type;
    }

    public int getConstant() {
        return constant;
    }

    public void setConstant(int constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return "IntValue " + constant;
    }

    @Override
    public ArrayList<String> to_byte_code() {
        ArrayList<String> byte_code = new ArrayList<String>();
        byte_code.add("ldc " + Integer.toString(constant));
        return byte_code;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
