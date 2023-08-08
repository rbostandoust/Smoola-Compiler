package ast.node.statement;

import ast.Type.Type;
import ast.Visitor;
import ast.node.expression.ArrayCall;
import ast.node.expression.Expression;
import ast.node.expression.Identifier;
import symbolTable.ItemNotFoundException;
import symbolTable.SymbolTable;
import symbolTable.SymbolTableItem;
import symbolTable.SymbolTableVariableItemBase;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static ast.VisitorImpl.symbol_table_items;

public class Assign extends Statement {
    private Expression lValue;
    private Expression rValue;

    public Assign(Expression lValue, Expression rValue) {
        this.lValue = lValue;
        this.rValue = rValue;
    }
    public Assign(Expression lValue, Expression rValue, int line) {
        this.lValue = lValue;
        this.rValue = rValue;
        this.line = line;
    }

    public Expression getlValue() {
        return lValue;
    }

    public void setlValue(Expression lValue) {
        this.lValue = lValue;
    }

    public Expression getrValue() {
        return rValue;
    }

    public void setrValue(Expression rValue) {
        this.rValue = rValue;
    }


    @Override
    public ArrayList<String> to_byte_code() {
        ArrayList<String> byte_code = new ArrayList<String>();
        if(lValue instanceof Identifier){
            if(SymbolTable.top.getInCurrentScope("#Variable_" + ((Identifier) lValue).getName()) == null)
            {
                 byte_code.add("aload_0");
            }
        }
        if(lValue instanceof ArrayCall)
        {
            byte_code.addAll(((ArrayCall)lValue).to_byte_code("left"));
        }
        if(rValue instanceof Identifier)
            byte_code.addAll(((Identifier) rValue).to_byte_code("right"));
        else if(rValue instanceof ArrayCall)
            byte_code.addAll(((ArrayCall) rValue).to_byte_code("right"));
        else
            byte_code.addAll(rValue.to_byte_code());

        if(lValue instanceof Identifier)
            byte_code.addAll(((Identifier) lValue).to_byte_code("left"));
        else if(lValue instanceof ArrayCall)
            byte_code.add("iastore");

        return byte_code;
    }
    @Override
    public String toString() {
        return "Assign";
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
