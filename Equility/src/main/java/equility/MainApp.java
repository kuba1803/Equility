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


//Karol: kolejność kart w main
//Karol: equi tylko przy ustawionych
//Witek: działoao odklikiwanie
//Witek: ogarnac barwene napisy
//Kuba: textbox w main: range+ equity
//Witek: wyeksponować table, zmiejszyć przyscki graczy,obramować stół
//Kuba: Górny Pasek
//Witek: wyswietlanie online rangu jest kapryśne
//Kuba: ręczne wppisywanie
//Witek: poprawić zczytywanie rangy
//Kuba: poprawić menu w trainingu i w maine
//Karol: wyświetlanie liczb w trainingu czyli po prostu ma dzialac caly training
//Witek: tytuły okienek traningowych
//Kuba: jar na windowsa zeby dzialal

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
        stage.setResizable(false);
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
