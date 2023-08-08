package ast.node.expression.Value;

import ast.Type.Type;
import ast.Visitor;

import java.util.ArrayList;

public class BooleanValue extends Value {
    private boolean constant;

    public BooleanValue(boolean constant, Type type) {
        this.constant = constant;
        this.type = type;
    }
    public int get_constant(){
        return constant ? 1 : 0;
    }
    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return "BooleanValue " + constant;
    }

    @Override
    public ArrayList<String> to_byte_code() {
        ArrayList<String> byte_code = new ArrayList<String>();
        byte_code.add("ldc " + Integer.toString(get_constant()));
        return byte_code;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
