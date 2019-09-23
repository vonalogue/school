package IMS;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * Utility interface for parsing and packing text input.
 * 
 * @author dgo
 */
public interface InputParser {
    
    // Caller must catch a NullPointerException 
    // to handle user error.
    default ArrayList parsedInput(RadioButton inHouse,                  // Pass null when processing product data, as this only applies to parts.
                                  TextField idField,
                                  TextField nameField,
                                  TextField stockField,
                                  TextField priceField,
                                  TextField minField,
                                  TextField maxField,
                                  TextField companyOrMachineIdField) {  // Pass null when processing product data, as this only applies to parts.
        int id = 0;
        String name = "";
        int stock = 0;
        double price = 0.0;
        int min = 0;
        int max = 0;

        int machineId = 0;
        String company = "";
        
        boolean isPart = inHouse != null;           // Boolean used to determine whether a part or a product is being processed. 
        
        // Data to update.
        
        ArrayList data = new ArrayList();
        data.add(id);
        data.add(name);
        data.add(stock);
        data.add(price);
        data.add(min);
        data.add(max);
        if (isPart) {
            if (inHouse.isSelected())
                data.add(machineId);
            else
                data.add(company);
        }
        
        // Map containing the actual TextFields as values and their descriptions as keys (Strings)
        // to provide information in error prompts.

        Map<String, TextField> fields = new LinkedHashMap(); 
        fields.put("ID", idField);
        fields.put("name", nameField);
        fields.put("stock", stockField);
        fields.put("price", priceField);
        fields.put("minimum quantity", minField);
        fields.put("maximum quantity", maxField);
        if (isPart) {
            if (inHouse.isSelected())
                fields.put("machine ID", companyOrMachineIdField);
            else
                fields.put("company name", companyOrMachineIdField);
        }

        // Check for incomplete fields.
        
        for (String fieldName : fields.keySet()) {
            TextField field = fields.get(fieldName);
            
            if (field.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incomplete field");
                alert.setHeaderText(null);
                alert.setContentText("You did not specify the part's " + fieldName + ".");
                alert.showAndWait();
                return null;
            }
        }
        
        // Parse the text in the fields and check for invalid user input (clashing data types).
        
        String fieldValue = "", fieldName = "", dataClass = "";
        
        for (int x = 0; x < data.size(); x++) {
            dataClass = data.get(x).getClass().getName().substring(10).toLowerCase();
            fieldName = (String)fields.keySet().toArray()[x];
            fieldValue = fields.get(fieldName).getText();
            
            try {
                switch(dataClass) {
                    case ("integer"):
                        data.set(x, Integer.parseInt(fieldValue));
                        break;
                    case ("string"):
                        data.set(x, fieldValue);
                        break;
                    case ("double"):
                        data.set(x, Double.parseDouble(fieldValue));
                        break;
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid input");
                alert.setHeaderText(null);
                alert.setContentText("Invalid input (\"" + fieldValue + "\") entered for " + fieldName + ".\nExpected a(n) " + dataClass + ".");
                alert.showAndWait();
                return null;
            }
        } 

        // Make sure the stock, minimum, maximum, and price values make sense.
        
        stock = (Integer)data.get(2);
        min = (Integer)data.get(4);
        max = (Integer)data.get(5);
        price = (Double)data.get(3);
        
        if (min < 0 || max < 0 || stock < 0 || price < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid input");
            alert.setHeaderText(null);
            alert.setContentText("Negative values are not allowed.");
            alert.showAndWait();
            return null;
        }
        
        if (min > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid input");
            alert.setHeaderText(null);
            alert.setContentText("Minimum quantity must be less than maximum quantity.");
            alert.showAndWait();
            return null;
        }
        
        if (stock < min || stock > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid input");
            alert.setHeaderText(null);
            alert.setContentText("Stock must fall between the minimum and maximum quantities allowed.");
            alert.showAndWait();
            return null;
        }   
        
        return data;
    } 
}
    
