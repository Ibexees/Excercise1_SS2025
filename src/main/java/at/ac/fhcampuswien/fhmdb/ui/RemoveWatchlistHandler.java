package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.logic.models.Movie;

@FunctionalInterface
public interface RemoveWatchlistHandler
{
    void handle(Movie movie);

}
