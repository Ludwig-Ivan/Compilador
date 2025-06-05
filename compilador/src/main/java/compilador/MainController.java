package compilador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.regex.Pattern;

import compilador.TablaLit.Literal;
import compilador.TablaToken.Token;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;

public class MainController {

    @FXML
    private Menu MenuItemRecientes;
    @FXML
    private TabPane TabEditor, TabTbls;
    @FXML
    private Button ToolBarBtnAbrir, ToolBarBtnNuevo, ToolBarBtnGuardar, ToolBarBtnAnalizar;
    @FXML
    private MenuItem MenuItemNuevo, MenuItemAbrir, MenuItemGuardar, MISalir, MIRutas;
    @FXML
    private TextArea TxtSinRes, TxtConsola;
    @FXML
    TableView<Token> TblTokens;
    @FXML
    TableView<Literal> TblLit;
    @FXML
    TreeView<File> TVArc;
    @FXML
    private Parent root; // Puede ser AnchorPane, BorderPane, etc. con fx:id="root"

    @FXML
    public void initialize() {
        // ArbolProyecto arb = new ArbolProyecto(App.rutaProyecto, TVArc);
        Platform.runLater(() -> {
            if (App.mode.equals("white")) {
                AccionBtnModoClaro();
            } else {
                AccionBtnModoOscuro();
            }
            updateMenuItemRecent();

            TVArc.setCellFactory(tv -> new TreeCell<>() {
                @Override
                protected void updateItem(File item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.getName().isEmpty() ? item.getPath() : item.getName());

                        // 🔑 Recupera el graphic del TreeItem actual
                        TreeItem<File> treeItem = getTreeItem();
                        if (treeItem != null) {
                            setGraphic(treeItem.getGraphic());
                        }
                    }
                }
            });

            ArbolProyecto ap = new ArbolProyecto(App.rutaProyecto, TVArc);

            TVArc.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    TreeItem<File> itemSeleccionado = TVArc.getSelectionModel().getSelectedItem();

                    if (itemSeleccionado != null) {
                        File archivo = itemSeleccionado.getValue();
                        if (archivo.isFile() && archivo.getName().endsWith(".lcl")) {
                            // 🧠 Aquí defines lo que debe hacer: abrir en el editor, etc.
                            System.out.println("Abrir archivo: " + archivo.getAbsolutePath());

                            // Por ejemplo:
                            Archivo arc;
                            if (archivo.exists()) {
                                for (Archivo a : App.archivos)
                                    if (a.getRuta().toString().equals(archivo.getAbsolutePath())) {
                                        mostrarAlerta("Advertencia", "El archivo ya está abierto en otra pestaña.");
                                        return;
                                    }

                                arc = new Archivo(archivo.getAbsolutePath());
                                App.archivos.add(arc);
                                agregarPestana(arc);
                                addRecentFile(arc.getRuta().toString());
                                System.out.println("Pestaña abierta para: " + arc.getRuta());
                            }
                        }
                    }
                }
            });

            Scene scene = ToolBarBtnAbrir.getScene();

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.EQUALS, KeyCombination.CONTROL_DOWN),
                    () -> this.AccionBtnZoomIn());

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.MINUS, KeyCombination.CONTROL_DOWN),
                    () -> this.AccionBtnZoomOut());

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
                    () -> this.AccionToolBarBtnGuardar());
        });

    }

    @FXML
    private void AccionMIRutas() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecciona una carpeta");
        File arc = directoryChooser.showDialog(ToolBarBtnAbrir.getParent().getScene().getWindow());
        App.rutaProyecto = arc.getAbsolutePath();
        // ArbolProyecto arb = new ArbolProyecto(arc.getAbsolutePath(), TVArc);
    }

    @FXML
    private void AccionMISalir() {
        App.updateConfig();
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void AccionToolBarBtnNuevo() {
        try {
            String nombre, ruta;
            Archivo arc;
            Stage stage = (Stage) TabEditor.getScene().getWindow();

            if (stage == null) {
                mostrarAlerta("Error", "No se pudo obtener la ventana principal.");
                return;
            }

            if (App.rutaProyecto.isBlank()) {
                ruta = seleccionarDir(stage);
            } else {
                if (new File(App.rutaProyecto).exists())
                    ruta = App.rutaProyecto;
                else
                    ruta = seleccionarDir(stage);
            }

            System.out.println("Directorio seleccionado: " + ruta);
            nombre = mostrarInputDialog("Escribe nombre de archivo", null, "Escribe el nombre del archivo");

            if (nombre != null) {
                if (!nombre.endsWith(".lcl"))
                    nombre += ".lcl";
                arc = new Archivo(ruta, nombre, "");
                App.archivos.add(arc);
                agregarPestana(arc);
                addRecentFile(ruta);
            } else
                mostrarAlerta("Error", "Nombre de archivo no proporcionado");

        } catch (Exception e) {
            System.out.println("Error en AccionToolBarBtnNuevo: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "Error al procesar la acción Nuevo: " + e.getMessage());
        }
    }

    @FXML
    public void AccionBtnNuevaVentana() {
        try {
            // Verifica que el archivo FXML esté en el mismo paquete que esta clase en la
            // carpeta resources
            URL fxmlLocation = MainController.class.getResource("main.fxml");
            if (fxmlLocation == null) {
                throw new IOException("FXML no encontrado en /com/ejemplo/ventanaSecundaria.fxml");
            }

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(fxmlLocation);
            Parent root = loader.load();

            Stage nuevaVentana = new Stage();
            nuevaVentana.setTitle("Ventana Secundaria");
            nuevaVentana.setScene(new Scene(root, 400, 300));
            nuevaVentana.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error al cargar FXML");
            alert.setHeaderText("No se pudo abrir la nueva ventana");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void AccionBtnModoOscuro() {
        App.mode = "black";
        Scene scene = ToolBarBtnAbrir.getScene();

        scene.getStylesheets().removeAll(App.lightModeCss, App.darkModeCss, App.darkKeywordsCss);
        scene.getStylesheets().add(App.darkModeCss);
        scene.getStylesheets().add(App.darkKeywordsCss);

    }

    @FXML
    private void AccionBtnModoClaro() {
        Scene scene = ToolBarBtnAbrir.getScene();

        scene.getStylesheets().removeAll(App.lightModeCss, App.darkModeCss, App.darkKeywordsCss);
        scene.getStylesheets().add(App.lightModeCss);
        App.mode = "white";
    }

    @FXML
    public void AccionBtnZoomIn() {
        List<Tab> tabs = TabEditor.getTabs();
        App.fontSize += 2;
        for (Tab tab : tabs) {
            Node content = tab.getContent();
            Pestana pest = (Pestana) content;
            pest.setFontSize(App.fontSize);
        }
    }

    //Dar formato al texto
    @FXML
    private void AccionBtnFormatear() {
        Tab tab = TabEditor.getSelectionModel().getSelectedItem();
        if (tab != null && tab.getContent() instanceof Pestana) {
            Pestana pest_act = (Pestana) tab.getContent();
            String content = pest_act.getText();

            String[] lines = content.split("\\r?\\n");
            StringBuilder formatted = new StringBuilder();
            int indentLevel = 0;

            for (String line : lines) {
                String trimmed = line.trim();

                if (trimmed.startsWith("}")) {
                    indentLevel = Math.max(0, indentLevel - 1);
                }

                for (int i = 0; i < indentLevel; i++) {
                    formatted.append("\t");
                }
                formatted.append(trimmed).append("\n");

                if (trimmed.endsWith("{")) {
                    indentLevel++;
                }
            }

            pest_act.setText(formatted.toString());
        }
    }

    //Cambiar el nombre de una variable
    @FXML
    private void AccionBtnRenombrar() {
        Tab tab = TabEditor.getSelectionModel().getSelectedItem();
        if (tab != null && tab.getContent() instanceof Pestana) {
            String palabraOriginal, palabraNueva;
            palabraOriginal = mostrarInputDialog("Escribe el nombre del id a cambiar el nombre:", null, "Escribe el nombre del id a cambiar el nombre:");
            palabraNueva = mostrarInputDialog("Escribe el nuevo nombre del id:", null, "Escribe el nuevo nombre del id:");
            Pestana pest_act = (Pestana) tab.getContent();
            String content = pest_act.getText();

            String contenidoModificado = content.replaceAll("\\b" + Pattern.quote(palabraOriginal) + "\\b", palabraNueva);


            pest_act.setText(contenidoModificado);
        }
    }

    @FXML
    public void AccionBtnZoomOut() {
        List<Tab> tabs = TabEditor.getTabs();
        App.fontSize -= 2;
        for (Tab tab : tabs) {
            Node content = tab.getContent();
            Pestana pest = (Pestana) content;
            pest.setFontSize(App.fontSize);
        }
    }

    /**
     * Metodo de accion para boton abrir de la tool-bar.
     * Se encarga de abrir un nuevo archivo de codigo, y mostrarlo en el editor de
     * codigo.
     * 
     * @implSpec
     *           Obtiene el archivo que se mando a seleccionar.
     * @implSpec
     *           Verifica que no exista otro archivo abierto
     * @implSpec
     *           Agrega el archivo en la lista de archivos activos.
     * @implSpec
     *           DEspues agrega la pestana al editor de codigo
     */
    @FXML
    private void AccionToolBarBtnAbrir() {
        try {
            Stage stage = (Stage) TabEditor.getScene().getWindow();
            String ruta;
            Archivo arc;
            if (stage != null) {
                ruta = seleccionarArc(stage, "*.lcl");
                if (ruta != null) {
                    for (Archivo a : App.archivos)
                        if (a.getRuta().toString().equals(ruta)) {
                            mostrarAlerta("Advertencia", "El archivo ya está abierto en otra pestaña.");
                            return;
                        }

                    arc = new Archivo(ruta);
                    App.archivos.add(arc);
                    agregarPestana(arc);
                    addRecentFile(ruta);
                    System.out.println("Pestaña abierta para: " + arc.getRuta());
                }
            }
        } catch (Exception e) {
            System.out.println("Error en AccionToolBarBtnAbrir: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "Error al procesar la acción Abrir: " + e.getMessage());
        }
    }

    /**
     * Metodo de accion del boton de tool-bar para guardar la ventana de codigo
     * actual.
     * 
     * @implSpec
     *           Obtiene el tab seleccionado y determina que no sea null y su
     *           contenido sea de pestana.
     * @implSpec
     *           Obtiene el contenido de la pestana actual, la ruta de ese pestana.
     * @implSpec
     *           Busca el archivo correspondiente en la lista de archivos abiertos.
     * @implSpec
     *           Al encontrarlo sobre-escribe el contenido anterior con el contenido
     *           actual de la pestana.
     * @implSpec
     *           En caso de no tener una pestana seleccionada, mandara una alerta.
     */
    @FXML
    public void AccionToolBarBtnGuardar() {
        Tab tab = TabEditor.getSelectionModel().getSelectedItem();
        if (tab != null && tab.getContent() instanceof Pestana) {
            Pestana pest_act = (Pestana) tab.getContent();
            String content = pest_act.getText(); // ? contenido de la pestana actual
            String ruta = pest_act.getRuta(); // ? ruta de la pestana actual
            System.out.println("Guardando contenido en: " + ruta);
            App.archivos.forEach(arc -> {
                // ? Si las rutas concuerda, sobre-escribe el contenido
                if (arc.getRuta().toString().equals(ruta)) {
                    arc.write(content);
                    System.out.println("Contenido guardado exitosamente");
                } // TODO : si las rutas no concuerda, mandar una alerta.
            });
        } else
            mostrarAlerta("Advertencia", "No hay ninguna pestaña seleccionada para guardar.");
    }

    /**
     * Metodo de accion para el boton de la tool-bar encargado de inicializar el
     * analisis lexico-sintactico.
     * 
     * @implSpec
     *           El metodo obtiene la ventana de codigo seleccionada en el momento.
     * @implSpec
     *           Obtiene el contenido de la ventana de codigo y esa es la entrada
     *           que el lexico usara para analizar.
     * @implSpec
     *           Enseguida se analiza token por token encontrados en la entrada y se
     *           indica si es posible seguir o se tiene que detener el analisis por
     *           alguna situacion critica (producciones no determinadas, o terminos
     *           de tokens inesperados).
     * @implSpec
     *           Si se encuentran errores tanto lexicos como sintacticos, son
     *           reportados, y se determina que el analisis fue incorrecto
     * @implSpec
     *           En caso contrario se reporta que el analisis se realizo con exito
     * @implSpec
     *           Si no encuentra alguna pestana seleccionada se lanzara una alerta
     */
    @FXML
    private void AccionToolBarBtnAnalizar() {
        Tab tab = TabEditor.getSelectionModel().getSelectedItem();

        // ? Determina si el contenido obtenido del tab seleccionado es una pestana
        if (tab != null && tab.getContent() instanceof Pestana) {
            Pestana pestana = (Pestana) tab.getContent();
            String entrada = pestana.getText();
            Lexico lex = new Lexico();
            Sintactico sin = new Sintactico("P");
            boolean ban = true;
            Token token;

            TxtConsola.clear();
            limpiarTblTab();
            sin.importarExcel(getClass().getResourceAsStream("/compilador/Simbolos_MegaVerdaderos.txt"));
            lex.Analizar(entrada); // ? Mandamos la entrada de codigo a lexico

            // ? Control de seguimiento para analisis de tokens
            while (ban) {
                token = App.tbl_token.SiguienteToken();
                if (token == null)
                    break;
                ban = sin.AnalizarToken(token);
            }

            App.tbl_token.MostrarTokens(TblTokens);
            App.tbl_lit.MostrarLits(TblLit);
            App.tbl_error.MostrarErrores(TxtConsola);

            TxtSinRes.clear();
            TxtSinRes.appendText("Pila:\n");
            sin.historial_pila.forEach(e -> TxtSinRes.appendText(e + "\n"));

            // ? Si existe algun error, el analisis fue incorrecto, en caso contrario,
            // ? analisis correcto
            if (!App.tbl_error.getErrores().isEmpty()) {
                TxtSinRes.appendText("Analisis Sintactico Erroneo");

            } else {
                TxtSinRes.appendText("Analisis sintáctico completado correctamente");
                AnalizadorDec anadec = new AnalizadorDec();
                anadec.Analizar();
                anadec.MostrarTbls(TabTbls);
            }

        } else {
            mostrarAlerta("Advertencia", "No hay ninguna pestaña seleccionada.");
        }
    }

    /**
     * Metodo encargado de la seleccion de directorio.
     * Hace uso del obtjeto DirectoryChooser para abrir una ventana de dialogo en la
     * posicion de stage mandado.
     * 
     * @param stage -> Ventana en la que aparecera el selector de directorios.
     * @return Devuelve el AbsolutePath del directorio seleccionado, en caso
     *         contrario null
     */
    private String seleccionarDir(Stage stage) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Selecciona una carpeta");
        File dir = dirChooser.showDialog(stage);
        return dir != null ? dir.getAbsolutePath() : null;
    }

    /**
     * Metodo encargado de la seleccion de archivos con cierto tipo de extension.
     * 
     * @param stage -> Ventana en la q se mostrara el selector de archivos.
     * @param ext   -> Extension de archivos que buscara el selector.
     * @return La ruta del archivo seleccionado | null en caso de no encontrarlo
     */
    private String seleccionarArc(Stage stage, String ext) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione un archivo");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Archivos de texto", ext != null ? ext : "*.txt"));
        if (!App.rutaProyecto.isBlank()) {
            fileChooser.setInitialDirectory(new File(App.rutaProyecto));
        }

        File res = fileChooser.showOpenDialog(stage);
        return res != null ? res.getAbsolutePath().trim() : null;
    }

    /**
     * Metodo encargado de agregar al nueva pestana en referencia al archivo
     * recibido, al editor de codigo.
     * 
     * @implSpec
     *           Crea una nueva pestana con la ruta del archivo recibido.
     * @implSpec
     *           Agrega el contenido del archivo en la pestana si existe.
     * @implSpec
     *           Agrega esa pestana a la lista de pestanas activas.
     * @implSpec
     *           Agrega la pestana al editor de codigo.
     * @implSpec
     *           Selecciona la pestana recien agregada.
     * 
     * @param arc -> Archivo a crear pestana
     */
    private void agregarPestana(Archivo arc) {
        try {
            Pestana pestana = new Pestana(arc.getRuta().toString());
            String contenido = arc.toString();
            Tab tab;

            if (contenido != null)
                pestana.setText(contenido);
            pestana.setFontSize(App.fontSize);
            tab = new Tab(arc.getRuta().getFileName().toString(), pestana);

            tab.setOnClosed((e) -> {
                Pestana pest = (Pestana) tab.getContent();
                Archivo at = null;
                for (Archivo a : App.archivos) {
                    if (a.getRuta().toAbsolutePath().toString().equals(pest.getRuta())) {
                        at = a;
                        break;
                    }
                }
                App.archivos.remove(at);
            });

            TabEditor.getTabs().add(tab);
            TabEditor.getSelectionModel().select(tab);
            System.out.println("Pestaña agregada exitosamente: " + arc.getRuta().getFileName());

        } catch (Exception e) {
            System.out.println("Error al agregar pestaña: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo agregar la pestaña: " + e.getMessage());
        }
    }

    /**
     * Metodo encargado de mostrar una alerta
     * 
     * @param titulo  -> Titulo de la alerta
     * @param mensaje -> Mensaje de la alerta
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Metodo encargado de mostrar un InputDialog para obtener una respuesta.
     * 
     * @param titulo    -> Titulo del dialog
     * @param cabecera  -> Cabecera del dialog
     * @param contenido -> Contenido del dialog
     * @return Resultado obtenido de la ventana del dialog | null en caso de no
     *         exitir respuesta.
     */
    private String mostrarInputDialog(String titulo, String cabecera, String contenido) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titulo);
        dialog.setHeaderText(cabecera);
        dialog.setContentText(contenido);

        Optional<String> resultado = dialog.showAndWait();
        if (!resultado.isPresent() || resultado.get().trim().isEmpty())
            return null;
        return resultado.get().trim();
    }

    private void limpiarTblTab() {
        List<Tab> pest = new ArrayList<>();

        for (Tab t : TabTbls.getTabs()) {
            if (t.getText().equals("Tokens") || t.getText().equals("Literales") || t.getText().equals("Sintactico")) {
                pest.add(t);
            }
        }

        TabTbls.getTabs().clear();

        for (Tab t : pest) {
            TabTbls.getTabs().add(t);
        }
    }

    private void addRecentFile(String ruta) {
        if (App.recentfiles.contains(ruta)) {
            App.recentfiles.remove(App.recentfiles.indexOf(ruta));
            App.recentfiles.add(ruta);
        } else {
            if (App.recentfiles.size() >= 10) {
                App.recentfiles.remove(0);
                App.recentfiles.add(ruta);
            } else {
                App.recentfiles.add(ruta);
            }
        }
        updateMenuItemRecent();
    }

    private void updateMenuItemRecent() {
        MenuItemRecientes.getItems().clear();

        for (int con = App.recentfiles.size() - 1; con >= 0; con--) {
            File file = new File(App.recentfiles.get(con));
            String fileName = file.getName();
            MenuItem item = new MenuItem(fileName);
            item.setOnAction(event -> {
                Archivo arc;
                if (file.exists()) {
                    for (Archivo a : App.archivos)
                        if (a.getRuta().toString().equals(file.getAbsolutePath())) {
                            mostrarAlerta("Advertencia", "El archivo ya está abierto en otra pestaña.");
                            return;
                        }

                    arc = new Archivo(file.getAbsolutePath());
                    App.archivos.add(arc);
                    agregarPestana(arc);
                    updateMenuItemRecent();
                    System.out.println("Pestaña abierta para: " + arc.getRuta());
                }
            });

            MenuItemRecientes.getItems().add(item);
        }
    }
}

class Componente {
    private SimpleStringProperty id;
    private SimpleStringProperty tipo;
    private SimpleStringProperty grupo;
    private SimpleStringProperty line;
    private SimpleStringProperty pos;

    public Componente(String id, String tipo, String grupo, String line, String pos) {
        this.id = new SimpleStringProperty(id);
        this.tipo = new SimpleStringProperty(tipo);
        this.grupo = new SimpleStringProperty(grupo);
        this.line = new SimpleStringProperty(line);
        this.pos = new SimpleStringProperty(pos);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id = new SimpleStringProperty(id);
    }

    public String getTipo() {
        return tipo.get();
    }

    public void setTipo(String tipo) {
        this.tipo = new SimpleStringProperty(tipo);
    }

    public String getGrupo() {
        return grupo.get();
    }

    public void setGrupo(String grupo) {
        this.grupo = new SimpleStringProperty(grupo);
    }

    public String getLine() {
        return line.get();
    }

    public void setLine(String line) {
        this.line = new SimpleStringProperty(line);
    }

    public String getPos() {
        return pos.get();
    }

    public void setPos(String pos) {
        this.pos = new SimpleStringProperty(pos);
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public SimpleStringProperty tipoProperty() {
        return tipo;
    }

    public SimpleStringProperty grupoProperty() {
        return grupo;
    }

    public SimpleStringProperty lineProperty() {
        return line;
    }

    public SimpleStringProperty posProperty() {
        return pos;
    }

}