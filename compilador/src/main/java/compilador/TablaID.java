package compilador;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TablaID {
    public static List<Identificador> tbl_id;

    public TablaID() {
        tbl_id = new ArrayList<>();
    }

    public int AgregarID(String nom) {
        Identificador id = new Identificador(nom);

        if (!ContieneID(nom))
            tbl_id.add(id);

        for (Identificador i : tbl_id) {
            if (i.getNom().equals(nom))
                return tbl_id.indexOf(i);
        }

        return -1;
    }

    public void CambiarTipoDatoID(int ind, String tipo_dato) {
        Identificador id = tbl_id.get(ind);
        id.setDato(tipo_dato);
    }

    public Identificador BuscarID(int ind) {
        return tbl_id.get(ind);
    }

    public void MostrarIDs(TableView<Identificador> tabla) {

        tabla.getColumns().clear();

        TableColumn<Identificador, String> columnNom = new TableColumn<>("Nombre");
        columnNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        TableColumn<Identificador, String> columnTipoDato = new TableColumn<>("Tipo Dato");
        columnTipoDato.setCellValueFactory(new PropertyValueFactory<>("dato"));
        TableColumn<Identificador, String> columnAmbito = new TableColumn<>("Ambito");
        columnAmbito.setCellValueFactory(new PropertyValueFactory<>("ambito"));

        tabla.getColumns().addAll(columnNom, columnTipoDato, columnAmbito);

        ObservableList<Identificador> datos = FXCollections.observableArrayList();

        for (Identificador id : tbl_id)
            datos.add(id);

        tabla.setItems(datos);

    }

    private boolean ContieneID(String nom) {
        boolean ban = false;
        for (Identificador id : tbl_id) {
            if (id.getNom().equals(nom)) {
                ban = true;
                break;
            }
        }
        return ban;
    }

    public void CambiarAmbito(int ind, String ambito) {
        Identificador id = tbl_id.get(ind);
        id.setAmbito(ambito);
    }

    public static class Identificador {

        private final SimpleStringProperty nom;
        private final SimpleStringProperty dato;
        private final SimpleStringProperty ambito;

        public Identificador(String nom) {
            this.nom = new SimpleStringProperty(nom);
            this.dato = new SimpleStringProperty();
            this.ambito = new SimpleStringProperty();
        }

        @SuppressWarnings("exports")
        public SimpleStringProperty nomProperty() {
            return nom;
        }

        @SuppressWarnings("exports")
        public SimpleStringProperty datoProperty() {
            return dato;
        }

        @SuppressWarnings("exports")
        public SimpleStringProperty ambitoroperty() {
            return ambito;
        }

        public String getNom() {
            return nom.get();
        }

        public void setNom(String nom) {
            this.nom.set(nom);
        }

        public String getDato() {
            return dato.get();
        }

        public void setDato(String dato) {
            this.dato.set(dato);
        }

        public String getAmbito() {
            return ambito.get();
        }

        public void setAmbito(String ambito) {
            this.ambito.set(ambito);
        }

    }
}
