package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieComparator;
import at.ac.fhcampuswien.fhmdb.models.Rating;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public TextField yearField;
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
            ratingComboBox.setPromptText("Filter by Rating and Above");
            ratingComboBox.getItems().addAll(Rating.values());


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
        ratingComboBox.setValue(null);
        genreComboBox.resetValidation();
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
        System.out.println("Filter Button pressed");
        String searchText;
        Genre genre = null;
        Rating param;

        if(searchField.getText() != null)
        {
            searchText = searchField.getText();
            parameters.put("query",searchText);
        }

        if (genreComboBox.getSelectionModel().getSelectedItem() != null) {
            genre = Genre.valueOf(genreComboBox.getSelectionModel().getSelectedItem().toString());
            parameters.put("genre",genre.toString());
        }

        if (ratingComboBox.getSelectionModel().getSelectedItem() != null) {
            param = Rating.valueOf(ratingComboBox.getSelectionModel().getSelectedItem().toString());
            parameters.put("ratingFrom", Integer.toString(param.getRating()));
        }

        if(yearField.getText()!=null)
        {
            parameters.put("releaseYear",yearField.getText());
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
        System.out.println(mostCastActor);
        return mostCastActor;
    }

    int getLongestMovieTitle(List<Movie> movies)
    {
        int titleCharacterCount = 0;
        return titleCharacterCount;
    }

    long countMoviesFrom(List<Movie> movies, String director)
    {
        return 0;
    }
    List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear)
    {
        return new ArrayList<>();
    }




}