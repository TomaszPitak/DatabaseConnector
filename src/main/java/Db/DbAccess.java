package Db;

import Entities.*;
import Singleton.SingletonConnection;
import javax.persistence.*;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 * Obsługuje operacje na bazie danych.
 *
 * @author Tomasz Pitak
 */
public class DbAccess {

    private static SessionFactory sessionFactory;
    private static Session session;
    /**
     * Przechowuje informację o stanie połączenia (true/false)
     */
    public static boolean CONNECTION;

    /**
     * Pobiera nazwę bazy danych, z którą się łączy.
     *
     * @return name nazwę bazy danych, jeśi nawiązano połączenie; "błąd
     * połączenia", jeśli nie udało się nawiązać połączenia.
     */
    public static String getDatabaseName() {
        String name;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            name = session.createSQLQuery("SELECT DATABASE()").getSingleResult().toString();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            CONNECTION = false;
            return "błąd połączenia";
        }
        CONNECTION = true;
        return name;
    }

    /**
     * Ustawia zmienną statyczną 'sessionFactory'.
     */
    static {
        try {
            sessionFactory = SingletonConnection.getSessionFactory();
        } catch (Exception e) {
        }
    }

    /**
     * Pobiera z bazy danych wszystkie rezerwacje na dany numer PESEL.
     *
     * @param pesel numer PESEL pasażera
     * @return tablica pobranych rezerwacji
     */
    public static ArrayList<Rezerwacja> wyszukajWgPeselu(String pesel) {

        session = sessionFactory.openSession();
        session.beginTransaction();

        ArrayList<Rezerwacja> array = new ArrayList();

        //pobranie listy obiektów z bazy
        List<Rezerwacja> r = session.createSQLQuery("SELECT * from rezerwacja inner join pasazer on rezerwacja.idPasazera=pasazer.idPasazera where pesel=\"" + pesel + "\"").addEntity(Rezerwacja.class).list();
        //dodanie wierszy do tablicy
        for (Rezerwacja e : r) {
            array.add(e);
        }

        session.getTransaction().commit();
        session.close();

        return array;
    }

    /**
     * Pobiera z bazy danych wszystkie rezerwacje na dane nazwisko.
     *
     * @param nazwisko nazwisko pasażera
     * @return tablica pobranych rezerwacji
     */
    public static ArrayList<Rezerwacja> wyszukajWgNazwiska(String nazwisko) {

        session = sessionFactory.openSession();
        session.beginTransaction();

        ArrayList<Rezerwacja> array = new ArrayList();

        List<Rezerwacja> lista = session.createSQLQuery("SELECT * from rezerwacja inner join pasazer on rezerwacja.idPasazera=pasazer.idPasazera where nazwisko=\"" + nazwisko + "\"").addEntity(Rezerwacja.class).list();

        for (Rezerwacja e : lista) {
            array.add(e);
        }

        session.getTransaction().commit();
        session.close();

        return array;
    }

    /**
     * Pobiera z bazy danych wszystkie rezerwacje na lot do danego miasta.
     *
     * @param miasto nazwa miasta, do którego realizowany jest lot
     * @return tablica pobranych rezerwacji
     */
    public static ArrayList<Rezerwacja> wyszukajWgMiasta(String miasto) {

        session = sessionFactory.openSession();
        session.beginTransaction();

        ArrayList<Rezerwacja> array = new ArrayList();

        List<Rezerwacja> lista = session.createSQLQuery("SELECT * from rezerwacja inner join lot on rezerwacja.idLotu=lot.idLotu where miejscePrzylotu=\"" + miasto + "\"").addEntity(Rezerwacja.class).list();

        for (Rezerwacja e : lista) {
            array.add(e);
        }

        session.getTransaction().commit();
        session.close();

        return array;
    }

    /**
     * Dodaje rezerwację do bazy danych. Jeśli w bazie danych nie istanieje
     * pasażer o podanym numerze PESEL, zostanie on utworzony.
     *
     * @param imie imię pasażera
     * @param nazwisko nazwisko pasażera
     * @param pesel numer PESEL pasażera
     * @param numerTelefonu numer telefonu pasażera
     * @param email adres e-mail pasażera
     * @param przylot nazwa miasta, do którego realizowany jest lot
     * @param data data wylotu
     * @param numerSiedzenia numer siedzenia w samolocie
     * @param bagaz waga bagażu
     *
     * @return dodana rezerwacja
     */
    public static Rezerwacja dodajR(String imie, String nazwisko, String pesel, String numerTelefonu, String email, String przylot, String data, String numerSiedzenia, String bagaz) {
        session = sessionFactory.openSession();
        session.beginTransaction();

        Rezerwacja rezerwacja;
        Pasazer pasazer;

        //pobranie obiektu Lot
        Lot lot = (Lot) session.createSQLQuery("SELECT * from lot where dataWylotu=\"" + data + "\" AND miejscePrzylotu=\"" + przylot + "\"").addEntity(Lot.class).getSingleResult();

        try {
            //sprawdzenie czy osoba na którą chcemy dokonać rezerwacji istnieje już w bazie
            pasazer = (Pasazer) session.createSQLQuery("SELECT * from pasazer where pesel=\"" + pesel + "\"").addEntity(Pasazer.class).getSingleResult();
            //dodanie rezerwacji
            rezerwacja = new Rezerwacja(Integer.parseInt(numerSiedzenia), Integer.parseInt(bagaz), pasazer, lot);
            session.save(rezerwacja);
        } catch (NoResultException e) {//obiekt pasazer nie istnieje w bazie
            //utworzenie nowego pasazera
            pasazer = new Pasazer(imie, nazwisko, pesel, numerTelefonu, email);
            //dodanie pasazera
            session.save(pasazer);
            //dodanie rezerwacji
            rezerwacja = new Rezerwacja(Integer.parseInt(numerSiedzenia), Integer.parseInt(bagaz), pasazer, lot);
            session.save(rezerwacja);
        }

        session.getTransaction().commit();
        session.close();
        return rezerwacja;
    }

    /**
     * Aktualizuje obiekt Rezerwacja w bazie danych oraz związane z nim obiekty
     * 'pasazer' i 'lot'. Jeśli zmienimy dane pasażera w jednej rezerwacji,
     * zmiany bedą dotyczyć wszystkich rezerwacji na tego pasażera.
     *
     * @param imie
     * @param nazwisko
     * @param pesel
     * @param numerTelefonu
     * @param email
     * @param przylot
     * @param data
     * @param numerSiedzenia
     * @param bagaz
     * @param idRezerwacji
     */
    public static void updateR(String imie, String nazwisko, String pesel, String numerTelefonu, String email, String przylot, String data, String numerSiedzenia, String bagaz, String idRezerwacji) {

        session = sessionFactory.openSession();
        session.beginTransaction();

        //pobranie obiektu rezerwacja z bazy
        Rezerwacja rezerwacja = (Rezerwacja) session.createSQLQuery("SELECT * from rezerwacja where idRezerwacji=\"" + idRezerwacji + "\"").addEntity(Rezerwacja.class).getSingleResult();
        //wyodrębnienie obiektu pasazer z obiektu rezerwacja
        Pasazer pasazer = rezerwacja.getIdPasazera();
        //pobranie obiektu lot z bazy
        //Lot lot = rezerwacja.getIdLotu();

        Lot lot = (Lot) session.createSQLQuery("SELECT * from lot where miejscePrzylotu=\"" + przylot + "\" AND dataWylotu=\"" + data + "\"").addEntity(Lot.class).getSingleResult();

        //aktualizacja parametrów pasazera
        pasazer.setEmail(email);
        pasazer.setImie(imie);
        pasazer.setNazwisko(nazwisko);
        pasazer.setNumerTelefonu(numerTelefonu);
        pasazer.setPesel(pesel);
        //aktualizacja parametrów rezerwacji
        rezerwacja.setBagaz(Integer.parseInt(bagaz));
        rezerwacja.setNumerSiedzenia(Integer.parseInt(numerSiedzenia));
        rezerwacja.setIdLotu(lot);
        //aktualizacja parametrów lotu
        rezerwacja.setIdLotu(lot);
        //update obiektu pasazer
        session.update(pasazer);
        //update obiektu rezerwacja
        session.update(rezerwacja);

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Aktualizuje obiekt Lot w bazie danych. Zmiany zostaną zastosowane dla
     * wszystkich rezerwacji na ten lot.
     *
     * @param data
     * @param miasto
     * @param idLotu
     */
    public static void updateL(String data, String miasto, String idLotu) {

        session = sessionFactory.openSession();
        session.beginTransaction();

        //pobranie obiektu lot z bazy
        Lot lot = (Lot) session.createSQLQuery("SELECT * from lot where idLotu=\"" + idLotu + "\"").addEntity(Lot.class).getSingleResult();

        //aktualizacja parametrów lotu
        lot.setMiejscePrzylotu(miasto);
        lot.setDataWylotu(LocalDate.parse(data));

        //update obiektu lot
        session.update(lot);

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Usuwa rezerwację z bazy danych. Usuwając ostatnią rezerwację na danego
     * pasażera, usuwamy również tego pasażera.
     *
     * @param rezerwacja
     */
    public static void deleteR(Rezerwacja rezerwacja) {

        session = sessionFactory.openSession();
        session.beginTransaction();

        //pobranie obiektu rezerwacja o takim id co w tabeli
        Rezerwacja r = session.get(Rezerwacja.class, rezerwacja.getIdRezerwacji());
        //pobranie obiektu pasazer z obiektu rezerwacja
        Pasazer pasazer = rezerwacja.getIdPasazera();
        //pobranie wszystkich rezerwacji pasazera
        List<Rezerwacja> lista = session.createSQLQuery("SELECT * from rezerwacja where idPasazera=\"" + pasazer.getIdPasazera() + "\"").addEntity(Rezerwacja.class).list();
        //jesli liczba rezerwacji na danego pasazera == 1
        if (lista.size() == 1) {
            //usuniecie obiektu rezerwacja spowoduje równiez usunięcie obiektu pasazer
            r.setIdLotu(null);
        } else {
            //usuniecie obiektu rezerwacja nie spowoduje usuniecia obiektów pasazer i lot
            r.setIdLotu(null);
            r.setIdPasazera(null);
        }

        session.delete(r);

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Usuwa lot z bazy danych. Usuwając lot usuwamy także wszystkie rezerwacje
     * na ten lot. Jeśli usuniemy przy tym wszystkie rezerwacje danego pasażera,
     * to usuwany jest również ten pasażer.
     *
     * @param lot
     */
    public static void deleteL(Lot lot) {

        session = sessionFactory.openSession();
        session.beginTransaction();

        Lot l = session.get(Lot.class, lot.getIdLotu());
        List<Rezerwacja> lista = session.createSQLQuery("SELECT * from rezerwacja where idLotu=\"" + lot.getIdLotu() + "\"").addEntity(Rezerwacja.class).list();

        if (lista.isEmpty()) {
            session.delete(l);
        } else {
            session.getTransaction().commit();
            session.close();
            //usuniecie wszystkich rezerwacji na dany lot
            for (int i = 0; i < lista.size(); i++) {
                deleteR(lista.get(i));
            }
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(l);
        }

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Pobiera listę wszystkich miast z bazy danych, do których realizowane są
     * loty.
     *
     * @return tablica pobranych miast
     */
    public static ArrayList<String> pobierzMiasta() {

        session = sessionFactory.openSession();
        session.beginTransaction();

        //pobranie obiektów lot z unikalnymi wartosciami kolumny miejscePrzylotu 
        List<Lot> lista = session.createSQLQuery("select * from lot group by miejscePrzylotu").addEntity(Lot.class).list();
        ArrayList<String> miasta = new ArrayList<>();
        //pobranie samych miast
        for (int i = 0; i < lista.size(); i++) {
            miasta.add(lista.get(i).getMiejscePrzylotu());
        }

        session.getTransaction().commit();
        session.close();
        return miasta;
    }

    /**
     * Pobiera listę dat, w których realizowane są loty do danego miasta.
     *
     * @param miasto nazwa miasta
     * @return tablica pobranych dat
     */
    public static ArrayList<String> pobierzDaty(String miasto) {

        session = sessionFactory.openSession();
        session.beginTransaction();

        List<Lot> lista = session.createSQLQuery("SELECT * from lot where miejscePrzylotu=\"" + miasto + "\"").addEntity(Lot.class).list();
        ArrayList<String> daty = new ArrayList<>();

        for (int i = 0; i < lista.size(); i++) {
            daty.add(lista.get(i).getDataWylotu().toString());
        }

        session.getTransaction().commit();
        session.close();
        return daty;
    }

    //wyszukiwanie lotów
    /**
     * Wyszukuje wszystkie loty do danego miasta.
     *
     * @param miasto nazwa miasta
     * @return tablica pobranych lotów
     */
    public static ArrayList<Lot> wyszukajLoty(String miasto) {

        session = sessionFactory.openSession();
        session.beginTransaction();

        List<Lot> lista = session.createSQLQuery("SELECT * from lot where miejscePrzylotu=\"" + miasto + "\"").addEntity(Lot.class).list();
        ArrayList<Lot> loty = new ArrayList<>();

        for (Lot e : lista) {
            loty.add(e);
        }

        session.getTransaction().commit();
        session.close();

        return loty;
    }

    /**
     * Dodaje lot do bazy danych.
     *
     * @param data data wylotu
     * @param miasto nazwa miasta
     * @return dodany lot
     */
    public static Lot dodajLot(String data, String miasto) {
        session = sessionFactory.openSession();
        session.beginTransaction();

        Lot lot = null;
        try {
            session.createSQLQuery("SELECT * from lot where dataWylotu=\"" + data + "\" AND miejscePrzylotu=\"" + miasto + "\"").addEntity(Lot.class).getSingleResult();
            return lot;
        } catch (Exception e) {
            lot = new Lot(LocalDate.parse(data), miasto);
            session.save(lot);
        }

        session.getTransaction().commit();
        session.close();
        return lot;
    }

    /**
     * Wyszukuje pasażera
     *
     * @param pesel
     * @return true - jeśli pasażer istanieje w bazie false - jeśli pasażer nie
     * istnieje w bazie
     */
    public static boolean wyszukajPasazera(String pesel) {

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Pasazer r = (Pasazer) session.createSQLQuery("SELECT * from pasazer where pesel=\"" + pesel + "\"").addEntity(Pasazer.class).getSingleResult();
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

}
