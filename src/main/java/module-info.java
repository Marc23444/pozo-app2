module com.example.applicationassignment2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens baseline to javafx.fxml;
    exports baseline;
}