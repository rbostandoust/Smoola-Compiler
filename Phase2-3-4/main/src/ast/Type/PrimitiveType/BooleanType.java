package ast.Type.PrimitiveType;

import ast.Type.Type;

public class BooleanType extends Type {

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public String to_byte_code() {
        return "Z";
    }
}
