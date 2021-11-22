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
            -createItem()
            -initialize(), which overrides a method in Initializable
            -addItem()
            -checkSerial()

            -nameError()
            -valueError()
            -serialError()

            -deleteItem()
            -searchList()
            -clearList()
            -clearSearch()

            -saveList()
            -saveAsHTML()
            -saveAsTSV()
            -saveAsJSON()

            -loadList()
            -loadHTML()
            -loadTSV()
            -loadJSON()

            -helpButton()
       -FXML uses the majority of these functions to provide functionality to the layout built in sceneBuilder.
       -A more detailed explanation of these methods can be found in the comments associated with them.
 */
package baseline;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    //Table is used to display the data in a tabular format
    @FXML
    private TableView<Item> table;

    //3 Columns which are stored inside table
    @FXML
    private TableColumn<Item,String> col1; //Value

    @FXML
    private TableColumn<Item,String>  col2;//Name

    @FXML
    private TableColumn<Item, String> col3;//Serial

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
        //Adds a new item object to the list
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
        //Overrides the behavior for the onEditCommit function
        col1.setOnEditCommit(
                tableEdit -> {
                    //Trys to parse the new table value as a Double
                    try
                    {
                        //Removes the $ from the front of the value
                        String tempValue = tableEdit.getNewValue().substring(1);

                        Double.parseDouble(tempValue);
                        tableEdit.getTableView().getItems().get(tableEdit.getTablePosition().getRow()).setValue(tempValue);
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
        //Overrides the behavior for the onEditCommit function
        col2.setOnEditCommit(
                tableEdit -> {
                    //If the new name is between 2 and 256 characters it gets set
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
                    //Runs the checkSerial method, if it returns true the value is updated, error is shown if not
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
            //Checks the length of the name, if it doesnt pass an error is shown.
            if(nameTextField.getText().length() >= 2 && nameTextField.getText().length() <=256)
            {
                //Checks that the value can be parsed as a double, if not an error is shown.
                if(Double.parseDouble(valueTextField.getText()) > 0.0)
                {
                    //Calls the checkSerial method, if it returns false an error is shown.
                    if(checkSerial(serialTextField.getText()))
                    {
                        //Creates a new item.
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
            e.printStackTrace();
        }
    }

    //Checks if the String serial is in the proper format and does not exist within the list
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

    //Displays an error message related to the name field
    private void nameError()
    {
        Alert nameErrorAlert = new Alert(Alert.AlertType.ERROR);
        nameErrorAlert.setHeaderText("Invalid Name");
        nameErrorAlert.setContentText("The Item's name must be between 2 and 256 characters.");
        nameErrorAlert.showAndWait();
    }

    //Displays an error message related to the value field
    private void valueError()
    {
        Alert valueErrorAlert = new Alert(Alert.AlertType.ERROR);
        valueErrorAlert.setHeaderText("Invalid Value");
        valueErrorAlert.setContentText("The Item's value must be greater than 0.0 and a valid double.");
        valueErrorAlert.showAndWait();
    }

    //Displays an error message related to the serial field
    private void serialError()
    {
        Alert serialErrorAlert = new Alert(Alert.AlertType.ERROR);
        serialErrorAlert.setHeaderText("Invalid Serial");
        serialErrorAlert.setContentText("The Item's serial must in the format A-XXX-XXX-XXX and cannot be a duplicate.");
        serialErrorAlert.showAndWait();
    }

    //Finds the item selected and deletes it
    @FXML
    public void deleteItem()
    {
        try{

            //Creates a temporary list
            ObservableList<Item> rows;
            //Makes sure items is set to what's currently displayed in the table
            items = table.getItems();

            //rows is set to the currently selected item
            rows = table.getSelectionModel().getSelectedItems();

            //Prevents an out-of-bounds exception
            if(items.size() > 1)
            {
                for (Item item: rows)
                {
                    //removes the selected item from the list
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
            e.printStackTrace();
        }
    }

    //Searches the list based on the name and serial search textfield
    //Called when the Search button is pressed
    @FXML
    public void searchList()
    {
        displayedItems.clear();
        //If there is data in both the name and serial search fields
        if(!Objects.equals(nameSearchTextField.getText(), "") && !Objects.equals(serialSearchTextField.getText(), ""))
        {
            //Temp values
            String tName = nameSearchTextField.getText();
            String tSerial = serialSearchTextField.getText();

            //For each search's  through and displays any matching items
            for (Item item : items)
            {
                if (Objects.equals(item.getName(), tName) && Objects.equals(item.getSerial(), tSerial)) {
                    displayedItems.add(item);
                    table.setItems(displayedItems);
                }
            }
        }
        //If there is data only in the serial search field
        else if(!Objects.equals(serialSearchTextField.getText(), ""))
        {
            String tSerial = serialSearchTextField.getText();
            //For each search's  through and displays any matching items
            for (Item item : items) {

                if (Objects.equals(item.getSerial(), tSerial))
                {
                    displayedItems.add(item);
                    table.setItems(displayedItems);
                }
            }
        }
        //If there is data in only the name search field
        else if(!Objects.equals(nameSearchTextField.getText(), ""))
        {
            String tName = nameSearchTextField.getText();
            //For each search's  through and displays any matching items
            for (Item item : items) {

                if (Objects.equals(item.getName(), tName))
                {
                    displayedItems.add(item);
                    table.setItems(displayedItems);
                }
            }
        }

    }

    //Deletes all items in the list
    @FXML
    public void clearList()
    {
        //Clears both of the lists
        items.clear();
        displayedItems.clear();
    }

    //Clears the search and displays the original data
    @FXML
    public void clearSearch()
    {
        try
        {
            //Clears the text fields and sets the table data back to items
            nameSearchTextField.clear();
            serialSearchTextField.clear();
            table.setItems(items);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        displayedItems.clear();
    }

    //Saves the list to a file (file type is selected by the user)
    @FXML
    public void saveList()
    {
        //Creates a new stage where the user can select the file they wish to save to.
        Stage stage = new Stage();
        //List of acceptable extensions
        String[] extensions = {".html",".txt",".json"};
        FileChooser.ExtensionFilter myFilter = new FileChooser.ExtensionFilter("Save As",extensions);

        //Creates a new file chooser dialogue window
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Inventory");
        fileChooser.getExtensionFilters().add(myFilter);
        //Sets the save file chosen by the user to the file output
        File output = fileChooser.showSaveDialog(stage);

        //Output is HTML
        if(output.getName().contains(extensions[0]))
        {
            saveAsHTML(output);
        }
        //Output is TSV
        else if(output.getName().contains(extensions[1]))
        {
            saveAsTSV(output);
        }
        //Output is JSON
        else if(output.getName().contains(extensions[2]))
        {
            saveAsJSON(output);
        }

    }

    //Saving helper Functions
    public void saveAsHTML(File output)
    {
        //Text at the top of the file
        String htmlStartText = """
                <html>
                <head>
                <title>Inventory Data</title>
                </head>
                <body>
                <table>
                """;
        //Text at the end of the file
        String htmlEndText = """
                </table>
                </body>
                </html>
                """;

        //Uses a file writer to write to the file output
        try(FileWriter myWriter = new FileWriter(output))
        {
            myWriter.write(htmlStartText);

            //Goes through each item and gets their values
            for (Item temp : items)
            {

                myWriter.write("<tr>\n<td>"+temp.getValue()+"</td>\n<td>"+temp.getName()+"</td>\n<td>"+temp.getSerial()+"</td>\n</tr>");
            }

            //Finishes the file with the end text
            myWriter.write(htmlEndText);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveAsTSV(File output)
    {

        //Uses a file writer to write to the file output
        try(FileWriter myWriter = new FileWriter(output))
        {
            //Header text
            myWriter.write("Serial Number\tName\tValue\n");
            for (Item temp : items)
            {
                //Places tabs between the values
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
        //Uses the Gson library to serialize the data
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
        //Creates a fileChooser to select the file to load from
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Inventory");

        //Sets the file input equal to the chosen file
        File input = fileChooser.showOpenDialog(stage);

        //Clear out the current items in the list
        items.clear();

        //Try catch block for the file
        try
        {
            //Scanner is used to read in input from the file
            Scanner in = new Scanner(input);

            //Loading HTML files
            if(input.getName().contains(".html"))
            {
                loadHTML(in);
            }
            //Loading TSV files
            else if (input.getName().contains(".txt"))
            {
                loadTSV(in);
            }
            //Loading JSON files
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
        //Temp values
        String name;
        String serial;
        String value;

        //Skips the top of the file
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
            //Fields are set to the raw text data of each line
            value = in.nextLine();
            name = in.nextLine();
            serial = in.nextLine();

            //Fields are trimmed down to the usable values
            value = value.substring(5,value.length()-5);
            name = name.substring(4,name.length()-5);
            serial = serial.substring(4,serial.length()-5);

            //An item is created
            createItem(Double.parseDouble(value),name,serial);

            //When the </table> line is found the loop breaks.
            if(in.nextLine().contains("</table>"))
            {
                break;
            }
        }


    }

    public void loadTSV(Scanner in)
    {
        //Temp fields
        String name;
        String serial;
        String value;

        //Skips the header
        in.nextLine();

        while(in.hasNext())
        {
            //Each value is set
            serial = in.next();
            name = in.next();
            value = in.next();
            //Gets rid of the $ symbol
            value = value.substring(1);

            //New item is created
            createItem(Double.parseDouble(value),name,serial);
        }
    }

    public void loadJSON(Scanner in)
    {
        //Temp values
        String name;
        String serial;
        String value;

        //.next will now go to each comma character
        in.useDelimiter(",");
        try
        {
            while (in.hasNext())
            {
                //Fields are set
                value = in.next();
                name = in.next();
                serial = in.next();

                //Fields are trimmed down
                value = value.substring(9);
                name = name.substring(8,name.length()-1);
                serial = serial.substring(10,serial.length()-2);

                //Sometimes value will still contain a :, if this occurs it is removed
                if(value.contains(":"))
                {
                    value = value.substring(1);
                }
                //Sometimes serial will still contain a ", if this occurs it is removed
                if(serial.contains("\""))
                {
                    serial = serial.substring(0,serial.length()-1);
                }

                //Creates the item
                createItem(Double.parseDouble(value),name,serial);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    //Help button functionality
    @FXML
    public void helpButton()
    {
        //Creating and naming the stage
        Stage instructions = new Stage();
        instructions.setTitle("User Guide");

        //Grabbing the User_Guide file
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