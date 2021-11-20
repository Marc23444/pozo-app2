module baseline {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires com.google.gson;


    opens baseline to javafx.fxml;

    exports baseline;

}