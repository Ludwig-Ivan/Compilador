package compilador;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TextArea;

public class TablaError {

    // Lista para almacenar errores
    private final List<Error> errores = new ArrayList<>();

    // Agregar un nuevo error
    public void agregarError(String tipo, String lexema, String linea, String columna, String desc) {
        errores.add(new Error(tipo, lexema, linea, columna, desc));
    }

    // Obtener todos los errores
    public List<Error> getErrores() {
        return errores;
    }

    // Mostrar errores
    @SuppressWarnings("exports")
    public void MostrarErrores(TextArea component) {
        int lin = 0;
        String tipo = "";
        if (!errores.isEmpty()) {
            component.clear();
            for (Error e : errores) {
                if (lin != Integer.parseInt(e.getLinea()) && !tipo.equals(e.getTipo())) {
                    lin = Integer.parseInt(e.getLinea());
                    tipo = e.getTipo();
                    component.appendText(e.toString());
                }
            }
        }
    }

    // Limpiar la lista de errores
    public void limpiar() {
        errores.clear();
    }

    // Clase interna que representa un error
    public static class Error {
        private final String tipo;
        private final String lexema;
        private final String linea;
        private final String columna;
        private final String desc;

        public Error(String tipo, String lexema, String linea, String columna, String desc) {
            this.tipo = tipo;
            this.lexema = lexema;
            this.linea = linea;
            this.columna = columna;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public String getTipo() {
            return tipo;
        }

        public String getLexema() {
            return lexema;
        }

        public String getLinea() {
            return linea;
        }

        public String getColumna() {
            return columna;
        }

        @Override
        public String toString() {
            return String.format("Error [%s] en la linea %s : %s \n", tipo, linea,
                    desc.contains("$") ? desc.replace("$", "<Fin de archivo>") : desc,
                    lexema.equals("$") ? "Fin de archivo" : lexema);
        }
    }
}
