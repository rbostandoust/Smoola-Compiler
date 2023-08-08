package ast.node.declaration;

import ast.Type.PrimitiveType.BooleanType;
import ast.Type.PrimitiveType.IntType;
import ast.Type.PrimitiveType.StringType;
import ast.Type.Type;
import ast.Visitor;
import ast.node.expression.Identifier;

import java.util.ArrayList;

public class VarDeclaration extends Declaration {
    private Identifier identifier;
    private Type type;
    private int index;
    public VarDeclaration(Identifier identifier, Type type) {
        this.identifier = identifier;
        this.type = type;
    }
    public VarDeclaration(Identifier identifier, Type type, int i) {
          this.identifier = identifier;
          this.type = type;
          this.index = i;
    }
    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "VarDeclaration";
    }

    @Override
    public ArrayList<String> to_byte_code() {

        ArrayList<String> byte_code = new ArrayList<String>();

        if (type instanceof IntType)
            byte_code.add(".field protected " + this.identifier.getName() + " " + this.type.to_byte_code() + " = 0");
        else if (type instanceof StringType)
            byte_code.add(".field protected " + this.identifier.getName() + " " + this.type.to_byte_code() + " = \" \"");
        else if (type instanceof BooleanType)
            byte_code.add(".field protected " + this.identifier.getName() + " " + this.type.to_byte_code() + " = 0");
        else
            byte_code.add(".field protected " + this.identifier.getName() + " " + this.type.to_byte_code());
        return byte_code;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
