/*
 *  UCF COP3330 Fall 2021 Application Assignment 2 Solution
 *  Copyright 2021 Marcelino Pozo
 */
/*
    PSUEDOCODE
    -This class will be used to control the main fxml file inventory-gui.fxml
        -This class will include fields for:
            -2 Observable lists of items
            -A table
            -3 Columns
            -5 Textfields
        -Methods in this class will include:
            -initialize, which overrides a method in Initializable
            -createItem()
            -addItem()
            -deleteItem()
            -sortList()
            -clearList()
            -saveList()
            -loadList()
       -All of these methods excluding initialize and createItem are used by FXML
       -FXML uses these functions to provide functionality to the layout built in sceneBuilder.
       -A more detailed explanation of these methods can be found in the comments associated with them.
 */
package baseline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    //Full List of Items
    ObservableList<Item> items = FXCollections.observableArrayList();
    //List used to display the completed or incomplete items
    ObservableList<Item> displayedItems = FXCollections.observableArrayList();

    //Used to display the data

    @FXML
    private TableView<Item> table;

    @FXML
    private TableColumn<Item,String> col1;

    @FXML
    private TableColumn<Item,String>  col2;

    @FXML
    private TableColumn<Item, String> col3;

    //Used to add new items

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField serialTextField;

    @FXML
    private TextField valueTextField;

    //Used for searching

    @FXML
    private TextField nameSearchTextField;

    @FXML
    private TextField serialSearchTextField;

    //Creates an item and adds it to the list
    public void createItem(double value, String name, String serial)
    {
        items.add(new Item(value,name,serial));
    }

    //Initializes the table
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //Setups the data types for each table
        col1.setCellValueFactory(new PropertyValueFactory<>("Value"));
        col2.setCellValueFactory(new PropertyValueFactory<>("Name"));
        col3.setCellValueFactory(new PropertyValueFactory<>("Serial"));

        //Allows table to be editable
        table.setEditable(true);

        //Sets the CellFactory types for the columns
        col1.setCellFactory(TextFieldTableCell.forTableColumn());
        col1.setOnEditCommit(
                tableEdit -> tableEdit.getTableView().getItems().get(tableEdit.getTablePosition().getRow()).setValue(tableEdit.getNewValue())
        );
        col2.setCellFactory(TextFieldTableCell.forTableColumn());
        col2.setOnEditCommit(
                tableEdit -> tableEdit.getTableView().getItems().get(tableEdit.getTablePosition().getRow()).setName(tableEdit.getNewValue())
        );
        col3.setCellFactory(TextFieldTableCell.forTableColumn());
        col3.setOnEditCommit(
                tableEdit -> tableEdit.getTableView().getItems().get(tableEdit.getTablePosition().getRow()).setSerial(tableEdit.getNewValue())
        );

        //Sets the items of the table
        table.setItems(items);
    }

    //Called from the add button, takes the data in the text fields and passes them to createItem()
    @FXML
    public void addItem()
    {

        try
        {
            if(nameTextField.getText().length() >= 2 && nameTextField.getText().length() <=256)
            {
                if(Double.parseDouble(valueTextField.getText()) > 0.0)
                {
                    if(checkSerial(serialTextField.getText()))
                    {
                        createItem(Double.parseDouble(valueTextField.getText()),nameTextField.getText(),serialTextField.getText());
                    }
                    else
                    {
                        serialError();
                    }
                }
                else
                {
                    valueError();
                }
            }
            else
            {
                nameError();
            }
        }
        catch (Exception e)
        {

        }
    }

    private boolean checkSerial(String serial)
    {
       //Uses regex and the .matches function to check the format  of serial.
        if(!serial.matches("\\p{Upper}-\\d{3}-\\d{3}-\\d{3}"))
        {
            return false;
        }

        //Checks if the serial exists inside of items
        for (Item item : items)
        {
            if(item.getSerial().equals(serial))
            {
                return false;
            }
        }
        return true;
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

    //Finds the item selected and deletes it
    @FXML
    public void deleteItem()
    {
        try{
            ObservableList<Item> rows;
            items = table.getItems();

            rows = table.getSelectionModel().getSelectedItems();

            //This if else block prevents an out-of-bounds exception from occurring
            if(items.size() > 1)
            {
                for (Item item: rows)
                {
                    items.remove(item);
                }
            }
            else
            {
                items.clear();
            }
        }
        catch (Exception e)
        {

        }
    }

    //Sorts the table columns
    @FXML
    public void sortList()
    {
        displayedItems.clear();
        try{
            if(!Objects.equals(nameSearchTextField.getText(), ""))
            {
                if(!Objects.equals(serialSearchTextField.getText(), ""))
                {
                    String tName = nameSearchTextField.getText();
                    String tSerial = serialSearchTextField.getText();

                    for (Item item : items) {

                        if (Objects.equals(item.getName(), tName) && Objects.equals(item.getSerial(), tSerial)) {
                            displayedItems.add(item);
                            table.setItems(displayedItems);
                        }
                    }
                }
                else
                {
                    String tName = nameSearchTextField.getText();
                    for (Item item : items) {

                        if (Objects.equals(item.getName(), tName)) {
                            displayedItems.add(item);
                            table.setItems(displayedItems);
                        }
                    }
                }
            }
            else
            {
                if(!Objects.equals(serialSearchTextField.getText(), ""))
                {
                    String tSerial = serialSearchTextField.getText();
                    for (Item item : items) {

                        if (Objects.equals(item.getSerial(), tSerial)) {
                            displayedItems.add(item);
                            table.setItems(displayedItems);
                        }
                    }
                }

            }
        }
        catch (Exception e)
        {

        }

    }

    //Deletes all items in the list
    @FXML
    public void clearList()
    {
        items.clear();
        clearSearch();
    }

    @FXML
    public void clearSearch()
    {
        nameSearchTextField.clear();
        serialSearchTextField.clear();

        displayedItems.clear();
        table.setItems(items);
    }



    //Saves the list to a file (file type is selected by the user)
    @FXML
    public void saveList()
    {

    }
    //Loads an inventory from a file
    @FXML
    public void loadList()
    {

    }


}