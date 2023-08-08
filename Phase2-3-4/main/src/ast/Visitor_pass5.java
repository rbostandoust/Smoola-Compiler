package ast;
//import ast.VisitorImpl;

import ast.Type.PrimitiveType.BooleanType;
import ast.Type.PrimitiveType.IntType;
import ast.Type.PrimitiveType.StringType;
import ast.node.Program;
import ast.node.declaration.ClassDeclaration;
import ast.node.declaration.MethodDeclaration;
import ast.node.declaration.VarDeclaration;
import ast.node.expression.*;
import ast.node.expression.Value.BooleanValue;
import ast.node.expression.Value.IntValue;
import ast.node.expression.Value.StringValue;
import ast.node.statement.*;
import symbolTable.ItemAlreadyExistsException;
import symbolTable.SymbolTable;
import symbolTable.SymbolTableVariableItemBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Visitor_pass5 extends VisitorImpl {

    public ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();

    public boolean is_class = false;
    public SymbolTable symbolTable_top;
    public int method_index = 1;

    public void writeUsingFiles(ArrayList<String> class_lines, String class_name) {
        try {
            Path file = Paths.get("./output/" + class_name + ".j");
            Files.write(file, class_lines);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<String> java_main_gen(String MainClassName) {
        ArrayList<String> lines = new ArrayList<String>();
        lines.add(
                ".class public JavaMain\n" +
                        ".super java/lang/Object\n" +
                        ".method public <init>()V\n" +
                        "aload_0 ; push this\n" +
                        "invokespecial java/lang/Object/<init>()V ; call super\n" +
                        "return\n" +
                        ".end method\n" +
                        ".method public static main([Ljava/lang/String;)V\n" +
                        ".limit stack 2\n" +
                        "new " + MainClassName + " \n" +
                        "dup\n" +
                        "invokespecial " + MainClassName + "/<init>()V\n" +
                        "invokevirtual " + MainClassName + "/main()I\n" +
                        "return\n" +
                        ".end method\n"
        );
        return lines;
    }
    private ArrayList<String> java_object_gen(String MainClassName) {
        ArrayList<String> lines = new ArrayList<String>();
        lines.add(
                ".class public Object\n" +
                        ".super java/lang/Object\n" +
                        ".method public <init>()V\n" +
                        "aload_0 ; push this\n" +
                        "invokespecial java/lang/Object/<init>()V ; call super\n" +
                        "return\n" +
                        ".end method\n" +
                        ".method public toString()Ljava/lang/String;\n" +
                        ".limit stack 2\n" +
                        "ldc " +'"' + "Object"+ '"' + "\n"+
                        "areturn\n" +
                        ".end method\n"
        );
        return lines;
    }
    @Override
    public void visit(Program program) {
        symbolTable_top = SymbolTable.top;
        writeUsingFiles(java_main_gen(program.getMainClass().getName().getName()), "JavaMain");
        writeUsingFiles(java_object_gen(program.getMainClass().getName().getName()), "Object");

        main_class_flag = true;
        program.getMainClass().accept(this);
        main_class_flag = false;
        writeUsingFiles(lines.get(lines.size() - 1), program.getMainClass().getName().getName());
        for (ClassDeclaration classDec : program.getClasses()) {
            classDec.accept(this);
            writeUsingFiles(lines.get(lines.size() - 1), classDec.getName().getName());
        }
    }

    @Override
    public void visit(ClassDeclaration classDeclaration) {

        lines.add(classDeclaration.to_byte_code());
        String className = classDeclaration.getName().getName();

        classDeclaration.getName().accept(this);

        if (classDeclaration.getParentName() != null) {
            classDeclaration.getParentName().accept(this);
        }
        SymbolTable.push(symbol_table_items.get(className));

        for (VarDeclaration varDec : classDeclaration.getVarDeclarations()) {
            is_class = true;
            varDec.accept(this);
        }
        is_class = false;

        ArrayList<String> class_byte_code = lines.get(lines.size() - 1);
        class_byte_code.addAll(classDeclaration.constructor_byte_code());
        lines.set(lines.size() - 1, class_byte_code);

        for (MethodDeclaration methodDec : classDeclaration.getMethodDeclarations()) {
            methodDec.accept(this);
        }
        SymbolTable.pop();
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration) {

        SymbolTable top = SymbolTable.top;
        SymbolTable currentMethod = new SymbolTable();
        currentMethod.setPre(top);
        SymbolTable.push(currentMethod);

        ArrayList<String> method_byte_code = lines.get(lines.size() - 1);
        method_byte_code.addAll(methodDeclaration.to_byte_code());
        lines.set(lines.size() - 1, method_byte_code);

        methodDeclaration.getName().accept(this);

        for (VarDeclaration argDec : methodDeclaration.getArgs()) {
            argDec.accept(this);
            method_index += 1;
        }
        for (VarDeclaration localVarDec : methodDeclaration.getLocalVars()) {
            localVarDec.accept(this);
            if(!is_class) {
                ArrayList<String> initial_byte_code = lines.get(lines.size() - 1);
                if (localVarDec.getType() instanceof IntType) {
                    Assign assignment = new Assign(localVarDec.getIdentifier(), new IntValue(0, new IntType()));
                    initial_byte_code.addAll(assignment.to_byte_code());
                } else if (localVarDec.getType() instanceof StringType) {
                    Assign assignment = new Assign(localVarDec.getIdentifier(), new StringValue("\"\"", new StringType()));
                    initial_byte_code.addAll(assignment.to_byte_code());
                } else if (localVarDec.getType() instanceof BooleanType) {
                    Assign assignment = new Assign(localVarDec.getIdentifier(), new BooleanValue(false, new BooleanType()));
                    initial_byte_code.addAll(assignment.to_byte_code());
                }
                lines.set(lines.size() - 1, method_byte_code);
            }
            method_index += 1;
        }
        method_index = 1;

        for (Statement stmt : methodDeclaration.getBody()) {
            stmt.accept(this);
        }
        methodDeclaration.getReturnValue().accept(this);

        method_byte_code = lines.get(lines.size() - 1);
        method_byte_code.addAll(methodDeclaration.return_byte_code());
        lines.set(lines.size() - 1, method_byte_code);
        SymbolTable.pop();

    }

    @Override
    public void visit(VarDeclaration varDeclaration) {

        String varName = varDeclaration.getIdentifier().getName();
        ArrayList<String> variable_byte_code = lines.get(lines.size() - 1);
        if (is_class) {
            variable_byte_code.addAll(varDeclaration.to_byte_code());
        }
        lines.set(lines.size() - 1, variable_byte_code);

        SymbolTableVariableItemBase variable_item = new SymbolTableVariableItemBase(varName, varDeclaration.getType(), method_index);
        try {
            SymbolTable.top.put(variable_item);
        } catch (ItemAlreadyExistsException e) { }



        varDeclaration.getIdentifier().accept(this);
    }

    @Override
    public void visit(ArrayCall arrayCall) {
        arrayCall.getInstance().accept(this);
        arrayCall.getIndex().accept(this);
    }

    @Override
    public void visit(BinaryExpression binaryExpression) {
        binaryExpression.getLeft().accept(this);
        binaryExpression.getRight().accept(this);
    }

    @Override
    public void visit(Identifier identifier) {

    }

    @Override
    public void visit(Length length) {

        length.getExpression().accept(this);
    }

    @Override
    public void visit(MethodCall methodCall) {

        methodCall.getInstance().accept(this);
        methodCall.getMethodName().accept(this);
        for (Expression exp : methodCall.getArgs()) {
            exp.accept(this);
        }
    }

    @Override
    public void visit(NewArray newArray) {

    }

    @Override
    public void visit(NewClass newClass) {
        newClass.getClassName().accept(this);
    }

    @Override
    public void visit(This instance) {
        //TODO check this
    }

    @Override
    public void visit(UnaryExpression unaryExpression) {

        unaryExpression.getValue().accept(this);
    }

    @Override
    public void visit(BooleanValue value) {

    }

    @Override
    public void visit(IntValue value) {

    }

    @Override
    public void visit(StringValue value) {

    }

    @Override
    public void visit(Assign assign) {
        if (!(assign.getlValue() == null || assign.getrValue() == null)) {
            assign.getlValue().accept(this);
            assign.getrValue().accept(this);

            ArrayList<String> assign_byte_code = lines.get(lines.size() - 1);
            assign_byte_code.addAll(assign.to_byte_code());
            lines.set(lines.size() - 1, assign_byte_code);

        } else if (assign.getrValue() != null) {
            assign.getrValue().accept(this);
            if(assign.getrValue() instanceof MethodCall && main_class_flag) {
                ArrayList<String> methodCall_byte_code = lines.get(lines.size() - 1);
                methodCall_byte_code.addAll(assign.getrValue().to_byte_code());
                lines.set(lines.size() - 1, methodCall_byte_code);
            }

        } else if (assign.getlValue() != null) {
            assign.getlValue().accept(this);

        }
    }

    @Override
    public void visit(Block block) {
        for (Statement stms : block.getBody()) {
            stms.accept(this);
        }
    }

    @Override
    public void visit(Conditional conditional) {
        conditional.getExpression().accept(this);
//        conditional.getConsequenceBody().accept(this);
//        if (conditional.getAlternativeBody() != null)
//            conditional.getAlternativeBody().accept(this);
        ArrayList<String> conditional_byte_code = lines.get(lines.size() - 1);
        conditional_byte_code.addAll(conditional.to_byte_code());
        lines.set(lines.size() - 1, conditional_byte_code);
    }

    @Override
    public void visit(MethodCallInMain methodCallInMain) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(While loop) {
        loop.getCondition().accept(this);
        //loop.getBody().accept(this);
        ArrayList<String> loop_byte_code = lines.get(lines.size() - 1);
        loop_byte_code.addAll(loop.to_byte_code());
        lines.set(lines.size() - 1, loop_byte_code);
    }

    @Override
    public void visit(Write write) {

        write.getArg().accept(this);
        ArrayList<String> variable_byte_code = lines.get(lines.size() - 1);
        variable_byte_code.addAll(write.to_byte_code());
        lines.set(lines.size() - 1, variable_byte_code);
    }
}
