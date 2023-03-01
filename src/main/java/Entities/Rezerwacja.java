package Entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Reprezentuje tabelę 'rezerwacja' w bazie danych. 
 * 
 * @author Tomasz Pitak
 */
@Entity
@Table(name = "rezerwacja")
public class Rezerwacja implements Serializable {

    /**
     * Unikalny identyfikator rezerwacji. Generowany automatycznie.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRezerwacji")
    private int idRezerwacji;
  
    /**
     * Obiekt Pasazer. W bazie danych reprezentuje klucz obcy tabeli 'pasazer'.
     */
    @ManyToOne(targetEntity = Pasazer.class, cascade = {CascadeType.ALL})
    @JoinColumn(name = "idPasazera", nullable = false/* insertable = false*/)
    private Pasazer idPasazera;
    
    /**
     * Obiekt Lot. W bazie danych reprezentuje klucz obcy tabeli 'lot'.
     */
    @ManyToOne(targetEntity = Lot.class,cascade = {CascadeType.ALL})
    @JoinColumn(name = "idLotu", nullable = false)
    private Lot idLotu;
    
    /**
     * Numer siedzenia w samolocie.
     */
    @Column(name = "numerSiedzenia")
    private int numerSiedzenia;
    
    /**
     * Waga bagażu.
     */
    @Column(name = "bagaz")
    private int bagaz;

    public Rezerwacja() {
    }

    /**
     * Konstruuje obiekt Rezerwacja.
     * 
     * @param numerSiedzenia numer siedzenia w samolocie
     * @param bagaz waga bagażu
     * @param idPasazera obiekt Pasazer
     * @param idLotu obiekt Lot
     */
    public Rezerwacja( int numerSiedzenia, int bagaz, Pasazer idPasazera, Lot idLotu) {
        this.idPasazera = idPasazera;
        this.idLotu = idLotu;
        this.numerSiedzenia = numerSiedzenia;
        this.bagaz = bagaz;
    }

    /**
     * Konstruuje obiekt Rezerwacja wraz z określonym identyfikatorem. Używany podczas odczytu formatu XML.
     * 
     * @param idRezerwacji
     * @param idPasazera
     * @param idLotu
     * @param numerSiedzenia
     * @param bagaz 
     */
    public Rezerwacja(int idRezerwacji, Pasazer idPasazera, Lot idLotu, int numerSiedzenia, int bagaz) {
        this.idRezerwacji = idRezerwacji;
        this.idPasazera = idPasazera;
        this.idLotu = idLotu;
        this.numerSiedzenia = numerSiedzenia;
        this.bagaz = bagaz;
    }    

    public int getIdRezerwacji() {
        return idRezerwacji;
    }

    public Pasazer getIdPasazera() {
        return idPasazera;
    }

    public Lot getIdLotu() {
        return idLotu;
    }

    public int getNumerSiedzenia() {
        return numerSiedzenia;
    }

    public int getBagaz() {
        return bagaz;
    }

    public void setIdRezerwacji(int idRezerwacji) {
        this.idRezerwacji = idRezerwacji;
    }

    public void setIdPasazera(Pasazer idPasazera) {
        this.idPasazera = idPasazera;
    }

    public void setIdLotu(Lot idLotu) {
        this.idLotu = idLotu;
    }

    public void setNumerSiedzenia(int numerSiedzenia) {
        this.numerSiedzenia = numerSiedzenia;
    }

    public void setBagaz(int bagaz) {
        this.bagaz = bagaz;
    }

    @Override
    public String toString() {
        return "Rezerwacja{" + "idRezerwacji=" + idRezerwacji + ", idPasazera=" + idPasazera + ", idLotu=" + idLotu + ", numerSiedzenia=" + numerSiedzenia + ", bagaz=" + bagaz + '}';
    }   
}
