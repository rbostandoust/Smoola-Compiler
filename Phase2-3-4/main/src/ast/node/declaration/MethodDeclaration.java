package ast.node.declaration;

import ast.Type.ArrayType.ArrayType;
import ast.Type.PrimitiveType.BooleanType;
import ast.Type.PrimitiveType.IntType;
import ast.Type.PrimitiveType.StringType;
import ast.Type.Type;
import ast.Type.UserDefinedType.UserDefinedType;
import ast.Visitor;
import ast.node.expression.Expression;
import ast.node.expression.Identifier;
import ast.node.expression.Value.IntValue;
import ast.node.expression.Value.StringValue;
import ast.node.statement.Statement;

import java.util.ArrayList;

public class MethodDeclaration extends Declaration {
    private Expression returnValue;
    private Type returnType;
    private Identifier name;
    private ArrayList<VarDeclaration> args = new ArrayList<>();
    private ArrayList<VarDeclaration> localVars = new ArrayList<>();
    private ArrayList<Statement> body = new ArrayList<>();

    public MethodDeclaration(Identifier name) {
        this.name = name;
    }

    public Expression getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Expression returnValue) {
        this.returnValue = returnValue;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public Identifier getName() {
        return name;
    }

    public void setName(Identifier name) {
        this.name = name;
    }

    public ArrayList<VarDeclaration> getArgs() {
        return args;
    }

    public ArrayList<Type> getArgsType() {
        ArrayList<Type> argTypes = new ArrayList<>();
        for(VarDeclaration arg : args) {
            argTypes.add(arg.getType());
        }
        return argTypes;
    }

    public void addArg(VarDeclaration arg) {
        this.args.add(arg);
    }

    public ArrayList<Statement> getBody() {
        return body;
    }

    public void addStatement(Statement statement) {
        this.body.add(statement);
    }

    public void addStatements(ArrayList <Statement> body) {
        this.body = body;
    }

    public ArrayList<VarDeclaration> getLocalVars() {
        return localVars;
    }

    public void addLocalVar(VarDeclaration localVar) {
        this.localVars.add(localVar);
    }

    @Override
    public String toString() {
        return "MethodDeclaration";
    }

    @Override
    public ArrayList<String> to_byte_code() {

        ArrayList<String> byte_code = new ArrayList<String>();
        String arg_types = "";

        for(VarDeclaration var : this.args){
            Type var_type = var.getType();
            arg_types += var_type.to_byte_code();
        }
        byte_code.add(".method public " + this.name.getName() + "(" + arg_types + ")" + returnType.to_byte_code());

        int variables_count = this.args.size() + this.localVars.size();
        byte_code.add(".limit stack " + Integer.toString(variables_count + 20));
        byte_code.add(".limit locals " + Integer.toString(variables_count + 2));

        return byte_code;

    }

    public ArrayList<String> return_byte_code() {

        ArrayList<String> byte_code = new ArrayList<String>();
        byte_code.addAll(returnValue.to_byte_code());
        if(returnType instanceof IntType ||returnType instanceof BooleanType)
            byte_code.add("ireturn");
        else
            byte_code.add("areturn");

        byte_code.add(".end method");
        return byte_code;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
