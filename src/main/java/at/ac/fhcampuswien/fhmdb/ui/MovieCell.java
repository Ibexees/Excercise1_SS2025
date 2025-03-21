package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genres = new Label();
    private final Label releaseYear = new Label();
    private String imgUrl;
    private final Label lengthInMinutes = new Label();
    private final Label mainCast = new Label();
    private final Label directors = new Label();
    private final Label writers = new Label();
    private final Label rating = new Label();
    private final VBox layout = new VBox(title, detail, genres,releaseYear,rating,lengthInMinutes,mainCast,directors,writers);

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);  //Damit es zu keinen doppelten Einträgen kommt in der UI
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            genres.setText(movie.getGenresAsString());
            releaseYear.setText("Release Year: " + Integer.toString(movie.getReleaseYear()));
           lengthInMinutes.setText("length in minutes: "+Integer.toString(movie.getLengthInMinutes()));
           rating.setText("Rating out of 10: " +Double.toString( movie.getRating()));



            mainCast.setText("Main Cast: " +Arrays.toString(movie.getMainCast()));
            writers.setText("writers: " + Arrays.toString(movie.getWriters()));
            directors.setText("directors: " + Arrays.toString(movie.getDirectors()));

            // color scheme
            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            genres.getStyleClass().add("text-white");
            releaseYear.getStyleClass().add("text-white");
            rating.getStyleClass().add("text-white");
            mainCast.getStyleClass().add("text-white");
            writers.getStyleClass().add("text-white");
            directors.getStyleClass().add("text-white");
            lengthInMinutes.getStyleClass().add("text-white");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout
            title.fontProperty().set(title.getFont().font(20));
            detail.setMaxWidth(this.getScene().getWidth() - 30);
            detail.setWrapText(true);
            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);
            setGraphic(layout);
        }
    }
}

