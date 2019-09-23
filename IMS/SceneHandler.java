package IMS;

import IMS.model.Inventory;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Utility interface for handling scenes and passing data from one window to the next.
 * 
 * @author dgo
 */
public interface SceneHandler {
    
    void initInventory(Inventory inventory);
    void initStage(Stage stage);
    
    default void transition(Stage stage, Inventory inventory, String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent root = loader.load();
        
        SceneHandler controller = loader.getController();
        controller.initInventory(inventory);
        controller.initStage(stage); 
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
