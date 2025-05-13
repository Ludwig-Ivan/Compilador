package compilador;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Vector;

public class App extends Application {

    private static Scene scene;
    public static Vector<Archivo> archivos = new Vector<>(); // ? Vector para el manejo de archivos
    public static Vector<Pestana> pestanas = new Vector<>(); // ? Vector para el manejo de pestanas
    // TODO: agregar mas configuraciones iniciales
    public static int fontSizeGlobal = 12; // ? Configuracion inicial de fuente establecida en la App

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