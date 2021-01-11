import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("input/pageInsert.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Cruiser");
        primaryStage.setResizable(false);
        primaryStage.setMaxHeight(720);
        primaryStage.setMaxWidth(1280);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
