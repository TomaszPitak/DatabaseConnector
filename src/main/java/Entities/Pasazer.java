package Entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Reprezentuje tabelę 'pasazer' w bazie danych. 
 * 
 * @author Tomasz Pitak
 */
@Entity
@Table(name = "pasazer")
public class Pasazer implements Serializable {

    /**
     * Unikalny identyfikator pasażera. Generowany automatycznie.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPasazera")
    private int idPasazera;
    
    /**
     * Imię pasażera.
     */
    @Column(name = "imie")
    private String imie;
    
    /**
     * Nazwisko pasażera.
     */
    @Column(name = "nazwisko")
    private String nazwisko;
    
    /**
     * numer PESEL pasażera.
     */
    @Column(name = "pesel")
    private String pesel;
    
    /**
     * Numer telefonu pasażera.
     */
    @Column(name = "numerTelefonu")
    private String numerTelefonu;
    
    /**
     * Adres e-mail pasażera.
     */
    @Column(name = "email")
    private String email;

    /**
     * Konstruuje obiekt Pasazer.
     * 
     * @param imie imię pasażera
     * @param nazwisko nazwisko pasażera
     * @param pesel numer PESEL pasażera
     * @param numerTelefonu numer telefonu pasażera
     * @param email adres e-mail pasażera
     */
    public Pasazer(String imie, String nazwisko, String pesel, String numerTelefonu, String email) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pesel = pesel;
        this.numerTelefonu = numerTelefonu;
        this.email = email;
    }

    public Pasazer() {
    }

    public int getIdPasazera() {
        return idPasazera;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getPesel() {
        return pesel;
    }

    public String getNumerTelefonu() {
        return numerTelefonu;
    }

    public String getEmail() {
        return email;
    }

    public void setIdPasazera(int idPasazera) {
        this.idPasazera = idPasazera;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public void setNumerTelefonu(String numerTelefonu) {
        this.numerTelefonu = numerTelefonu;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Pasazer{" + "id_pasazera=" + idPasazera + ", imie=" + imie + ", nazwisko=" + nazwisko + ", pesel=" + pesel + ", numer_telefonu=" + numerTelefonu + ", email=" + email + '}';
    }
}
