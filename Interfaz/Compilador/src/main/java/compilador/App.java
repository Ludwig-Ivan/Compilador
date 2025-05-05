package compilador;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Vector;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Vector<Archivo> archivos = new Vector<>();
    public static Vector<Pestana> pestanas = new Vector<>();
    public static int fontSizeGlobal = 12;

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main"));
        scene.getStylesheets().add(App.class.getResource("java-keywords.css").toExternalForm());
        stage.setTitle("Compilador -> from Ludwig, Sergio y Leo");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}