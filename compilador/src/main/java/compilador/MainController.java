package compilador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import compilador.TablaID.Identificador;
import compilador.TablaLit.Literal;
import compilador.TablaToken.Token;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private TabPane TabEditor, TabTbls;
    @FXML
    private Button ToolBarBtnAbrir, ToolBarBtnNuevo, ToolBarBtnGuardar, ToolBarBtnAnalizar;
    @FXML
    private MenuItem MenuItemNuevo, MenuItemAbrir, MenuItemGuardar, MISalir;
    @FXML
    private TextArea TxtSinRes, TxtConsola;
    @FXML
    TableView<Token> TblTokens;
    @FXML
    TableView<Literal> TblLit;

    @FXML
    private void AccionMISalir() {
        ((Stage) ToolBarBtnAbrir.getParent().getScene().getWindow()).close();
    }

    /**
     * Metodo encargado de generar una nueva pestana y un nuevo archivo de codigo
     * 
     * @implSpec
     *           Obtiene el stage donde se mostraran las ventanas emergentes.
     * @implSpec
     *           Obtiene la ruta donde se guardara el nuevo archivo.
     * @implSpec
     *           Obtiene el nombre que se le asignara al archivo en el formato
     *           deseado.
     * @implSpec
     *           Crea el archivo, lo agrega al contenedor de pestanas.
     * @implSpec
     *           Y lo agrega a la lista de los archivos abiertos actualmente.
     */
    @FXML
    private void AccionToolBarBtnNuevo() {
        try {
            String nombre, ruta;
            Archivo arc;
            Stage stage = (Stage) TabEditor.getScene().getWindow(); // ? obtiene la ventana principal

            if (stage == null) {
                mostrarAlerta("Error", "No se pudo obtener la ventana principal.");
                return;
            }

            ruta = seleccionarDir(stage);
            System.out.println("Directorio seleccionado: " + ruta);
            nombre = mostrarInputDialog("Escribe nombre de archivo", null, "Escribe el nombre del archivo");

            if (nombre != null) {
                if (!nombre.endsWith(".lcl"))
                    nombre += ".lcl";
                arc = new Archivo(ruta, nombre, "");
                App.archivos.add(arc);
                agregarPestana(arc);
            } else
                mostrarAlerta("Error", "Nombre de archivo no proporcionado");

        } catch (Exception e) {
            System.out.println("Error en AccionToolBarBtnNuevo: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "Error al procesar la acción Nuevo: " + e.getMessage());
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
    private void AccionToolBarBtnGuardar() {
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
            sin.importarExcel("src/main/resources/compilador/Simbolos_MegaVerdaderos.txt");
            lex.Analizar(entrada); // ? Mandamos la entrada de codigo a lexico

            // ? Control de seguimiento para analisis de tokens
            while (ban) {
                token = App.tbl_token.SiguienteToken();
                if (token == null)
                    break;
                // ban = sin.AnalizarToken(token);
            }

            App.tbl_token.MostrarTokens(TblTokens);
            App.tbl_lit.MostrarLits(TblLit);
            App.tbl_error.MostrarErrores(TxtConsola);

            TxtSinRes.clear();
            TxtSinRes.appendText("Pila:\n");
            sin.historial_pila.forEach(e -> TxtSinRes.appendText(e + "\n"));

            // ? Si existe algun error, el analisis fue incorrecto, en caso contrario,
            // ? analisis correcto
            if (!sin.errores.isEmpty()) {
                TxtSinRes.appendText("Analisis Sintactico Erroneo");
                sin.errores.forEach(e -> TxtConsola.appendText("- " + e + "\n"));
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

            App.pestanas.add(pestana);
            tab = new Tab(arc.getRuta().getFileName().toString(), pestana);
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