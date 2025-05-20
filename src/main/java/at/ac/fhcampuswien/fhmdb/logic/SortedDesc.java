package at.ac.fhcampuswien.fhmdb.logic;

import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.logic.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class SortedDesc implements SortState
{
    @Override
    public void sort(HomeController context)
    {
        context.setSortState(new SortedAsc());
        context.sortBtn.setText("Sorted Asc");

        ObservableList<Movie> observableMovies = context.getObservableMovies();
        observableMovies.sort(new MovieComparator());
        observableMovies = FXCollections.observableArrayList(observableMovies);
        context.setObservableMovies(observableMovies);
        context.movieListView.setItems(observableMovies);
    }
}
