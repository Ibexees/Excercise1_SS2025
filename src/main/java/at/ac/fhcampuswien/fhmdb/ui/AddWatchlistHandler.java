package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;

@FunctionalInterface
public interface AddWatchlistHandler
{
    void handle(Movie movie);
}
