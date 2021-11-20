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


    public void setValue(String value)
    {
        try
        {
            if(Double.parseDouble(value) > 0.0)
            {
                this.value = Double.parseDouble(value);
            }
        }
        catch (Exception e)
        {
            Alert valueErrorAlert = new Alert(Alert.AlertType.ERROR);
            valueErrorAlert.setHeaderText("Invalid Input");
            valueErrorAlert.setContentText("The Item's value must be greater than 0.0 and a valid double.");
            valueErrorAlert.showAndWait();
        }
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSerial(String serial)
    {

        this.serial = serial;
    }

    public String getValue()
    {
        return this.value.toString();
    }

    public String getName()
    {
        return this.name;
    }

    public String getSerial()
    {
        return this.serial;
    }
}
