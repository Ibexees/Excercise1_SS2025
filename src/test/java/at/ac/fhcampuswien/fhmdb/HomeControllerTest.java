package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.dataLayer.api.Deserializer;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.logic.MovieAnalysisService;
import at.ac.fhcampuswien.fhmdb.logic.SortState;
import at.ac.fhcampuswien.fhmdb.logic.models.Genre;
import at.ac.fhcampuswien.fhmdb.logic.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {


    private List<Movie> movies;

    private MovieAnalysisService movieAnalysisService;

    @BeforeEach
    void setUp() {
        // Initialize the services before each test

        movieAnalysisService = new MovieAnalysisService();
    }
    //Muss auf die neuen Attribute (releaseYear etc.) erweitert werden.
    /*@Test
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
    }*/

    @Test
    public void movies_sorted_by_title_asc() throws SQLException {
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
    public void movies_sorted_by_title_desc() throws SQLException {
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
    public void title_filter_not_case_sensitive() throws SQLException {
        HomeController homeController = new HomeController(); //homecontroller instanz erzeugen, für filterung von filmen

        List<Movie> testMovies = Arrays.asList( // testfilme mit 2 versionen
                new Movie("The Dark Knight","Action movie",Arrays.asList(Genre.ACTION)),
                new Movie("the dark knight","The battle between batman and joker",Arrays.asList(Genre.ACTION)),
                new Movie("willy wonka and the chocolate factory","Fantasy movie",Arrays.asList(Genre.FANTASY)),
                new Movie("Willy Wonka And The Chocolate Factory","Get the golden ticket",Arrays.asList(Genre.FANTASY)),
                new Movie("SCARFACE","The Life of Tony Montana",Arrays.asList(Genre.THRILLER)),
                new Movie("scarface","Thriller",Arrays.asList(Genre.THRILLER)),
                new Movie("Superbad","McLovin",Arrays.asList(Genre.COMEDY)),
                new Movie("SUPERBAD","Comedy Movie",Arrays.asList(Genre.COMEDY))
        );

        // suche nach the dark knight, simuliert benutzereingabe
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query", "the dark knight");

        // Use filterMovies method directly
        List<Movie> resultDarkKnight = homeController.filterMovies(testMovies, parameters);

        //erwartete Anzahl an Treffern
        assertEquals(2, resultDarkKnight.size());
        assertTrue(resultDarkKnight.stream().anyMatch(movie -> movie.getTitle().equalsIgnoreCase("The Dark Knight"))); //überflüssig? weil in der ersten Zeile werden schon 2 filme ausgespuckt
        assertFalse(resultDarkKnight.stream().anyMatch(movie -> movie.getTitle().equalsIgnoreCase("Superbad"))); //braucht man diese Zeile Robuster aber Logik ?
    }

    @Test //Iyobosa
    public void title_filter_accepts_substring_of_Movie_title() throws SQLException {
        HomeController homeController = new HomeController();

        List<Movie> testMovies = new ArrayList<>();
        testMovies.add(new Movie("Kung Fu Panda","Action movie",Arrays.asList(Genre.ACTION)));
        testMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));

        String searchText = "Kung";

        // Create parameters for filtering
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query", searchText);

        // Use filterMovies method directly
        List<Movie> filteredMovies = homeController.filterMovies(testMovies, parameters);

        assertFalse(filteredMovies.isEmpty(), "Die gefilterte Liste sollte nicht leer sein");
        assertEquals(filteredMovies.size(),1, "Es sollte genau ein Film mit 'Name' im Titel gefunden werden");
        assertEquals("Kung Fu Panda", filteredMovies.get(0).getTitle(), "Der gefundene Film sollte 'Kung Fu Panda' sein");
    }


    @Test //Elias
    public void only_genre_filtered_Movies_displayed() throws SQLException
    {
        HomeController homeController = new HomeController();

        //Filme für den Testfall generieren.
        List<Movie> testMovies = new ArrayList<>();
        testMovies.add(new Movie("The Dark Knight","Action movie",Arrays.asList(Genre.ACTION)));
        testMovies.add(new Movie("Mickey Mouse", "Pluto", Arrays.asList(Genre.WAR)));
        testMovies.add(new Movie("Batman", "Joker", Arrays.asList(Genre.DOCUMENTARY,Genre.SCIENCE_FICTION)));
        testMovies.add(new Movie("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));

        //Nach Genre.ACTION filtern
        Genre testGenre = Genre.ACTION;

        // Create parameters for filtering
        Map<String, String> parameters = new HashMap<>();
        parameters.put("genre", testGenre.toString());

        // Use filterMovies method directly
        List<Movie> filteredMovies = homeController.filterMovies(testMovies, parameters);

        assertFalse(filteredMovies.isEmpty(), "Die gefilterte Liste sollte nicht leer sein.");
        assertEquals(filteredMovies.size(),2, "Es sollten genau zwei Filme mit [ACTION] gefunden werden.");

        // Sort for consistent testing
        filteredMovies.sort(Comparator.comparing(Movie::getTitle));
        assertEquals("Into the Spiderverse", filteredMovies.get(0).getTitle(), "Into the Spiderverse ist [ACTION] Genre.");
        assertEquals("The Dark Knight", filteredMovies.get(1).getTitle(), "The Dark Knight ist [ACTION] Genre.");
    }

    @Test //Elias
    public void only_genre_and_title_filtered_movies_displayed() throws SQLException
    {
        HomeController homeController = new HomeController();

        //Filme für den Testfall generieren.
        List<Movie> testMovies = new ArrayList<>();
        testMovies.add(new Movie("The Dark Knight","Joker",Arrays.asList(Genre.ACTION)));
        testMovies.add(new Movie("Batman Begins", "Joker", Arrays.asList(Genre.ACTION,Genre.DRAMA)));
        testMovies.add(new Movie("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));
        testMovies.add(new Movie("The Dark Knight Rises", "Bane", Arrays.asList(Genre.ACTION)));

        //Nach Genre.ACTION & "Knight" filtern
        Genre testGenre = Genre.ACTION;
        String searchText = "Knight";

        // Create parameters for filtering
        Map<String, String> parameters = new HashMap<>();
        parameters.put("genre", testGenre.toString());
        parameters.put("query", searchText);

        // Use filterMovies method directly
        List<Movie> filteredMovies = homeController.filterMovies(testMovies, parameters);

        assertFalse(filteredMovies.isEmpty(), "Die gefilterte Liste sollte nicht leer sein.");
        assertEquals(filteredMovies.size(),2, "Es sollten genau zwei Filme mit [ACTION] & den Namen \"Knight\" beinhalten.");

        // Sort for consistent testing
        filteredMovies.sort(Comparator.comparing(Movie::getTitle));
        assertEquals("The Dark Knight", filteredMovies.get(0).getTitle(), "The Dark Knight ist [ACTION] Genre & der gesuchte String ist enthalten.");
        assertEquals("The Dark Knight Rises", filteredMovies.get(1).getTitle(), "The Dark Knight Rises ist [ACTION] Genre & der gesuchte String ist enthalten.");
    }

    @Test
    public void return_only_most_popular_Actor() {
        List<Movie> sortedMovies = new ArrayList<>();

        sortedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)).setMainCast(new String[]{"Nathan Graves","Alucard"}));
        sortedMovies.add(new Movie("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)).setMainCast(new String[]{"Nathan Graves","Julius Belmont"}));
        sortedMovies.add(new Movie("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setMainCast(new String[]{"Nathan Graves","Julius Belmont"}));
        sortedMovies.add(new Movie("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setMainCast(new String[]{"Soma Cruz","Yoko Belnades"}));
        sortedMovies.add(new Movie("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setMainCast(new String[]{"Jonathan Morris","Charlotte Aulin"}));

        String mostCastActor = "Nathan Graves"; // expected
        String mostCastActorFromMethod = movieAnalysisService.getMostPopularActor(sortedMovies); // actual

        assertEquals(mostCastActor, mostCastActorFromMethod, "Expected: " + mostCastActor + " Actual: " + mostCastActorFromMethod);
    }

    @Test
    public void return_multiple_most_popular_Actors() {
        List<Movie> sortedMovies = new ArrayList<>();

        sortedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)).setMainCast(new String[]{"Nathan Graves","Alucard"}));
        sortedMovies.add(new Movie("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)).setMainCast(new String[]{"Nathan Graves","Julius Belmont"}));
        sortedMovies.add(new Movie("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setMainCast(new String[]{"Nathan Graves","Julius Belmont"}));
        sortedMovies.add(new Movie("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setMainCast(new String[]{"Soma Cruz","Yoko Belnades"}));
        sortedMovies.add(new Movie("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setMainCast(new String[]{"Jonathan Morris","Julius Belmont"}));

        String mostCastActor = "Julius Belmont, Nathan Graves"; // expected
        String mostCastActorFromMethod = movieAnalysisService.getMostPopularActor(sortedMovies); // actual

        assertEquals(mostCastActor, mostCastActorFromMethod, "Expected: " + mostCastActor + " Actual: " + mostCastActorFromMethod);
    }

    @Test
    public void no_most_popular_Actor_returned() {
        List<Movie> sortedMovies = new ArrayList<>();

        sortedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        sortedMovies.add(new Movie("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)));
        sortedMovies.add(new Movie("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)));
        sortedMovies.add(new Movie("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)));
        sortedMovies.add(new Movie("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));

        String mostCastActor = ""; // expected
        String mostCastActorFromMethod = movieAnalysisService.getMostPopularActor(sortedMovies); // actual

        assertEquals(mostCastActor, mostCastActorFromMethod, "Expected: " + mostCastActor + " Actual: " + mostCastActorFromMethod);
    }

    @Test
    public void only_movies_between_years_returned() {
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)).setReleaseYear(2016));
        movies.add(new Movie("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)).setReleaseYear(2000));
        movies.add(new Movie("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setReleaseYear(2018));
        movies.add(new Movie("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setReleaseYear(2005));
        movies.add(new Movie("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setReleaseYear(2019));

        List<Movie> expectedMovies = new ArrayList<>();
        expectedMovies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)).setReleaseYear(2016));
        expectedMovies.add(new Movie("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setReleaseYear(2018));
        expectedMovies.add(new Movie("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setReleaseYear(2005));
        expectedMovies.add(new Movie("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setReleaseYear(2019));

        List<Movie> result = movieAnalysisService.getMoviesBetweenYears(movies, 2005, 2020);

        assertEquals(expectedMovies, result, "Expected: " + expectedMovies + " Actual: " + result);
    }

    @Test
    public void no_movies_between_years_returned() {
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)).setReleaseYear(2016));
        movies.add(new Movie("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)).setReleaseYear(2000));
        movies.add(new Movie("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setReleaseYear(2018));
        movies.add(new Movie("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setReleaseYear(2005));
        movies.add(new Movie("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setReleaseYear(2019));

        List<Movie> expectedMovies = new ArrayList<>();

        List<Movie> result = movieAnalysisService.getMoviesBetweenYears(movies, 2020, 2020);

        assertEquals(expectedMovies, result, "Expected: " + expectedMovies + " Actual: " + result);
    }

    @Test
    public void no_movies_release_year_set() {
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        movies.add(new Movie("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)));
        movies.add(new Movie("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setReleaseYear(2018));
        movies.add(new Movie("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setReleaseYear(2005));
        movies.add(new Movie("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setReleaseYear(2019));

        List<Movie> expectedMovies = new ArrayList<>();
        expectedMovies.add(new Movie("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)).setReleaseYear(2018));
        expectedMovies.add(new Movie("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)).setReleaseYear(2005));
        expectedMovies.add(new Movie("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)).setReleaseYear(2019));

        List<Movie> result = movieAnalysisService.getMoviesBetweenYears(movies, 2005, 2019);

        assertEquals(expectedMovies, result, "Expected: " + expectedMovies + " Actual: " + result);
    }

    @Test
    public void getLongestMovieTitle() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Southpaw", "Boxen", Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        movies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        movies.add(new Movie("Into the Spiderverse","interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.ACTION)));

        int longestTitleLength = movieAnalysisService.getLongestMovieTitle(movies);
        assertEquals(20, longestTitleLength, "Longest title has 20 characters");
    }

    @Test
    public void getLongestMovieTitle_emptyList() {
        List<Movie> movies = new ArrayList<>();

        int longestTitleLength = movieAnalysisService.getLongestMovieTitle(movies);
        assertEquals(0, longestTitleLength, "Longest title has 0 characters");
    }

    @Test
    public void test_CountMoviesFrom_givenDirector_returnCorrectCount() {
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Your Name", "Coming of Age romance", Arrays.asList(Genre.ROMANCE, Genre.DRAMA)));
        movies.add(new Movie("Southpaw", "Boxen", Arrays.asList(Genre.ROMANCE, Genre.DRAMA)));
        movies.add(new Movie("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.ACTION, Genre.ACTION)));
        movies.add(new Movie("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.ACTION, Genre.ACTION)));
        movies.add(new Movie("Into the Spiderverse", "interdimesional spider people", Arrays.asList(Genre.ACTION, Genre.ACTION)));

        // Set directors
        movies.get(0).setDirectors(new String[]{"Makato Shinkai"});
        movies.get(1).setDirectors(new String[]{"Antoine Fuqua"});
        movies.get(2).setDirectors(new String[]{"Martin Scorsese"});
        movies.get(3).setDirectors(new String[]{"Mark Osborne"});
        movies.get(4).setDirectors(new String[]{"Bob Persichetti"});

        long expected = 1;
        long actual = movieAnalysisService.countMoviesFrom(movies, "Makato Shinkai");

        assertEquals(expected, actual, "Expected: " + expected + " Actual: " + actual);
    }

    @Test
    public void test_CountMoviesFrom_wrongDirector_returnCorrectCount() {
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Your Name", "Coming of Age romance", Arrays.asList(Genre.ROMANCE, Genre.DRAMA)));
        movies.add(new Movie("Southpaw", "Boxen", Arrays.asList(Genre.ROMANCE, Genre.DRAMA)));
        movies.add(new Movie("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.ACTION, Genre.ACTION)));
        movies.add(new Movie("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.ACTION, Genre.ACTION)));
        movies.add(new Movie("Into the Spiderverse", "interdimesional spider people", Arrays.asList(Genre.ACTION, Genre.ACTION)));

        // Set directors
        movies.get(0).setDirectors(new String[]{"Makato Shinkai"});
        movies.get(1).setDirectors(new String[]{"Antoine Fuqua"});
        movies.get(2).setDirectors(new String[]{"Martin Scorsese"});
        movies.get(3).setDirectors(new String[]{"Mark Osborne"});
        movies.get(4).setDirectors(new String[]{"Bob Persichetti"});

        long expected = 0;
        long actual = movieAnalysisService.countMoviesFrom(movies, "Denzel Washington");

        assertEquals(expected, actual, "Expected: " + expected + " Actual: " + actual);
    }

    @Test
    public void test_CountMoviesFrom_noDirector_returnCorrectCount() {
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Your Name", "Coming of Age romance", Arrays.asList(Genre.ROMANCE, Genre.DRAMA)));
        movies.add(new Movie("Southpaw", "Boxen", Arrays.asList(Genre.ROMANCE, Genre.DRAMA)));
        movies.add(new Movie("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.ACTION, Genre.ACTION)));
        movies.add(new Movie("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.ACTION, Genre.ACTION)));
        movies.add(new Movie("Into the Spiderverse", "interdimesional spider people", Arrays.asList(Genre.ACTION, Genre.ACTION)));

        long expected = 0;
        long actual = movieAnalysisService.countMoviesFrom(movies, "Mark Osborne");

        assertEquals(expected, actual, "Expected: " + expected + " Actual: " + actual);
    }


    @Test
    public void testDeserializerWithOneSampleResponse() {
        String apiResponseJson = "[{ \"id\": \"81d317b0-29e5-4846-97a6-43c07f3edf4a\", " +
                "\"title\": \"The Godfather\", " +
                "\"genres\": [\"DRAMA\"], " +
                "\"releaseYear\": 1972, " +
                "\"description\": \"The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.\", " +
                "\"imgUrl\": \"https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg\", " +
                "\"lengthInMinutes\": 175, " +
                "\"directors\": [\"Francis Ford Coppola\"], " +
                "\"writers\": [\"Mario Puzo\", \"Francis Ford Coppola\"], " +
                "\"mainCast\": [\"Marlon Brando\", \"Al Pacino\", \"James Caan\"], " +
                "\"rating\": 9.2 }]";

        List<Movie> movieList = Deserializer.deserializeJsonToMovieModel(apiResponseJson);

        assertNotNull(movieList, "Movies list should not be null");
        assertEquals(1, movieList.size(), "List should contain exactly one movie");
        Movie movie = movieList.get(0); // Get the first movie in the list
        assertEquals("81d317b0-29e5-4846-97a6-43c07f3edf4a", movie.getId(), "ID should match");
        assertEquals("The Godfather", movie.getTitle(), "Title should match");
        assertTrue(movie.getGenres().contains(Genre.DRAMA), "Genres should contain DRAMA");
        assertEquals(1972, movie.getReleaseYear(), "Release year should match");
        assertEquals("The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.", movie.getDescription(), "Description should match");
        assertEquals("https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg", movie.getImgUrl(), "Img URL should match");
        assertEquals(175, movie.getLengthInMinutes(), "Length in minutes should match");
        assertTrue(Arrays.asList(movie.getDirectors()).contains("Francis Ford Coppola"), "Directors array should contain Francis Ford Coppola");
        assertTrue(Arrays.asList(movie.getWriters()).contains("Mario Puzo"), "Writers array should contain Mario Puzo");
        assertTrue(Arrays.asList(movie.getMainCast()).contains("Al Pacino"), "Main cast array should contain Al Pacino");
        assertEquals(9.2, movie.getRating(), "Rating should match");
    }

    @Test
    public void genreListToString()
    {
        ArrayList<Genre> list = new ArrayList<>();
        list.add(Genre.ACTION);
        list.add(Genre.ROMANCE);
        MovieEntity movieEntity = new MovieEntity("ID","title","desc",list,10,null,9,5);
        assertEquals("ACTION, ROMANCE",movieEntity.getGenres());
    }




}


