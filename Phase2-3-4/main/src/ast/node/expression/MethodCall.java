package ast.node.expression;

import ast.Type.Type;
import ast.Visitor;

import java.util.ArrayList;

public class MethodCall extends Expression {
    private Expression instance;
    private Identifier methodName;
    private ArrayList<Expression> args = new ArrayList<>();

    public MethodCall(Expression instance, Identifier methodName) {
        this.instance = instance;
        this.methodName = methodName;
    }

    public Expression getInstance() {
        return instance;
    }

    public void setInstance(Expression instance) {
        this.instance = instance;
    }

    public Identifier getMethodName() {
        return methodName;
    }

    public void setMethodName(Identifier methodName) {
        this.methodName = methodName;
    }

    public ArrayList<Expression> getArgs() {
        return args;
    }

    public void addArg(Expression arg) {
        this.args.add(arg);
    }

    @Override
    public String toString() {
        return "MethodCall";
    }

    @Override
    public ArrayList<String> to_byte_code() {
        ArrayList<String> byte_code = new ArrayList<String>();
        Type instance_type = instance.getType();

//        byte_code.add("new " + instance_type);
//        byte_code.add("dup");
        byte_code.addAll(instance.to_byte_code());
        String args_type = "";
        for(Expression arg : args){
            byte_code.addAll(arg.to_byte_code());
            args_type += arg.getType().to_byte_code();
        }
        byte_code.add("invokevirtual " + instance_type + "/" + methodName.getName() + "(" + args_type + ")" + methodName.getType().to_byte_code());
        return byte_code;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
