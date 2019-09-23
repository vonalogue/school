package IMS.view_controller;

import IMS.SceneHandler;
import IMS.model.Inventory;
import IMS.model.Part;
import IMS.model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
 * FXML Controller class for the main window, which displays the two inventory tables and their functions.
 *
 * @author dgo
 */
public class MainController implements Initializable, SceneHandler {

    private Inventory inventory;
    private Stage stage;
    
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partId;
    @FXML
    private TableColumn<Part, String> partName;
    @FXML
    private TableColumn<Part, Integer> partStock;
    @FXML
    private TableColumn<Part, String> partPrice;
    @FXML
    private TextField searchPartField;
    
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> productId;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, Integer> productStock;
    @FXML
    private TableColumn<Product, String> productPrice;
    @FXML
    private TextField searchProductField;
          
    @Override
    public void initInventory(Inventory inventory) {
        this.inventory = inventory;
        this.inventory.deselectPart();
        this.inventory.deselectProduct();
        
        partTable.setItems(this.inventory.getAllParts());
        productTable.setItems(this.inventory.getAllProducts());
    }
   
    @Override
    public void initStage(Stage stage) {
        this.stage = stage;
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getId()).asObject());
        partName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));
        partStock.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getStock()).asObject());
        partPrice.setCellValueFactory(p  -> new SimpleStringProperty(p.getValue().getFormattedPrice()));
        
        partTable.setOnMousePressed(e -> inventory.selectPart(partTable.getSelectionModel().getSelectedItem()));
        
        productId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getId()).asObject());
        productName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));
        productStock.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getStock()).asObject());
        productPrice.setCellValueFactory(p  -> new SimpleStringProperty(p.getValue().getFormattedPrice()));
        
        productTable.setOnMousePressed(e -> inventory.selectProduct(productTable.getSelectionModel().getSelectedItem()));
        
        searchPartField.setPromptText("name or ID");
        searchProductField.setPromptText("name or ID");
    }
   
    public void addPart() throws IOException {        
        transition(stage, inventory, "AddPart.fxml");
        stage.setTitle("Add part");
    }
    
    public void modifyPart() throws IOException {
        Part selectedPart = inventory.getSelectedPart();
        
        if (selectedPart != null) {
            transition(stage, inventory, "ModifyPart.fxml");
            stage.setTitle("Modify part");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            
            if (inventory.getAllParts().isEmpty()) {
                alert.setTitle("No parts");
                alert.setContentText("No parts are available.");
            } else {
                alert.setTitle("No part selected");
                alert.setContentText("To modify a part, you must select one first.");
            }
            alert.showAndWait();
        }
    }
    
    public void deletePart() {
        Part selectedPart = inventory.getSelectedPart();
        
        if (selectedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete part");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete \"" + selectedPart.getName() + "?\""
                    + "\n\nNOTE: Deleting a part may result in the deletion of a product "
                    + "if the part is the only one associated with it.");

            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType yesButton = new ButtonType("Yes");

            alert.getButtonTypes().setAll(noButton, yesButton);
            Optional<ButtonType> selection = alert.showAndWait();
            
            // Delete the part and all instances associated with products.
            // If a deleted part is the sole part associated with a product, 
            // delete the product as well to enforce the minimum parts requirement. 
            
            if (selection.get() == yesButton) {
                inventory.deletePart(selectedPart); 
                
                ObservableList<Product> allProducts = inventory.getAllProducts();
                ObservableList<Part> productParts = null;
                Product product = null; 
                Part part = null;
                
                for (int pr = 0; pr < allProducts.size(); pr++) {
                    product = allProducts.get(pr);
                    productParts = product.getAllParts();
                    
                    for (int pt = 0; pt < productParts.size(); pt++) {    
                        part = productParts.get(pt);
                        
                        if (part == selectedPart)
                            product.deletePart(part);
                        if (productParts.isEmpty())
                            inventory.deleteProduct(product);
                    }
                }
                inventory.deselectPart();
            }
            
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);          
            alert.setHeaderText(null);
            if (inventory.getAllParts().isEmpty()) {
                alert.setTitle("No parts");
                alert.setContentText("No parts are available.");
            } else {
                alert.setTitle("No part selected");
                alert.setContentText("To delete a part, you must select one first.");
            }
            alert.showAndWait();
        }
    }
    
    public void searchPart() {
        String query = searchPartField.getText();
        Part part;
        
        if (query.matches("\\d+"))    // Query is a number (part ID).
            part = inventory.lookUpPart(Integer.parseInt(query));
        else
            part = inventory.lookUpPart(query);
            
        partTable.getItems().stream()
                             .filter(p -> p == part)
                             .findAny()
                             .ifPresent(p -> {
                                partTable.getSelectionModel().select(p);
                                partTable.scrollTo(p);
        });    
    }

    public void addProduct() throws IOException {
        if (!inventory.getAllParts().isEmpty()) {
            transition(stage, inventory, "AddProduct.fxml");
            stage.setTitle("Add product");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Part required");
            alert.setContentText("There are no parts to associate with products.\nAt least one part is required to add a product.");
            alert.showAndWait();
        }
    }
    
    public void modifyProduct() throws IOException {
        Product selectedProduct = inventory.getSelectedProduct();
        
        if (selectedProduct != null) {
            transition(stage, inventory, "ModifyProduct.fxml");
            stage.setTitle("Modify product");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            
            if (inventory.getAllProducts().isEmpty()) {
                alert.setTitle("No products");
                alert.setContentText("No products are available.");
            } else {
                alert.setTitle("No product selected");
                alert.setContentText("To modify a product, you must select one first.");
            }
            alert.showAndWait();
        }
    }
    
    public void deleteProduct() {
        Product selectedProduct = inventory.getSelectedProduct();
        
        if (selectedProduct != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete product");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete \"" + selectedProduct.getName() + "?\"");

            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType yesButton = new ButtonType("Yes");

            alert.getButtonTypes().setAll(noButton, yesButton);
            Optional<ButtonType> selection = alert.showAndWait();
            
            if (selection.get() == yesButton) {
                inventory.deleteProduct(selectedProduct);
                inventory.deselectProduct();
            }
            
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);          
            alert.setHeaderText(null);
            if (inventory.getAllProducts().isEmpty()) {
                alert.setTitle("No products");
                alert.setContentText("No products are available.");
            } else {
                alert.setTitle("No product selected");
                alert.setContentText("To delete a product, you must select one first.");
            }
            alert.showAndWait();
        }
    }
    
    public void searchProduct() {
        String query = searchProductField.getText();
        Product product;
        
        if (query.matches("\\d+"))    // Query is a number (product ID).
            product = inventory.lookUpProduct(Integer.parseInt(query));
        else
            product = inventory.lookUpProduct(query);
            
        productTable.getItems().stream()
                             .filter(p -> p == product)
                             .findAny()
                             .ifPresent(p -> {
                                productTable.getSelectionModel().select(p);
                                productTable.scrollTo(p);
        });    
    }
        
    public void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");

        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType yesButton = new ButtonType("Yes");

        alert.getButtonTypes().setAll(noButton, yesButton);
        Optional<ButtonType> selection = alert.showAndWait();

        if (selection.get() == yesButton)
            stage.close();
    }

}
    