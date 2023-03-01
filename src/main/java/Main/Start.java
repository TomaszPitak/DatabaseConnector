package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import static Db.DbAccess.CONNECTION;

/**
 * Klasa odpowiedzialna za uruchomienie aplikacji.*
 * 
 * @author Tomasz Pitak
 */

public class Start extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Zasoby/Main.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setTitle("Database Connector");
        stage.setScene(scene);
        stage.show();
        
        if (!CONNECTION) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR");
            alert.setContentText("Błąd połączenia z bazą!");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
