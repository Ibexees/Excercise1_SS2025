package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genres = new Label();
    private final Label releaseYear = new Label();
    private final Label lengthInMinutes = new Label();
    private final Label mainCast = new Label();
    private final Label directors = new Label();
    private final Label writers = new Label();
    private final Label rating = new Label();
    private final Button showDetailsBtn = new Button("Show Details");
    private final Button addWatchlistBtn = new Button("Add to Watchlist");
    private final Button removeWatchlistBtn = new Button("Remove from Watchlist");

    private final VBox textLayout = new VBox(title, detail, genres, releaseYear, rating, lengthInMinutes, mainCast, directors, writers);
    private final ImageView posterView = new ImageView();
    private final HBox buttonsHbox = new HBox(showDetailsBtn, addWatchlistBtn,removeWatchlistBtn);
    private final HBox layout = new HBox(/*posterView,*/ textLayout,buttonsHbox);
    private final MovieCellActionHandler actionHandler;

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.getStyleClass().add("movie-cell");

            title.setText(movie.getTitle());
            detail.setText(movie.getDescription() != null ? movie.getDescription() : "No description available");
            genres.setText(movie.getGenresAsString());
            releaseYear.setText("Release Year: " + movie.getReleaseYear());
            lengthInMinutes.setText("Length in minutes: " + movie.getLengthInMinutes());
            rating.setText("Rating out of 10: " + movie.getRating());
            mainCast.setText("Main Cast: " + Arrays.toString(movie.getMainCast()));
            writers.setText("Writers: " + Arrays.toString(movie.getWriters()));
            directors.setText("Directors: " + Arrays.toString(movie.getDirectors()));


            // Load image
            if (movie.getImgUrl() != null && !movie.getImgUrl().isEmpty()) {
                Image posterImage = new Image(movie.getImgUrl(), true);
                posterView.setImage(posterImage);
                posterView.setFitWidth(100);
                posterView.setFitHeight(150);
                //posterView.setPreserveRatio(true);
            } else {
                posterView.setImage(null);
                posterView.setFitWidth(100);
                posterView.setFitHeight(150);
            }

            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            genres.getStyleClass().add("text-white");
            releaseYear.getStyleClass().add("text-white");
            rating.getStyleClass().add("text-white");
            mainCast.getStyleClass().add("text-white");
            writers.getStyleClass().add("text-white");
            directors.getStyleClass().add("text-white");
            lengthInMinutes.getStyleClass().add("text-white");
            addWatchlistBtn.getStyleClass().add("backgroud-yellow");
            showDetailsBtn.getStyleClass().add("backgroud-yellow");


            textLayout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));
            textLayout.setPadding(new Insets(10));
            textLayout.setSpacing(10);
            textLayout.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            layout.setSpacing(3); // Space between image and text
            layout.setPadding(new Insets(10));
            layout.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            if (!HomeController.buttonsVisible) {
                showWatchlistButtons();
            } else {
                showHomeButtons();
            }

            buttonsHbox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            buttonsHbox.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));
            buttonsHbox.setPadding(new Insets(5));
            buttonsHbox.setSpacing(3);

            setGraphic(layout);
        }

    }

    public void showWatchlistButtons() {

        showDetailsBtn.setVisible(false);
        showDetailsBtn.setManaged(false);
        addWatchlistBtn.setVisible(false);
        addWatchlistBtn.setManaged(false);
        removeWatchlistBtn.setVisible(true);
        removeWatchlistBtn.setManaged(true);
    }

    public void showHomeButtons() {

        showDetailsBtn.setVisible(true);
        showDetailsBtn.setManaged(true);
        addWatchlistBtn.setVisible(true);
        addWatchlistBtn.setManaged(true);
        removeWatchlistBtn.setVisible(false);
        removeWatchlistBtn.setManaged(false);
    }

    public MovieCell(MovieCellActionHandler handler) {
        this.actionHandler = handler;

        // setup layout etc.
        addWatchlistBtn.setOnAction(e -> {
            if (getItem() != null) {
                actionHandler.onAddWatchlistClicked(getItem());
            }
        });

        showDetailsBtn.setOnAction(e -> {
            if (getItem() != null) {
                actionHandler.onShowDetailsClicked(getItem());
            }
        });

        removeWatchlistBtn.setOnAction(e -> {
            if (getItem() != null) {
                actionHandler.onRemoveWatchlistClicked(getItem());
            }
        });
    }
}


