package baseline;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class InventoryControllerTest{

    @Test
    void createItem()
    {

        InventoryController testController = new InventoryController();
        ObservableList<Item> testItems =  FXCollections.observableArrayList();

        testItems.add(new Item(10.0,"testProduct","A-222-222-222"));


        testController.createItem(10.0,"testProduct","A-222-222-222");

        Assertions.assertEquals(testItems.get(0).getName(),testController.items.get(0).getName());
        Assertions.assertEquals(testItems.get(0).getSerial(),testController.items.get(0).getSerial());
        Assertions.assertEquals(testItems.get(0).getValue(),testController.items.get(0).getValue());

    }

    @Test
    void checkSerial()
    {
        InventoryController testController = new InventoryController();

        Assertions.assertTrue(testController.checkSerial("A-222-222-222"));

        testController.items.add(new Item(10.0,"testProduct","A-222-222-222"));

        Assertions.assertFalse(testController.checkSerial("A-222-222-222"));
    }

    @Test
    void clearList()
    {
        InventoryController testController = new InventoryController();

        testController.items.add(new Item(10.0,"testProduct","A-222-222-222"));
        testController.items.add(new Item(10.0,"testProduct2","A-222-222-223"));
        testController.items.add(new Item(10.0,"testProduct3","A-222-222-224"));

        testController.clearList();

        Assertions.assertEquals(testController.items.size(),0);

    }

    @Test
    void clearSearch()
    {
        InventoryController testController = new InventoryController();

        testController.displayedItems.add(new Item(10.0,"testProduct","A-222-222-222"));
        testController.displayedItems.add(new Item(10.0,"testProduct2","A-222-222-223"));
        testController.displayedItems.add(new Item(10.0,"testProduct3","A-222-222-224"));

        testController.clearSearch();

        Assertions.assertEquals(testController.displayedItems.size(),0);
    }



}