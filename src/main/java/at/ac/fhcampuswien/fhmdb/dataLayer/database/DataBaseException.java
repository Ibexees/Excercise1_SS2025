package at.ac.fhcampuswien.fhmdb.dataLayer.database;

public class DataBaseException extends RuntimeException {// macht ausnahmen zu einer unexpected exception
    // man muss nichts explizit mit throws deklarieren,

    public DataBaseException(String message) {
        super(message);
    }
    // konstruktor nur für fehlermeldung

    public DataBaseException(String message, Throwable cause) {
        super(message, cause);
    }
    // konstruktor für fehlermeldung und message
}

// klasse speziell für datenbankfehler, behandelt diese
// databe wird in anderen klassen aufgerufen
