package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.ApiException;
import at.ac.fhcampuswien.fhmdb.api.Deserializer;
import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.dataLayer.DataBaseException;
import at.ac.fhcampuswien.fhmdb.dataLayer.MovieRepository;
import at.ac.fhcampuswien.fhmdb.dataLayer.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.dataLayer.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieComparator;
import at.ac.fhcampuswien.fhmdb.models.Rating;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.ui.MovieCellActionHandler;
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
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    private MovieRepository movieRepository = new MovieRepository();
    private WatchlistRepository watchlistRepository = new WatchlistRepository();


    private boolean asc;

    public HomeController() throws SQLException {
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

    /*
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Boolean internetConnection = false;
        try {
            allMovies = Movie.initializeMovies(parameters);
            movieRepository.removeAll(); // clear old DB data
            movieRepository.addAllMovies(allMovies); // cache new movies
            initializeWatchlistFromDB();
        } catch (SQLException e) {
            showDatabaseErrorDialog(e);
        }

        resetBtn.setDisable(true);
            resetBtn.setVisible(false);
            observableMovies.addAll(allMovies);         // add dummy data to observable list

            // initialize UI stuff
            movieListView.setItems(observableMovies);   // set data of observable list to list view
            movieListView.setCellFactory(movieListView -> new MovieCell(
                    movie -> onAddWatchlistClicked(movie),
                    movie -> showMovieDetails(movie),
                    movie -> onRemoveWatchlistClicked(movie))
            ); // use custom cell factory to display data //erweitert um this Parameter um einen Referenz auf Homecontroller als "Eventhandler" an MovieCell zu Übergeben

            genreComboBox.getItems().addAll(Genre.values());
            genreComboBox.setPromptText("Filter by Genre");
            ratingComboBox.setPromptText("Filter by Rating and Above");
            ratingComboBox.getItems().addAll(Rating.values());

        searchBtn.setOnAction(this::handleFilter);
        resetBtn.setOnAction(this::handleReset);

        homeBtn.setOnAction(this::handleReset);
        watchlistBtn.setOnAction(this::displayWatchList);

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
            movieListView.setCellFactory(movieListView -> new MovieCell(
                    movie -> onAddWatchlistClicked(movie),
                    movie -> showMovieDetails(movie),
                    movie -> onRemoveWatchlistClicked(movie))
            ); // use custom cell factory to display data
        });



    }
*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadMoviesFromAPIAndCache();
        setupInitialUIState();
        setupMovieListView();
        setupComboBoxes();
        setupButtonHandlers();
        setupSortButton();
        setupResetListener();
    }

    private void loadMoviesFromAPIAndCache() {
        try {
            allMovies = initializeMovies(parameters);
            movieRepository.removeAll();
            movieRepository.addAllMovies(allMovies);
            initializeWatchlistFromDB();
        } catch (SQLException e) {
            //showDatabaseErrorDialog(e);
        }
    }

    public static List<Movie> initializeMovies(Map<String, String> parameters) {
        List<Movie> movies = new ArrayList<>();

        try {
            // Fetch movies from the API
            String apiResponse = MovieAPI.getMovies(parameters);
            movies = Deserializer.deserializeJsonToMovieModel(apiResponse);
            System.out.println("Fetched movies from API.");
        } catch (ApiException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("API Error");
            alert.setHeaderText("Failed to fetch movies from API");
            alert.setContentText(e.getMessage());
            alert.show();

            System.err.println("API failed: " + e.getMessage());

            MovieRepository movieRepository = new MovieRepository();
            try {
                // Fallback
                movies = movieRepository.getAllMovies();
                System.out.println("Loaded movies from database fallback.");
            } catch (SQLException dbException) {
                System.err.println("Database loading failed: " + dbException.getMessage());
            }
        } catch (IOException e) {

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

    private void setupSortButton() {
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

    /**handles set Parameters and Filters movie List accordingly*/
    private void handleFilter(ActionEvent actionEvent) {
        System.out.println("Filter Button pressed");
        String searchText = "";
        Genre genre = null;
        Rating param;
        parameters = new HashMap<>();

        if (searchField.getText() != null) {
            searchText = searchField.getText();
            parameters.put("query", searchText);
        }

        if (genreComboBox.getSelectionModel().getSelectedItem() != null) {
            genre = Genre.valueOf(genreComboBox.getSelectionModel().getSelectedItem().toString());
            parameters.put("genre", genre.toString());
        }

        if (ratingComboBox.getSelectionModel().getSelectedItem() != null) {
            param = Rating.valueOf(ratingComboBox.getSelectionModel().getSelectedItem().toString());
            parameters.put("ratingFrom", Integer.toString(param.getRating()));
        }

        if (yearField.getText() != null) {
            parameters.put("releaseYear", yearField.getText());
        }


            // New Filter handled by API
            ObservableList<Movie> filteredMoviesByAPI =
                    FXCollections.observableArrayList(initializeMovies(parameters));
            observableMovies = filteredMoviesByAPI;

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

    /**
     * This method identifies the actor(s) who appear in the most movies from a given list, and returns their name(s) as a comma-separated String
     * Its purpose is to find the actor(s) who are most frequently featured across the provided list of Movie objects and return their name(s) as a comma-separated String
     * Key concept used is "Stream", which processes list cleanly instead of loops
     * @param movies (list of movie objects to analyse)
     * @return String (names of the actor(s) with the most appearance
     */
    String getMostPopularActor(List<Movie> movies)
    {
        Map<String,Long> actorMap = new HashMap<>();
        Long maxFeatures = (long) 0;
        String mostCastActor = "";
        Stream<Movie> movieStream = movies.stream();

        actorMap = movieStream
                .flatMap(movie ->  Arrays.stream(movie.getMainCast())) //Actors aus Movie und Maincast array extrahieren
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting())); //nach actor gruppieren und vorkommen zählen

        maxFeatures = actorMap.values().stream()
                .max((cntActor1,cntActor2) -> Long.compare(cntActor1,cntActor2)) //häufigstes Vorkommen feststellen
                .orElse((long) (0));

        Long finalMaxFeatures = maxFeatures;
        mostCastActor = actorMap.entrySet().stream()
                .filter(features -> features.getValue() == finalMaxFeatures) //filtern auf nur maximales Vorkommen
                .map(entry -> entry.getKey())// passenden Schlüssel zum höchsten Vorkommen finden
                .collect(Collectors.toList())
                .stream().collect(Collectors.joining(", ")); //Liste komma separiert weil Laut angabe String returned werden muss
        //System.out.println(mostCastActor);
        return mostCastActor;
    }

    /**
     * This method finds the length of the longest movie title from a given list of Movie objects
     * Key concept used is "Stream", for processing the list cleanly
     * map() is used to extract movie titles
     * mapToInt() is used to convert strings to their lengths
     * max() is used to find the longest length
     * orElse(0) is used to handle the case where the list is empty
     * @param movies (list of movie objects to examine)
     * @return int (length of longest title)
     *
     */
    int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream() // stream erstellt, wandelt liste von filmen in datenstrom um
                .map(Movie::getTitle)     //holt aus movie object den titel raus, strom hat nur noch mehr die strings
                .mapToInt(String::length) // jeder titel wird in seine länge umgewandelt, stream enthält länge
                .max() // sucht größte länge raus
                .orElse(0); //falls liste leer = 0
    }


    /**
     * This method counts how many movies in the provided list were directed by a specific director
     * Key concept used is "Stream", for processing the list cleanly
     * filter() is used to only keep the movies that match the director
     * Arrays.asList() converts array of directors to a list for searching
     * contains() checks if the director is listed
     * count() returns the number of matching elements
     * @param movies (list of movie objects to examine)
     * @param director (the director's name to search for)
     * @return long (number of movies directed by the given name)
     */
    long countMoviesFrom(List<Movie> movies, String director)
    {
        return movies.stream()
                .filter(movie -> movie.getDirectors() != null &&
                        Arrays.asList(movie.getDirectors()).contains(director))
                .count();
    }

    /**
     * This method is used to return a list of movies that were released between two given years
     * Key concept used is "Stream", for processing the list cleanly
     * filter() to select only movies within the year range
     * toList() converts the result back to a list
     * @param movies (full list of movie objects to filter)
     * @param startYear (the earliest release year to include)
     * @param endYear (the latest release year to include)
     * @return List (filtered list of movies released in the specific range)
     */
    List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear)
    {

        Stream<Movie> movieStream = movies.stream();

        movies = movieStream
                .filter(m -> m.getReleaseYear() >= startYear && m.getReleaseYear()<= endYear)
                .toList();

        return movies;
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
                e.printStackTrace();
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

        if(movieInList)
        {
            watchListMovies.remove(movie);
            try
            {
                watchlistRepository.removeFromWatchlist(movie.getId());
            } catch (SQLException e)
            {
                //TODO: GUI Exceptionhandling
                throw new RuntimeException(e);
            }
            System.out.println("Movie removed from Watchlist");
        }
        else
        {
            System.out.println("Movie does not exist");
        }
    }

}
