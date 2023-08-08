package ast.node.statement;

import ast.Visitor;
import ast.node.expression.Expression;

import java.util.ArrayList;

public class While extends Statement {
    private Expression condition;
    private Statement body;
    private static int lable_index = 0;

    public While(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public Statement getBody() {
        return body;
    }

    public void setBody(Statement body) {
        this.body = body;
    }

    @Override
    public ArrayList<String> to_byte_code() {
        ArrayList<String> byte_code = new ArrayList<String>();
        byte_code.add("BEGIN_WHILE_" + Integer.toString(lable_index) + " :");
        byte_code.addAll(condition.to_byte_code());
        byte_code.add("ifeq END_WHILE_" + Integer.toString(lable_index));
        byte_code.addAll(body.to_byte_code());
        byte_code.add("goto BEGIN_WHILE_" + Integer.toString(lable_index));
        byte_code.add("END_WHILE_" + Integer.toString(lable_index) + " :");
        lable_index ++;
        return byte_code;
    }

    @Override
    public String toString() {
        return "While";
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
