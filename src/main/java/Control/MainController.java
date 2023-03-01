package Control;

import Entities.*;
import Db.DbAccess;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleStringProperty;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.w3c.dom.NodeList;
import javafx.scene.Scene;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.util.regex.*;

/**
 * Kontroler głównego okna programu.
 *
 * @author Tomasz Pitak
 */
public class MainController implements Initializable {

    @FXML
    private TableColumn<Rezerwacja, String> bagazCol;

    @FXML
    private TableColumn<Rezerwacja, Void> btnDeleteCol;

    @FXML
    private TableColumn<Rezerwacja, Void> btnEditCol;

    @FXML
    private TableColumn<Lot, Void> btnDeleteLCol;

    @FXML
    private TableColumn<Lot, Void> btnEditLCol;

    @FXML
    private Button btnMenuDodajL;

    @FXML
    private Button btnMenuDodajR;

    @FXML
    private Button btnMenuInfo;

    @FXML
    private Button btnMenuWczytajR;

    @FXML
    private Button btnMenuWyszukajL;

    @FXML
    private Button btnMenuWyszukajR;

    @FXML
    private Button btnMenuZapiszR;

    @FXML
    private ComboBox<String> comboDataWylotu;

    @FXML
    private ComboBox<String> comboLoty;

    @FXML
    private ComboBox<String> comboMiejscePrzylotu;

    @FXML
    private ComboBox<String> comboWczytaj;

    @FXML
    private ComboBox<String> comboWyszukaj;

    @FXML
    private ComboBox<String> comboZapisz;

    @FXML
    private TableColumn<Lot, String> dataLCol;

    @FXML
    private TableColumn<Rezerwacja, String> dataWylotuCol;

    @FXML
    private TableColumn<Rezerwacja, String> emailCol;

    @FXML
    private TextField fieldBagaz;

    @FXML
    private TextField fieldEmail;

    @FXML
    private TextField fieldImie;

    @FXML
    private TextField fieldNazwisko;

    @FXML
    private TextField fieldNumerSiedzenia;

    @FXML
    private TextField fieldNumerTelefonu;

    @FXML
    private TextField fieldPesel;

    @FXML
    private TextField fieldWczytaj;

    @FXML
    private TextField fieldWyszukaj;

    @FXML
    private TextField fieldZapisz;

    @FXML
    private TextField fieldMiejscePrzylotu;

    @FXML
    private TextField fieldDataWylotu;

    @FXML
    private TableColumn<Lot, String> idLotuLCol;

    @FXML
    private TableColumn<Rezerwacja, String> idRezerwacjiCol;

    @FXML
    private TableColumn<Rezerwacja, String> imieCol;

    @FXML
    private Label labelBaza;

    @FXML
    private TableColumn<Lot, String> miastoLCol;

    @FXML
    private TableColumn<Rezerwacja, String> miejscePrzylotuCol;

    @FXML
    private TableColumn<Rezerwacja, String> nazwiskoCol;

    @FXML
    private TableColumn<Rezerwacja, String> numerSiedzeniaCol;

    @FXML
    private TableColumn<Rezerwacja, String> numerTelefonuCol;

    @FXML
    private Pane paneDodajForm;

    @FXML
    private Pane paneLotyForm;

    @FXML
    private Pane paneTabLoty;

    @FXML
    private Pane paneTabela;

    @FXML
    private Pane paneDodajFormL;

    @FXML
    private Pane paneWczytajForm;

    @FXML
    private Pane paneWyszukajForm;

    @FXML
    private Pane paneZapiszForm;

    @FXML
    private Pane paneInfo;

    @FXML
    private TextFlow textFlow;

    @FXML
    private TableColumn<Rezerwacja, String> peselCol;

    /**
     * Wyświetla obiekty Lot.
     */
    @FXML
    private TableView<Lot> tabelaL;

    /**
     * Wyświetla obiekty Rezerwacja.
     */
    @FXML
    private TableView<Rezerwacja> tabelaR;

    /**
     * Dane do tabeli rezerwacji.
     */
    public static ObservableList<Rezerwacja> daneR = FXCollections.observableArrayList();

    /**
     * Dane do tabeli lotów.
     */
    public static ObservableList<Lot> daneL = FXCollections.observableArrayList();

    /**
     * Itemy do combobox w panelu wyszukiwania rezerwacji.
     */
    private final String[] comboWyszukajItems = {"nazwisko", "PESEL", "miasto"};
    /**
     * Itemy do combobox w panelu zapisu danych tabeli rezerwacji do pliku.
     */
    private final String[] formaty1 = {"xml", "txt", "bin"};
    /**
     * Itemy do combobox w panelu odczyu danych do tabeli rezerwacji z pliku.
     */
    private final String[] formaty2 = {"xml", "bin"};
    /**
     * Text do wyświetlenia w panelu informacji.
     */
    private final Text text = new Text("""
                                       
                                       DatabaseConnector pozwala łączyć się z bazą MySQL o nazwie "baza_rezerwacji".
                                       Przy pierwszym uruchomieniu aplikacja utworzy bazę baza_rezerwacji 
                                       i wszystkie tabele.
                                       
                                       Aplikacja umożliwia wykonywać operacje(CRUD) na bazie danych baza_rezerwacji.
                                       Jeśli w bazie nie istnieje jeszcze pasażer o danym peselu, dodając rezerwację dodany zostanie jednocześnie pasażer.
                                       Gdy usunięta zostanie ostatnia rezerwacja na danego pasażera, usunięty zostanie również ten pasazer.
                                       Usuwając lot usuwamy także wszystkie rezerwacje na ten lot. 
                                       Jeśli usuniemy przy tym wszystkie rezerwacje danego pasażera, to usuwany jest również ten pasażer.
                                       
                                       Aplikacja umożliwia zapis aktualnego widoku tabeli rezerwacji do plików w trzech formatach (XML, TXT, BIN) oraz odczyt z formatów XML i BIN.
                                       """);

    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnMenuWyszukajR.setStyle("-fx-background-color : #000070; -fx-text-fill : red");
        tabelaR.setPlaceholder(new Label("Brak danych w tabeli"));
        tabelaL.setPlaceholder(new Label("Brak danych w tabeli"));

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////     
        //tabela rezerwacji
        //kolumna Id rezerwacji
        idRezerwacjiCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getIdRezerwacji())));

        //kolumna Imie
        imieCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getIdPasazera().getImie()));

        //kolumna Nazwisko
        nazwiskoCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getIdPasazera().getNazwisko()));

        //kolumna Pesel
        peselCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getIdPasazera().getPesel()));

        //kolumna Numer Telefonu
        numerTelefonuCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getIdPasazera().getNumerTelefonu()));

        //kolumna Adres e-mail
        emailCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getIdPasazera().getEmail()));

        //kolumna Miejsce przylotu
        miejscePrzylotuCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getIdLotu().getMiejscePrzylotu()));

        //kolumna Data wylotu
        dataWylotuCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getIdLotu().getDataWylotu().toString()));

        //kolumna Numer siedzenia
        numerSiedzeniaCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumerSiedzenia())));

        //kolumna Bagaż
        bagazCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(cellData.getValue().getBagaz())));

        //button edit
        btnEditCol.setCellFactory(new Callback<TableColumn<Rezerwacja, Void>, TableCell<Rezerwacja, Void>>() {
            @Override
            public TableCell<Rezerwacja, Void> call(final TableColumn<Rezerwacja, Void> param) {
                final TableCell<Rezerwacja, Void> cell = new TableCell<Rezerwacja, Void>() {

                    Image image = new Image(getClass().getResourceAsStream("/Images/edit.png"), 25, 25, false, false);
                    private final Button btn = new Button();

                    {
                        btn.setOnAction((ActionEvent event) -> {

                            //pobranie rezerwacji do kontrolera ramki
                            EditControllerR.rezerwacja = getTableView().getItems().get(getIndex());
                            EditControllerR.row = getIndex();
                            EditControllerR.kryterium = comboWyszukaj.getValue();
                            EditControllerR.s = fieldWyszukaj.getText();

                            try {
                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setOpacity(1);
                                stage.setTitle("Edit");
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Zasoby/EditR.fxml"));
                                Parent root = fxmlLoader.load();
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.showAndWait();
                            } catch (IOException e) {
                                alert.setTitle("ERROR");
                                alert.setHeaderText("ERROR");
                                alert.setContentText("Błąd załadowania modułu Edycji!");
                                alert.showAndWait();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.setStyle("-fx-background-color: #ffffff; ");
                            btn.setGraphic(new ImageView(image));
                            setGraphic(btn);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        //button delete
        btnDeleteCol.setCellFactory(new Callback<TableColumn<Rezerwacja, Void>, TableCell<Rezerwacja, Void>>() {
            @Override
            public TableCell<Rezerwacja, Void> call(final TableColumn<Rezerwacja, Void> param) {
                final TableCell<Rezerwacja, Void> cell = new TableCell<Rezerwacja, Void>() {

                    Image image = new Image(getClass().getResourceAsStream("/Images/delete.jpg"), 25, 25, false, false);
                    private final Button btn = new Button();

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Rezerwacja r = getTableView().getItems().get(getIndex());
                            DbAccess.deleteR(r);
                            daneR.remove(r);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.setStyle("-fx-background-color: #ffffff; ");
                            btn.setGraphic(new ImageView(image));
                            setGraphic(btn);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        tabelaR.setItems(daneR);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //tabela lotów  
        idLotuLCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(String.valueOf(String.valueOf(cellData.getValue().getIdLotu()))));

        dataLCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getDataWylotu().toString()));

        miastoLCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getMiejscePrzylotu()));

        //button edit
        btnEditLCol.setCellFactory(new Callback<TableColumn<Lot, Void>, TableCell<Lot, Void>>() {
            @Override
            public TableCell<Lot, Void> call(final TableColumn<Lot, Void> param) {
                final TableCell<Lot, Void> cell = new TableCell<Lot, Void>() {

                    Image image = new Image(getClass().getResourceAsStream("/Images/edit.png"), 25, 25, false, false);
                    private final Button btn = new Button();

                    {
                        btn.setOnAction((ActionEvent event) -> {

                            //pobranie lotu do kontrolera ramki
                            EditControllerL.lot = getTableView().getItems().get(getIndex());
                            EditControllerL.row = getIndex();
                            EditControllerL.miasto = comboLoty.getValue();

                            try {
                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setOpacity(1);
                                stage.setTitle("Edit");
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Zasoby/EditL.fxml"));
                                Parent root = fxmlLoader.load();
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.showAndWait();
                            } catch (IOException e) {
                                alert.setTitle("ERROR");
                                alert.setHeaderText("ERROR");
                                alert.setContentText("Błąd załadowania modułu Edycji!");
                                alert.showAndWait();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.setStyle("-fx-background-color: #ffffff; ");
                            btn.setGraphic(new ImageView(image));
                            setGraphic(btn);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        //button delete
        btnDeleteLCol.setCellFactory(new Callback<TableColumn<Lot, Void>, TableCell<Lot, Void>>() {
            @Override
            public TableCell<Lot, Void> call(final TableColumn<Lot, Void> param) {
                final TableCell<Lot, Void> cell = new TableCell<Lot, Void>() {

                    Image image = new Image(getClass().getResourceAsStream("/Images/delete.jpg"), 25, 25, false, false);
                    private final Button btn = new Button();

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Lot l = getTableView().getItems().get(getIndex());
                            DbAccess.deleteL(l);
                            daneL.remove(l);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.setStyle("-fx-background-color: #ffffff; ");
                            btn.setGraphic(new ImageView(image));
                            setGraphic(btn);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        tabelaL.setItems(daneL);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //listener na zmiane itemu w comboBox
        comboMiejscePrzylotu.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String oldvalue, String newvalue) {//zmiana itemu
                comboDataWylotu.getItems().clear();//wyczyszczenie listy
                comboDataWylotu.getItems().addAll(DbAccess.pobierzDaty(comboMiejscePrzylotu.getValue()/*getSelectionModel().getSelectedItem()*/));//dodanie dat 
                comboDataWylotu.getSelectionModel().selectFirst();
            }
        });

        labelBaza.setText(DbAccess.getDatabaseName());
        comboWyszukaj.getItems().addAll(comboWyszukajItems);
        comboZapisz.getItems().addAll(formaty1);
        comboZapisz.getSelectionModel().selectFirst();
        comboWczytaj.getItems().addAll(formaty2);
        comboWczytaj.getSelectionModel().selectFirst();
        text.setFill(Color.WHITE);
        textFlow.getChildren().add(text);
        paneWyszukajForm.toFront();
        paneTabela.toFront();
    }

    /**
     * Rejestruje kliknięcie przycisku w panelu menu. W zależności od
     * klikniętego przycisku wyświetla odpowiednie panele.
     *
     * @param actionEvent
     */
    @FXML
    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnMenuWyszukajR) {
            resetColor(btnMenuDodajR);
            resetColor(btnMenuZapiszR);
            resetColor(btnMenuWczytajR);
            resetColor(btnMenuInfo);
            setColor(btnMenuWyszukajR);
            resetColor(btnMenuWyszukajL);
            resetColor(btnMenuDodajL);
            paneWyszukajForm.toFront();
            paneTabela.toFront();
        }
        if (actionEvent.getSource() == btnMenuDodajR) {
            setColor(btnMenuDodajR);
            resetColor(btnMenuZapiszR);
            resetColor(btnMenuWczytajR);
            resetColor(btnMenuInfo);
            resetColor(btnMenuWyszukajR);
            resetColor(btnMenuWyszukajL);
            resetColor(btnMenuDodajL);
            comboMiejscePrzylotu.getItems().clear();
            try {
                comboMiejscePrzylotu.getItems().addAll(DbAccess.pobierzMiasta());
                comboDataWylotu.getItems().addAll(DbAccess.pobierzDaty(comboMiejscePrzylotu.getValue()));
            } catch (Exception e) {
            }
            comboMiejscePrzylotu.getSelectionModel().selectFirst();
            comboDataWylotu.getSelectionModel().selectFirst();
            paneDodajForm.toFront();
            paneTabela.toFront();
        }
        if (actionEvent.getSource() == btnMenuZapiszR) {
            resetColor(btnMenuDodajR);
            setColor(btnMenuZapiszR);
            resetColor(btnMenuWczytajR);
            resetColor(btnMenuInfo);
            resetColor(btnMenuWyszukajR);
            resetColor(btnMenuWyszukajL);
            resetColor(btnMenuDodajL);
            paneZapiszForm.toFront();
            paneTabela.toFront();
        }
        if (actionEvent.getSource() == btnMenuWczytajR) {
            resetColor(btnMenuDodajR);
            resetColor(btnMenuZapiszR);
            setColor(btnMenuWczytajR);
            resetColor(btnMenuInfo);
            resetColor(btnMenuWyszukajR);
            resetColor(btnMenuWyszukajL);
            resetColor(btnMenuDodajL);
            paneWczytajForm.toFront();
            paneTabela.toFront();
        }
        if (actionEvent.getSource() == btnMenuWyszukajL) {
            resetColor(btnMenuDodajR);
            resetColor(btnMenuZapiszR);
            resetColor(btnMenuWczytajR);
            resetColor(btnMenuInfo);
            resetColor(btnMenuWyszukajR);
            setColor(btnMenuWyszukajL);
            resetColor(btnMenuDodajL);
            comboLoty.getItems().clear();
            try {
                comboLoty.getItems().addAll(DbAccess.pobierzMiasta());
            } catch (Exception e) {
            }
            comboLoty.getSelectionModel().selectFirst();
            paneLotyForm.toFront();
            paneTabLoty.toFront();
        }

        if (actionEvent.getSource() == btnMenuDodajL) {
            resetColor(btnMenuDodajR);
            resetColor(btnMenuZapiszR);
            resetColor(btnMenuWczytajR);
            resetColor(btnMenuInfo);
            resetColor(btnMenuWyszukajR);
            resetColor(btnMenuWyszukajL);
            setColor(btnMenuDodajL);
            paneDodajFormL.toFront();
            paneTabLoty.toFront();
        }
        if (actionEvent.getSource() == btnMenuInfo) {
            resetColor(btnMenuDodajR);
            resetColor(btnMenuZapiszR);
            resetColor(btnMenuWczytajR);
            setColor(btnMenuInfo);
            resetColor(btnMenuWyszukajR);
            resetColor(btnMenuWyszukajL);
            resetColor(btnMenuDodajL);
            paneInfo.toFront();
        }
    }

    /**
     * Ustawia kolor aktywnego przycisku w menu.
     *
     * @param btn
     */
    void setColor(Button btn) {
        btn.setStyle("-fx-background-color : #000070; -fx-text-fill : red");
    }

    /**
     * Ustawia kolor nieaktywnego przycisku w menu.
     *
     * @param btn
     */
    void resetColor(Button btn) {
        btn.setStyle("-fx-background-color : #191970");
    }

    /**
     * Wywołuje metodę dodania rezerwacji do bazy danych. Dodaje dodaną
     * rezerwację do tabeli rezerwacji, gdy powinna znaleźć się w aktualnym
     * widoku tabeli.
     *
     * @param actionEvent
     */
    @FXML
    public void btnDodaj(ActionEvent actionEvent) {

        if (DbAccess.wyszukajPasazera(fieldPesel.getText())) {
            
            int error = 0;
            int i = 0;
            
            if (fieldNumerSiedzenia.getText().equals("")) {
                error++;
                alert.setContentText("Wpisz numer siedzenia.");
                alert.showAndWait();
            } else {
                i = 0;
                String nrSiedz = fieldNumerSiedzenia.getText();
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

            if (fieldBagaz.getText().equals("")) {
                error++;
                alert.setContentText("Wpisz bagaż.");
                alert.showAndWait();
            } else {
                i = 0;
                String bagaz = fieldBagaz.getText();
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
                //dodanie nowego obiektu rezerwacja (i/lub pasazer) do bazy oraz dodanie go do listy jeśli dodana rezerwacja powinna znależć się w aktualnym widoku tabeli
                Rezerwacja r = DbAccess.dodajR(fieldImie.getText(), fieldNazwisko.getText(), fieldPesel.getText(), fieldNumerTelefonu.getText(), fieldEmail.getText(), comboMiejscePrzylotu.getValue()/*getSelectionModel().getSelectedItem()*/, comboDataWylotu.getValue()/*getSelectionModel().getSelectedItem()*/, fieldNumerSiedzenia.getText(), fieldBagaz.getText());
                
                if (fieldWyszukaj.getText().equals(fieldPesel.getText()) || fieldWyszukaj.getText().equals(fieldNazwisko.getText()) || fieldWyszukaj.getText().equals(comboMiejscePrzylotu.getValue())) {
                    daneR.add(r);
                }
                alert.setTitle("Info");
                alert.setHeaderText("Info");
                alert.setContentText("Pomyślnie dodano.");
                alert.showAndWait();
            }
            
            
        } else {

            int error = 0;
            int i = 0;

            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR");

            if (fieldImie.getText().equals("")) {
                error++;
                alert.setContentText("Wpisz imię.");
                alert.showAndWait();
            } else {
                String imie = fieldImie.getText();
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

            if (fieldNazwisko.getText().equals("")) {
                error++;
                alert.setContentText("Wpisz nazwisko.");
                alert.showAndWait();
            } else {
                i = 0;
                String nazwisko = fieldNazwisko.getText();
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

            if (fieldPesel.getText().equals("")) {
                error++;
                alert.setContentText("Wpisz pesel.");
                alert.showAndWait();
            } else if (fieldPesel.getText().length() != 11) {
                error++;
                alert.setContentText("Numer PESEL nie zawiera 11 cyfr.");
                alert.showAndWait();
            } else {
                i = 0;
                String pesel = fieldPesel.getText();
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

            if (fieldNumerTelefonu.getText().equals("")) {
                error++;
                alert.setContentText("Wpisz numer telefonu.");
                alert.showAndWait();
            } else if (fieldNumerTelefonu.getText().length() != 9) {
                error++;
                alert.setContentText("Numer telefonu nie zawiera 9 cyfr.");
                alert.showAndWait();
            } else {
                i = 0;
                String nrTel = fieldNumerTelefonu.getText();
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

            if (fieldEmail.getText().equals("")) {
                error++;
                alert.setContentText("Wpisz e-mail.");
                alert.showAndWait();
            } else {
                //Regular Expression   
                String regex = "^(.+)@(.+)$";
                //Compile regular expression to get the pattern  
                Pattern pattern = Pattern.compile(regex);

                if (!pattern.matcher(fieldEmail.getText()).matches()) {
                    error++;
                    alert.setContentText("Zły format email.");
                    alert.showAndWait();
                }
            }

            if (fieldNumerSiedzenia.getText().equals("")) {
                error++;
                alert.setContentText("Wpisz numer siedzenia.");
                alert.showAndWait();
            } else {
                i = 0;
                String nrSiedz = fieldNumerSiedzenia.getText();
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

            if (fieldBagaz.getText().equals("")) {
                error++;
                alert.setContentText("Wpisz bagaż.");
                alert.showAndWait();
            } else {
                i = 0;
                String bagaz = fieldBagaz.getText();
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
                //dodanie nowego obiektu rezerwacja (i/lub pasazer) do bazy oraz dodanie go do listy jeśli dodana rezerwacja powinna znależć się w aktualnym widoku tabeli
                Rezerwacja r = DbAccess.dodajR(fieldImie.getText(), fieldNazwisko.getText(), fieldPesel.getText(), fieldNumerTelefonu.getText(), fieldEmail.getText(), comboMiejscePrzylotu.getValue()/*getSelectionModel().getSelectedItem()*/, comboDataWylotu.getValue()/*getSelectionModel().getSelectedItem()*/, fieldNumerSiedzenia.getText(), fieldBagaz.getText());

                if (fieldWyszukaj.getText().equals(fieldPesel.getText()) || fieldWyszukaj.getText().equals(fieldNazwisko.getText()) || fieldWyszukaj.getText().equals(comboMiejscePrzylotu.getValue())) {
                    daneR.add(r);
                }
                alert.setTitle("Info");
                alert.setHeaderText("Info");
                alert.setContentText("Pomyślnie dodano.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Sortuje dane w tabeli lotów rosnąco.
     *
     * @param actionEvent
     */
    @FXML
    public void btnSortR(ActionEvent actionEvent) {
        List<Lot> lista = daneL.stream().collect(Collectors.toList());
        Comparator<Lot> comparator = (c1, c2) -> {
            return (c1.getDataWylotu().toString()).compareTo(c2.getDataWylotu().toString());
        };
        Collections.sort(lista, comparator);
        daneL.setAll(lista);
    }

    /**
     * Sortuje dane w tabeli lotów malejąco.
     *
     * @param actionEvent
     */
    @FXML
    public void btnSortM(ActionEvent actionEvent) {
        List<Lot> lista = daneL.stream().collect(Collectors.toList());
        Comparator<Lot> comparator = (c1, c2) -> {
            return (c1.getDataWylotu()).compareTo(c2.getDataWylotu());
        };
        Collections.sort(lista, comparator.reversed());
        // Collections.reverse(lista);
        daneL.setAll(lista);
    }

    /**
     * Wywołuje metodę wyszukania lotów bazie danych oraz wyswietla w tabeli
     * lotów pobrane loty.
     *
     * @param actionEvent
     */
    @FXML
    public void btnLoty(ActionEvent actionEvent) {
        daneL.setAll(DbAccess.wyszukajLoty(comboLoty.getValue()/*getSelectionModel().getSelectedItem()*/));
    }

    /**
     * Opcja logowania nie jest jeszcze dostępna.
     *
     * @param actionEvent
     */
    @FXML
    public void btnZaloguj(ActionEvent actionEvent) {
        alert.setTitle("Info");
        alert.setHeaderText("Info");
        alert.setContentText("Opcja logowania nie jest jeszcze dostępna.");
        alert.showAndWait();
    }

    /**
     * Dodaje lot, jeśli dany lot jeszcze nie istnieje. Dodaje lot do tabeli
     * lotów, jesli powinien znaleźć się w aktualnym widoku tabeli.
     *
     * @param actionEvent
     */
    @FXML
    public void btnDodajLot(ActionEvent actionEvent) {
        int error = 0;
        int i = 0;

        alert.setTitle("ERROR");
        alert.setHeaderText("ERROR");
        if (fieldDataWylotu.getText().equals("")) {
            error++;
            alert.setContentText("Wpisz datę.");
            alert.showAndWait();
        }
        if (fieldMiejscePrzylotu.getText().equals("")) {
            error++;
            alert.setContentText("Wpisz miasto.");
            alert.showAndWait();
        } else {
            i = 0;
            String miasto = fieldMiejscePrzylotu.getText();
            char[] charsMiasto = miasto.toCharArray();
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
            LocalDate.parse(fieldDataWylotu.getText());    
        } catch (Exception e) {
            error++;
            alert.setContentText("Zły format daty.");
            alert.showAndWait();
        }

        
        if (error == 0) {
            Lot lot = DbAccess.dodajLot(fieldDataWylotu.getText(), fieldMiejscePrzylotu.getText());
            if (lot == null) {
              alert.setContentText("Lot już istnieje.");
              alert.showAndWait();
            } else {
                if (fieldMiejscePrzylotu.getText().equals(comboLoty.getValue()/*getSelectionModel().getSelectedItem()*/)) {
                  daneL.add(lot);
                }
                alert.setTitle("Info");
                alert.setHeaderText("Info");
                alert.setContentText("Pomyślnie dodano.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Wczytuje dane do tabeli rezerwacji z formatów XML i BIN.
     *
     * @param actionEvent
     */
    @FXML
    public void btnWczytaj(ActionEvent actionEvent) {
        alert.setTitle("ERROR");
        alert.setHeaderText("ERROR");

        if (fieldWczytaj.getText().equals("")) {
            alert.setContentText("Podaj nazwę pliku.");
            alert.showAndWait();
        }
        String nazwa = fieldWczytaj.getText();
        String format = comboWczytaj.getValue()/*getSelectionModel().getSelectedItem()*/;

        if (format.equals("bin")) { //odczyt formatu binarnego
            try {
                FileInputStream plikobiektow = new FileInputStream(nazwa + "." + format);
                ObjectInputStream strumienobiektow = new ObjectInputStream(plikobiektow);

                daneR.setAll((ArrayList<Rezerwacja>) strumienobiektow.readObject());

            } catch (Exception e) {
                alert.setContentText("Błąd odczytu formatu binarnego.");
                alert.showAndWait();
            }
        }

        if (format.equals("xml")) {//odczyt formatu xml
            try {
                // File selectedFile = fileChooser.showOpenDialog(primaryStage);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(nazwa + "." + format);
                doc.getDocumentElement().normalize();

                NodeList nList = doc.getElementsByTagName("Rezerwacja");
                daneR.removeAll();
                tabelaR.getItems().clear();//wyczyszczenie tabeli          

                String idRezerwacji = "", imie = "", nazwisko = "", pesel = "", numerTelefonu = "", email = "", przylot = "", data = "", nrSiedzenia = "", bagaz = "";

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    org.w3c.dom.Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        idRezerwacji = eElement.getElementsByTagName("idRezerwacji").item(0).getTextContent();
                        imie = eElement.getElementsByTagName("imię").item(0).getTextContent();
                        nazwisko = eElement.getElementsByTagName("nazwisko").item(0).getTextContent();
                        pesel = eElement.getElementsByTagName("pesel").item(0).getTextContent();
                        numerTelefonu = eElement.getElementsByTagName("numerTelefonu").item(0).getTextContent();
                        email = eElement.getElementsByTagName("adresEmail").item(0).getTextContent();
                        przylot = eElement.getElementsByTagName("miejscePrzylotu").item(0).getTextContent();
                        data = eElement.getElementsByTagName("dataWylotu").item(0).getTextContent();
                        nrSiedzenia = eElement.getElementsByTagName("numerSiedzenia").item(0).getTextContent();
                        bagaz = eElement.getElementsByTagName("bagaż").item(0).getTextContent();
                    }

                    Pasazer p = new Pasazer(imie, nazwisko, pesel, numerTelefonu, email);
                    Lot l = new Lot(LocalDate.parse(data), przylot);
                    Rezerwacja r = new Rezerwacja(Integer.parseInt(idRezerwacji), p, l, Integer.parseInt(nrSiedzenia), Integer.parseInt(bagaz));
                    daneR.add(r);
                }
            } catch (Exception e) {
                alert.setContentText("Błąd odczytu formatu XML.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Zapisuje dane z tabeli rezerwacji do formatów XML, TXT i BIN.
     *
     * @param actionEvent
     */
    @FXML
    public void btnZapisz(ActionEvent actionEvent) {
        alert.setTitle("ERROR");
        alert.setHeaderText("ERROR");

        if (fieldZapisz.getText().equals("")) {
            alert.setContentText("Podaj nazwę pliku.");
            alert.showAndWait();
        }

        if (comboZapisz.getValue()/*getSelectionModel().getSelectedItem()*/.equals("xml")) {//wybrany format xml
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

                //utworzenie dokumentu
                Document doc = dBuilder.newDocument();

                // Utworzenie korzenia o nazwie rezerwacje
                Element rootElement = doc.createElement("Rezerwacje");
                // Dodanie korzenia rezerwacje do dokumentu
                doc.appendChild(rootElement);

                // dla wiersza tabeli w liście:
                for (Rezerwacja elem : daneR) {
                    // Dodanie nowej gałęzi rezerwacja do korzenia rezerwacje
                    Element rezerwacja = doc.createElement("Rezerwacja");
                    rootElement.appendChild(rezerwacja);

                    // Utworzenie nowej gałęzi idRezerwacji
                    Element idRezerwacji = doc.createElement("idRezerwacji");
                    // Utworzenie wartości dla gałęzi i doadnie jej.
                    idRezerwacji.appendChild(doc.createTextNode(String.valueOf(elem.getIdRezerwacji())));
                    // Dodanie gałęzi idRezerwacji do rezerwacji
                    rezerwacja.appendChild(idRezerwacji);

                    Element imie = doc.createElement("imię");
                    imie.appendChild(doc.createTextNode(elem.getIdPasazera().getImie()));
                    rezerwacja.appendChild(imie);

                    Element nazwisko = doc.createElement("nazwisko");
                    nazwisko.appendChild(doc.createTextNode(elem.getIdPasazera().getNazwisko()));
                    rezerwacja.appendChild(nazwisko);

                    Element pesel = doc.createElement("pesel");
                    pesel.appendChild(doc.createTextNode(elem.getIdPasazera().getPesel()));
                    rezerwacja.appendChild(pesel);

                    Element nrTelefonu = doc.createElement("numerTelefonu");
                    nrTelefonu.appendChild(doc.createTextNode(elem.getIdPasazera().getNumerTelefonu()));
                    rezerwacja.appendChild(nrTelefonu);

                    Element email = doc.createElement("adresEmail");
                    email.appendChild(doc.createTextNode(elem.getIdPasazera().getEmail()));
                    rezerwacja.appendChild(email);

                    Element przylot = doc.createElement("miejscePrzylotu");
                    przylot.appendChild(doc.createTextNode(elem.getIdLotu().getMiejscePrzylotu()));
                    rezerwacja.appendChild(przylot);

                    Element data = doc.createElement("dataWylotu");
                    data.appendChild(doc.createTextNode(elem.getIdLotu().getDataWylotu().toString()));
                    rezerwacja.appendChild(data);

                    Element nrSiedzenia = doc.createElement("numerSiedzenia");
                    nrSiedzenia.appendChild(doc.createTextNode(String.valueOf(elem.getNumerSiedzenia())));
                    rezerwacja.appendChild(nrSiedzenia);

                    Element bagaz = doc.createElement("bagaż");
                    bagaz.appendChild(doc.createTextNode(String.valueOf(elem.getBagaz())));
                    rezerwacja.appendChild(bagaz);
                }
                // zapis do xml
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(fieldZapisz.getText() + ".xml"));
                transformer.transform(source, result);
            } catch (Exception exc) {
                alert.setContentText("Błąd zapisu formatu XML.");
                alert.showAndWait();
            }
            alert.setTitle("Info");
            alert.setHeaderText("Info");
            alert.setContentText("Zapisano plik XML.");
            alert.showAndWait();
        }
        if (comboZapisz.getValue()/*getSelectionModel().getSelectedItem()*/.equals("txt")) {//wybrany format txt
            try (FileWriter fw = new FileWriter(fieldZapisz.getText() + ".txt", false);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {

                out.write("Data utworzenia pliku: " + LocalDateTime.now().toLocalDate());
                out.println();
                out.write("Godzina utworzenia pliku: " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute());
                out.println();
                out.println();

                int i = 0;
                for (Rezerwacja r : daneR) {//dla kazdego obiektu w liscie
                    i++;
                    out.write("wiersz " + i);
                    out.println();
                    out.write("ID rezerwacji: ");
                    out.write(String.valueOf(r.getIdRezerwacji()));
                    out.println();
                    out.write("Imię: ");
                    out.write(r.getIdPasazera().getImie());
                    out.println();
                    out.write("Nazwisko: ");
                    out.write(r.getIdPasazera().getNazwisko());
                    out.println();
                    out.write("Pesel: ");
                    out.write(r.getIdPasazera().getPesel());
                    out.println();
                    out.write("Numer telefonu: ");
                    out.write(r.getIdPasazera().getNumerTelefonu());
                    out.println();
                    out.write("Adres e-mail: ");
                    out.write(r.getIdPasazera().getEmail());
                    out.println();
                    out.write("Miejsce przylotu: ");
                    out.write(r.getIdLotu().getMiejscePrzylotu());
                    out.println();
                    out.write("Data przylotu: ");
                    out.write(r.getIdLotu().getDataWylotu().toString());
                    out.println();
                    out.write("Numer siedzenia: ");
                    out.write(String.valueOf(r.getNumerSiedzenia()));
                    out.println();
                    out.write("Bagaż(kg): ");
                    out.write(String.valueOf(r.getBagaz()));
                    out.println();
                    out.println();
                }
                out.close();
            } catch (IOException e) {
                alert.setContentText("Błąd zapisu formatu tekstowego.");
                alert.showAndWait();
            }
            alert.setTitle("Info");
            alert.setHeaderText("Info");
            alert.setContentText("Zapisano plik TXT.");
            alert.showAndWait();
        }
        if (comboZapisz.getValue()/*getSelectionModel().getSelectedItem()*/.equals("bin")) {//wybrany format bin
            try {
                FileOutputStream plikobiektow = new FileOutputStream(fieldZapisz.getText() + ".bin");
                ObjectOutputStream strumienobiektow = new ObjectOutputStream(plikobiektow);
                strumienobiektow.writeObject(new ArrayList<Rezerwacja>(daneR));
                strumienobiektow.close();
            } catch (IOException e) {
                alert.setContentText("Błąd zapisu formatu binarnego.");
                alert.showAndWait();
            }
            alert.setTitle("Info");
            alert.setHeaderText("Info");
            alert.setContentText("Zapisano plik BIN.");
            alert.showAndWait();
        }
    }

    /**
     * Wywołuje metodę wyszukania rezerwacji w bazie danych w zależności od
     * wybranego kryterium. Dodaje pobrane rezerwacje do tabeli rezerwacji.
     *
     * @param actionEvent
     */
    @FXML
    public void btnWyszukaj(ActionEvent actionEvent) {

        if (fieldWyszukaj.getText().equals("")) {
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR");
            alert.setContentText("Nie można wyszukać. Uzupełnij dane.");
            alert.showAndWait();
        } else if (comboWyszukaj.getValue()/*getSelectionModel().getSelectedItem()*/.equals("PESEL")) {
            int error = 0;
            int i = 0;

            if (fieldWyszukaj.getText().length() != 11) {
                error++;
                alert.setContentText("Numer PESEL nie zawiera 11 cyfr.");
                alert.showAndWait();
            }

            i--;
            String pesel = fieldWyszukaj.getText();
            char[] charsPesel = pesel.toCharArray();
            for (char c : charsPesel) {
                if (!Character.isDigit(c) && i == 0) {
                    i++;
                    error++;
                    alert.setContentText("Pesel zawiera litery.");
                    alert.showAndWait();
                }
            }

            if (error == 0) {
                ArrayList<Rezerwacja> array = DbAccess.wyszukajWgPeselu(fieldWyszukaj.getText());
                daneR.setAll(array);
            }
        } else if (comboWyszukaj.getValue()/*getSelectionModel().getSelectedItem()*/.equals("nazwisko")) {
            int error = 0;
            int i = 0;

            String nazwisko = fieldWyszukaj.getText();
            char[] charsNazwisko = nazwisko.toCharArray();
            for (char c : charsNazwisko) {
                if (Character.isDigit(c) && i == 0) {
                    i++;
                    error++;
                    alert.setContentText("Nazwisko zawiera cyfry.");
                    alert.showAndWait();
                }
            }
            if (error == 0) {
                ArrayList<Rezerwacja> array = DbAccess.wyszukajWgNazwiska(fieldWyszukaj.getText());
                daneR.setAll(array);
            }
        } else if (comboWyszukaj.getValue()/*getSelectionModel().getSelectedItem()*/.equals("miasto")) {
            int error = 0;
            int i = 0;

            String miasto = fieldWyszukaj.getText();
            char[] charsMiasto = miasto.toCharArray();
            for (char c : charsMiasto) {
                if (Character.isDigit(c) && i == 0) {
                    i++;
                    error++;
                    alert.setContentText("Miasto zawiera cyfry.");
                    alert.showAndWait();
                }
            }
            if (error == 0) {
                ArrayList<Rezerwacja> array = DbAccess.wyszukajWgMiasta(fieldWyszukaj.getText());
                daneR.setAll(array);
            }
        }
    }
}
