package symbolTable;

import ast.Type.Type;

public class SymbolTableVariableItemBase extends SymbolTableItem {

    private int index;
    protected Type type;

    public SymbolTableVariableItemBase(String name, Type type, int index) {
        this.name = name;
        this.type = type;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getKey() {
        return "#Variable_" + name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }


}
