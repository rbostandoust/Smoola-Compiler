package symbolTable;

import ast.Type.Type;

import java.util.ArrayList;

public class SymbolTableClassItem extends SymbolTableItem {

    public SymbolTableClassItem(String name) {
        this.name = name;
    }

    @Override
    public String getKey() {
        return "#Class_"+name;
    }

}
