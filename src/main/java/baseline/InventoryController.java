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

import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.google.gson.Gson;


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
                tableEdit -> {
                    try
                    {
                        Double.parseDouble(tableEdit.getNewValue());
                        tableEdit.getTableView().getItems().get(tableEdit.getTablePosition().getRow()).setValue(tableEdit.getNewValue());
                    }
                    catch (Exception e)
                    {
                        //Shows error
                        valueError();
                        //Refresh the table
                        table.getColumns().get(0).setVisible(false);
                        table.getColumns().get(0).setVisible(true);
                    }
                }
        );
        col2.setCellFactory(TextFieldTableCell.forTableColumn());
        col2.setOnEditCommit(
                tableEdit -> {
                    if(tableEdit.getNewValue().length() >= 2 && tableEdit.getNewValue().length() <= 256)
                    {
                        tableEdit.getTableView().getItems().get(tableEdit.getTablePosition().getRow()).setName(tableEdit.getNewValue());
                    }
                    else
                    {
                        //Shows error
                        nameError();
                        //Refresh the table
                        table.getColumns().get(1).setVisible(false);
                        table.getColumns().get(1).setVisible(true);
                    }
                }
        );
        col3.setCellFactory(TextFieldTableCell.forTableColumn());
        col3.setOnEditCommit(
                tableEdit -> {

                    if(checkSerial(tableEdit.getNewValue()))
                    {
                        tableEdit.getTableView().getItems().get(tableEdit.getTablePosition().getRow()).setSerial(tableEdit.getNewValue());
                    }
                    else
                    {
                        //Shows error
                        serialError();
                        //Refresh the table
                        table.getColumns().get(2).setVisible(false);
                        table.getColumns().get(2).setVisible(true);
                    }

                }
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

    public boolean checkSerial(String serial)
    {
       //Uses regex and the .matches function to check the format  of serial.
        if(!serial.matches("\\p{Upper}-\\w{3}-\\w{3}-\\w{3}"))
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
    public void searchList()
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
        displayedItems.clear();
    }

    @FXML
    public void clearSearch()
    {
        try
        {
            nameSearchTextField.clear();
            serialSearchTextField.clear();
            table.setItems(items);
        }
        catch (Exception e)
        {

        }

        displayedItems.clear();
    }

    //Saves the list to a file (file type is selected by the user)
    @FXML
    public void saveList()
    {
        //Creates a new stage where the user can select the file they wish to save to.
        Stage stage = new Stage();
        String[] extensions = {".html",".txt",".json"};

        FileChooser.ExtensionFilter myFilter = new FileChooser.ExtensionFilter("Save As",extensions);



        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Inventory");
        fileChooser.getExtensionFilters().add(myFilter);
        File output = fileChooser.showSaveDialog(stage);

        if(output.getName().contains(".html"))
        {
            saveAsHTML(output);
        }
        else if(output.getName().contains(".txt"))
        {
            saveAsTSV(output);
        }
        else if(output.getName().contains(".json"))
        {
            saveAsJSON(output);
        }




    }

    //Saving helper Functions
    public void saveAsHTML(File output)
    {
        String htmlStartText = """
                <html>
                    <head>
                        <title>Inventory Data</title>
                    </head>
                    <body>
                    <table>
                """;

        String htmlEndText = """
                    </table>
                    </body>
                </html>
                """;

        try(FileWriter myWriter = new FileWriter(output))
        {
            myWriter.write(htmlStartText);

            for (Item temp : items)
            {
                myWriter.write("<tr>\n<td>"+temp.getValue()+"</td>\n<td>"+temp.getName()+"</td>\n<td>"+temp.getSerial()+"</td>\n</tr>");
            }

            myWriter.write(htmlEndText);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveAsTSV(File output)
    {

        try(FileWriter myWriter = new FileWriter(output))
        {
            myWriter.write("Serial Number\tName\tValue\n");
            for (Item temp : items)
            {
                myWriter.write(temp.getSerial()+"\t"+temp.getName()+"\t"+temp.getValue()+"\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void saveAsJSON(File output)
    {
        Gson gson = new Gson();


        try(FileWriter myWriter = new FileWriter(output))
        {
            gson.toJson(items,myWriter);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //Loads an inventory from a file
    @FXML
    public void loadList()
    {
        //Create a fileChooser to select the file to load from
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Inventory");


        File input = fileChooser.showOpenDialog(stage);

        //Clear out the current items in the list
        items.clear();

        //Try catch block for the file
        try
        {
            //Scanner is used to read in input from the file
            Scanner in = new Scanner(input);

            if(input.getName().contains(".html"))
            {
                loadHTML(in);
            }
            else if (input.getName().contains(".txt"))
            {
                loadTSV(in);
            }
            else if(input.getName().contains(".json"))
            {

                loadJSON(in);
            }

            in.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Loading Helper Functions
    public void loadHTML(Scanner in)
    {
        String name;
        String serial;
        String value;

        in.skip(Pattern.compile("""
                                <html>
                                    <head>
                                        <title>Inventory Data</title>
                                    </head>
                                    <body>
                                    <table>
                                <tr>
                                """));

        while(in.hasNextLine())
        {


            value = in.nextLine();
            name = in.nextLine();
            serial = in.nextLine();


            value = value.substring(4,value.length()-5);
            name = name.substring(4,name.length()-5);
            serial = serial.substring(4,serial.length()-5);


            createItem(Double.parseDouble(value),name,serial);

            if(in.nextLine().contains("</table>"))
            {
                break;
            }
        }


    }

    public void loadTSV(Scanner in)
    {
        String name;
        String serial;
        String value;


        in.nextLine();
        while(in.hasNext())
        {
            serial = in.next();
            name = in.next();
            value = in.next();
            createItem(Double.parseDouble(value),name,serial);
        }
    }

    public void loadJSON(Scanner in)
    {
        String name;
        String serial;
        String value;

        in.useDelimiter(",");
        try
        {
            while (in.hasNext())
            {
                value = in.next();
                name = in.next();
                serial = in.next();

                value = value.substring(9);
                name = name.substring(8,name.length()-1);
                serial = serial.substring(10,serial.length()-2);

                if(value.contains(":"))
                {
                    value = value.substring(1);
                }

                if(serial.contains("\""))
                {
                    serial = serial.substring(0,serial.length()-1);
                }


                createItem(Double.parseDouble(value),name,serial);
            }
        }
        catch (Exception ignored)
        {

        }


    }

    //Help button functionality
    @FXML
    public void helpButton()
    {
        //Creating and naming the stage
        Stage instructions = new Stage();
        instructions.setTitle("User Guide");

        //Grabbing the User_Guide file, I opted to use a file because putting a 15 line text block would look awful.
        File userGuide = new File("User_Guide.txt");
        try (Scanner input = new Scanner(userGuide)) {

            //Creating a dialogBox and adding all the text
            VBox dialogVbox = new VBox(5);
            while (input.hasNextLine()) {
                dialogVbox.getChildren().add(new Text(input.nextLine()));
            }
            //Creating and displaying the scene
            Scene dialogScene = new Scene(dialogVbox, 850, 600);
            instructions.setScene(dialogScene);
            instructions.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }




}