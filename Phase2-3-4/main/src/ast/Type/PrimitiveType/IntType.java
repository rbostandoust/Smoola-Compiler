package ast.Type.PrimitiveType;

import ast.Type.Type;

public class IntType extends Type {
    @Override
    public String toString() {
        return "int";
    }

    @Override
    public String to_byte_code() {
        return "I";
    }
}
