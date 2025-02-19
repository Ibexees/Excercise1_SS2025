package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieComparator;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    private boolean isSet = false;

    public List<Movie> allMovies = Movie.initializeMovies();

    private ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!isSet) {
            observableMovies.addAll(allMovies);         // add dummy data to observable list

            // initialize UI stuff
            movieListView.setItems(observableMovies);   // set data of observable list to list view
            movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

            genreComboBox.getItems().addAll(Genre.values());
            genreComboBox.setPromptText("Filter by Genre");
            isSet = true;
        }

        // TODO add event handlers to buttons and call the regarding methods
        // either set event handlers in the fxml file (onAction) or add them here
        searchBtn.setOnAction(this::handle);

        // Sort button example:
        sortBtn.setOnAction(actionEvent -> {
            //movieList Leeren
            movieListView = new JFXListView<>();
            if(sortBtn.getText().equals("Sort (asc)"))
            {
                    boolean asc = true;
                    observableMovies = (ObservableList<Movie>) sortMovies(asc,observableMovies);
                    sortBtn.setText("Sort (desc)");
            }
           else
            {
                boolean asc = false;
                observableMovies = (ObservableList<Movie>) sortMovies(asc,observableMovies);
                sortBtn.setText("Sort (asc)");
            }
            movieListView.setItems( observableMovies);   // set data of observable list to list view
            movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data
        });



    }

    //TODO: Write Method
    public List<Movie> filterMovies(Genre genres, String filtertext)
    {
        observableMovies.clear();
        ObservableList<Movie> filteredObservableMovies = FXCollections.observableArrayList();
        for (Movie movie : allMovies) {
            if(movie.getGenres() != null && movie.getGenres().contains(genres)) {
                filteredObservableMovies.add(movie);
            }
        }
        return filteredObservableMovies;
    }



    public List<Movie> sortMovies(boolean sortLogic,List<Movie> observableMovies)
    {
            //Aufsteigend sortieren mit MovieComparator Klasse
            if(sortLogic)
            {observableMovies.sort(new MovieComparator());}
            else
            {//Aufsteigend sortieren mit MovieComparator Klasse
            observableMovies.sort(new MovieComparator().reversed());}

        return(observableMovies);

    }

    private void handle(ActionEvent actionEvent) {
        System.out.println("Filter Button pressed");
        String searchText = searchField.getText();
        Genre genre = null;

        if (genreComboBox.getSelectionModel().getSelectedItem() != null) {
            genre = Genre.valueOf(genreComboBox.getSelectionModel().getSelectedItem().toString());
        }

        ObservableList<Movie> filteredMovies = (ObservableList<Movie>) filterMovies(genre, searchText);

        System.out.println("ObservableMovies after filtering: " + filteredMovies);
        ObservableList<Movie> observableMovies2 = FXCollections.observableArrayList();
        movieListView.setItems(observableMovies2);
        movieListView.setCellFactory(null);
        movieListView.setCellFactory(movieListView -> new MovieCell());
// Perform some operations like clearing the items (if needed)
        observableMovies.clear();
        observableMovies.setAll(filteredMovies);
        //movieListView.setItems(observableMovies);
        movieListView.setItems(observableMovies);

// Reapply the custom cellFactory


    }
}