package compilador;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TablaLit {
    public static List<Literal> tbl_lit;

    public TablaLit() {
        tbl_lit = new ArrayList<>();
    }

    public int AgregarLit(String tipo, String valor, String rep_text) {
        Literal lit = new Literal(tipo, valor, rep_text);

        if (!ContieneLit(valor))
            tbl_lit.add(lit);

        for (Literal l : tbl_lit) {
            if (l.getValor().equals(valor))
                return tbl_lit.indexOf(l);
        }

        return -1;
    }

    public Literal BuscarID(int ind) {
        return tbl_lit.get(ind);
    }

    public void MostrarLits(TableView<Literal> tabla) {

        tabla.getColumns().clear();

        TableColumn<Literal, String> columnTipo = new TableColumn<>("Tipo");
        columnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        TableColumn<Literal, String> columnValor = new TableColumn<>("Valor");
        columnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        TableColumn<Literal, String> columnRepText = new TableColumn<>("Representacion Textual");
        columnRepText.setCellValueFactory(new PropertyValueFactory<>("rep_text"));

        tabla.getColumns().addAll(columnTipo, columnValor, columnRepText);

        ObservableList<Literal> datos = FXCollections.observableArrayList();

        for (Literal lit : tbl_lit)
            datos.add(lit);

        tabla.setItems(datos);

    }

    private boolean ContieneLit(String valor) {
        boolean ban = false;
        for (Literal l : tbl_lit) {
            if (l.getValor().equals(valor)) {
                ban = true;
                break;
            }
        }
        return ban;
    }

    public static class Literal {
        private final SimpleStringProperty tipo, valor, rep_text;

        public Literal(String tipo, String valor, String rep_text) {
            this.tipo = new SimpleStringProperty(tipo);
            this.valor = new SimpleStringProperty(valor);
            this.rep_text = new SimpleStringProperty(rep_text);
        }

        @SuppressWarnings("exports")
        public SimpleStringProperty tipoProperty() {
            return tipo;
        }

        @SuppressWarnings("exports")
        public SimpleStringProperty valorProperty() {
            return valor;
        }

        @SuppressWarnings("exports")
        public SimpleStringProperty repTextProperty() {
            return rep_text;
        }

        public String getTipo() {
            return tipo.get();
        }

        public void setTipo(String tipo) {
            this.tipo.set(tipo);
        }

        public String getValor() {
            return valor.get();
        }

        public void setValor(String valor) {
            this.valor.set(valor);
        }

        public String getRep_text() {
            return rep_text.get();
        }

        public void setRep_text(String rep_text) {
            this.rep_text.set(rep_text);
        }
    }
}
