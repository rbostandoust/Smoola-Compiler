package ast.node.expression;

import ast.Type.PrimitiveType.BooleanType;
import ast.Type.PrimitiveType.IntType;
import ast.Type.Type;
import ast.Visitor;
import symbolTable.ItemNotFoundException;
import symbolTable.SymbolTable;
import symbolTable.SymbolTableItem;
import symbolTable.SymbolTableVariableItemBase;


import java.util.ArrayList;

import static ast.VisitorImpl.symbol_table_items;


public class Identifier extends Expression {
    private String name;

    public Identifier(String name) {
        this.name = name;
    }
    public Identifier(String name, int line){
        this.name = name;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Identifier " + name;
    }

    @Override
    public ArrayList<String>to_byte_code() {
        return to_byte_code("right");
    }

    public ArrayList<String> to_byte_code(String left_or_right) {
        ArrayList<String> byte_code = new ArrayList<String>();
        SymbolTable local_top = SymbolTable.top;
        SymbolTableItem local_param = local_top.getInCurrentScope("#Variable_" + name);
        if(local_param != null) {
            if (local_param instanceof SymbolTableVariableItemBase) {
                Type local_param_type = ((SymbolTableVariableItemBase) local_param).getType();
                int local_param_index = ((SymbolTableVariableItemBase) local_param).getIndex();
                if (local_param_type instanceof IntType || local_param_type instanceof BooleanType) {
                    if (left_or_right.equals("right"))
                        byte_code.add("iload " + Integer.toString(local_param_index));
                    else
                        byte_code.add("istore " + Integer.toString(local_param_index));
                } else {
                    if (left_or_right.equals("right"))
                        byte_code.add("aload " + Integer.toString(local_param_index));
                    else
                        byte_code.add("astore " + Integer.toString(local_param_index));
                }
            }
        }
        else {
            while (local_top.getPre() != null) {
                local_top = local_top.getPre();
                try {
                    local_param = local_top.get("#Variable_" + name);
                    Type local_param_type;
                    if (local_param instanceof SymbolTableVariableItemBase) {
                        local_param_type = ((SymbolTableVariableItemBase) local_param).getType();
                        for (String key : symbol_table_items.keySet()) {
                            if (symbol_table_items.get(key) == local_top) {
                                if (left_or_right.equals("right")) {
                                    byte_code.add("aload_0");
                                    byte_code.add("getfield " + key + "/" + name + " " + local_param_type.to_byte_code());
                                    return byte_code;
                                } else {
                                    byte_code.add("putfield " + key + "/" + name + " " + local_param_type.to_byte_code());
                                    return byte_code;
                                }
                            }
                        }
                    }
                } catch (ItemNotFoundException e1) {
                }
            }
        }
        return byte_code;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
