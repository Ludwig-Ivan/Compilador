package compilador;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import compilador.TablaID.Identificador;
import compilador.TablaLit.Literal;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TablaToken {

    private static List<Token> tbl_tokens;
    private static ListIterator<Token> tbIterator;
    private int pos;

    public TablaToken() {
        tbl_tokens = new ArrayList<>();
        tbIterator = tbl_tokens.listIterator();
    }

    public void AgregarToken(String tipo, int linea, int columna, String ref) {
        Token tkn = new Token(tipo, linea, columna, ref);
        pos = tbIterator.nextIndex();
        tbIterator.add(tkn);
        tbIterator = tbl_tokens.listIterator(pos);
    }

    public void ResetPos() {
        tbIterator = tbl_tokens.listIterator();
    }

    public Token AnteriorToken() {
        if (tbIterator.hasPrevious())
            return tbIterator.previous();
        return null;
    }

    public Token SiguienteToken() {
        if (tbIterator.hasNext())
            return tbIterator.next();
        return null;
    }

    public void MostrarTokens(TableView<Token> tabla) {

        tabla.getColumns().clear();

        TableColumn<Token, String> columnTipo = new TableColumn<>("Comp");
        columnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        TableColumn<Token, String> columnReferencia = new TableColumn<>("Lexema");
        columnReferencia.setCellValueFactory(new PropertyValueFactory<>("ref"));
        // TableColumn<Token, String> columnLinea = new TableColumn<>("Linea");
        // columnLinea.setCellValueFactory(new PropertyValueFactory<>("linea"));
        // TableColumn<Token, String> columnColumna = new TableColumn<>("Columna");
        // columnColumna.setCellValueFactory(new PropertyValueFactory<>("columna"));

        tabla.getColumns().addAll(columnTipo, /* columnLinea, columnColumna, */ columnReferencia);

        ObservableList<Token> datos = FXCollections.observableArrayList();

        for (Token id : tbl_tokens) {

            if (id.getTipo().equals("ID") || id.getTipo().equals("IDM") || id.getTipo().equals("IDF")
                    || id.getTipo().equals("IDP")) {
                Identificador i = App.tbl_id.BuscarID(Integer.parseInt(id.getRef()));
                datos.add(new Token(id.getTipo(), id.getLinea(), id.getColumna(), i.getNom()));
                continue;
            }

            if (id.getTipo().equals("LITERAL")) {
                Literal l = App.tbl_lit.BuscarID(Integer.parseInt(id.getRef()));
                datos.add(new Token(id.getTipo(), id.getLinea(), id.getColumna(), l.getComp()));
                continue;
            }

            datos.add(id);
        }

        tabla.setItems(datos);

    }

    /**
     * Esta clase representa un elemento tipo token, el cual resguarda
     * tanto tipo, linea y columna del token, y referencia a (tbl de simbolos y
     * palabras reservadas, tbl de identificadores o tbl de literales), permitiendo
     * su manipulacion y validacion de elementos
     */
    public static class Token {

        private final SimpleStringProperty tipo;
        private final SimpleStringProperty ref;
        private final SimpleIntegerProperty linea;
        private final SimpleIntegerProperty columna;

        /**
         * Constructor para la creacion de token
         * 
         * @param tipo    -> tipo de lexema
         * @param lexema  -> el lexema correspondiente
         * @param linea   -> numero de linea en donde se encontro el lexema
         * @param columna -> numero de columa en donde se encontro el lexema
         */
        public Token(String tipo, int linea, int columna, String ref) {
            this.tipo = new SimpleStringProperty(tipo);
            this.linea = new SimpleIntegerProperty(linea);
            this.columna = new SimpleIntegerProperty(columna);
            this.ref = new SimpleStringProperty(ref);
        }

        @SuppressWarnings("exports")
        public SimpleStringProperty tipoProperty() {
            return tipo;
        }

        @SuppressWarnings("exports")
        public SimpleStringProperty refProperty() {
            return ref;
        }

        @SuppressWarnings("exports")
        public SimpleIntegerProperty lineaProperty() {
            return linea;
        }

        @SuppressWarnings("exports")
        public SimpleIntegerProperty columnaProperty() {
            return columna;
        }

        /**
         * Metodo sobre escrito para convertir el token en string
         * 
         * @return -> regresa una cadena con informacion del lexema
         */
        @Override
        public String toString() {
            return String.format("%d~%d~%s~%s", linea.get(), columna.get(), tipo.get(), ref.get());
        }

        public String getTipo() {
            return tipo.get();
        }

        public void setTipo(String tipo) {
            this.tipo.set(tipo);
        }

        public String getRef() {
            return ref.get();
        }

        public void setRef(String ref) {
            this.ref.set(ref);
        }

        public int getLinea() {
            return linea.get();
        }

        public void setLinea(int linea) {
            this.linea.set(linea);
            ;
        }

        public int getColumna() {
            return columna.get();
        }

        public void setColumna(int columna) {
            this.columna.set(columna);
        }
    }
}
