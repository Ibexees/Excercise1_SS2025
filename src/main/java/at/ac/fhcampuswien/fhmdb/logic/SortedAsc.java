package at.ac.fhcampuswien.fhmdb.logic;

import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.logic.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class SortedAsc implements SortState
{
    @Override
    public void sort(HomeController context)
    {
        context.setSortState(new SortedDesc());
        context.sortBtn.setText("Sorted Desc");

        ObservableList<Movie> observableMovies = context.getObservableMovies();

        if(!HomeController.buttonsVisible)
        {observableMovies = context.getWatchListMovies();}

        observableMovies.sort(new MovieComparator().reversed());
        observableMovies = FXCollections.observableArrayList(observableMovies);
        context.setObservableMovies(observableMovies);
        context.movieListView.setItems(observableMovies);

    }
}
