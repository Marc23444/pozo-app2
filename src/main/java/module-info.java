module com.example.applicationassignment2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;


    opens baseline to javafx.fxml;
    exports baseline;
}