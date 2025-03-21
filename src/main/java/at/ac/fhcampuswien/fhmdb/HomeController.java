package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieComparator;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @FXML
    public HBox upperHbox;

    @FXML
    public JFXButton resetBtn;
    @FXML
    public JFXComboBox ratingComboBox;
    @FXML
    public JFXComboBox yearComboBox;
    private BooleanProperty isFiltered = new SimpleBooleanProperty(false);

    private Map<String,String> parameters = new HashMap<>();
    public List<Movie> allMovies = Movie.initializeMovies(parameters);

    private ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    private boolean asc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        resetBtn.setDisable(true);
            resetBtn.setVisible(false);
            observableMovies.addAll(allMovies);         // add dummy data to observable list

            // initialize UI stuff
            movieListView.setItems(observableMovies);   // set data of observable list to list view
            movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

            genreComboBox.getItems().addAll(Genre.values());
            genreComboBox.setPromptText("Filter by Genre");
            ratingComboBox.setPromptText("Filter by Rating");
            yearComboBox.setPromptText("Filter by Year");

        // TODO add event handlers to buttons and call the regarding methods
        // either set event handlers in the fxml file (onAction) or add them here
        searchBtn.setOnAction(this::handleFilter);
        resetBtn.setOnAction(this::handleReset);
        isFiltered.addListener((observable, oldValue, newValue) -> controlResetButton());

        // Sort button example:
        sortBtn.setOnAction(actionEvent -> {
            //movieList Leeren
            //movieListView = new JFXListView<>(); //braucht man nicht, würde mit filtern auch nicht funktionieren (von Elias)
            if(sortBtn.getText().equals("Sort (asc)"))
            {
                    asc = true;
                    allMovies = sortMovies(asc,allMovies); //damit alle Movies sortieren, ist relevant für das Filtern.
                    observableMovies = (ObservableList<Movie>) sortMovies(asc,observableMovies);

                    sortBtn.setText("Sort (desc)");
            }
           else
            {
                asc = false;
                allMovies = sortMovies(asc,allMovies); //damit alle Movies sortieren, ist relevant für das Filtern.
                observableMovies = (ObservableList<Movie>) sortMovies(asc,observableMovies);

                sortBtn.setText("Sort (asc)");
            }
            movieListView.setItems( observableMovies);   // set data of observable list to list view
            movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data
        });



    }

    public void setAllMovies(ObservableList<Movie> movieList)
    {
        allMovies = movieList;
        observableMovies = movieList;
    }

    private void handleReset(ActionEvent actionEvent) {
        observableMovies.clear();
        observableMovies.addAll(allMovies);
        movieListView.setItems(observableMovies);

        resetBtn.setVisible(false);
        resetBtn.setManaged(false);
        resetBtn.setDisable(true);

        genreComboBox.setValue(null);
        genreComboBox.resetValidation();
        searchField.clear();
        isFiltered.set(false);
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

    private void handleFilter(ActionEvent actionEvent) {
        System.out.println("Filter Button pressed");
        String searchText = searchField.getText();
        Genre param = null;

        if (genreComboBox.getSelectionModel().getSelectedItem() != null) {
            param = Genre.valueOf(genreComboBox.getSelectionModel().getSelectedItem().toString());
            parameters.put("genre",param.toString());
        }

        if (ratingComboBox.getSelectionModel().getSelectedItem() != null) {
            param = Genre.valueOf(ratingComboBox.getSelectionModel().getSelectedItem().toString());
            parameters.put("rating",param.toString());
        }

        //New Filter handled by API
        ObservableList<Movie> filteredMoviesByAPI;
        filteredMoviesByAPI =  FXCollections.observableArrayList(Movie.initializeMovies(parameters));
        observableMovies = filteredMoviesByAPI;

        //old Logic, now handled by API
        //ObservableList<Movie> filteredMovies;
        //filteredMovies = (ObservableList<Movie>) filterMovies(genre, searchText);

        movieListView.setItems(observableMovies);
        movieListView.refresh();
        isFiltered.set(true);
    }

    //Not in use anymore, handled by API
    public List<Movie> filterMovies(Genre genres, String searchText)
    {
        ObservableList<Movie> filteredObservableMovies = FXCollections.observableArrayList();

        for (Movie movie : allMovies) {
            if(movie.getGenres().contains(genres)||genres == null) {
                if (searchText.isEmpty()) {
                    filteredObservableMovies.add(movie);
                }
                else {
                    if (movie.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                        filteredObservableMovies.add(movie);
                    }
                    else continue;
                }
            }
        }

        return filteredObservableMovies;
    }

    public void controlResetButton() {
        if (isFiltered.get()) {
            resetBtn.setVisible(true);
            resetBtn.setManaged(true);
            resetBtn.setDisable(false);
        }
        else {
            resetBtn.setVisible(false);
            resetBtn.setManaged(false);
            resetBtn.setDisable(true);
        }
    }
}