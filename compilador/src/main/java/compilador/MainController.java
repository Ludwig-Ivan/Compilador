package compilador;

import java.io.File;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private TabPane TabEditor;
    @FXML
    private Button ToolBarBtnAbrir, ToolBarBtnNuevo, ToolBarBtnGuardar, ToolBarBtnAnalizar;
    @FXML
    private MenuItem MenuItemNuevo, MenuItemAbrir, MenuItemGuardar;
    @FXML
    private TextArea outputText;

    private AnalizadorLexico analizador = new AnalizadorLexico();

    // @FXML
    // private void initialize() {
    // System.out.println("Inicializando MainController");
    // // Verificar inyección de TabEditor y outputText
    // if (TabEditor == null) {
    // System.out.println("Error: TabEditor no está inyectado");
    // }
    // if (outputText == null) {
    // System.out.println("Error: outputText no está inyectado");
    // }
    // // Verificar inyección de botones del ToolBar
    // if (ToolBarBtnNuevo == null) {
    // System.out.println("Error: ToolBarBtnNuevo no está inyectado");
    // } else {
    // System.out.println("ToolBarBtnNuevo inyectado, disable: " +
    // ToolBarBtnNuevo.isDisable());
    // }
    // if (ToolBarBtnAbrir == null) {
    // System.out.println("Error: ToolBarBtnAbrir no está inyectado");
    // } else {
    // System.out.println("ToolBarBtnAbrir inyectado, disable: " +
    // ToolBarBtnAbrir.isDisable());
    // }
    // if (ToolBarBtnGuardar == null) {
    // System.out.println("Error: ToolBarBtnGuardar no está inyectado");
    // } else {
    // System.out.println("ToolBarBtnGuardar inyectado, disable: " +
    // ToolBarBtnGuardar.isDisable());
    // }
    // if (ToolBarBtnAnalizar == null) {
    // System.out.println("Error: ToolBarBtnAnalizar no está inyectado");
    // } else {
    // System.out.println("ToolBarBtnAnalizar inyectado, disable: " +
    // ToolBarBtnAnalizar.isDisable());
    // }
    // // Verificar inyección de MenuItems
    // if (MenuItemNuevo == null) {
    // System.out.println("Error: MenuItemNuevo no está inyectado");
    // } else {
    // System.out.println("MenuItemNuevo inyectado, disable: " +
    // MenuItemNuevo.isDisable());
    // }
    // if (MenuItemAbrir == null) {
    // System.out.println("Error: MenuItemAbrir no está inyectado");
    // } else {
    // System.out.println("MenuItemAbrir inyectado, disable: " +
    // MenuItemAbrir.isDisable());
    // }
    // if (MenuItemGuardar == null) {
    // System.out.println("Error: MenuItemGuardar no está inyectado");
    // } else {
    // System.out.println("MenuItemGuardar inyectado, disable: " +
    // MenuItemGuardar.isDisable());
    // }
    // }

    @FXML
    private void AccionToolBarBtnNuevo() {
        System.out.println("Botón Nuevo clicado (desde ToolBar o Menu)");
        try {
            Stage stage = (Stage) TabEditor.getScene().getWindow();
            if (stage == null) {
                System.out.println("Error: No se pudo obtener el Stage");
                mostrarAlerta("Error", "No se pudo obtener la ventana principal.");
                return;
            }
            System.out.println("Stage obtenido: " + stage.getTitle());
            seleccionarDir(stage);
        } catch (Exception e) {
            System.out.println("Error en AccionToolBarBtnNuevo: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "Error al procesar la acción Nuevo: " + e.getMessage());
        }
    }

    @FXML
    private void AccionToolBarBtnAbrir() {
        System.out.println("Botón Abrir clicado (desde ToolBar o Menu)");
        try {
            Stage stage = (Stage) TabEditor.getScene().getWindow();
            if (stage == null) {
                System.out.println("Error: No se pudo obtener el Stage");
                mostrarAlerta("Error", "No se pudo obtener la ventana principal.");
                return;
            }
            System.out.println("Stage obtenido: " + stage.getTitle());
            seleccionarArc(stage);
        } catch (Exception e) {
            System.out.println("Error en AccionToolBarBtnAbrir: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "Error al procesar la acción Abrir: " + e.getMessage());
        }
    }

    @FXML
    private void AccionToolBarBtnGuardar() {
        System.out.println("Botón Guardar clicado (desde ToolBar o Menu)");
        Tab tab = TabEditor.getSelectionModel().getSelectedItem();
        if (tab != null && tab.getContent() instanceof Pestana) {
            Pestana pest_act = (Pestana) tab.getContent();
            String content = pest_act.getText();
            String ruta = pest_act.getRuta();
            System.out.println("Guardando contenido en: " + ruta);
            App.archivos.forEach(arc -> {
                if (arc.getRuta().toString().equals(ruta)) {
                    arc.write(content);
                    System.out.println("Contenido guardado exitosamente");
                }
            });
        } else {
            System.out.println("No hay pestaña seleccionada para guardar");
            mostrarAlerta("Advertencia", "No hay ninguna pestaña seleccionada para guardar.");
        }
    }

    @FXML
    private void AccionToolBarBtnAnalizar() {
        System.out.println("Botón Analizar clicado");
        Tab tab = TabEditor.getSelectionModel().getSelectedItem();
        if (tab != null && tab.getContent() instanceof Pestana) {
            Pestana pestana = (Pestana) tab.getContent();
            String entrada = pestana.getText();
            System.out.println("Texto en pestaña: " + entrada);
            List<AnalizadorLexico.Token> tokens = analizador.analizar(entrada);
            StringBuilder resultado = new StringBuilder();
            boolean hasErrors = false;
            for (AnalizadorLexico.Token token : tokens) {
                resultado.append(token.toString()).append("\n");
                if (token.tipo.equals("ERROR")) {
                    hasErrors = true;
                }
            }
            System.out.println("Tokens generados:\n" + resultado.toString());
            outputText.setText(resultado.toString());
            if (hasErrors) {
                mostrarAlerta("Errores léxicos",
                        "Se encontraron errores léxicos. Revisa la pestaña Tokens para más detalles.");
            }
        } else {
            System.out.println("No hay pestaña seleccionada");
            mostrarAlerta("Advertencia", "No hay ninguna pestaña seleccionada.");
        }
    }

    private void seleccionarDir(Stage stage) {
        System.out.println("Seleccionando directorio para nuevo archivo");
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Selecciona una carpeta");
        File dir = dirChooser.showDialog(stage);
        if (dir == null) {
            System.out.println("No se seleccionó ninguna carpeta");
            return;
        }
        String ruta = dir.getAbsolutePath();
        System.out.println("Directorio seleccionado: " + ruta);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Entrada requerida");
        dialog.setHeaderText(null);
        dialog.setContentText("Escriba el nombre del archivo (sin extensión)");

        Optional<String> resultado = dialog.showAndWait();
        if (!resultado.isPresent() || resultado.get().trim().isEmpty()) {
            System.out.println("No se proporcionó un nombre de archivo");
            mostrarAlerta("Error", "Debe proporcionar un nombre de archivo válido.");
            return;
        }

        String nombre = resultado.get().trim();
        if (!nombre.endsWith(".lcl")) {
            nombre += ".lcl";
        }

        try {
            Archivo arc = new Archivo(ruta, nombre, "");
            App.archivos.add(arc);
            agregarPestana(arc);
            System.out.println("Pestaña creada para: " + arc.getRuta());
        } catch (Exception e) {
            System.out.println("Error al crear archivo: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo crear el archivo: " + e.getMessage());
        }
    }

    private void seleccionarArc(Stage stage) {
        System.out.println("Seleccionando archivo para abrir");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione un archivo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto", "*.lcl"));
        File res = fileChooser.showOpenDialog(stage);

        if (res != null) {
            String ruta = res.getAbsolutePath();
            System.out.println("Ruta seleccionada: " + ruta);
            for (Archivo arc : App.archivos) {
                if (arc.getRuta().toString().equals(ruta)) {
                    System.out.println("El archivo ya está abierto");
                    mostrarAlerta("Advertencia", "El archivo ya está abierto en otra pestaña.");
                    return;
                }
            }
            try {
                Archivo arc = new Archivo(ruta);
                App.archivos.add(arc);
                agregarPestana(arc);
                System.out.println("Pestaña abierta para: " + arc.getRuta());
            } catch (Exception e) {
                System.out.println("Error al abrir archivo: " + e.getMessage());
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo abrir el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("No se seleccionó ningún archivo");
        }
    }

    private void agregarPestana(Archivo arc) {
        System.out.println("Agregando pestaña para: " + arc.getRuta());
        try {
            Pestana pestana = new Pestana(arc.getRuta().toString());
            String contenido = arc.toString();
            if (contenido != null) {
                pestana.setText(contenido);
            }
            App.pestanas.add(pestana);
            Tab tab = new Tab(arc.getRuta().getFileName().toString(), pestana);
            TabEditor.getTabs().add(tab);
            TabEditor.getSelectionModel().select(tab);
            System.out.println("Pestaña agregada exitosamente: " + arc.getRuta().getFileName());
        } catch (Exception e) {
            System.out.println("Error al agregar pestaña: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo agregar la pestaña: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}