package compilador;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class App extends Application {

    private static Scene scene;
    // TODO: Analizar el manejo de archivos y pestanas
    public static Vector<Archivo> archivos = new Vector<>(); // ? Vector para el manejo de archivos
    public static Vector<Pestana> pestanas = new Vector<>(); // ? Vector para el manejo de pestanas

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
        tbl_sim_res.put("void", "RESERVADA");
        tbl_sim_res.put("true", "RESERVADA");
        tbl_sim_res.put("false", "RESERVADA");
        tbl_sim_res.put("break", "RESERVADA");
        tbl_sim_res.put("continue", "RESERVADA");
        tbl_sim_res.put("main", "RESERVADA");
        tbl_sim_res.put("read", "RESERVADA");

        // ? Simbolos
        // ? --> Simbolos Ordinarios
        tbl_sim_res.put(",", "SIMBOLO");
        tbl_sim_res.put(";", "SIMBOLO");
        tbl_sim_res.put("(", "SIMBOLO");
        tbl_sim_res.put(")", "SIMBOLO");
        tbl_sim_res.put("{", "SIMBOLO");
        tbl_sim_res.put("}", "SIMBOLO");
        tbl_sim_res.put("[", "SIMBOLO");
        tbl_sim_res.put("]", "SIMBOLO");
        tbl_sim_res.put("?", "SIMBOLO");
        tbl_sim_res.put(":", "SIMBOLO");
        tbl_sim_res.put("#", "SIMBOLO");
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
        tbl_sim_res.put("++", "SIMBOLO");
        tbl_sim_res.put("--", "SIMBOLO");
    }

    // TODO: agregar mas configuraciones iniciales
    public static int fontSizeGlobal = 12; // ? Configuracion inicial de fuente establecida en la App
    public static String rutaProyecto = "";

    /**
     * Metodo para inicializar la escena principal
     * 
     * @param stage -> el stage principal
     */
    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main"), 800, 485); // Especificar dimensiones explícitas
        scene.getStylesheets().add(App.class.getResource("java-keywords.css").toExternalForm()); // Corregir ruta del
                                                                                                 // CSS
        stage.setTitle("Compilador -> from Ludwig, Sergio y Leo");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();
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
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml")); // Corregir ruta del FXML
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}