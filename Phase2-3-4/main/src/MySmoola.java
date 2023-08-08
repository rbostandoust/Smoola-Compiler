import ast.VisitorImpl;
import ast.node.Program;
import org.antlr.v4.runtime.*;
import java.io.IOException;
import ast.*;
import symbolTable.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

// Visit https://stackoverflow.com/questions/26451636/how-do-i-use-antlr-generated-parser-and-lexer
public class MySmoola {
    public static void main(String[] args) throws IOException {
        CharStream reader = CharStreams.fromFileName(args[0]);
        SmoolaLexer lexer = new SmoolaLexer(reader);   // SmoolaLexer in your project
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SmoolaParser parser = new SmoolaParser(tokens);   // SmoolaParser in your project
        Program p = parser.program().p; // program is the name of the start rule

        SymbolTable.push(new SymbolTable());
        SymbolTable.error = false;
        SymbolTable.has_error = true;
        SymbolTable.pass1_error = false;
        SymbolTable.pass2_error = true;
        SymbolTable.pass3_error = true;
        SymbolTable.pass4_error = true;

        VisitorImpl visitor = new VisitorImpl();
        p.accept(visitor);
        SymbolTable.pass1_error = true;
        SymbolTable.pass2_error = false;
        p.accept(visitor);
        SymbolTable.pass2_error = true;
        SymbolTable.pass3_error = false;
        p.accept(visitor);
        SymbolTable.pass3_error = true;
        SymbolTable.pass4_error = false;
        //syn_program.accept(visitor);
        Visitor_pass4 pass4 = new Visitor_pass4();
        if(SymbolTable.circular_inheritance == false) {
            p.accept(pass4);
        }
        SymbolTable.pass4_error = true;
        SymbolTable.pass5_error = false;
        Visitor_pass5 pass5 = new Visitor_pass5();
        p.accept(pass5);
        SymbolTable.pass5_error = true;

        HashMap<Integer, ArrayList<String>> dumb_erorrs = visitor.getErrors();
        if(SymbolTable.error == false){
            SymbolTable.has_error = false;
        }
        else {
            boolean dumb_error_flag = false;
            ArrayList<Integer> keys = new ArrayList(dumb_erorrs.keySet());
            for (Integer i : keys)
            {
                ArrayList<String> error_temp = dumb_erorrs.get(i);
                for(String error : error_temp) {
                    if(error_temp != null)
                        dumb_error_flag = true;
                }
            }

            if(dumb_error_flag == false)
                SymbolTable.has_error = false;
        }
        if(SymbolTable.has_error == false)
            p.accept(visitor);
        else{
            HashMap<Integer, ArrayList<String>> errors = visitor.getErrors();
            ArrayList<Integer> keys = new ArrayList(errors.keySet());
            Collections.sort(keys);
            for (Integer i : keys)
            {
                ArrayList<String> error_temp = errors.get(i);
                for(String error : error_temp)
                    System.out.println("Line:" + i + error);
            }
        }
    }
}
