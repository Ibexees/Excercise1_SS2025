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


    //Muss auf die neuen Attribute (releaseYear etc.) erweitert werden.
    @Test
    public void movie_should_have_Attributes()
    {
        //Erwartete Attribute
        List<String> expectedFields = Arrays.asList("id","title","description","genres","releaseYear","imgUrl","lengthInMinutes","mainCast","directors","writers","rating");

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
    public void title_filter_not_case_sensitive() {
        HomeController homeController = new HomeController();
        homeController.allMovies = Arrays.asList(
                    new Movie("The Dark Knight","Action movie",Arrays.asList(Genre.ACTION)),
                    new Movie("the dark knight","The battle between batman and joker",Arrays.asList(Genre.ACTION)),
                    new Movie("willy wonka and the chocolate factory","Fantasy movie",Arrays.asList(Genre.FANTASY)),
                    new Movie("Willy Wonka And The Chocolate Factory","Get the golden ticket",Arrays.asList(Genre.FANTASY)),
                    new Movie("SCARFACE","The Life of Tony Montana",Arrays.asList(Genre.THRILLER)),
                    new Movie("scarface","Thriller",Arrays.asList(Genre.THRILLER)),
                    new Movie("Superbad","McLovin",Arrays.asList(Genre.COMEDY)),
                    new Movie("SUPERBAD","Comedy Movie",Arrays.asList(Genre.COMEDY))
            );

            // suche nach the dark knight
        String searchQuery ="the dark knight";
        List<Movie> resultDarkKnight =homeController.filterMovies(null,"the dark knight");


        //erwartete Anzahl an Treffern
        assertEquals(2, resultDarkKnight.size());
        assertTrue(resultDarkKnight.stream().anyMatch(movie -> movie.getTitle().equalsIgnoreCase("The Dark Knight"))); //braucht man diese Zeile Robuster aber Logik ?
        assertFalse(resultDarkKnight.stream().anyMatch(movie -> movie.getTitle().equalsIgnoreCase("Superbad"))); //braucht man diese Zeile Robuster aber Logik ?


    }

    @Test //Iyobosa
    public void title_filter_accepts_substring_of_Movie_title(){
        HomeController homeController = new HomeController();

        List<Movie> testMovies = new ArrayList<>();
        testMovies.add(new Movie("Kung Fu Panda","Action movie",Arrays.asList(Genre.ACTION)));
        testMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));

        homeController.allMovies = testMovies; //von Elias

        String searchText = "Kung";

        List<Movie> filteredMovies = homeController.filterMovies(null, searchText);

        assertFalse(filteredMovies.isEmpty(), "Die gefilterte Liste sollte nicht leer sein");
        assertEquals(filteredMovies.size(),1, "Es sollte genau ein Film mit 'Name' im Titel gefunden werden");
        assertEquals("Kung Fu Panda", filteredMovies.get(0).getTitle(), "Der gefundene Film sollte 'Kung Fu Panda' sein");

    }


    @Test //Elias
    public void only_genre_filtered_Movies_displayed()
    {
        HomeController homeController = new HomeController();

        //Filme für den Testfall generieren.
        List<Movie> testMovies = new ArrayList<>();
        testMovies.add(new Movie("The Dark Knight","Action movie",Arrays.asList(Genre.ACTION)));
        testMovies.add(new Movie("Mickey Mouse", "Pluto", Arrays.asList(Genre.WAR)));
        testMovies.add(new Movie("Batman", "Joker", Arrays.asList(Genre.DOCUMENTARY,Genre.SCIENCE_FICTION)));
        testMovies.add(new Movie("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));

        homeController.allMovies = testMovies;

        //Nach Genre.ACTION filtern
        Genre testGenre = Genre.ACTION;

        //Die gefilterten Filme vom HomeController erhalten.
        List<Movie> filteredMovies = homeController.filterMovies(testGenre,"");

        assertFalse(filteredMovies.isEmpty(), "Die gefilterte Liste sollte nicht leer sein.");
        assertEquals(filteredMovies.size(),2, "Es sollten genau zwei Filme mit [ACTION] gefunden werden.");
        assertEquals("The Dark Knight", filteredMovies.get(0).getTitle(), "The Dark Knight ist [ACTION] Genre.");
        assertEquals("Into the Spiderverse", filteredMovies.get(1).getTitle(), "Into the Spiderverse ist [ACTION] Genre.");
    }

    @Test //Elias
    public void only_genre_and_title_filtered_movies_displayed()
    {
        HomeController homeController = new HomeController();

        //Filme für den Testfall generieren.
        List<Movie> testMovies = new ArrayList<>();
        testMovies.add(new Movie("The Dark Knight","Joker",Arrays.asList(Genre.ACTION)));
        testMovies.add(new Movie("Batman Begins", "Joker", Arrays.asList(Genre.ACTION,Genre.DRAMA)));
        testMovies.add(new Movie("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));
        testMovies.add(new Movie("The Dark Knight Rises", "Bane", Arrays.asList(Genre.ACTION)));

        homeController.allMovies = testMovies;

        //Nach Genre.ACTION & "Knight" filtern
        Genre testGenre = Genre.ACTION;
        String searchText = "Knight";

        //Die gefilterten Filme vom HomeController erhalten.
        List<Movie> filteredMovies = homeController.filterMovies(testGenre,searchText);

        assertFalse(filteredMovies.isEmpty(), "Die gefilterte Liste sollte nicht leer sein.");
        assertEquals(filteredMovies.size(),2, "Es sollten genau zwei Filme mit [ACTION] & den Namen \"Knight\" beinhalten.");
        assertEquals("The Dark Knight", filteredMovies.get(0).getTitle(), "The Dark Knight ist [ACTION] Genre & der gesuchte String ist enthalten.");
        assertEquals("The Dark Knight Rises", filteredMovies.get(1).getTitle(), "The Dark Knight Rises ist [ACTION] Genre & der gesuchte String ist enthalten.");
    }

    @Test
    public void return_only_most_popular_Actor()
    {
        HomeController homeController = new HomeController();
        List<Movie> sortedMovies = new ArrayList<>();

        sortedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)).setMainCast(new String[]{"Nathan Graves","Alucard"}));
        sortedMovies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)).setMainCast(new String[]{"Nathan Graves","Julius Belmont"}));
        sortedMovies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setMainCast(new String[]{"Nathan Graves","Julius Belmont"}));
        sortedMovies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setMainCast(new String[]{"Soma Cruz","Yoko Belnades"}));
        sortedMovies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setMainCast(new String[]{"Jonathan Morris","Charlotte Aulin"}));


        String mostCastActor = "Nathan Graves"; //expected
        String mostCastActorFromMethod; //actual
        mostCastActorFromMethod = homeController.getMostPopularActor(sortedMovies);

        assertEquals(mostCastActor,mostCastActorFromMethod,"expected: "+mostCastActor+" actual: "+mostCastActorFromMethod);

    }

    @Test
    public void return_multiple_most_popular_Actors()
    {
        HomeController homeController = new HomeController();
        List<Movie> sortedMovies = new ArrayList<>();

        sortedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)).setMainCast(new String[]{"Nathan Graves","Alucard"}));
        sortedMovies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)).setMainCast(new String[]{"Nathan Graves","Julius Belmont"}));
        sortedMovies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setMainCast(new String[]{"Nathan Graves","Julius Belmont"}));
        sortedMovies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setMainCast(new String[]{"Soma Cruz","Yoko Belnades"}));
        sortedMovies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setMainCast(new String[]{"Jonathan Morris","Julius Belmont"}));


        String mostCastActor = "Julius Belmont, Nathan Graves"; //expected
        String mostCastActorFromMethod; //actual
        mostCastActorFromMethod = homeController.getMostPopularActor(sortedMovies);

        assertEquals(mostCastActor,mostCastActorFromMethod,"expected: "+mostCastActor+" actual: "+mostCastActorFromMethod);

    }

    @Test
    public void no_most_popular_Actor_returned()
    {
        HomeController homeController = new HomeController();
        List<Movie> sortedMovies = new ArrayList<>();

        sortedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        sortedMovies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)));
        sortedMovies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)));
        sortedMovies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)));
        sortedMovies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));


        String mostCastActor = ""; //expected
        String mostCastActorFromMethod; //actual
        mostCastActorFromMethod = homeController.getMostPopularActor(sortedMovies);

        assertEquals(mostCastActor,mostCastActorFromMethod,"expected: "+mostCastActor+" actual: "+mostCastActorFromMethod);

    }

    @Test
    public void only_movies_between_years_returned()
    {
        HomeController homeController = new HomeController();
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)).setReleaseYear(2016));
        movies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)).setReleaseYear(2000));
        movies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setReleaseYear(2018));
        movies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setReleaseYear(2005));
        movies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setReleaseYear(2019));

        List<Movie> expectedMovies = new ArrayList<>();
        expectedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)).setReleaseYear(2016));
        expectedMovies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setReleaseYear(2018));
        expectedMovies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setReleaseYear(2005));
        expectedMovies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setReleaseYear(2019));


        assertEquals(expectedMovies,homeController.getMoviesBetweenYears(movies,2005,2020),"expected: "+expectedMovies+" actual: "+movies);

    }

    @Test
    public void no_movies_between_years_returned()
    {
        HomeController homeController = new HomeController();
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)).setReleaseYear(2016));
        movies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)).setReleaseYear(2000));
        movies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setReleaseYear(2018));
        movies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setReleaseYear(2005));
        movies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setReleaseYear(2019));

        List<Movie> expectedMovies = new ArrayList<>();

        assertEquals(expectedMovies,homeController.getMoviesBetweenYears(movies,2020,2020),"expected: "+expectedMovies+" actual: "+movies);

    }

    @Test
    public void no_movies_release_year_set()
    {
        HomeController homeController = new HomeController();
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        movies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)));
        movies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setReleaseYear(2018));
        movies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setReleaseYear(2005));
        movies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setReleaseYear(2019));

        List<Movie> expectedMovies = new ArrayList<>();
        expectedMovies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setReleaseYear(2018));
        expectedMovies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setReleaseYear(2005));
        expectedMovies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setReleaseYear(2019));

        assertEquals(expectedMovies,homeController.getMoviesBetweenYears(movies,2005,2019),"expected: "+expectedMovies+" actual: "+movies);

    }
}
