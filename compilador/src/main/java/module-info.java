module compilador {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.fxmisc.richtext;
    requires org.fxmisc.flowless;
    requires reactfx;
    requires javafx.base;

    opens compilador to javafx.fxml;

    exports compilador;
}
