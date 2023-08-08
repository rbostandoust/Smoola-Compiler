package ast.node.expression.Value;

import ast.Type.Type;
import ast.Visitor;

import java.util.ArrayList;

public class StringValue extends Value {
    private String constant;

    public StringValue(String constant, Type type) {
        this.constant = constant;
        this.type = type;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return "StringValue " + constant;
    }

    @Override
    public ArrayList<String> to_byte_code() {
        ArrayList<String> byte_code = new ArrayList<String>();
        byte_code.add("ldc " + constant);
        return byte_code;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
