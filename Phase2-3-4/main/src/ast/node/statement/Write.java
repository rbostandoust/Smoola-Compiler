package ast.node.statement;

import ast.Type.ArrayType.ArrayType;
import ast.Type.PrimitiveType.StringType;
import ast.Visitor;
import ast.node.expression.ArrayCall;
import ast.node.expression.Expression;
import ast.node.expression.Identifier;
import ast.node.expression.Value.BooleanValue;
import ast.node.expression.Value.IntValue;
import ast.node.expression.Value.StringValue;

import java.util.ArrayList;

public class Write extends Statement {
    private Expression arg;

    public Write(Expression arg) {
        this.arg = arg;
    }

    public Expression getArg() {
        return arg;
    }

    public void setArg(Expression arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        return "Write";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public ArrayList<String> to_byte_code() {

        ArrayList<String> byte_code = new ArrayList<String>();
        byte_code.add("getstatic java/lang/System/out Ljava/io/PrintStream;");
        byte_code.addAll(arg.to_byte_code());
        if (!(arg.getType() instanceof ArrayType))
            byte_code.add("invokevirtual java/io/PrintStream/println("+ arg.getType().to_byte_code() + ")V");
        else {
            byte_code.add("invokestatic java/util/Arrays.toString([I)Ljava/lang/String;");
            byte_code.add("invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
        }
        return byte_code;
    }
}
