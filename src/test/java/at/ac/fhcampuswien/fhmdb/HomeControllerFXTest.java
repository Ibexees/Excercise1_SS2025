package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.api.FxRobot;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static javafx.application.Application.launch;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class HomeControllerFXTest extends ApplicationTest
{

    private FxRobot robot;
    private HomeController homeController;


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 890, 620);
        homeController = fxmlLoader.getController();
        stage.setScene(scene);
        stage.show();
    }


    @BeforeEach
    void setUp() {
        assertNotNull(homeController, "Der HomeController sollte initialisiert sein.");
        robot = new FxRobot();
    }

    @Test
    void testAscSortButtonUpdatesListView() {

        //Testmöglichkeit für alle Filme in initializeMovies benötigt aber Schleife in Testcase!
        //List<Movie> movies = new ArrayList<>();
        ObservableList movies;
        movies = FXCollections.observableArrayList();
        movies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        movies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));
        /*movies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)));
        movies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)));
        movies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)));*/

        boolean asc = true;

        homeController.setAllMovies(movies);
        homeController.allMovies = homeController.sortMovies(asc,movies);


        // Klicke auf den Button mit der ID #sortBtn
        robot.clickOn("#sortBtn"); //Drücken auf den Sortier Knopf
        robot.clickOn("#sortBtn"); //2tes drücken auf den Sortier Knopf, damit desc sortiert wird

        // Zugriff auf die ListView
        ListView<Movie> listView = robot.lookup("#movieListView").queryAs(ListView.class);
        ObservableList<Movie> DisplayedMovies = listView.getItems();

        // Prüfen, ob die Reihenfolge korrekt ist
        assertEquals("Into the Spiderverse", DisplayedMovies.get(1).getTitle());
        assertEquals("Your Name", DisplayedMovies.get(0).getTitle());


    }

    @Test
    void testDescSortButtonUpdatesListView() {

        //Testmöglichkeit für alle Filme in initializeMovies benötigt aber Schleife in Testcase!
        //List<Movie> movies = new ArrayList<>();
        ObservableList movies;
        movies = FXCollections.observableArrayList();
        movies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        movies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));
        /*movies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)));
        movies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)));
        movies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)));*/

        boolean asc = false;

        homeController.setAllMovies(movies);
        homeController.allMovies = homeController.sortMovies(asc,movies);


        // Klicke auf den Button mit der ID #sortBtn
        robot.clickOn("#sortBtn"); //Drücken auf den Sortier Knopf
        robot.clickOn("#sortBtn"); //2tes drücken auf den Sortier Knopf, damit desc sortiert wird

        // Zugriff auf die ListView
        ListView<Movie> listView = robot.lookup("#movieListView").queryAs(ListView.class);
        ObservableList<Movie> DisplayedMovies = listView.getItems();

        // Prüfen, ob die Reihenfolge korrekt ist
        assertEquals("Into the Spiderverse", DisplayedMovies.get(1).getTitle());
        assertEquals("Your Name", DisplayedMovies.get(0).getTitle());


    }
}


