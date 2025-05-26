package compilador;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TextArea;

public class TablaError {

    // Lista para almacenar errores
    private final List<Error> errores = new ArrayList<>();

    // Agregar un nuevo error
    public void agregarError(String tipo, String lexema, int linea, int columna) {
        errores.add(new Error(tipo, lexema, linea, columna));
    }

    // Obtener todos los errores
    public List<Error> getErrores() {
        return errores;
    }

    // Mostrar errores
    @SuppressWarnings("exports")
    public void MostrarErrores(TextArea component) {
        if (!errores.isEmpty()) {
            component.clear();
            for (Error e : errores)
                component.appendText(e.toString());

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
        private final int linea;
        private final int columna;

        public Error(String tipo, String lexema, int linea, int columna) {
            this.tipo = tipo;
            this.lexema = lexema;
            this.linea = linea;
            this.columna = columna;
        }

        public String getTipo() {
            return tipo;
        }

        public String getLexema() {
            return lexema;
        }

        public int getLinea() {
            return linea;
        }

        public int getColumna() {
            return columna;
        }

        @Override
        public String toString() {
            return String.format("Error [%s] en '%s' (línea %d, columna %d) \n", tipo, lexema, linea, columna);
        }
    }
}
