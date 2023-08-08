package ast.node.statement;

import ast.Visitor;
import ast.node.Node;

import java.util.ArrayList;

public class Statement extends Node {

    @Override
    public String toString() {
        return "Statement";
    }

    @Override
    public ArrayList<String> to_byte_code() {
        return null;
    }

    @Override
    public void accept(Visitor visitor) {}
}
