package include;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import map.ControllerMap;

public class ControllerNavigation {

    //// Navigate to page
    // Define new page
    // Generate new page/window
    // Display new page
    public void goToInsert(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../input/pageInsert.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //// Navigate to page
    // Define new page
    // Use controller from pagemap to send over data
    // Generate new page/window
    // Display new page
    public void goToMap(ActionEvent event, ControllerSession session) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../map/pageMap.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        ControllerMap controllerMap = loader.getController();
        controllerMap.loadSessionController(session);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
