package ast.Type;

public class NoType extends Type{
    @Override
    public String toString() {
        return "NoType";
    }

    @Override
    public String to_byte_code() {
        return "N";
    }
}
