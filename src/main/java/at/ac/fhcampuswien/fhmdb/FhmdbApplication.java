package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.dataLayer.database.DataBaseException;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.DatabaseManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
            fxmlLoader.setControllerFactory(new ControllerFactory());
            Scene scene = new Scene(fxmlLoader.load(), 890, 620);
            stage.setTitle("FHMDb");
            stage.setScene(scene);
            stage.show();

        }
        catch(DataBaseException e)
        {
            System.out.println("HERE");
        }
        /*
        catch (IOException e)
        {
            e.printStackTrace();
            //handleStartupException(e);
        }
        */

       catch (Exception e) {
            handleStartupException(e);
        }


    }

    private void handleStartupException(Exception e) {
        e.printStackTrace();

        // Root cause finden
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        String title;
        String errorMessage;

        if (cause instanceof DataBaseException) {
            title = "Database Error";
            errorMessage = "Failed to connect to the database: " + cause.getMessage();
        } else if (cause instanceof IOException) {
            title = "File Error";
            errorMessage = "Failed to access database file: " + cause.getMessage();
        } else {
            title = "Application Error";
            errorMessage = "An unexpected error occurred: " + cause.getMessage();
        }

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            alert.showAndWait();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}