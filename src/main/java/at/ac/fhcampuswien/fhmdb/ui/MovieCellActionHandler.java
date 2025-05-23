package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.logic.models.Movie;

public interface MovieCellActionHandler
{
    void onAddWatchlistClicked(Movie movie);
    void onShowDetailsClicked(Movie movie);
    void onRemoveWatchlistClicked(Movie movie);
}
