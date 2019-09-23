package IMS.view_controller;

import IMS.InputParser;
import IMS.SceneHandler;
import IMS.model.Inventory;
import IMS.model.Part;
import IMS.model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class for modifying a product.
 *
 * @author dgo
 */
public class ModifyProductController implements Initializable, SceneHandler, InputParser {

    private Inventory inventory;
    private Product selectedProduct;
    private Stage stage;
    private ObservableList<Part> associatedParts;
    
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField stockField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField minField;
    @FXML
    private TextField maxField;

    @FXML
    private TableView<Part> availableTable;
    @FXML
    private TableColumn<Part, Integer> availableId;
    @FXML
    private TableColumn<Part, String> availableName;
    @FXML
    private TableColumn<Part, Integer> availableStock;
    @FXML
    private TableColumn<Part, String> availablePrice;
    @FXML
    private TextField searchField;
    
    @FXML
    private TableView<Part> associatedTable;
    @FXML
    private TableColumn<Part, Integer> associatedId;
    @FXML
    private TableColumn<Part, String> associatedName;
    @FXML
    private TableColumn<Part, Integer> associatedStock;
    @FXML
    private TableColumn<Part, String> associatedPrice;
    
    @Override
    public void initInventory(Inventory inventory) {
        this.inventory = inventory;
        inventory.deselectPart();
        
        selectedProduct = inventory.getSelectedProduct();
        associatedParts = FXCollections.observableArrayList(selectedProduct.getAllParts());        // Make a copy of the existing associated parts to avoid modifying the 
                                                                                                   // product's actual list of parts in real time before saving.
        idField.setText(Integer.toString(selectedProduct.getId()));
        nameField.setText(selectedProduct.getName());
        stockField.setText(Integer.toString(selectedProduct.getStock()));
        priceField.setText(Double.toString(selectedProduct.getPrice()));
        minField.setText(Integer.toString(selectedProduct.getMin()));
        maxField.setText(Integer.toString(selectedProduct.getMax()));
        
        availableTable.setItems(this.inventory.getAllParts());
        associatedTable.setItems(associatedParts);
    }
    
    @Override
    public void initStage(Stage stage) {
        this.stage = stage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        availableId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getId()).asObject());
        availableName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));
        availableStock.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getStock()).asObject());
        availablePrice.setCellValueFactory(p  -> new SimpleStringProperty(p.getValue().getFormattedPrice()));
        
        availableTable.setOnMousePressed(e -> inventory.selectPart(availableTable.getSelectionModel().getSelectedItem()));
        
        associatedId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getId()).asObject());
        associatedName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));
        associatedStock.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getStock()).asObject());
        associatedPrice.setCellValueFactory(p  -> new SimpleStringProperty(p.getValue().getFormattedPrice()));

        searchField.setPromptText("name or ID");
        idField.setDisable(true);
    }
    
    public void addAvailable() { 
        Part selectedAvailable = inventory.getSelectedPart();
        
         if (selectedAvailable != null) {
            if (!associatedParts.contains(selectedAvailable))                
                associatedParts.add(selectedAvailable);
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Part already exists");
                alert.setHeaderText(null);
                alert.setContentText("\"" + selectedAvailable.getName() + "\" is already associated with the product.");
                alert.showAndWait();
            }} else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No part selected");
                alert.setHeaderText(null);
                alert.setContentText("To add a part, you must select one first.");
                alert.showAndWait();
        }
    }
         
    public void deleteAssociated() {
        Part selectedAssociated = associatedTable.getSelectionModel().getSelectedItem();
        
         if (selectedAssociated != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete part");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete \"" + selectedAssociated.getName() + "?\"");

            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType yesButton = new ButtonType("Yes");

            alert.getButtonTypes().setAll(noButton, yesButton);
            Optional<ButtonType> selection = alert.showAndWait();
            
            if (selection.get() == yesButton)
                associatedParts.remove(selectedAssociated);
        
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No selection");
            alert.setHeaderText(null);
            alert.setContentText("To delete a part, you must select one first.");
            alert.showAndWait();
        }
    }
    
    public void searchAvailable() {
        String query = searchField.getText();
        Part part;
        
        if (query.matches("\\d+"))    // Query is a number (part ID).
            part = inventory.lookUpPart(Integer.parseInt(query));
        else
            part = inventory.lookUpPart(query);
            
        availableTable.getItems().stream()
                             .filter(p -> p == part)
                             .findAny()
                             .ifPresent(p -> {
                               availableTable.getSelectionModel().select(p);
                               availableTable.scrollTo(p);
        });    
    }
    
    public void modifyProduct() throws IOException {
        if (associatedParts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No parts");
            alert.setHeaderText(null);
            alert.setContentText("At least one part is required.");
            alert.showAndWait();
            return;
        }
        
        // Parse and validate text input.
        
        Product product;
        ArrayList data;
        
        try {
            data = parsedInput(null, idField, nameField, stockField, priceField, minField, maxField, null);
        } catch (NullPointerException n) {
            return;
        }

        int id = (Integer)data.get(0);
        String name = (String)data.get(1);
        int stock = (Integer)data.get(2);
        double price = (Double)data.get(3);
        int min = (Integer)data.get(4);
        int max = (Integer)data.get(5);
        
        // Check to make sure the product's price doesn't fall below the total cost of the associated parts.
        
        double partsCost = 0.0;        
        for (Part part : associatedParts)
            partsCost += part.getPrice();
      
        if (price < partsCost) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid input");
            alert.setHeaderText(null);
            alert.setContentText("The product's price cannot fall below the total cost of its parts.");
            alert.showAndWait();
            return;
        }
        
        // Update the product's fields and parts.
        
        selectedProduct.setId(id);
        selectedProduct.setName(name);
        selectedProduct.setStock(stock);
        selectedProduct.setPrice(price);
        selectedProduct.setMin(min);
        selectedProduct.setMax(max);
        
        ObservableList<Part> existingParts = selectedProduct.getAllParts();
        Part part = null;
                
        for (int x = 0; x < existingParts.size(); x++) {                        // Get rid of any existing parts that do not occur in associatedParts.
            part = existingParts.get(x);
            if (!associatedParts.contains(part))
                selectedProduct.deletePart(part);
        }
        
        for (int x = 0; x < associatedParts.size(); x++) {                      // Add any new parts found in associatedParts to the product.
            part = associatedParts.get(x);
            if (!existingParts.contains(part))
                selectedProduct.addPart(part);
        }
        
        transition(stage, inventory, "main.fxml");
        stage.setTitle("IMS");
    }
        
    public void cancel() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancellation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to cancel?");

        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType yesButton = new ButtonType("Yes");
  
        alert.getButtonTypes().setAll(noButton, yesButton);     
        Optional<ButtonType> selection = alert.showAndWait();
        
        if (selection.get() == yesButton) {
            transition(stage, inventory, "main.fxml");
            stage.setTitle("IMS");
        }
    }
    
}
