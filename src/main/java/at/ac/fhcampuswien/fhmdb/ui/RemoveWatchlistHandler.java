package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.fxml.FXML;

@FunctionalInterface
public interface RemoveWatchlistHandler
{
    void handle(Movie movie);

}
