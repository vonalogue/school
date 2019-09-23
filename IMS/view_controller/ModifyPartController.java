package IMS.view_controller;

import IMS.InputParser;
import IMS.SceneHandler;
import IMS.model.InHouse;
import IMS.model.Inventory;
import IMS.model.Outsourced;
import IMS.model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class for modifying a part.
 *
 * @author dgo
 */
public class ModifyPartController implements Initializable, SceneHandler, InputParser {

    private Inventory inventory;
    private Part selectedPart;
    private Stage stage;
    
    @FXML
    private RadioButton inHouse;
    @FXML
    private RadioButton outsourced;
    
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
    private TextField companyOrMachineIdField;
    
    @FXML
    private Label companyOrMachineIdLabel;
    
    @Override
    public void initInventory(Inventory inventory) {
        this.inventory = inventory;
        selectedPart = inventory.getSelectedPart();
        
        idField.setText(Integer.toString(selectedPart.getId()));
        nameField.setText(selectedPart.getName());
        stockField.setText(Integer.toString(selectedPart.getStock()));
        priceField.setText(Double.toString(selectedPart.getPrice()));
        minField.setText(Integer.toString(selectedPart.getMin()));
        maxField.setText(Integer.toString(selectedPart.getMax()));
        
        if (selectedPart.getClass().getName().equals("IMS.model.InHouse")) {
            InHouse selectedInHousePart = (InHouse)selectedPart;
            inHouse.setSelected(true);
            companyOrMachineIdField.setText(Integer.toString(selectedInHousePart.getMachineId()));
            companyOrMachineIdLabel.setText("Machine ID");
        } else {
            Outsourced selectedOutsourcedPart = (Outsourced)selectedPart;
            outsourced.setSelected(true);
            companyOrMachineIdField.setText(selectedOutsourcedPart.getCompanyName());
            companyOrMachineIdLabel.setText("Company");
        }
    }
   
    @Override
    public void initStage(Stage stage) {
        this.stage = stage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup radios = new ToggleGroup();
        inHouse.setToggleGroup(radios);
        outsourced.setToggleGroup(radios);
        
        inHouse.setOnAction(e -> companyOrMachineIdLabel.setText("Machine ID"));
        outsourced.setOnAction(e -> companyOrMachineIdLabel.setText("Company"));
        
        idField.setDisable(true);
    }    
    
    public void modify() throws IOException {
        // Parse and validate text input.
        
        String partClass = selectedPart.getClass().getName().substring(10).toLowerCase();
        ArrayList data; 
        
        try {
            data = parsedInput(inHouse, idField, nameField, stockField, priceField, minField, maxField, companyOrMachineIdField);
        } catch (NullPointerException n) {
            return;
        }

        int id = (Integer)data.get(0);
        String name = (String)data.get(1);
        int stock = (Integer)data.get(2);
        double price = (Double)data.get(3);
        int min = (Integer)data.get(4);
        int max = (Integer)data.get(5);
        
        int machineId = 0;
        String company = "";
        if (inHouse.isSelected())
            machineId = (Integer)data.get(6);
        else
            company = (String)data.get(6);
        
        // Update the part with its setters if its type remains unchanged; otherwise, 
        // point to a new part object of the new specified type and loop through
        // the associated parts lists of products to update any copies accordingly
        // (via the inventory object's changePart() method).
        
        if (inHouse.isSelected() && !partClass.equals("inhouse")) {
            inventory.changePart(selectedPart, new InHouse(id, name, stock, price, min, max, machineId));
        } else if (outsourced.isSelected() && !partClass.equals("outsourced")) {
            inventory.changePart(selectedPart, new Outsourced(id, name, stock, price, min, max, company));
        } else {
            selectedPart.setId(id);
            selectedPart.setName(name);
            selectedPart.setStock(stock);
            selectedPart.setPrice(price);
            selectedPart.setMin(min);
            selectedPart.setMax(max);
            
            if (partClass.equals("inhouse"))
                ((InHouse)selectedPart).setMachineId(machineId);
            else
                ((Outsourced)selectedPart).setCompanyName(company);
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