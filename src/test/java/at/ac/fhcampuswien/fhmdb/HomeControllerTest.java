package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.api.FxRobot;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {


    @Test
    public void movie_should_have_Attributes()
    {
        //Erwartete Attribute
        List<String> expectedFields = Arrays.asList("title","description","genres");

        //Tatsächliche Attribute aus Klasse
        Field[] fields = Movie.class.getDeclaredFields();
        List<String> actualFields = new ArrayList<>();
        for(Field field : fields)
        {
            actualFields.add(field.getName());
        }
        assertEquals(actualFields,expectedFields, "Unterschiede gefunden! Erwartet: " + expectedFields + ", aber war: " + actualFields);
    }

    @Test
    public void movies_sorted_by_title_asc()
    {
        List<Movie> sortedMovies = new ArrayList<>();
        sortedMovies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));
        sortedMovies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)));
        sortedMovies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)));
        sortedMovies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)));
        sortedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));

        List<Movie> unsortedMovies = new ArrayList<>();
        unsortedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        unsortedMovies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));
        unsortedMovies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)));
        unsortedMovies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)));
        unsortedMovies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)));

        HomeController homeController = new HomeController();
        boolean asc = true;
        unsortedMovies = homeController.sortMovies(asc,unsortedMovies);

        assertEquals(sortedMovies,unsortedMovies);
    }

    @Test
    public void movies_sorted_by_title_desc()
    {
        List<Movie> sortedMovies = new ArrayList<>();
        sortedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        sortedMovies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)));
        sortedMovies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)));
        sortedMovies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)));
        sortedMovies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));

        List<Movie> unsortedMovies = new ArrayList<>();
        unsortedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        unsortedMovies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));
        unsortedMovies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)));
        unsortedMovies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)));
        unsortedMovies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)));

        HomeController homeController = new HomeController();
        boolean asc = false;
        unsortedMovies = homeController.sortMovies(asc,unsortedMovies);

        assertEquals(sortedMovies,unsortedMovies);

    }




    @Test //Nancy
    public void title_filter_not_case_sensitive()
    {

    }

    @Test //Iyobosa
    public void title_filter_accepts_substring_of_Movie_title()
    {

    }


    @Test //Elias
    public void only_genre_filtered_Movies_displayed()
    {

    }

    @Test //*
    public void only_genre_and_title_filtered_movies_displayed()
    {

    }
}