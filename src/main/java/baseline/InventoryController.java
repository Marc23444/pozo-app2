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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
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
    private TableColumn<Item,Double> col1;

    @FXML
    private TableColumn<Item,String>  col2;

    @FXML
    private TableColumn<Item, String>  col3;

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

    }

    //Initializes the table
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

    }

    //Called from the add button, takes the data in the text fields and passes them to createItem()
    @FXML
    public void addItem()
    {

    }

    //Finds the item selected and deletes it
    @FXML
    public void deleteItem()
    {

    }

    //Sorts the table columns
    @FXML
    public void sortList()
    {

    }

    //Deletes all items in the list
    @FXML
    public void clearList()
    {

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