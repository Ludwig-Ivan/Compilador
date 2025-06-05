package compilador;

import java.io.File;

import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ArbolProyecto {

    public static String ruta_raiz;

    public ArbolProyecto(String ruta, TreeView<File> treeview) {
        File archivo = new File(ruta);
        if (!archivo.exists() || !archivo.isDirectory()) {
            throw new IllegalArgumentException("Ruta inválida");
        }

        ruta_raiz = ruta;
        TreeItem<File> root = crearArbol(archivo);
        treeview.setRoot(root);
        root.setExpanded(true); // Expande la raíz por defecto
    }

    private TreeItem<File> crearArbol(File archivo) {
        TreeItem<File> nodo = new TreeItem<>(archivo);

        if (archivo.isDirectory()) {
            Image img = new Image(getClass().getResourceAsStream("/icons/folder.png"));
            ImageView icon = new ImageView(img);
            icon.setFitWidth(16); // ancho deseado
            icon.setFitHeight(16); // alto deseado
            nodo.setGraphic(icon);
        } else if (archivo.getName().endsWith(".lcl")) {
            Image img = new Image(getClass().getResourceAsStream("/icons/file.png"));
            ImageView icon = new ImageView(img);
            icon.setFitWidth(16); // ancho deseado
            icon.setFitHeight(16); // alto deseado
            nodo.setGraphic(icon);
        } else {
            nodo.setGraphic(new Label("❓")); // otros tipos de archivo
        }

        if (archivo.isDirectory()) {
            File[] hijos = archivo.listFiles();

            if (hijos != null) {
                for (File hijo : hijos) {
                    if (hijo.isDirectory() || hijo.getName().toLowerCase().endsWith(".lcl")) {
                        nodo.getChildren().add(crearArbol(hijo));
                    }
                }
            }
        }
        return nodo;
    }
}