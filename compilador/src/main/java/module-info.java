module compilador {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.fxmisc.richtext;
    requires org.fxmisc.flowless;
    requires reactfx;
    requires javafx.base;
    requires org.json;

    opens compilador to javafx.fxml;

    exports compilador;
}
