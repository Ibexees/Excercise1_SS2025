package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        sortedMovies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)));
        sortedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));

        List<Movie> unsortedMovies = new ArrayList<Movie>();
        unsortedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        unsortedMovies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));
        unsortedMovies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)));

        HomeController homeController = new HomeController();
       // assertEquals(sortedMovies,homeController.sortMovies(unsortedMovies));
    }

    @Test
    public void movies_sorted_by_title_desc()
    {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        movies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)));
        movies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));

    }


    @Test
    public void title_filter_not_case_sensitive()
    {

    }

    @Test
    public void title_filter_accepts_substring_of_Movie_title()
    {

    }


    @Test
    public void only_genre_filtered_Movies_displayed()
    {

    }

    @Test
    public void only_genre_and_title_filtered_movies_displayed()
    {

    }
}