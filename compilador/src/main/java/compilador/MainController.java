package compilador;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {

    // ? Componentes de la vista asociada
    @FXML
    private TabPane TabEditor;
    @FXML
    private Button ToolBarBtnAbrir, ToolBarBtnNuevo, ToolBarBtnGuardar;

    // ? Metodo de la vista asociada
    @FXML
    private void AccionToolBarBtnAbrir() throws IOException {
        Stage stage = (Stage) TabEditor.getScene().getWindow();
        seleccionarArc(stage);
    }

    @FXML
    private void AccionToolBarBtnNuevo() throws IOException {
        Stage stage = (Stage) TabEditor.getScene().getWindow();
        seleccionarDir(stage);
    }

    @FXML
    private void AccionToolBarBtnGuardar() throws IOException {
        Tab tab = TabEditor.getSelectionModel().getSelectedItem();
        if (tab != null) {
            if (tab.getContent() instanceof Pestana) {
                Pestana pest_act = (Pestana) tab.getContent();
                String content = pest_act.getText();
                String ruta = pest_act.getRuta();
                App.archivos.forEach(arc -> {
                    if (arc.getRuta().toString().equals(ruta)) {
                        arc.write(content);
                    }
                });
            }
        } else {
            // Codigo para Guardar Como
        }
    }

    // ? Metodo para Zoom de Ventana
    @FXML
    private void AccionMenuBarBtnZoom() {

    }

    // ? Metodo para seleccionar directorio y crear archivo, y abrir ventana
    private void seleccionarDir(Stage stage) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Selecciona una carpeta");
        String ruta = dirChooser.showDialog(stage).getAbsolutePath();

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Entrada requerida");
        dialog.setHeaderText(null);
        dialog.setContentText("Escriba el nombre del archivo");

        Optional<String> resultado = dialog.showAndWait();

        String res = resultado.orElse(null);

        if (ruta != null && res != null) {
            Archivo arc = new Archivo(ruta, res, ".lcl");
            agregarPestana(arc);
        } else {
            Alert oba = new Alert(AlertType.ERROR);
            oba.setTitle("Error");
            oba.setContentText("Error en la ruta o nombre de archivo");
        }

    }

    // ? Metodo para seleccionar archivo y crear pestana
    private void seleccionarArc(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione un archivo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto", "*.lcl"));
        File res = fileChooser.showOpenDialog(stage);

        if (res != null) {
            String ruta = res.getAbsolutePath();
            System.out.println("Ruta seleccionada: " + ruta);
            Archivo arc = new Archivo(ruta);
            agregarPestana(arc);
        }
    }

    // ? Metodo para agregar una Pestana
    private void agregarPestana(Archivo arc) {
        App.archivos.add(arc);
        App.pestanas.add(new Pestana(arc.getRuta().toString()));
        Pestana lastp = App.pestanas.lastElement();
        lastp.setText(arc.toString());
        Tab tab = new Tab(lastp.getRuta(), lastp);
        TabEditor.getTabs().add(tab);
    }
}
