package compilador;

import java.util.Stack;

import compilador.TablaToken.Token;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class AnalizadorDec {
    private TablaSimbolosManager tbl_manager;

    public AnalizadorDec() {
        tbl_manager = new TablaSimbolosManager();
    }

    public void Analizar() {
        App.tbl_token.ResetPos();
        Token tkn = App.tbl_token.SiguienteToken();
        String nombre;
        Stack<String> pilaScopes = new Stack<>();

        while (tkn != null) {
            String ToR = tkn.getRef();

            switch (ToR) {
                case "program":
                    tkn = App.tbl_token.SiguienteToken();
                    if (tkn != null && tkn.getTipo().equals("IDM")) {
                        nombre = App.tbl_id.BuscarID(Integer.parseInt(tkn.getRef())).getNom();
                        tbl_manager.entrarScope(nombre);
                        pilaScopes.push(nombre);
                    } else {
                        System.out.println("Error: se esperaba identificador tras 'program'");
                    }
                    break;

                case "int":
                case "float":
                case "char":
                case "bool":
                case "cadena":
                    tkn = App.tbl_token.SiguienteToken();
                    if (tkn != null && tkn.getTipo().equals("ID")) {
                        String id = App.tbl_id.BuscarID(Integer.parseInt(tkn.getRef())).getNom();
                        tbl_manager.insertarID("ID", "", ToR, id, "");
                    } else {
                        System.out.println("Error: se esperaba identificador tras tipo de dato");
                    }
                    break;

                case "func":
                case "procedure":
                    boolean esFuncion = ToR.equals("func");
                    tkn = App.tbl_token.SiguienteToken();

                    if (tkn != null && ((esFuncion && tkn.getTipo().equals("IDF"))
                            || (!esFuncion && tkn.getTipo().equals("IDP")))) {
                        nombre = App.tbl_id.BuscarID(Integer.parseInt(tkn.getRef())).getNom();
                        tbl_manager.insertarID(tkn.getTipo(), "", "", nombre, nombre);
                        tbl_manager.entrarScope(nombre);
                        pilaScopes.push(nombre);

                        // Función: esperar ": tipo"
                        if (esFuncion) {
                            tkn = App.tbl_token.SiguienteToken(); // ':'
                            tkn = App.tbl_token.SiguienteToken(); // tipo
                        }

                        // Esperar '('
                        tkn = App.tbl_token.SiguienteToken(); // '('

                        // Leer parámetros
                        tkn = App.tbl_token.SiguienteToken();
                        while (tkn != null && !tkn.getRef().equals(")")) {
                            String tipoParam = tkn.getRef();
                            tkn = App.tbl_token.SiguienteToken(); // ID
                            if (tkn != null && tkn.getTipo().equals("ID")) {
                                String idParam = App.tbl_id.BuscarID(Integer.parseInt(tkn.getRef())).getNom();
                                tbl_manager.insertarID("ID", "", tipoParam, idParam, "");
                                tkn = App.tbl_token.SiguienteToken();
                                if (tkn.getRef().equals(",")) {
                                    tkn = App.tbl_token.SiguienteToken();
                                }
                            }
                        }

                        // Esperar '{'
                        while (tkn != null && !tkn.getRef().equals("{")) {
                            tkn = App.tbl_token.SiguienteToken();
                        }

                        // Procesar bloque
                        int bloque = 1;
                        while (bloque > 0 && (tkn = App.tbl_token.SiguienteToken()) != null) {
                            if (tkn.getRef().equals("{"))
                                bloque++;
                            else if (tkn.getRef().equals("}"))
                                bloque--;
                        }

                        // Salir del scope correctamente
                        if (!pilaScopes.isEmpty()) {
                            tbl_manager.salirScope();
                            pilaScopes.pop();
                        }

                        // No avanzar más tokens aquí, ya lo hizo el while. Continuar con el bucle
                        // principal
                        continue;
                    } else {
                        System.out.println("Error: identificador inválido tras 'func' o 'procedure'");
                    }
                    break;

                // Las llaves externas no abren/cambian scope, ya estamos en el de 'program'
                case "{":
                case "}":
                    // Las ignoramos para evitar interferir con el control de scopes semánticos
                    break;

                default:
                    break;
            }

            tkn = App.tbl_token.SiguienteToken();
        }

        // Aseguramos el cierre del scope principal si quedó abierto
        while (!pilaScopes.isEmpty()) {
            tbl_manager.salirScope();
            pilaScopes.pop();
        }
    }

    public void MostrarTbls(Object view) {
        TabPane tabpanel = (TabPane) view;

        for (TablaSimbolos tbl_sim : tbl_manager.list_tbl_cmp) {
            Tab tab = new Tab(tbl_sim.getNombre(), tbl_sim.TblID());
            tabpanel.getTabs().add(tab);
        }
    }
}
