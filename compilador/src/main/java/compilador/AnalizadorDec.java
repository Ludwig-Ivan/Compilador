package compilador;

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
        String nombre, ToR;
        int con = 0;

        while (tkn != null) {
            ToR = tkn.getRef();
            switch (ToR) {
                case "program": // Creacion de la tabla de simbolos general
                    tkn = App.tbl_token.SiguienteToken();
                    if (tkn.getTipo().equals("ID")) {
                        nombre = (App.tbl_id.BuscarID(Integer.parseInt(tkn.getRef()))).getNom();
                        tbl_manager.entrarScope(nombre);
                    } else {
                        System.out.println("Error al entrar al scope principal");
                    }
                    break;

                case "int":
                case "float":
                case "char":
                case "bool":
                case "cadena":
                    tkn = App.tbl_token.SiguienteToken();
                    if (tkn.getTipo().equals("ID")) {
                        tbl_manager.insertarID("ID", "", "",
                                (App.tbl_id.BuscarID(Integer.parseInt(tkn.getRef()))).getNom(), "");
                    } else {
                        System.out.println("Error al registrar la declaracion");
                    }
                    break;

                case "func":
                    tkn = App.tbl_token.SiguienteToken();
                    if (tkn.getTipo().equals("ID")) {
                        nombre = (App.tbl_id.BuscarID(Integer.parseInt(tkn.getRef()))).getNom();
                        tbl_manager.insertarID("IDF", "", "", nombre, "");
                        tbl_manager.entrarScope(nombre);
                    } else {
                        System.out.println("Error al entrar al scope principal");
                    }
                    break;

                case "main":
                    tbl_manager.insertarID("main", "", "", "main", "");
                    tbl_manager.entrarScope("main");
                    break;

                case "{":
                    con++;
                    break;
                case "}":
                    con--;
                    if (con == 1) {
                        tbl_manager.setCimaRef(tbl_manager.salirScope());
                    }
                    if (con == 0) {
                        tbl_manager.salirScope();
                    }
                    break;

                default:
                    break;
            }
            tkn = App.tbl_token.SiguienteToken();
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
