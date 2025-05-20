package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.dataLayer.api.MovieAPIException;
import at.ac.fhcampuswien.fhmdb.dataLayer.api.Deserializer;
import at.ac.fhcampuswien.fhmdb.dataLayer.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.DataBaseException;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.logic.MovieAnalysisService;
import at.ac.fhcampuswien.fhmdb.logic.NotSorted;
import at.ac.fhcampuswien.fhmdb.logic.SortState;
import at.ac.fhcampuswien.fhmdb.logic.models.Genre;
import at.ac.fhcampuswien.fhmdb.logic.models.Movie;
import at.ac.fhcampuswien.fhmdb.logic.MovieComparator;
import at.ac.fhcampuswien.fhmdb.logic.models.Rating;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.ui.MovieCellActionHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable, MovieCellActionHandler
{
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
    public TextField yearField;
    @FXML
    public JFXButton homeBtn;
    @FXML
    public JFXButton watchlistBtn;
    @FXML
    public JFXButton aboutBtn;

    public static boolean buttonsVisible = true;
    private BooleanProperty isFiltered = new SimpleBooleanProperty(false);

    private Map<String,String> parameters = new HashMap<>();
    public List<Movie> allMovies;
    public ObservableList<Movie> watchListMovies = FXCollections.observableArrayList();
    public ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    public MovieRepository movieRepository;
    private WatchlistRepository watchlistRepository;

    private MovieAnalysisService movieAnalysisService;
    private SortState sortState;
    private boolean asc;

    public void setSortState(SortState sortState)
    {
        this.sortState = sortState;
    }

    public SortState getSortState()
    {
        return sortState;
    }

    public HomeController() {
            movieAnalysisService = new MovieAnalysisService();
            movieRepository = new MovieRepository();
            watchlistRepository = new WatchlistRepository();
    }

    private void initializeWatchlistFromDB() throws SQLException
    {
        List<WatchlistMovieEntity> dbWatchlist = watchlistRepository.getWatchlist();
        ArrayList<Movie> initCache = new ArrayList<>();

        for(WatchlistMovieEntity dbWatchlistMovie :  dbWatchlist)
        {
           initCache.addAll(allMovies.stream().filter(movie -> movie.getId().equals( dbWatchlistMovie.getApiId())).toList());
        }
        Set<Movie> erasedDuplicates = new TreeSet<Movie>(new MovieComparator());
        erasedDuplicates.addAll(initCache);
        watchListMovies.addAll(erasedDuplicates);
    }

    public void showMovieDetails(Movie movie)
    {
        return;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            // Init der Movies
            loadMoviesFromAPIAndRefreshDatabase();
            setupInitialUIState();
            setupMovieListView();
            setupComboBoxes();
            setupButtonHandlers();
            setupSortButton();
            setupResetListener();
    }

    private void loadMoviesFromAPIAndRefreshDatabase() {
        try {
            allMovies = initializeMovies(parameters);
            movieRepository.removeAll();
            movieRepository.addAllMovies(allMovies);
            initializeWatchlistFromDB();
        } catch (DataBaseException e) {
            showErrorDialog("Database error", "failed to save movies" );
        } catch (Exception e) {
            showErrorDialog("Unknown error", "unexpected error occured" );

        }
    }

    public List<Movie> initializeMovies(Map<String, String> parameters) {
        List<Movie> movies = new ArrayList<>();
        try {
            // Fetch movies von API
            String apiResponse = MovieAPI.getMovies(parameters);
            movies = Deserializer.deserializeJsonToMovieModel(apiResponse);
            System.out.println("Fetched movies from API.");
        } catch (MovieAPIException e) {

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("API Error");
                alert.setHeaderText("Failed to fetch movies from API");
                alert.setContentText(e.getMessage());
                alert.initModality(Modality.APPLICATION_MODAL);

                alert.showAndWait();
            });
            System.err.println("API failed: " + e.getMessage());
            try {
                // Fallback: Movies von Database holen
                movies = movieRepository.getAllMovies();
                System.out.println("Loaded movies from database fallback.");
            } catch (DataBaseException dbException) {
                System.err.println("Database loading failed: " + dbException.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize movies: " + e.getMessage(), e);
        }

        return movies;
    }

    private void setupInitialUIState() {
        resetBtn.setDisable(true);
        resetBtn.setVisible(false);
        observableMovies.addAll(allMovies);
    }

    private void setupMovieListView() {
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell(
                movie -> onAddWatchlistClicked(movie),
                movie -> showMovieDetails(movie),
                movie -> onRemoveWatchlistClicked(movie))
        );
    }

    private void setupComboBoxes() {
        genreComboBox.getItems().addAll(Genre.values());
        genreComboBox.setPromptText("Filter by Genre");

        ratingComboBox.getItems().addAll(Rating.values());
        ratingComboBox.setPromptText("Filter by Rating and Above");
    }

    private void setupButtonHandlers() {
        searchBtn.setOnAction(this::handleFilter);

        resetBtn.setOnAction(event -> {
            try {
                handleReset(event);
            } catch (SQLException e) {
                showDatabaseErrorDialog(e);
            }
        });

        homeBtn.setOnAction(event -> {
            try {
                handleReset(event);
            } catch (SQLException e) {
                showDatabaseErrorDialog(e);
            }
        });

        watchlistBtn.setOnAction(this::displayWatchList);
    }

    private void showDatabaseErrorDialog(SQLException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setHeaderText("An error occurred while accessing the database.");
        alert.setContentText(e.getMessage());
    }

    private void setupResetListener() {
        isFiltered.addListener((observable, oldValue, newValue) -> controlResetButton());
    }


    //ersetzt durch SortState Pattern
    /*private void setupSortButton() {
        sortBtn.setOnAction(actionEvent -> {
            asc = sortBtn.getText().equals("Sort (asc)");
            allMovies = sortMovies(asc, allMovies);
            observableMovies = FXCollections.observableArrayList(sortMovies(asc, observableMovies));

            sortBtn.setText(asc ? "Sort (desc)" : "Sort (asc)");

            movieListView.setItems(observableMovies);
            movieListView.setCellFactory(movieListView -> new MovieCell(
                    movie -> onAddWatchlistClicked(movie),
                    movie -> showMovieDetails(movie),
                    movie -> onRemoveWatchlistClicked(movie))
            );
        });
    }*/
    private void setupSortButton()
    {
        sortState = new NotSorted();
        sortBtn.setOnAction(actionEvent -> {

            sortState.sort(this);

            movieListView.setCellFactory(movieListView -> new MovieCell(
                    movie -> onAddWatchlistClicked(movie),
                    movie -> showMovieDetails(movie),
                    movie -> onRemoveWatchlistClicked(movie)));

        });
    }

    public ObservableList<Movie> getObservableMovies()
    {
        return observableMovies;
    }

    public void setObservableMovies(ObservableList<Movie> observableMovies)
    {
        this.observableMovies = observableMovies;
    }

    private void displayWatchList(ActionEvent event)
    {
        buttonsVisible = false;
        movieListView.setItems(watchListMovies);


    }


    public void setAllMovies(ObservableList<Movie> movieList)
    {
        allMovies = movieList;
        observableMovies = movieList;
    }

    /**Resets all Parameters and fetches all movies from the API*/
    private void handleReset(ActionEvent actionEvent) throws SQLException {
        buttonsVisible = true;
        observableMovies.clear();
        allMovies = FXCollections.observableArrayList(initializeMovies(null));
        observableMovies.addAll(allMovies);
        movieListView.setItems(observableMovies);

        resetBtn.setVisible(false);
        resetBtn.setManaged(false);
        resetBtn.setDisable(true);

        genreComboBox.getSelectionModel().clearSelection();
        ratingComboBox.getSelectionModel().clearSelection();
        genreComboBox.getSelectionModel().clearSelection();
        searchField.clear();
        yearField.clear();
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

        // Clear previous filtered results
        observableMovies.clear();

        // Prepare filter parameters
        Map<String, String> parameters = new HashMap<>();

        if (searchField.getText() != null && !searchField.getText().isEmpty()) {
            parameters.put("query", searchField.getText());
        }

        if (genreComboBox.getSelectionModel().getSelectedItem() != null) {
            Genre genre = Genre.valueOf(genreComboBox.getSelectionModel().getSelectedItem().toString());
            parameters.put("genre", genre.toString());
        }

        if (ratingComboBox.getSelectionModel().getSelectedItem() != null) {
            Rating rating = Rating.valueOf(ratingComboBox.getSelectionModel().getSelectedItem().toString());
            parameters.put("ratingFrom", Integer.toString(rating.getRating()));
        }

        if (yearField.getText() != null && !yearField.getText().isEmpty()) {
            try {
                Integer.parseInt(yearField.getText()); // Prüfen, ob es eine Nummer ist
                parameters.put("releaseYear", yearField.getText());
            } catch (NumberFormatException e) {

                //yearField.clear();
                showErrorDialog("Info", "Invalid year in searchBox.");
                //throw new NumberFormatException("Invalid year");

            }
        }

        try {

            if (!parameters.isEmpty()) {
                System.out.println("Attempting to use API for filtering with parameters: " + parameters);
                // Zuerst mit der API versuchen zu filtern.
                try {

                    String apiResponse = MovieAPI.getMovies(parameters);
                    List<Movie> filteredMovies = Deserializer.deserializeJsonToMovieModel(apiResponse);
                    observableMovies.addAll(filteredMovies);
                    System.out.println("Successfully filtered via API, found " + filteredMovies.size() + " movies");
                } catch (MovieAPIException e) {
                    System.out.println("API filtering failed: " + e.getMessage());
                    // Daten aus der Database holen und local filtern.
                    performLocalFiltering(parameters);
                }
            } else {
                observableMovies.addAll(allMovies);
            }

            // Update UI
            movieListView.setItems(observableMovies);
            movieListView.refresh();
            isFiltered.set(!parameters.isEmpty());

        } catch (Exception e) {
            System.err.println("Filtering failed: " + e.getMessage());
        }
    }

    public void performLocalFiltering(Map<String, String> parameters)  {
        List<Movie> moviesToFilter;

        moviesToFilter = movieRepository.getAllMovies();
        System.out.println(moviesToFilter.size() + " movies from database loaded");


        // Apply filtering
        List<Movie> filteredMovies = filterMovies(moviesToFilter, parameters);

        // Clear and update observable list
        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }

    public List<Movie> filterMovies(List<Movie> movies, Map<String, String> parameters) {
        // Parameter rausholen
        String searchText = parameters.getOrDefault("query", "");
        Genre genre = parameters.containsKey("genre") ? Genre.valueOf(parameters.get("genre")) : null;
        Integer ratingFrom = parameters.containsKey("ratingFrom") ? Integer.parseInt(parameters.get("ratingFrom")) : null;
        String releaseYear = parameters.getOrDefault("releaseYear", "");

        //nach Parametern filtern
        return movies.stream()
                .filter(movie -> {
                    // Search text filter
                    boolean matchesSearchText = searchText.isEmpty() ||
                            movie.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                            movie.getDescription().toLowerCase().contains(searchText.toLowerCase());

                    // Genre filter
                    boolean matchesGenre = genre == null || movie.getGenres().contains(genre);

                    // Rating filter
                    boolean matchesRating = ratingFrom == null || movie.getRating() >= ratingFrom;

                    // Release year filter
                    boolean matchesReleaseYear = releaseYear.isEmpty() ||
                            Integer.toString(movie.getReleaseYear()).equals(releaseYear);

                    return matchesSearchText && matchesGenre && matchesRating && matchesReleaseYear;
                })
                .collect(Collectors.toList());
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


    @Override
    public void onAddWatchlistClicked(Movie movie)
    {
        boolean movieInList = watchListMovies.stream().
                anyMatch(contain -> contain.equals(movie));

        if(!movieInList)
        {
            watchListMovies.add(movie);
            try
            {
                watchlistRepository.addToWatchlist(new WatchlistMovieEntity(movie));
            } catch (SQLException | DataBaseException e){
                showErrorDialog("Database Error", "failed to add movie to watchlist");

            } catch (Exception e) {
                showErrorDialog("unexpected error", "unexpected error occured");
            }
        }
        else
        {
            System.out.println("Movie already in Watchlist");
        }

    }

    @Override
    public void onShowDetailsClicked(Movie movie)
    {
        System.out.println("showDetailsClicked");
    }

    @Override
    public void onRemoveWatchlistClicked(Movie movie)
    {

        boolean movieInList = watchListMovies.stream().
                anyMatch(contain -> contain.equals(movie));

        if (movieInList) {
            watchListMovies.remove(movie);
            try {
                watchlistRepository.removeFromWatchlist(movie.getId());
                showErrorDialog("Success", "Movie successfully removed from watchlist!");
            } catch (DataBaseException e) {
                showErrorDialog("Database Error", "Failed to remove movie from watchlist: " + e.getMessage());
            } catch (Exception e) {
                showErrorDialog("Unexpected Error", "An unexpected error occurred while removing from the watchlist: " + e.getMessage());
            }
        } else {
            showErrorDialog("Info", "Movie is not in the watchlist.");
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}



