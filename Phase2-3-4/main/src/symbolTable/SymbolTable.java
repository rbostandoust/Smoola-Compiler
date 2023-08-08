package symbolTable;

import java.util.*;
import ast.Type.UserDefinedType.*;

public class SymbolTable {

	SymbolTable pre;
	HashMap<String, SymbolTableItem> items;

	// Static members region
	public static boolean pass1_error;
	public static boolean pass2_error;
	public static boolean pass3_error;
	public static boolean pass4_error;
	public static boolean pass5_error;
	public static boolean has_error;
	public static boolean error;
	public static boolean circular_inheritance;

	public static SymbolTable top;

	private static Stack<SymbolTable> stack = new Stack<SymbolTable>();


	public void setPre(SymbolTable pre){
		this.pre = pre;
	}
	public SymbolTable getPre(){
		return pre;
	}
	// Use it in pass 1 scope start
	public static void push(SymbolTable symbolTable) {
		if(top != null)
			stack.push(top);
		top = symbolTable;
	}

	// Use it in pass1 scope end
	public static void pop() {
		top = stack.pop();
	}

	// End of static members region

	public SymbolTable() {
		this(null);
	}

	public SymbolTable(SymbolTable pre) {
		this.pre = pre;
		this.items = new HashMap<String, SymbolTableItem>();
	}

	public void put(SymbolTableItem item) throws ItemAlreadyExistsException {
		if(items.containsKey(item.getKey())){
			throw new ItemAlreadyExistsException();
		}
		items.put(item.getKey(), item);
	}

	public SymbolTableItem getInCurrentScope(String key) {
		return items.get(key);
	}

	public SymbolTableItem get(String key) throws ItemNotFoundException {
		SymbolTableItem value = items.get(key);
		if(value == null && pre != null)
			return pre.get(key);
		else if(value == null)
			throw new ItemNotFoundException();
		else
			return value;
	}

	public SymbolTableItem get_parent(String key) throws ItemNotFoundException {
		if(pre != null)
			return pre.get(key);
		return null;
	}


	public SymbolTable getPreSymbolTable() {
		return pre;
	}
}
