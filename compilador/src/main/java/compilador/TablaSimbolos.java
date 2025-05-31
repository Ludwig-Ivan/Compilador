package compilador;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TablaSimbolos {
    private String nombre;
    private List<Identificador> list_id;

    public TablaSimbolos(String nombre) {
        this.nombre = nombre;
        list_id = new ArrayList<>();
    }

    public boolean InsertarIdentificador(String comp, String valor, String tipo, String nombre, String link) {
        Identificador id = new Identificador(comp, valor, tipo, nombre, link);
        for (Identificador i : list_id) {
            if (i.getNombre().equals(id.getNombre())) {
                System.out.println("Identificador ya existente");
                return false;
            }
        }

        list_id.add(id);
        return true;
    }

    public String getNombre() {
        return nombre;
    }

    public void MostrarListID() {
        for (Identificador id : list_id) {
            System.out.println(
                    String.format("Comp: %s | Nom: %s | Tipo: %s | Valor: %s | Link: %s", id.getComp(),
                            id.getNombre(),
                            id.getTipo(),
                            id.getValor(),
                            id.getLink()));
        }
    }

    public TableView<Identificador> TblID() {
        TableView<Identificador> tblview = new TableView<>();

        TableColumn<Identificador, String> colNom = new TableColumn<>("Nombre");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Identificador, String> colComp = new TableColumn<>("Comp");
        colComp.setCellValueFactory(new PropertyValueFactory<>("comp"));
        TableColumn<Identificador, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        TableColumn<Identificador, String> colVal = new TableColumn<>("Valor");
        colVal.setCellValueFactory(new PropertyValueFactory<>("valor"));
        TableColumn<Identificador, String> colLink = new TableColumn<>("Link");
        colLink.setCellValueFactory(new PropertyValueFactory<>("link"));

        tblview.getColumns().addAll(colComp, colVal, colTipo, colNom, colLink);

        ObservableList<Identificador> datos = FXCollections.observableArrayList();

        for (Identificador id : list_id)
            datos.add(id);

        tblview.setItems(datos);

        return tblview;
    }

    public void setRefLast(String nom) {
        list_id.get(list_id.size() - 1).setLink(nom);
    }

    public static class Identificador {
        private final SimpleStringProperty comp, valor, tipo, nombre, link;

        public Identificador(String comp, String valor, String tipo, String nombre, String link) {
            this.comp = new SimpleStringProperty(comp);
            this.valor = new SimpleStringProperty(valor);
            this.tipo = new SimpleStringProperty(tipo);
            this.nombre = new SimpleStringProperty(nombre);
            this.link = new SimpleStringProperty(link);
        }

        public String getComp() {
            return comp.get();
        }

        public void setComp(String comp) {
            this.comp.set(comp);
        }

        public String getValor() {
            return valor.get();
        }

        public void setValor(String valor) {
            this.valor.set(valor);
        }

        public String getTipo() {
            return tipo.get();
        }

        public void setTipo(String tipo) {
            this.tipo.set(tipo);
        }

        public String getNombre() {
            return nombre.get();
        }

        public void setNombre(String nombre) {
            this.nombre.set(nombre);
        }

        public String getLink() {
            return link.get();
        }

        public void setLink(String link) {
            this.link.set(link);
        }
    }
}
