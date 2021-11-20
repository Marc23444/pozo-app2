/*
 *  UCF COP3330 Fall 2021 Application Assignment 2 Solution
 *  Copyright 2021 Marcelino Pozo
 */
/*
    PSUEDOCODE
    -This class will handle the data of the program, the data is stored in 3 fields
        -Value
        -Name
        -Serial
    -This class has one parameterized constructor
        -This constructor will take in a double and 2 strings
    -Finally this class has 3 getter methods so data can be returned.
 */
package baseline;

import javafx.scene.control.Alert;

public class Item {

    private Double value;
    private String name;
    private String serial;

    public Item(double value, String name, String serial)
    {
        this.value = value;
        this.name = name;
        this.serial = serial;
    }

    private void nameError()
    {
        Alert nameErrorAlert = new Alert(Alert.AlertType.ERROR);
        nameErrorAlert.setHeaderText("Invalid Input");
        nameErrorAlert.setContentText("The Item's name must be between 2 and 256 characters.");
        nameErrorAlert.showAndWait();
    }

    private void valueError()
    {
        Alert valueErrorAlert = new Alert(Alert.AlertType.ERROR);
        valueErrorAlert.setHeaderText("Invalid Input");
        valueErrorAlert.setContentText("The Item's value must be greater than 0.0 and a valid double.");
        valueErrorAlert.showAndWait();
    }

    private void serialError()
    {
        Alert serialErrorAlert = new Alert(Alert.AlertType.ERROR);
        serialErrorAlert.setHeaderText("Invalid Input");
        serialErrorAlert.setContentText("The Item's serial must in the format A-XXX-XXX-XXX and cannot be a duplicate.");
        serialErrorAlert.showAndWait();
    }

    public void setValue(String value)
    {
        this.value = Double.parseDouble(value);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSerial(String serial) {

        this.serial = serial;
    }

    public String getValue() {

        return value.toString();
    }

    public String getName(){
        return name;
    }

    public String getSerial() {
        return serial;
    }
}
