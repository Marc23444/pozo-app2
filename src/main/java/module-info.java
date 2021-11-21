module baseline {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires com.google.gson;

    opens baseline to com.google.gson, javafx.fxml;

    exports baseline;

}