package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.logic.models.Movie;

@FunctionalInterface
public interface ShowDetailsHandler
{
    void handle(Movie movie);
}
