package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static javafx.application.Application.launch;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class HomeControllerFXTest extends ApplicationTest
{

    private FxRobot robot;



    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 890, 620);
        stage.setScene(scene);
        stage.show();
    }


    @BeforeEach
    void setUp() {
        robot = new FxRobot();
    }

    @Test
    void testAscSortButtonUpdatesListView() {
        // Klicke auf den Button mit der ID #sortBtn

        robot.clickOn("#sortBtn"); //Drücken auf den Sortier Knopf

        // Zugriff auf die ListView
        ListView<Movie> listView = robot.lookup("#movieListView").queryAs(ListView.class);
        ObservableList<Movie> movies = listView.getItems();

        // Prüfen, ob die Reihenfolge korrekt ist
        assertEquals("Into the Spiderverse", movies.get(0).getTitle());
        assertEquals("Kung Fu Panda", movies.get(1).getTitle());
        assertEquals("Shutter Island", movies.get(2).getTitle());
        assertEquals("Southpaw", movies.get(3).getTitle());
        assertEquals("Your Name", movies.get(4).getTitle());
    }

    @Test
    void testDescSortButtonUpdatesListView() {

        //Testmöglichkeit für alle Filme in initializeMovies benötigt aber Schleife in Testcase!
        List<Movie> movies = Movie.initializeMovies();
        HomeController homeController = new HomeController();
        boolean asc = false;
        movies = homeController.sortMovies(asc,movies);

        // Klicke auf den Button mit der ID #sortBtn
        robot.clickOn("#sortBtn"); //Drücken auf den Sortier Knopf
        robot.clickOn("#sortBtn"); //2tes drücken auf den Sortier Knopf, damit desc sortiert wird

        // Zugriff auf die ListView
        ListView<Movie> listView = robot.lookup("#movieListView").queryAs(ListView.class);
        ObservableList<Movie> DisplayedMovies = listView.getItems();

        // Prüfen, ob die Reihenfolge korrekt ist
        assertEquals("Into the Spiderverse", DisplayedMovies.get(4).getTitle());
        assertEquals("Kung Fu Panda", DisplayedMovies.get(3).getTitle());
        assertEquals("Shutter Island", DisplayedMovies.get(2).getTitle());
        assertEquals("Southpaw", DisplayedMovies.get(1).getTitle());
        assertEquals("Your Name", DisplayedMovies.get(0).getTitle());


    }
}


