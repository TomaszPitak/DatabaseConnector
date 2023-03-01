package Control;

import Entities.Lot;
import Db.DbAccess;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Kontroler okna edycji lotu.
 *
 * @author Tomasz Pitak
 */
public class EditControllerL implements Initializable {

    @FXML
    private TextField fieldDataEditL;

    @FXML
    private TextField fieldMiastoEditL;

    public static Lot lot;
    public static int row;
    public static String miasto;

    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fieldDataEditL.setText(lot.getDataWylotu().toString());
        fieldMiastoEditL.setText(lot.getMiejscePrzylotu());
    }

    /**
     * Wywołuje metodę aktualizacji danego lotu w bazie danych. Aktualizuje dany
     * lot w tabeli.
     *
     * @param actionEvent
     */
    @FXML
    public void btnZapiszEditL(ActionEvent actionEvent) {
        int error = 0;
        int i = 0;

        alert.setTitle("ERROR");
        alert.setHeaderText("ERROR");
        if (fieldMiastoEditL.getText().equals("")) {
            alert.setContentText("Wpisz datę.");
            alert.showAndWait();
        }
        if (fieldMiastoEditL.getText().equals("")) {
            alert.setContentText("Wpisz miasto.");
            alert.showAndWait();
        } else {
            i = 0;
            String miasto2 = fieldMiastoEditL.getText();
            char[] charsMiasto = miasto2.toCharArray();
            for (char c : charsMiasto) {
                if (Character.isDigit(c) && i == 0) {
                    i++;
                    error++;
                    alert.setContentText("Miasto zawiera cyfry.");
                    alert.showAndWait();
                }
            }
        }

        try {
            LocalDate.parse(fieldDataEditL.getText());
        } catch (Exception e) {
            alert.setContentText("Zły format daty.");
            alert.showAndWait();
        }

        if (error == 0) {
            DbAccess.updateL(fieldDataEditL.getText(), fieldMiastoEditL.getText(), String.valueOf(lot.getIdLotu()));

            MainController.daneL.setAll(DbAccess.wyszukajLoty(miasto));

            alert.setTitle("Info");
            alert.setHeaderText("Info");
            alert.setContentText("Pomyślnie edytowano.");
            alert.showAndWait();
        }
    }
}
