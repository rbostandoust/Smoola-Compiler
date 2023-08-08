package ast.node.expression;
import ast.Type.PrimitiveType.BooleanType;
import ast.Type.PrimitiveType.IntType;
import ast.Visitor;
import ast.node.statement.Assign;
import symbolTable.SymbolTable;

import java.util.ArrayList;

public class BinaryExpression extends Expression {

    private Expression left;
    private Expression right;
    private BinaryOperator binaryOperator;
    private static int lable_index = 0;

    public BinaryExpression(Expression left, Expression right, BinaryOperator binaryOperator) {
        this.left = left;
        this.right = right;
        this.binaryOperator = binaryOperator;
    }

    public Expression getLeft() {
        return left;
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    public BinaryOperator getBinaryOperator() {
        return binaryOperator;
    }

    public void setBinaryOperator(BinaryOperator binaryOperator) {
        this.binaryOperator = binaryOperator;
    }

    @Override
    public String toString() {
        return "BinaryExpression " + binaryOperator.name();
    }

    @Override
    public ArrayList<String> to_byte_code() {
        ArrayList<String> byte_code = new ArrayList<String>();
        if(binaryOperator.equals(BinaryOperator.add) || binaryOperator.equals(BinaryOperator.sub)
                || binaryOperator.equals(BinaryOperator.mult) || binaryOperator.equals(BinaryOperator.div)) {
            byte_code.addAll(left.to_byte_code());
            byte_code.addAll(right.to_byte_code());
            switch (binaryOperator){
                case add:
                    byte_code.add("iadd");
                    break;
                case sub:
                    byte_code.add("isub");
                    break;
                case mult:
                    byte_code.add("imul");
                    break;
                case div:
                    byte_code.add("idiv");
                    break;
            }
        } else if(binaryOperator.equals(BinaryOperator.gt) || binaryOperator.equals(BinaryOperator.lt)) {
            byte_code.addAll(left.to_byte_code());
            byte_code.addAll(right.to_byte_code());
            if(binaryOperator.equals(BinaryOperator.lt)){
                byte_code.add("swap");
            }
            byte_code.add("if_icmple ELSE" + Integer.toString(lable_index));
            byte_code.add("ldc 1");
            byte_code.add("goto END" + Integer.toString(lable_index));
            byte_code.add("ELSE" + Integer.toString(lable_index) + " :");
            byte_code.add("ldc 0");
            byte_code.add("END" + Integer.toString(lable_index) + " :");
            lable_index ++;
        } else if(binaryOperator.equals(BinaryOperator.or)){
            byte_code.addAll(left.to_byte_code());
            byte_code.add("ifne ELSE" + Integer.toString(lable_index));
            byte_code.addAll(right.to_byte_code());
            byte_code.add("ifne ELSE" + Integer.toString(lable_index));
            byte_code.add("ldc 0");
            byte_code.add("goto END" + Integer.toString(lable_index));
            byte_code.add("ELSE" + Integer.toString(lable_index) + " :");
            byte_code.add("ldc 1");
            byte_code.add("END" + Integer.toString(lable_index) + " :");
            lable_index ++;
        } else if(binaryOperator.equals(BinaryOperator.and)){
            byte_code.addAll(left.to_byte_code());
            byte_code.add("ifeq ELSE" + Integer.toString(lable_index));
            byte_code.addAll(right.to_byte_code());
            byte_code.add("ifeq ELSE" + Integer.toString(lable_index));
            byte_code.add("ldc 1");
            byte_code.add("goto END" + Integer.toString(lable_index));
            byte_code.add("ELSE" + Integer.toString(lable_index) + " :");
            byte_code.add("ldc 0");
            byte_code.add("END" + Integer.toString(lable_index) + " :");
            lable_index++;
        } else if(binaryOperator.equals(BinaryOperator.eq) || binaryOperator.equals(BinaryOperator.neq)) {
            byte_code.addAll(left.to_byte_code());
            byte_code.addAll(right.to_byte_code());
            if (left.getType() instanceof IntType || left.getType() instanceof BooleanType) {
                if (binaryOperator.equals(BinaryOperator.neq))
                    byte_code.add("if_icmpeq ELSE" + Integer.toString(lable_index));
                else
                    byte_code.add("if_icmpne ELSE" + Integer.toString(lable_index));
            } else {
                if (binaryOperator.equals(BinaryOperator.neq))
                    byte_code.add("if_acmpeq ELSE" + Integer.toString(lable_index));
                else
                    byte_code.add("if_acmpne ELSE" + Integer.toString(lable_index));
            }
            byte_code.add("ldc 1");
            byte_code.add("goto END" + Integer.toString(lable_index));
            byte_code.add("ELSE" + Integer.toString(lable_index) + " :");
            byte_code.add("ldc 0");
            byte_code.add("END" + Integer.toString(lable_index) + " :");
            lable_index ++;
        } else if(binaryOperator.equals(BinaryOperator.assign)){
            Assign assignment = new Assign(left, right);
            byte_code.addAll(assignment.to_byte_code());
            byte_code.addAll(left.to_byte_code());
        }
        return byte_code;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
