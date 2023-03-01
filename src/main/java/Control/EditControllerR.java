package Control;

import Entities.Rezerwacja;
import Db.DbAccess;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ComboBox;
import javafx.beans.value.ObservableValue;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Kontroler okna edycji rezerwacji.
 *
 * @author Tomasz Pitak
 */
public class EditControllerR implements Initializable {

    @FXML
    private ComboBox<String> comboDataEdit;

    @FXML
    private ComboBox<String> comboMiastoEdit;

    @FXML
    private TextField fieldBagazEdit;

    @FXML
    private TextField fieldEmailEdit;

    @FXML
    private TextField fieldImieEdit;

    @FXML
    private TextField fieldNazwiskoEdit;

    @FXML
    private TextField fieldNrSiedzeniaEdit;

    @FXML
    private TextField fieldNrTelEdit;

    @FXML
    private TextField fieldPeselEdit;

    public static Rezerwacja rezerwacja;
    public static int row;
    public static String kryterium;
    public static String s;

    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ArrayList<String> items = DbAccess.pobierzMiasta();
        comboMiastoEdit.getItems().removeAll();
        for (int i = 0; i < items.size(); i++) {
            comboMiastoEdit.getItems().add(items.get(i));
        }

        comboMiastoEdit.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String oldvalue, String newvalue) {
                comboDataEdit.getItems().clear();
                comboDataEdit.getItems().addAll(DbAccess.pobierzDaty(comboMiastoEdit.getValue()/*getSelectionModel().getSelectedItem()*/));//dodanie dat 
                comboDataEdit.getSelectionModel().selectFirst();
            }
        });

        fieldImieEdit.setText(rezerwacja.getIdPasazera().getImie());
        fieldNazwiskoEdit.setText(rezerwacja.getIdPasazera().getNazwisko());
        fieldPeselEdit.setText(rezerwacja.getIdPasazera().getPesel());
        fieldNrTelEdit.setText(rezerwacja.getIdPasazera().getNumerTelefonu());
        fieldEmailEdit.setText(rezerwacja.getIdPasazera().getEmail());
        comboMiastoEdit.getSelectionModel().select(rezerwacja.getIdLotu().getMiejscePrzylotu());
        comboDataEdit.getSelectionModel().select(rezerwacja.getIdLotu().getDataWylotu().toString());
        fieldNrSiedzeniaEdit.setText(String.valueOf(rezerwacja.getNumerSiedzenia()));
        fieldBagazEdit.setText(String.valueOf(rezerwacja.getBagaz()));

    }

    /**
     * Wywołuje metodę aktualizacji danej rezerwacji w bazie danych. Aktualizuje
     * daną rezerwację w tabeli.
     *
     * @param actionEvent
     */
    @FXML
    public void btnZapiszEdit(ActionEvent actionEvent) {
        int error = 0;
        int i = 0;

        alert.setTitle("ERROR");
        alert.setHeaderText("ERROR");

        if (fieldImieEdit.getText().equals("")) {
            error++;
            alert.setContentText("Wpisz imię.");
            alert.showAndWait();
        } else {
            String imie = fieldImieEdit.getText();
            char[] charsImie = imie.toCharArray();
            for (char c : charsImie) {
                if (Character.isDigit(c) && i == 0) {
                    i++;
                    error++;
                    alert.setContentText("Imię zawiera cyfry.");
                    alert.showAndWait();
                }
            }
        }

        if (fieldNazwiskoEdit.getText().equals("")) {
            error++;
            alert.setContentText("Wpisz nazwisko.");
            alert.showAndWait();
        } else {
            i = 0;
            String nazwisko = fieldNazwiskoEdit.getText();
            char[] charsNazwisko = nazwisko.toCharArray();
            for (char c : charsNazwisko) {
                if (Character.isDigit(c) && i == 0) {
                    i++;
                    error++;
                    alert.setContentText("Nazwisko zawiera cyfry.");
                    alert.showAndWait();
                }
            }
        }

        if (fieldPeselEdit.getText().equals("")) {
            error++;
            alert.setContentText("Wpisz pesel.");
            alert.showAndWait();
        } else if (fieldPeselEdit.getText().length() != 11) {
            error++;
            alert.setContentText("Numer PESEL nie zawiera 11 cyfr.");
            alert.showAndWait();
        } else {
            i = 0;
            String pesel = fieldPeselEdit.getText();
            char[] charsPesel = pesel.toCharArray();
            for (char c : charsPesel) {
                if (!Character.isDigit(c) && i == 0) {
                    i++;
                    error++;
                    alert.setContentText("Pesel zawiera litery.");
                    alert.showAndWait();
                }
            }
        }

        if (fieldNrTelEdit.getText().equals("")) {
            error++;
            alert.setContentText("Wpisz numer telefonu.");
            alert.showAndWait();
        } else if (fieldNrTelEdit.getText().length() != 9) {
            error++;
            alert.setContentText("Numer telefonu nie zawiera 9 cyfr.");
            alert.showAndWait();
        } else {
            i = 0;
            String nrTel = fieldNrTelEdit.getText();
            char[] charsTel = nrTel.toCharArray();
            for (char c : charsTel) {
                if (!Character.isDigit(c) && i == 0) {
                    i++;
                    error++;
                    alert.setContentText("Numer telefonu zawiera litery.");
                    alert.showAndWait();
                }
            }
        }

        if (fieldEmailEdit.getText().equals("")) {
            error++;
            alert.setContentText("Wpisz e-mail.");
            alert.showAndWait();
        } else {
            //Regular Expression   
            String regex = "^(.+)@(.+)$";
            //Compile regular expression to get the pattern  
            Pattern pattern = Pattern.compile(regex);

            if (!pattern.matcher(fieldEmailEdit.getText()).matches()) {
                error++;
                alert.setContentText("Zły format email.");
                alert.showAndWait();
            }
        }

        if (fieldNrSiedzeniaEdit.getText().equals("")) {
            error++;
            alert.setContentText("Wpisz numer siedzenia.");
            alert.showAndWait();
        } else {
            i = 0;
            String nrSiedz = fieldNrSiedzeniaEdit.getText();
            char[] charsSiedz = nrSiedz.toCharArray();
            for (char c : charsSiedz) {
                if (!Character.isDigit(c) && i == 0) {
                    i++;
                    error++;
                    alert.setContentText("Numer siedzenia zawiera litery.");
                    alert.showAndWait();
                }
            }
        }

        if (fieldBagazEdit.getText().equals("")) {
            error++;
            alert.setContentText("Wpisz bagaż.");
            alert.showAndWait();
        } else {
            i = 0;
            String bagaz = fieldBagazEdit.getText();
            char[] charsBagaz = bagaz.toCharArray();
            for (char c : charsBagaz) {
                if (!Character.isDigit(c) && i == 0) {
                    i++;
                    error++;
                    alert.setContentText("Waga bagażu zawiera litery.");
                    alert.showAndWait();
                }
            }
        }

        if (error == 0) {
            DbAccess.updateR(fieldImieEdit.getText(), fieldNazwiskoEdit.getText(), fieldPeselEdit.getText(), fieldNrTelEdit.getText(), fieldEmailEdit.getText(), comboMiastoEdit.getValue()/*getSelectionModel().getSelectedItem()*/, comboDataEdit.getValue()/*getSelectionModel().getSelectedItem()*/, fieldNrSiedzeniaEdit.getText(), fieldBagazEdit.getText(), String.valueOf(rezerwacja.getIdRezerwacji()));
            alert.setTitle("Info");
            alert.setHeaderText("Info");
            alert.setContentText("Pomyślnie edytowano.");
            alert.showAndWait();

            if (kryterium.equals("PESEL")) {
                ArrayList<Rezerwacja> array = DbAccess.wyszukajWgPeselu(s);
                MainController.daneR.setAll(array);
            } else if (kryterium.equals("nazwisko")) {
                ArrayList<Rezerwacja> array = DbAccess.wyszukajWgNazwiska(s);
                MainController.daneR.setAll(array);
            } else if (kryterium.equals("miasto")) {
                ArrayList<Rezerwacja> array = DbAccess.wyszukajWgMiasta(s);
                MainController.daneR.setAll(array);
            }
        }
    }
}
