package IMS.view_controller;

import IMS.InputParser;
import IMS.SceneHandler;
import IMS.model.InHouse;
import IMS.model.Inventory;
import IMS.model.Outsourced;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class for adding a part.
 *
 * @author dgo
 */
public class AddPartController implements Initializable, SceneHandler, InputParser {
    
    private Inventory inventory;
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
        
        int id = inventory.getAllParts().size() + 1;
        idField.setDisable(true);
        idField.setText(Integer.toString(id));
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
    
        inHouse.setSelected(true);
        
        inHouse.setOnAction(e -> companyOrMachineIdLabel.setText("Machine ID"));
        outsourced.setOnAction(e -> companyOrMachineIdLabel.setText("Company"));
        
    }

    public void addPart() throws IOException {                
        // Parse and validate text input.
        
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
    
        // Add the new part to the inventory and transition back to the main scene.
        
        if (inHouse.isSelected()) 
            inventory.addPart(new InHouse(id, name, stock, price, min, max, machineId));
        else
            inventory.addPart(new Outsourced(id, name, stock, price, min, max, company));
        
        transition(stage, inventory, "main.fxml");
        stage.setTitle("IMS");
    }
    
    public void cancel() throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cancellation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to cancel?");

        ButtonType noButton = new ButtonType("No", ButtonData.CANCEL_CLOSE);
        ButtonType yesButton = new ButtonType("Yes");
  
        alert.getButtonTypes().setAll(noButton, yesButton);
        
        Optional<ButtonType> selection = alert.showAndWait();
        if (selection.get() == yesButton) {
            transition(stage, inventory, "main.fxml");
            stage.setTitle("IMS");
        }
    }

}

