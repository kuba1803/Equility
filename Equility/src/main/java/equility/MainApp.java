package equility;
import Model.MainModel;
import Controller.MainControler;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class MainApp extends Application {

    public static MainControler contr;
    @Override
    public void start(Stage stage) throws Exception {
        contr = new MainModel();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("Projekt Equility");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
