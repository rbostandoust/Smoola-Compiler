package ast.Type.ArrayType;

import ast.Type.Type;

public class ArrayType extends Type {
    private int size;

    public ArrayType() { }
    @Override
    public String toString() {
        return "int[]";
    }

    @Override
    public String to_byte_code() {
        return "[I";
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
