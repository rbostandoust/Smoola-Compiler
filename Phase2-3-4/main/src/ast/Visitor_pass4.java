package ast;
//import ast.VisitorImpl;
import ast.Type.ArrayType.ArrayType;
import ast.Type.NoType;
import ast.Type.PrimitiveType.BooleanType;
import ast.Type.PrimitiveType.IntType;
import ast.Type.PrimitiveType.StringType;
import ast.Type.Type;
import ast.Type.UserDefinedType.UserDefinedType;
import ast.node.Program;
import ast.node.declaration.ClassDeclaration;
import ast.node.declaration.MethodDeclaration;
import ast.node.declaration.VarDeclaration;
import ast.node.expression.*;
import ast.node.expression.Value.BooleanValue;
import ast.node.expression.Value.IntValue;
import ast.node.expression.Value.StringValue;
import ast.node.statement.*;
import symbolTable.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Visitor_pass4 extends VisitorImpl{
    public SymbolTable symbolTable_top;
    public String line_number;

    public ArrayList<Type> check_arg_type(ArrayList<Type> method_args){
        boolean arg_flag = true;
        for (int i = 0; i < method_args.size(); i++) {
            arg_flag = true;
            if (method_args.get(i) instanceof UserDefinedType) {
                for (UserDefinedType class_defined : class_defined_declaration) {
                    if (class_defined.getName().getName().equals(((UserDefinedType) method_args.get(i)).getName().getName())) {
                        method_args.set(i, class_defined);
                        arg_flag = false;
                    }
                }
                if(arg_flag == true){
                    method_args.set(i, new NoType());
                }
            }
        }
        return method_args;
    }
    static public boolean check_sub_type(Type left, Type right) {
        if (right instanceof UserDefinedType && left instanceof UserDefinedType) {
            while(symbol_table_items.get(((UserDefinedType) right).getName().getName()).getPre() != null) {
                for (String key : symbol_table_items.keySet()) {
                    if (symbol_table_items.get(key) == symbol_table_items.get(((UserDefinedType) right).getName().getName()).getPre()) {
                        if (key.equals(((UserDefinedType) left).getName().getName().toString())) {
                            return true;
                        } else {
                            for (UserDefinedType class_defined : class_defined_declaration) {
                                if (class_defined.getName().getName().equals(key) ){
                                    right =  class_defined;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }
    public void check_error(){
        ArrayList<Integer> keys = new ArrayList(errors.keySet());
        Collections.sort(keys);
        for (Integer i : keys)
        {
            ArrayList<String> error_temp = errors.get(i);
            ArrayList<String> class_error_array = new ArrayList<>();
            ArrayList<String> method_error_array = new ArrayList<>();
            ArrayList<String> deleted_array = new ArrayList<>();
            ArrayList<String> deleted_variable = new ArrayList<>();
            boolean flag = false;
            for(String error : error_temp)
            {
                if(error.toLowerCase().contains("class".toLowerCase())) {
                    if (error.toLowerCase().contains("is not declared".toLowerCase())) {
                        class_error_array.add(error);
                    }
                }
                if(error.toLowerCase().contains("classNoType".toLowerCase())){
                    deleted_array.add(error);
                    deleted_variable.add(error);

                }

            }
            for(String error : error_temp)
            {
                if(error.toLowerCase().contains("method".toLowerCase())) {
                    if (error.toLowerCase().contains("is not declared".toLowerCase())) {
                        method_error_array.add(error);
                    }
                }
            }
            for(String class_error : class_error_array) {
                for (String error : error_temp) {
                    if (error.toLowerCase().contains("variable".toLowerCase())) {
                        if (error.toLowerCase().contains("is not declared".toLowerCase())) {
                            int class_index = class_error.indexOf("is not declared");
                            int variable_index = error.indexOf("is not declared");
                            String str1 = class_error.substring(6, class_index);
                            String str2 = error.substring(9, variable_index);
                            if(str1.equals(str2)) {
                                deleted_array.add(error);
                            }
                        }
                    }
                }
            }
            for(String method_error : method_error_array) {
                for (String error : error_temp) {
                    if (error.toLowerCase().contains("variable".toLowerCase())) {
                        if (error.toLowerCase().contains("is not declared".toLowerCase())) {
                            int method_index = method_error.indexOf("is not declared");
                            int variable_index = error.indexOf("is not declared");
                            String str1 = method_error.substring(7, method_index);
                            String str2 = error.substring(9, variable_index);
                            if(str1.equals(str2)) {
                                deleted_array.add(error);
                            }
                        }
                    }
                }
            }
            for(String deleted_method : deleted_variable){
                for (String error : error_temp) {
                    if (error.toLowerCase().contains("variable".toLowerCase())) {
                        if (error.toLowerCase().contains("is not declared".toLowerCase())) {
                            int variable_index = error.indexOf("is not declared");
                            int deleted_method_index = deleted_method.indexOf("classNoType");
                            String str1 = deleted_method.substring(12);
                            String str2 = error.substring(10, variable_index-1);
                            if(str1.equals(str2)){
                                deleted_array.add(error);
                            }
                        }
                    }
                }
            }
            for (String error : deleted_array){
                error_temp.remove(error);
            }
            errors.put(i, error_temp);
        }
    }

    public boolean check_parent_Type(String left ,String right){
        if(left == null || right==null ) {
            return false;
        }
        else if(left.equals(right) || left.equals("#Class_"+right) )
            return true;

        SymbolTable parent_symbolTable =symbol_table_items.get(left).getPre();
        if(parent_symbolTable == null )
            return false;
        for ( String key : symbol_table_items.keySet() ) {
            if(symbol_table_items.get(key) == parent_symbolTable){
                return check_parent_Type(key,right);
            }
        }
        return check_parent_Type(left,right);
    }

    public Type unary_subType(Type expression, UnaryOperator op){
        if(op.equals(UnaryOperator.minus)) {
            if(!(expression instanceof IntType || expression instanceof NoType)){
                SymbolTable.error = true;
                add_error(Integer.valueOf(line_number), ":unsupported operand type for " + op.toString());
                //errors.put(Integer.valueOf(line_number), ":unsupported operand type for " + op.toString());
                return new NoType();
            }else{
                return new IntType();
            }
        }else if(op.equals(UnaryOperator.not)) {
            if(!(expression instanceof BooleanType || expression instanceof NoType)){
                SymbolTable.error = true;
                add_error(Integer.valueOf(line_number), ":unsupported operand type for " + op.toString());
                //errors.put(Integer.valueOf(line_number), ":unsupported operand type for " + op.toString());
                return new NoType();
            }else{
                return new BooleanType();
            }
        }
        return  new NoType();
    }

    public Type binary_subType(Type left_expression_type, Type right_expression_type, BinaryOperator op){
        if(op.equals(BinaryOperator.eq) || op.equals(BinaryOperator.neq)) {
            if(!((left_expression_type instanceof NoType || right_expression_type instanceof NoType) ||
                    ((left_expression_type.toString().equals(right_expression_type.toString())) &&
                    (left_expression_type instanceof IntType || left_expression_type instanceof ArrayType ||
                            left_expression_type instanceof StringType || left_expression_type instanceof UserDefinedType ||
                            left_expression_type instanceof BooleanType)))) {
                SymbolTable.error = true;
                add_error(Integer.valueOf(line_number), ":unsupported operand type for " + op.toString());
                //errors.put(Integer.valueOf(line_number), ":unsupported operand type for " + op.toString());
                return new NoType();
            }else if(left_expression_type instanceof UserDefinedType && right_expression_type instanceof UserDefinedType) {
                if (((UserDefinedType) left_expression_type).getClassDeclaration().getParentName() != null) {
                    if (check_parent_Type(((UserDefinedType) left_expression_type).getClassDeclaration().getParentName().getName(), ((UserDefinedType) right_expression_type).getClassDeclaration().getName().getName()))
                        return new BooleanType();
                    else
                        return new NoType();
                }else if (((UserDefinedType) right_expression_type).getClassDeclaration().getParentName() != null)
                    if(check_parent_Type(((UserDefinedType) right_expression_type).getClassDeclaration().getParentName().getName(), ((UserDefinedType) left_expression_type).getName().getName()))
                        return new BooleanType();
                    else
                        return new NoType();
                else
                    return new NoType();
            }else {
                return new BooleanType();
            }
        }
        else if (op.equals(BinaryOperator.mult) || op.equals(BinaryOperator.div) || op.equals(BinaryOperator.add) ||
                op.equals(BinaryOperator.sub) || op.equals(BinaryOperator.gt) || op.equals(BinaryOperator.lt)){
            if(!((left_expression_type instanceof IntType  || left_expression_type instanceof NoType) &&
                    (right_expression_type instanceof IntType || right_expression_type instanceof  NoType))){
                SymbolTable.error = true;
                add_error(Integer.valueOf(line_number), ":unsupported operand type for " + op.toString());
                //errors.put(Integer.valueOf(line_number), ":unsupported operand type for " + op.toString());
                return new NoType();
            }else if(op.equals(BinaryOperator.lt) || op.equals(BinaryOperator.gt)) {
                if(left_expression_type instanceof NoType || right_expression_type instanceof NoType)
                    return new NoType();
                else
                    return new BooleanType();
            }
            return new IntType();

        }else if (op.equals(BinaryOperator.and) || op.equals(BinaryOperator.or)){
            if(!((left_expression_type instanceof BooleanType || left_expression_type instanceof NoType) &&
                    (right_expression_type instanceof BooleanType || right_expression_type instanceof  NoType))) {
                SymbolTable.error = true;
                add_error(Integer.valueOf(line_number), ":unsupported operand type for " + op.toString());
                //errors.put(Integer.valueOf(line_number), ":unsupported operand type for " + op.toString());
                return new NoType();
            }else {
                return new BooleanType();
            }
        }
        return new NoType();

    }

    public Type get_type(Expression expression){
        if(expression instanceof IntValue) {
            expression.setType(new IntType());
            return new IntType();
        }
        if(expression instanceof BooleanValue) {
            expression.setType(new BooleanType());
            return new BooleanType();
        }
        if(expression instanceof StringValue) {
            expression.setType(new StringType());
            return new StringType();
        }
        if(expression instanceof ArrayCall) {
            Type instance_type = get_type(((ArrayCall) expression).getInstance());
            Type index_type = get_type(((ArrayCall) expression).getIndex());
            if(!(index_type instanceof IntType || index_type instanceof NoType)){
                SymbolTable.error = true;
                add_error(Integer.valueOf(expression.get_line_number()), ":ArrayCall index is not Integer" );
                return new NoType();
                //errors.put(Integer.valueOf(expression.get_line_number()), ":ArrayCall index is not Integer" );
            }
            if(instance_type instanceof ArrayType){
                expression.setType(new IntType());
                return new IntType();
            }else {
                SymbolTable.error = true;
                add_error(Integer.valueOf(expression.get_line_number()), ":ArrayCall instance is not ArrayType" );
                return new NoType();
            }
        }
        if(expression instanceof BinaryExpression){
            Type left_type = get_type(((BinaryExpression) expression).getLeft());
            Type right_type = get_type(((BinaryExpression) expression).getRight());
            BinaryOperator op = ((BinaryExpression) expression).getBinaryOperator();
            line_number = ((BinaryExpression) expression).getRight().get_line_number();
            Type result = binary_subType(left_type, right_type, op);
            if(op.equals(BinaryOperator.assign)){
                if(!(((BinaryExpression) expression).getLeft() instanceof ArrayCall || ((BinaryExpression) expression).getLeft() instanceof Identifier))
                {
                    if(!(right_type instanceof NoType && left_type instanceof NoType)) {
                        SymbolTable.error = true;
                        add_error(Integer.valueOf(line_number), ":left side of assignment must be a valid lvalue");
                    }
                    return new NoType();
                }else if(!(right_type instanceof NoType || right_type.toString().equals(left_type.toString()))){
                    SymbolTable.error = true;
                    add_error(Integer.valueOf(line_number), ":left side of assignment must be a valid lvalue");
                    return new NoType();
                }else{
                    expression.setType(left_type);
                    return left_type;
                }
            }
            expression.setType(result);
            return result;

        }
        if(expression instanceof Identifier) {
            try {
                SymbolTableItem identifier_symbolTableItem = SymbolTable.top.get("#Variable_" + ((Identifier) expression).getName());
                if (identifier_symbolTableItem instanceof SymbolTableVariableItemBase) {
                    Type identifier_type = get_identifier_type(((SymbolTableVariableItemBase) identifier_symbolTableItem).getType());
                    if(identifier_type instanceof UserDefinedType) {
                        for (UserDefinedType class_defined : class_defined_declaration) {
                            if (class_defined.getName().getName().equals(((UserDefinedType) identifier_type).getName().getName())) {
                                expression.setType(class_defined);
                                return class_defined;
                            }
                        }
                        for (UserDefinedType user_defined : user_defined_declaration) {
                            if(user_defined.getName().getName().equals(((UserDefinedType) identifier_type).getName().getName())) {
                                expression.setType(user_defined);
                                return user_defined;
                            }
                        }
                        return new NoType();
                    }else{
                        expression.setType(identifier_type);
                        return identifier_type;
                    }

                }
            } catch (ItemNotFoundException e) {
                return new NoType();
            }
        }
        if(expression instanceof Length) {
            Type exp_type = get_type(((Length) expression).getExpression());
            if(exp_type instanceof ArrayType || exp_type instanceof NoType){
                expression.setType(new IntType());
                return new IntType();
            }else {
                SymbolTable.error = true;
                add_error(Integer.valueOf(expression.get_line_number()), ":Length expression is not array" );
                //errors.put(Integer.valueOf(expression.get_line_number()), ":Length expression is not array" );
                return new NoType();
            }
        }
        if(expression instanceof MethodCall) {
            boolean arg_flag = false;
            boolean come_in = false;
            Type instance_exp_type_prev;
            //print(((MethodCall) expression).getInstance().toString());
            Type instance_exp_type = get_type(((MethodCall) expression).getInstance());
            Type first_instance_exp = instance_exp_type;
            Type identifier_type = get_type(((MethodCall) expression).getMethodName());
            ArrayList <Type> arg_types = new ArrayList<>();
            for (Expression arg_exp : ((MethodCall) expression).getArgs())
                arg_types.add(get_type(arg_exp));
            if(instance_exp_type instanceof UserDefinedType) {
                if(identifier_type instanceof UserDefinedType || identifier_type instanceof NoType) {
                    while(true){
                        ArrayList<MethodDeclaration> methods = ((UserDefinedType) instance_exp_type).getClassDeclaration().getMethodDeclarations();
                        for (MethodDeclaration method : methods) {
                            if (method.getName().getName().equals(((MethodCall) expression).getMethodName().getName())) {
                                ArrayList<Type> method_args = method.getArgsType();
                                if (arg_types.size() == method_args.size()) {
                                    arg_flag = true;
                                    method_args = check_arg_type(method_args);
                                    for (int i = 0; i < method_args.size(); i++) {
                                        if (method_args.get(i) instanceof UserDefinedType) {
                                            if (!(((UserDefinedType) method_args.get(i)).getName().getName().toString().equals(arg_types.get(i).toString()) || check_sub_type(method_args.get(i), arg_types.get(i)) || arg_types.get(i) instanceof NoType)) {
                                                arg_flag = false;
                                                break;
                                            }
                                        } else if (method_args.get(i) instanceof NoType) {
                                            if (!(arg_types.get(i) instanceof NoType)) {
                                                arg_flag = false;
                                                break;
                                            }
                                        } else {
                                            if (!(method_args.get(i).toString().equals(arg_types.get(i).toString()) || arg_types.get(i) instanceof NoType)) {
                                                arg_flag = false;
                                                break;
                                            }
                                        }
                                    }
                                    if (arg_flag == true) {
                                        if (main_class_flag == true)
                                            add_error(Integer.valueOf(expression.get_line_number()), "classNoType " + ((MethodCall) expression).getMethodName().getName());
                                        Type return_type = method.getReturnType();
                                        if (return_type instanceof UserDefinedType) {
                                            for (UserDefinedType class_defined : class_defined_declaration) {
                                                if (class_defined.getName().getName().equals(((UserDefinedType) return_type).getName().getName())) {
                                                    return_type = class_defined;
                                                    add_error(Integer.valueOf(expression.get_line_number()), "classNoType " + ((MethodCall) expression).getMethodName().getName());
                                                    ((MethodCall) expression).getMethodName().setType(return_type);
                                                    expression.setType(return_type);
                                                    return return_type;
                                                }
                                            }
                                            return new NoType();
                                        }
                                        add_error(Integer.valueOf(expression.get_line_number()), "classNoType " + ((MethodCall) expression).getMethodName().getName());
                                        ((MethodCall) expression).getMethodName().setType(return_type);
                                        expression.setType(return_type);
                                        return return_type;
                                    }
                                }
                            }
                        }
                        instance_exp_type_prev = instance_exp_type;
                        if(((UserDefinedType) instance_exp_type).getClassDeclaration().getParentName() != null) {
                            for (UserDefinedType class_defined : class_defined_declaration) {
                                if (class_defined.getName().getName().equals(((UserDefinedType) instance_exp_type).getClassDeclaration().getParentName().getName())) {
                                    instance_exp_type = class_defined;
                                    break;
                                }
                            }
                            if(((UserDefinedType) instance_exp_type).getName().getName().equals(((UserDefinedType) instance_exp_type_prev).getName().getName())){
                                break;
                            }
                        }else{
                            break;
                        }
                    }
                    SymbolTable.error = true;
                    add_error(Integer.valueOf(expression.get_line_number()), "classNoType " + ((MethodCall) expression).getMethodName().getName());
                    add_error(Integer.valueOf(expression.get_line_number()), ":there is no method named " + ((MethodCall) expression).getMethodName().getName() + " in class " + ((UserDefinedType) first_instance_exp).getName().getName());
                    return new NoType();
                }else{
                    SymbolTable.error = true;
                    add_error(Integer.valueOf(expression.get_line_number()), ":there is no class type for method call");
                    return new NoType();
                }
            }else{
                if(!(instance_exp_type instanceof NoType)) {
                    SymbolTable.error = true;
                    add_error(Integer.valueOf(expression.get_line_number()), ":there is no class type for method call");
                    return new NoType();
                }else{
                    SymbolTable.error = true;
                    if(!(((MethodCall) expression).getInstance() instanceof NewClass)) {
                        String str = ((MethodCall) expression).getInstance().toString().replace("Identifier ", "");
                        add_error(Integer.valueOf(expression.get_line_number()), ":class " + str + " is not declared");
                    }
                    add_error(Integer.valueOf(expression.get_line_number()), "classNoType " + ((MethodCall) expression).getMethodName().getName());
                    return new NoType();
                }
            }
        }
        if(expression instanceof NewArray) {
            //It doesn't have any error
            expression.setType(new ArrayType());
            return new ArrayType();
        }
        if(expression instanceof  NewClass) {
            Type identifier_type = get_type(((NewClass) expression).getClassName());
            if(identifier_type instanceof NoType) {
                SymbolTable new_class_symbolTable = symbol_table_items.get(((NewClass) expression).getClassName().getName());
                if(new_class_symbolTable != null){
                    for(UserDefinedType class_defined : class_defined_declaration){
                        if(class_defined.getName().getName().equals(((NewClass) expression).getClassName().getName())){
                            expression.setType(class_defined);
                            return class_defined;
                        }
                    }
                }
                else{
                    SymbolTable.error = true;
                    add_error(Integer.valueOf(expression.get_line_number()), ":class " + ((NewClass) expression).getClassName().getName()+ " is not declared" );
                    //errors.put(Integer.valueOf(expression.get_line_number()), ":class " + ((NewClass) expression).getClassName().getName()+ " is not declared" );
                    return new NoType();
                }
            }else {
                SymbolTable.error = true;
                add_error(Integer.valueOf(expression.get_line_number()), ":class " + ((NewClass) expression).getClassName().getName() + " is not declared" );
                //errors.put(Integer.valueOf(expression.get_line_number()), ":class " + ((NewClass) expression).getClassName().getName() +" is not declared" );
                return new NoType();
            }
        }
        if(expression instanceof This) {
            for ( String key : symbol_table_items.keySet() ) {
                if(symbol_table_items.get(key) == SymbolTable.top.getPre()){
                    for(UserDefinedType user_defined : class_defined_declaration){
                        if(user_defined.getName().getName().equals(key)){
                            expression.setType(user_defined);
                            return user_defined;
                        }
                    }
                }
            }
            return new NoType();
        }
        if(expression instanceof UnaryExpression) {
            Type unary_expression_type = get_type(((UnaryExpression) expression).getValue());
            UnaryOperator op = ((UnaryExpression) expression).getUnaryOperator();
            line_number = expression.get_line_number();
            expression.setType(unary_subType(unary_expression_type, op));
            return unary_subType(unary_expression_type, op);
        }
        return new NoType();
    }

    public Type get_identifier_type(Type type) {
        if(type.toString().equals("int"))
            return new IntType();
        if(type.toString().equals("boolean"))
            return new BooleanType();
        if(type.toString().equals("string"))
            return new StringType();
        if(type.toString().equals("int[]"))
            return new ArrayType();
        if(type.toString().equals("NoType"))
            return new NoType();
        return type;
    }

    @Override
    public void visit(Program program) {
        symbolTable_top = SymbolTable.top;
        main_class_flag = true;
        program.getMainClass().accept(this);
        main_class_flag = false;
        for (ClassDeclaration classDec : program.getClasses()) {
            classDec.accept(this);
        }
        check_error();
    }

    @Override
    public void visit(ClassDeclaration classDeclaration) {
        String className = classDeclaration.getName().getName();
        if(className.equals(class_defined_declaration.get(1).getName().getName())) {
            ArrayList<MethodDeclaration> methods = classDeclaration.getMethodDeclarations();
            if (methods.size() == 1) {
                if (!(methods.get(0).getName().getName().equals("main"))) {
                    SymbolTable.error = true;
                    add_error(Integer.valueOf(classDeclaration.get_line_number()), ":method name of main class should be \"main\"");
                }
            } else {
                SymbolTable.error = true;
                add_error(Integer.valueOf(classDeclaration.get_line_number()), ":main class should have one method");
            }
        }
        classDeclaration.getName().accept(this);

        if(classDeclaration.getParentName() != null) {
            boolean unDefined_class = false;
            for (UserDefinedType class_dec : class_defined_declaration){
                if(class_dec.getName().getName().equals(classDeclaration.getParentName().getName())){
                    unDefined_class = true;
                }
            }
            if(unDefined_class == false){
                SymbolTable.error = true;
                add_error(Integer.valueOf(classDeclaration.get_line_number()), ":class " + classDeclaration.getParentName().getName() + " is not declared");
            }
            classDeclaration.getParentName().accept(this);
        }
        SymbolTable.push(symbol_table_items.get(className));

        for(VarDeclaration varDec : classDeclaration.getVarDeclarations()) {
            varDec.accept(this);
        }

        for(MethodDeclaration methodDec : classDeclaration.getMethodDeclarations()) {
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
//        if(main_class_flag == false && methodDeclaration.getName().getName().equals("main")){
//            SymbolTable.error = true;
//            add_error(Integer.valueOf(methodDeclaration.get_line_number()), ":main function should be in main class");
//        }
        boolean flag = false;

        for(VarDeclaration argDec : methodDeclaration.getArgs()) {
            argDec.accept(this);

        }
        for(VarDeclaration localVarDec : methodDeclaration.getLocalVars()) {
            localVarDec.accept(this);
        }
        for(Statement stmt : methodDeclaration.getBody()) {
            stmt.accept(this);
        }
        methodDeclaration.getReturnValue().accept(this);

        Type return_value_type = get_type(methodDeclaration.getReturnValue());
        Type return_type = methodDeclaration.getReturnType();
        if(return_type instanceof UserDefinedType) {
            for (UserDefinedType class_defined : class_defined_declaration) {
                if (class_defined.getName().getName().equals(((UserDefinedType) return_type).getName().getName())) {
                    flag = true;
                    return_type = class_defined;
                }
            }
            if(flag == false){
                SymbolTable.error = true;
                add_error(Integer.valueOf(methodDeclaration.get_line_number()), ":class " + ((UserDefinedType) return_type).getName().getName() + " is not declared");
                return_type = new NoType();
            }
        }
        if(!(return_value_type instanceof NoType && return_type instanceof NoType)) {
            if (!(return_value_type instanceof NoType)) {
                if (!(return_type.toString().equals(return_value_type.toString()) || check_sub_type(return_type, return_value_type))) {
                    SymbolTable.error = true;
                    add_error(Integer.valueOf(methodDeclaration.get_line_number()), ":" + methodDeclaration.getName().getName() + " return type must be " + return_type.toString());
                }
            }
        }
        SymbolTable.pop();
    }

    @Override
    public void visit(VarDeclaration varDeclaration) {

        String varName = varDeclaration.getIdentifier().getName();
        if(varDeclaration.getType() instanceof UserDefinedType){
            try {
                symbolTable_top.get("#Class_" + ((UserDefinedType)varDeclaration.getType()).getName().getName());
            } catch (ItemNotFoundException e) {

                SymbolTable.error = true;
                add_error(Integer.valueOf(varDeclaration.get_line_number()), ":class " + ((UserDefinedType) varDeclaration.getType()).getName().getName() + " is not declared");
                varDeclaration.setType(new NoType());
                try {
                    SymbolTableItem identifier_symbolTableItem = SymbolTable.top.get("#Variable_" + varDeclaration.getIdentifier().getName());
                    if (identifier_symbolTableItem instanceof SymbolTableVariableItemBase) {
                        ((SymbolTableVariableItemBase) identifier_symbolTableItem).setType(new NoType());
                    }
                }catch(ItemNotFoundException e2) { }
                //errors.put(Integer.valueOf(varDeclaration.get_line_number()), ":variable " + ((UserDefinedType) varDeclaration.getType()).getName().getName() + " is not declared");
            }
        }
        SymbolTableVariableItemBase variable_item = new SymbolTableVariableItemBase(varName, varDeclaration.getType(),0);
        try {
            SymbolTable.top.put(variable_item);
        } catch (ItemAlreadyExistsException e) {

        }
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
        SymbolTable top = SymbolTable.top;

        try {
            symbolTable_top.get("#Class_"+identifier.getName());
        } catch (ItemNotFoundException e1) {
            try {
                top.get("#Class_" + identifier.getName());
            } catch (ItemNotFoundException e2) {
                try {
                    top.get("#Method_" + identifier.getName());
                } catch (ItemNotFoundException e3) {
                    try {
                        top.get("#Variable_" + identifier.getName());
                    } catch (ItemNotFoundException e4) {
                        SymbolTable.error = true;
                        add_error(Integer.valueOf(identifier.get_line_number()), ":variable " + identifier.getName() + " is not declared");
                        //errors.put(Integer.valueOf(identifier.get_line_number()), ":variable " + identifier.getName() + " is not declared");
                        try {
                            top.put(new SymbolTableVariableItemBase(identifier.getName(), new NoType(), index));
                            index++;
                        } catch (ItemAlreadyExistsException e5) { }
                    }
                }
            }
        }
    }

    @Override
    public void visit(Length length) {

        length.getExpression().accept(this);
    }

    @Override
    public void visit(MethodCall methodCall) {

        methodCall.getInstance().accept(this);
        methodCall.getMethodName().accept(this);
        ArrayList<Expression> args = methodCall.getArgs();
        for(Expression exp : args){
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

            Type right_type = get_type(assign.getrValue());
            Type left_type = get_type(assign.getlValue());

            if (!(assign.getlValue() instanceof ArrayCall || assign.getlValue() instanceof Identifier)) {
                if (!(right_type instanceof NoType && left_type instanceof NoType)) {
                    SymbolTable.error = true;
                    add_error(Integer.valueOf(assign.get_line_number()), ":left side of assignment must be a valid lvalue");
                }
            } else if (!(right_type instanceof NoType || right_type.toString().equals(left_type.toString()) || check_sub_type(left_type, right_type))) {
                SymbolTable.error = true;
                add_error(Integer.valueOf(assign.get_line_number()), ":unsupported operand type for assign");
            }

            assign.getlValue().accept(this);
            assign.getrValue().accept(this);
        } else if (assign.getrValue() instanceof MethodCall) {
            if( main_class_flag == true) {
                Type right_type = get_type(assign.getrValue());
                assign.getrValue().accept(this);
            }else {
                SymbolTable.error = true;
                add_error(Integer.valueOf(assign.get_line_number()), ":invalid statement out of main method");
            }
        } else {
            SymbolTable.error = true;
            add_error(Integer.valueOf(assign.get_line_number()), ":left side of assignment must be a valid lvalue");
        }
    }

    @Override
    public void visit(Block block) {
        for(Statement stms : block.getBody()) {
            stms.accept(this);
        }
    }

    @Override
    public void visit(Conditional conditional) {

        Type conditional_type = get_type(conditional.getExpression());
        if(!(conditional_type instanceof BooleanType || conditional_type instanceof NoType)){
            add_error(Integer.valueOf(conditional.get_line_number()), ":condition type must be boolean");
            //print("Line:" + conditional.get_line_number() + ":condition type must be boolean");
        }
        conditional.getExpression().accept(this);
        conditional.getConsequenceBody().accept(this);
        if( conditional.getAlternativeBody() != null)
            conditional.getAlternativeBody().accept(this);
    }

    @Override
    public void visit(MethodCallInMain methodCallInMain) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(While loop) {

        Type loop_type = get_type(loop.getCondition());
        if(!(loop_type instanceof BooleanType || loop_type instanceof NoType )){
            add_error(Integer.valueOf(loop.get_line_number()),":condition type must be boolean" );
            //print("Line:" + loop.get_line_number() + ":condition type must be boolean");
        }
        loop.getCondition().accept(this);
        loop.getBody().accept(this);

    }

    @Override
    public void visit(Write write) {
        Type write_type = get_type(write.getArg());
        if(!(write_type instanceof IntType || write_type instanceof NoType ||
                write_type instanceof ArrayType || write_type instanceof StringType)){
            add_error(Integer.valueOf(write.get_line_number()), ":unsupported type for writeln");
            //print("Line:" + write.get_line_number() + ":unsupported type for writeln");
        }
        write.getArg().accept(this);
    }
}