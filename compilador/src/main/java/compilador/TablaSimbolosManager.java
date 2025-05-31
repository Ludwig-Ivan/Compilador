package compilador;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TablaSimbolosManager {
    private Stack<TablaSimbolos> scopeStack;
    public List<TablaSimbolos> list_tbl_cmp;

    public TablaSimbolosManager() {
        scopeStack = new Stack<>();
        list_tbl_cmp = new ArrayList<>();
    }

    public void entrarScope(String name) {
        scopeStack.push(new TablaSimbolos(name));
    }

    public String salirScope() {
        if (scopeStack.size() > 1) {
            list_tbl_cmp.add(scopeStack.pop());
            return list_tbl_cmp.get(list_tbl_cmp.size() - 1).getNombre();
        }

        if (scopeStack.size() == 1) {
            list_tbl_cmp.add(scopeStack.pop());
        }

        return null;
    }

    public void setCimaRef(String nombre) {
        scopeStack.peek().setRefLast(nombre);
    }

    public boolean insertarID(String comp, String valor, String tipo, String nombre, String link) {
        return scopeStack.peek().InsertarIdentificador(comp, valor, tipo, nombre, link);
    }
}
