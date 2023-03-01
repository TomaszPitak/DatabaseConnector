
package Entities;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * Reprezentuje tabelę 'lot' w bazie danych. 
 * 
 * @author Tomasz Pitak
 */

@Entity
@Table(name = "lot")
public class Lot implements Serializable {
    
    /**
     * Unikalny identyfikator lotu. Generowany automatycznie.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLotu")
    private int idLotu;
    
    /**
     * Data wylotu z lotniska. 
     */
    @Column(name = "dataWylotu")
    private LocalDate dataWylotu;
    
    /**
     * Nazwa miasta, do którego jest realizowany lot.
     */
    @Column(name = "miejscePrzylotu")
    private String miejscePrzylotu;

    public Lot() {
    }

    /**
     * Konstruuje obiekt Lot.
     * 
     * @param dataWylotu data wylotu z lotniska
     * @param miejscePrzylotu nazwa miasta, do którego jest realizowany lot
     */
    public Lot( LocalDate dataWylotu, String miejscePrzylotu) {
        this.dataWylotu = dataWylotu;
        this.miejscePrzylotu = miejscePrzylotu;
    }

    public int getIdLotu() {
        return idLotu;
    }

    public LocalDate getDataWylotu() {
        return dataWylotu;
    }

    public String getMiejscePrzylotu() {
        return miejscePrzylotu;
    }

    public void setIdLotu(int idLotu) {
        this.idLotu = idLotu;
    }

    public void setDataWylotu(LocalDate dataWylotu) {
        this.dataWylotu = dataWylotu;
    }

    public void setMiejscePrzylotu(String miejscePrzylotu) {
        this.miejscePrzylotu = miejscePrzylotu;
    }

    @Override
    public String toString() {
        return "Lot{" + "id_lotu=" + idLotu + ", data_wylotu=" + dataWylotu + ", miejsce_przylotu=" + miejscePrzylotu + '}';
    }  
}
