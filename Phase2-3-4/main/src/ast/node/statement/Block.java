package ast.node.statement;

import ast.Visitor;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;

public class Block extends Statement {
    private ArrayList<Statement> body = new ArrayList<>();

    public Block(ArrayList<Statement> body) {
      this.body = body;
    }

    public ArrayList<Statement> getBody() {
        return body;
    }

    public void addStatement(Statement statement) {
        this.body.add(statement);
    }

    @Override
    public ArrayList<String> to_byte_code() {
        ArrayList<String> byte_code = new ArrayList<String>();
        for(Statement block_statement : body){
            byte_code.addAll(block_statement.to_byte_code());
        }
        return byte_code;
    }
    @Override
    public String toString() { return "Block"; }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
