package ast.Type.UserDefinedType;

import ast.Type.Type;
import ast.node.declaration.ClassDeclaration;
import ast.node.expression.Identifier;

import java.util.ArrayList;

public class UserDefinedType extends Type {
    private ClassDeclaration classDeclaration;
    private Identifier name;

    public UserDefinedType() {

    }

    public UserDefinedType(Identifier name){
      this.name = name;
    }

    public UserDefinedType(Identifier name, ClassDeclaration classDeclaration){
        this.name = name;
        this.classDeclaration = classDeclaration;
    }

    public ClassDeclaration getClassDeclaration() {
        return classDeclaration;
    }

    public void setClassDeclaration(ClassDeclaration classDeclaration) {
        this.classDeclaration = classDeclaration;
    }

    public Identifier getName() {
        return name;
    }

    public void setName(Identifier name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return classDeclaration.getName().getName();
    }

    @Override
    public String to_byte_code() {

        return "L" + name.getName() + ";";
    }
}
