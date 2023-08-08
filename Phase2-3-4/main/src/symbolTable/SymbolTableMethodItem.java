package symbolTable;

import ast.Type.Type;

import java.util.ArrayList;

public class SymbolTableMethodItem extends SymbolTableItem {

    ArrayList<Type> argTypes = new ArrayList<>();

    public SymbolTableMethodItem(String name, ArrayList<Type> argTypes) {
        this.name = name;
        this.argTypes = argTypes;
    }

    public ArrayList<Type> getArgsType(){
        return argTypes;
    }

    @Override
    public String getKey() {
        return "#Method_"+ name;
    }
}
