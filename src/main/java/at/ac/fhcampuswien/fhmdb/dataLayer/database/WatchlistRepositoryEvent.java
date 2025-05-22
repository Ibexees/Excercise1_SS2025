package at.ac.fhcampuswien.fhmdb.dataLayer.database;

import at.ac.fhcampuswien.fhmdb.logic.models.Movie;

public class WatchlistRepositoryEvent {

    public enum Type {
        MOVIE_ADDED,
        MOVIE_REMOVED,
        MOVIE_ALREADY_EXISTS,
        ERROR
    }

    private final Type type;
    private final String message;

    public WatchlistRepositoryEvent(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    // Getter
    public Type getType() { return type; }
    public String getMessage() { return message; }
}
