package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.dataLayer.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.dataLayer.api.MovieAPIRequestBuilder;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.WatchlistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Tests für Builder Pattern + Singleton Pattern zusammen
 * Testet nur die Pattern-Implementation, keine Repository-spezifischen Methoden
 */
class BuilderSingletonIntegrationTest {

    @BeforeEach
    void setUp() {
        // Reset singletons before each test
        MovieRepository.resetInstance();
        WatchlistRepository.resetInstance();
    }

    @AfterEach
    void tearDown() {
        // Clean up after tests
        MovieRepository.resetInstance();
        WatchlistRepository.resetInstance();
    }

    @Test
    void testRequirementsExample1_BuilderPattern() {
        // Exact example from Exercise 4 requirements:
        // String url = new MovieAPIRequestBuilder(base)
        //  .query("suchwort")
        //  .genre("ACTION")
        //  .releaseYear("2012")
        //  .ratingFrom("8.3")
        //  .build();

        String base = "https://prog2.fh-campuswien.ac.at/movies";

        // Act
        String url = new MovieAPIRequestBuilder(base)
                .query("suchwort")
                .genre("ACTION")
                .releaseYear("2012")
                .ratingFrom("8.3")
                .build();

        // Assert
        String expected = "https://prog2.fh-campuswien.ac.at/movies?query=suchwort&genre=ACTION&releaseYear=2012&ratingFrom=8.3";
        assertEquals(expected, url);
    }

    @Test
    void testRequirementsExample2_BuilderPattern() {
        // Exact example from Exercise 4 requirements:
        // String url = new MovieAPIRequestBuilder(base + "/" + id).build();

        String base = "https://prog2.fh-campuswien.ac.at/movies";
        String id = "12345";

        // Act
        String url = new MovieAPIRequestBuilder(base + "/" + id).build();

        // Assert
        String expected = "https://prog2.fh-campuswien.ac.at/movies/12345";
        assertEquals(expected, url);
    }

    @Test
    void testRepositoriesAreSingletons_RequirementFulfilled() {
        // Requirements: "Die Repository Klassen sind als Singleton implementiert"

        // Act
        MovieRepository movieRepo1 = MovieRepository.getInstance();
        MovieRepository movieRepo2 = MovieRepository.getInstance();

        WatchlistRepository watchlistRepo1 = WatchlistRepository.getInstance();
        WatchlistRepository watchlistRepo2 = WatchlistRepository.getInstance();

        // Assert
        assertSame(movieRepo1, movieRepo2, "MovieRepository should be singleton");
        assertSame(watchlistRepo1, watchlistRepo2, "WatchlistRepository should be singleton");
    }

    @Test
    void testBuilderWithMovieAPIStaticMethods() {
        // Test integration with MovieAPI static methods

        // Act
        MovieAPIRequestBuilder builder1 = MovieAPI.builder();
        String url1 = builder1.query("test").build();

        MovieAPIRequestBuilder builder2 = MovieAPI.builder("https://example.com");
        String url2 = builder2.query("test").build();

        // Assert
        assertTrue(url1.contains("query=test"));
        assertEquals("https://example.com?query=test", url2);
    }

    @Test
    void testPatternsWorkTogetherInRealisticScenario() {
        // Simulate realistic usage scenario combining both patterns

        // 1. Get singleton repositories (like HomeController does)
        MovieRepository movieRepo = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo = WatchlistRepository.getInstance();

        // 2. Use builder to create API URLs (like filtering would do)
        String filterUrl = MovieAPI.builder()
                .query("batman")
                .genre("ACTION")
                .releaseYear("2008")
                .build();

        // 3. Verify both work correctly
        assertNotNull(movieRepo, "MovieRepository singleton should be available");
        assertNotNull(watchlistRepo, "WatchlistRepository singleton should be available");
        assertTrue(filterUrl.contains("query=batman"), "Builder should create correct URL");

        // 4. Verify singletons remain consistent
        MovieRepository movieRepo2 = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo2 = WatchlistRepository.getInstance();

        assertSame(movieRepo, movieRepo2, "Repository instances should remain consistent");
        assertSame(watchlistRepo, watchlistRepo2, "Repository instances should remain consistent");
    }

    @Test
    void testBuilderPatternFlexibility() {
        // Test that builder allows different parameter combinations

        String base = "https://prog2.fh-campuswien.ac.at/movies";

        // Different combinations
        String url1 = new MovieAPIRequestBuilder(base).query("action").build();
        String url2 = new MovieAPIRequestBuilder(base).genre("COMEDY").build();
        String url3 = new MovieAPIRequestBuilder(base).query("batman").genre("ACTION").build();
        String url4 = new MovieAPIRequestBuilder(base).build(); // No parameters

        // Assert all work correctly
        assertEquals(base + "?query=action", url1);
        assertEquals(base + "?genre=COMEDY", url2);
        assertEquals(base + "?query=batman&genre=ACTION", url3);
        assertEquals(base, url4);
    }

    @Test
    void testSingletonPatternMemoryEfficiency() {
        // Test that singleton pattern doesn't create unnecessary instances

        // Create multiple references
        MovieRepository[] movieRefs = new MovieRepository[3];
        WatchlistRepository[] watchlistRefs = new WatchlistRepository[3];

        for (int i = 0; i < 3; i++) {
            movieRefs[i] = MovieRepository.getInstance();
            watchlistRefs[i] = WatchlistRepository.getInstance();
        }

        // All should reference the same objects
        for (int i = 1; i < 3; i++) {
            assertSame(movieRefs[0], movieRefs[i], "All MovieRepository references should be identical");
            assertSame(watchlistRefs[0], watchlistRefs[i], "All WatchlistRepository references should be identical");
        }
    }

    @Test
    void testExercise4Requirements_BothPatternsImplemented() {
        // Final verification that Exercise 4 requirements are met

        // 1. Builder Pattern requirement
        assertDoesNotThrow(() -> {
            String url = new MovieAPIRequestBuilder("https://prog2.fh-campuswien.ac.at/movies")
                    .query("test")
                    .genre("ACTION")
                    .build();
            assertNotNull(url);
        }, "Builder Pattern should be implemented and working");

        // 2. Singleton Pattern requirement
        assertDoesNotThrow(() -> {
            MovieRepository repo1 = MovieRepository.getInstance();
            MovieRepository repo2 = MovieRepository.getInstance();
            assertSame(repo1, repo2);

            WatchlistRepository watch1 = WatchlistRepository.getInstance();
            WatchlistRepository watch2 = WatchlistRepository.getInstance();
            assertSame(watch1, watch2);
        }, "Singleton Pattern should be implemented and working");
    }

    @Test
    void testBuilderAndSingletonWorkIndependently() {
        // Test that both patterns work independently

        // Test Builder independently
        String builderUrl = new MovieAPIRequestBuilder("https://test.com")
                .query("independent")
                .genre("TEST")
                .build();
        assertEquals("https://test.com?query=independent&genre=TEST", builderUrl);

        // Test Singleton independently
        MovieRepository repo1 = MovieRepository.getInstance();
        MovieRepository repo2 = MovieRepository.getInstance();
        assertSame(repo1, repo2);

        // Test they don't interfere with each other
        String builderUrl2 = new MovieAPIRequestBuilder("https://test2.com")
                .query("still_working")
                .build();
        assertEquals("https://test2.com?query=still_working", builderUrl2);

        MovieRepository repo3 = MovieRepository.getInstance();
        assertSame(repo1, repo3);
    }

    @Test
    void testCompleteWorkflow_HomeControllerSimulation() {
        // Simulate complete workflow like HomeController would use

        // 1. Initialize repositories (singleton)
        MovieRepository movieRepo = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo = WatchlistRepository.getInstance();

        // 2. Build filter URL (builder pattern)
        String filterUrl = MovieAPI.builder()
                .query("action movies")
                .genre("ACTION")
                .releaseYear("2023")
                .ratingFrom("7.0")
                .build();

        // 3. Verify everything works
        assertNotNull(movieRepo);
        assertNotNull(watchlistRepo);
        assertTrue(filterUrl.contains("query=action%20movies"));
        assertTrue(filterUrl.contains("genre=ACTION"));
        assertTrue(filterUrl.contains("releaseYear=2023"));
        assertTrue(filterUrl.contains("ratingFrom=7.0"));

        // 4. Verify singletons are maintained throughout workflow
        MovieRepository movieRepo2 = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo2 = WatchlistRepository.getInstance();
        assertSame(movieRepo, movieRepo2);
        assertSame(watchlistRepo, watchlistRepo2);
    }
}