package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.dataLayer.DataBaseException;
import at.ac.fhcampuswien.fhmdb.dataLayer.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.dataLayer.MovieEntity;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 890, 620);
            try {
                scene.getStylesheets().add(Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css")).toExternalForm());
            } catch (NullPointerException e) {
                System.err.println("CSS file not found");
                throw new DataBaseException("CSS file not found");
            }

            stage.setTitle("FHMDb");
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            System.err.println("Error loading the FXML file");
            throw new DataBaseException("Error loading the FXML file");
        } catch (Exception e) {
            System.err.println("Error during application start");
            throw new DataBaseException("unexpected startup error");
        }

    }

    public static void main(String[] args) {
        launch();
    }
}