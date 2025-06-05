package compilador;
//Comentario prueba
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.json.JSONObject;

public class App extends Application {

    private static Scene scene;
    public static Vector<Archivo> archivos = new Vector<>(); // ? Vector para el manejo de archivos

    // ? Elementos Globales del Compilador
    public static TablaToken tbl_token;
    public static TablaID tbl_id;
    public static TablaLit tbl_lit;
    public static TablaError tbl_error;
    public static HashMap<String, String> tbl_sim_res;

    // ? Inicializador de Instancia de la clase
    {
        tbl_sim_res = new HashMap<>();
        // ? Palabras Reservadas
        tbl_sim_res.put("if", "RESERVADA");
        tbl_sim_res.put("else", "RESERVADA");
        tbl_sim_res.put("while", "RESERVADA");
        tbl_sim_res.put("do", "RESERVADA");
        tbl_sim_res.put("return", "RESERVADA");
        tbl_sim_res.put("print", "RESERVADA");
        tbl_sim_res.put("int", "RESERVADA");
        tbl_sim_res.put("float", "RESERVADA");
        tbl_sim_res.put("char", "RESERVADA");
        tbl_sim_res.put("cadena", "RESERVADA");
        tbl_sim_res.put("bool", "RESERVADA");
        tbl_sim_res.put("true", "RESERVADA");
        tbl_sim_res.put("false", "RESERVADA");
        tbl_sim_res.put("break", "RESERVADA");
        tbl_sim_res.put("continue", "RESERVADA");
        tbl_sim_res.put("read", "RESERVADA");
        tbl_sim_res.put("program", "RESERVADA");
        tbl_sim_res.put("func", "RESERVADA");
        tbl_sim_res.put("procedure", "RESERVADA");
        tbl_sim_res.put("for", "RESERVADA");

        // ? Simbolos
        // ? --> Simbolos Ordinarios
        tbl_sim_res.put(",", "SIMBOLO");
        tbl_sim_res.put(";", "SIMBOLO");
        tbl_sim_res.put("(", "SIMBOLO");
        tbl_sim_res.put(")", "SIMBOLO");
        tbl_sim_res.put("{", "SIMBOLO");
        tbl_sim_res.put("}", "SIMBOLO");
        // tbl_sim_res.put("[", "SIMBOLO");
        // tbl_sim_res.put("]", "SIMBOLO");
        tbl_sim_res.put("?", "SIMBOLO");
        tbl_sim_res.put(":", "SIMBOLO");
        tbl_sim_res.put("#", "SIMBOLO");
        tbl_sim_res.put("@", "SIMBOLO");
        // ? --> Operadores
        tbl_sim_res.put("+", "SIMBOLO");
        tbl_sim_res.put("-", "SIMBOLO");
        tbl_sim_res.put("*", "SIMBOLO");
        tbl_sim_res.put("/", "SIMBOLO");
        tbl_sim_res.put("%", "SIMBOLO");
        tbl_sim_res.put("=", "SIMBOLO");
        tbl_sim_res.put("+=", "SIMBOLO");
        tbl_sim_res.put("-=", "SIMBOLO");
        tbl_sim_res.put("*=", "SIMBOLO");
        tbl_sim_res.put("/=", "SIMBOLO");
        tbl_sim_res.put("==", "SIMBOLO");
        tbl_sim_res.put("!=", "SIMBOLO");
        tbl_sim_res.put("<", "SIMBOLO");
        tbl_sim_res.put(">", "SIMBOLO");
        tbl_sim_res.put("<=", "SIMBOLO");
        tbl_sim_res.put(">=", "SIMBOLO");
        tbl_sim_res.put("&&", "SIMBOLO");
        tbl_sim_res.put("||", "SIMBOLO");
        tbl_sim_res.put("!", "SIMBOLO");
    }

    // TODO: agregar mas configuraciones iniciales
    // Hola como estas
    public static int fontSize = 12; // ? Configuracion inicial de fuente establecida en la App
    public static String rutaProyecto = "";
    public static String mode = "white";
    public static List<String> recentfiles;
    public static final String darkModeCss = App.class.getResource("/compilador/black_mode.css").toExternalForm();
    public static final String lightModeCss = App.class.getResource("/compilador/java-keywords.css").toExternalForm();
    public static final String darkKeywordsCss = App.class.getResource("/compilador/java-keywords-black-mode.css")
            .toExternalForm();

    /**
     * Metodo para inicializar la escena principal
     * 
     * @param stage -> el stage principal
     */
    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws IOException {

        stage.setOnCloseRequest(event -> {
            updateConfig();
        });

        cargarConfig();

        scene = new Scene(loadFXML("main"), 1025, 485); // Especificar dimensiones explícitas
        stage.setTitle("Compilador -> from Ludwig, Sergio y Leo");
        stage.setScene(scene);
        stage.setMinWidth(1025);
        stage.setMinHeight(500);
        stage.show();

    }

    private void cargarConfig() {
        Archivo arc = new Archivo("C:/Apps/Compilador/config.json");

        if (!arc.Existe()) {
            arc = new Archivo("C:/Apps/Compilador", "config", ".json");
            JSONObject obj = new JSONObject();
            obj.put("configAutor", "Compilator");
            obj.put("fontSize", 12);
            obj.put("rutaProyect", "");
            obj.put("mode", "white");
            obj.put("recentfiles", new ArrayList<>());
            arc.write(obj.toString());
        }

        if (arc.Existe()) {
            String jsonString = arc.toString();
            JSONObject obj = new JSONObject(jsonString);

            fontSize = obj.getInt("fontSize");
            rutaProyecto = obj.getString("rutaProyect");
            mode = obj.getString("mode");
            recentfiles = (List) obj.getJSONArray("recentfiles").toList();
        }
    }

    public static void updateConfig() {
        Archivo arc = new Archivo("C:/Apps/Compilador/config.json");
        if (arc.Existe()) {
            JSONObject obj = new JSONObject(arc.toString());
            obj.put("fontSize", fontSize);
            obj.put("rutaProyect", rutaProyecto);
            obj.put("mode", mode);
            obj.put("recentfiles", recentfiles);
            arc.write(obj.toString());
        }
    }

    /**
     * Se encarga de cargar y establecer como root en la scene principal
     * 
     * @param fxml -> nombre del archivo fxml
     * @throws IOException
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Carga un archivo .fxml a la scene principal
     * 
     * @param fxml -> nombre de archivo fxml para cargarlo en la scene
     * @return el elemento cargado (Parent) de la vista fxml
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml")); // Corregir
                                                                                       // ruta del
                                                                                       // FXML
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}