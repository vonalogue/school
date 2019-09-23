package IMS;

import IMS.model.Inventory;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Start the program!
 * 
 * @author dgo
 */
public class Program extends Application implements SceneHandler {
    
    private Inventory inventory = new Inventory();
    
    @Override
    public void initInventory(Inventory inventory) {}
    
    @Override
    public void initStage(Stage stage) {}
    
    @Override
    public void start(Stage stage) throws Exception {
        transition(stage, inventory, "view_controller/main.fxml");
        stage.setResizable(false);
        stage.setTitle("IMS");
    }

    public static void main(String[] args) {
        launch(args);
    }    
}
