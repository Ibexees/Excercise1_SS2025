package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.dataLayer.api.MovieAPIException;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.DataBaseException;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.logic.models.Movie;
import at.ac.fhcampuswien.fhmdb.logic.models.Genre;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerExceptionTest {

    private TestableHomeController homeController;
    private List<Movie> testMovies;

    @BeforeEach
    void setUp() {
        homeController = new TestableHomeController();

        // Create test data
        testMovies = Arrays.asList(
                new Movie("Test Movie 1", "Description 1", Arrays.asList(Genre.ACTION))
                        .setId("1").setReleaseYear(2020).setRating(8.0),
                new Movie("Test Movie 2", "Description 2", Arrays.asList(Genre.COMEDY))
                        .setId("2").setReleaseYear(2021).setRating(7.5)
        );

        homeController.allMovies = new ArrayList<>(testMovies);
        homeController.observableMovies = FXCollections.observableArrayList();
    }


    @Test
    void testInitializeMovies_APIException_FallsBackToDatabase() {
        // Arrange
        homeController.setApiShouldFail(true);
        homeController.setDatabaseShouldFail(false);
        Map<String, String> parameters = new HashMap<>();

        // Act
        List<Movie> result = homeController.initializeMovies(parameters);

        // Assert
        assertEquals(testMovies.size(), result.size());
        assertTrue(homeController.wasApiCalled());
        assertTrue(homeController.wasDatabaseFallbackCalled());
    }

    @Test
    void testInitializeMovies_APIException_DatabaseAlsoFails() {
        // Arrange
        homeController.setApiShouldFail(true);
        homeController.setDatabaseShouldFail(true);
        Map<String, String> parameters = new HashMap<>();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> homeController.initializeMovies(parameters));

        assertTrue(exception.getMessage().contains("Failed to initialize movies"));
        assertTrue(homeController.wasApiCalled());
        assertTrue(homeController.wasDatabaseFallbackCalled());
    }

    @Test
    void testInitializeMovies_APIWorks_DatabaseNotCalled() {
        // Arrange
        homeController.setApiShouldFail(false);
        homeController.setDatabaseShouldFail(true);
        Map<String, String> parameters = new HashMap<>();

        // Act
        List<Movie> result = homeController.initializeMovies(parameters);

        // Assert
        assertNotNull(result);
        assertTrue(homeController.wasApiCalled());
        assertFalse(homeController.wasDatabaseFallbackCalled());
    }


    @Test
    void testPerformLocalFiltering_DatabaseException() {
        // Arrange
        homeController.setDatabaseShouldFail(true);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query", "test");

        // Act & Assert
        assertThrows(DataBaseException.class, () -> {
            homeController.performLocalFiltering(parameters);
        });
    }

    @Test
    void testPerformLocalFiltering_DatabaseWorks() {
        // Arrange
        homeController.setDatabaseShouldFail(false);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query", "Test Movie 1");

        // Act
        homeController.performLocalFiltering(parameters);

        // Assert
        assertEquals(1, homeController.observableMovies.size());
        assertEquals("Test Movie 1", homeController.observableMovies.get(0).getTitle());
    }

    @Test
    void testLoadMoviesFromAPIAndRefreshDatabase_DatabaseSaveException() {
        // Arrange
        homeController.setApiShouldFail(false);
        homeController.setDatabaseSaveOperationsShouldFail(true);

        // Act & Assert
        assertThrows(DataBaseException.class, () -> {
            homeController.testLoadMoviesFromAPIAndRefreshDatabase();
        });
    }

    @Test
    void testLoadMoviesFromAPIAndRefreshDatabase_DatabaseRemoveAllException() {
        // Arrange
        homeController.setApiShouldFail(false);
        homeController.setDatabaseRemoveAllShouldFail(true);

        // Act & Assert
        assertThrows(DataBaseException.class, () -> {
            homeController.testLoadMoviesFromAPIAndRefreshDatabase();
        });
    }

    private static class TestableHomeController extends HomeController {
        private boolean apiShouldFail = false;
        private boolean databaseShouldFail = false;
        private boolean databaseSaveOperationsShouldFail = false;
        private boolean databaseRemoveAllShouldFail = false;
        private boolean apiCalled = false;
        private boolean databaseFallbackCalled = false;

        public TestableHomeController() {
            super();
            // Create a test repository that can simulate failures
            this.movieRepository = new TestableMovieRepository();
        }

        public void setApiShouldFail(boolean apiShouldFail) {
            this.apiShouldFail = apiShouldFail;
        }

        public void setDatabaseShouldFail(boolean databaseShouldFail) {
            this.databaseShouldFail = databaseShouldFail;
            ((TestableMovieRepository) this.movieRepository).setShouldFailOnRead(databaseShouldFail);
        }

        public void setDatabaseSaveOperationsShouldFail(boolean shouldFail) {
            this.databaseSaveOperationsShouldFail = shouldFail;
            ((TestableMovieRepository) this.movieRepository).setShouldFailOnSave(shouldFail);
        }

        public void setDatabaseRemoveAllShouldFail(boolean shouldFail) {
            this.databaseRemoveAllShouldFail = shouldFail;
            ((TestableMovieRepository) this.movieRepository).setShouldFailOnRemoveAll(shouldFail);
        }

        public boolean wasApiCalled() {
            return apiCalled;
        }

        public boolean wasDatabaseFallbackCalled() {
            return databaseFallbackCalled;
        }

        // Test method to simulate loadMoviesFromAPIAndRefreshDatabase without UI dependencies
        public void testLoadMoviesFromAPIAndRefreshDatabase() {
            try {
                allMovies = initializeMovies(new HashMap<>());
                movieRepository.removeAll();
                movieRepository.addAllMovies(allMovies);
            } catch (DataBaseException e) {
                throw e; // Re-throw for test verification
            } catch (Exception e) {
                throw new RuntimeException("Unexpected error", e);
            }
        }

        @Override
        public List<Movie> initializeMovies(Map<String, String> parameters) {
            List<Movie> movies = new ArrayList<>();

            try {
                // Simulate API call
                apiCalled = true;
                if (apiShouldFail) {
                    throw new MovieAPIException("Simulated API failure");
                }

                // Simulate successful API response
                movies.addAll(allMovies);
                System.out.println("Fetched movies from API.");

            } catch (MovieAPIException e) {
                System.err.println("API failed: " + e.getMessage());

                try {
                    // Simulate database fallback
                    databaseFallbackCalled = true;
                    movies = movieRepository.getAllMovies();
                    System.out.println("Loaded movies from database fallback.");

                } catch (DataBaseException dbException) {
                    System.err.println("Database loading failed: " + dbException.getMessage());
                    throw new RuntimeException("Failed to initialize movies: " + dbException.getMessage(), dbException);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize movies: " + e.getMessage(), e);
            }

            return movies;
        }

        @Override
        public void performLocalFiltering(Map<String, String> parameters) {
            List<Movie> moviesToFilter = movieRepository.getAllMovies();
            System.out.println(moviesToFilter.size() + " movies from database loaded");

            // Apply filtering
            List<Movie> filteredMovies = filterMovies(moviesToFilter, parameters);

            // Clear and update observable list
            observableMovies.clear();
            observableMovies.addAll(filteredMovies);
        }
    }

    private static class TestableMovieRepository extends MovieRepository {
        private boolean shouldFailOnRead = false;
        private boolean shouldFailOnSave = false;
        private boolean shouldFailOnRemoveAll = false;
        private List<Movie> testData = Arrays.asList(
                new Movie("Test Movie 1", "Description 1", Arrays.asList(Genre.ACTION))
                        .setId("1").setReleaseYear(2020).setRating(8.0),
                new Movie("Test Movie 2", "Description 2", Arrays.asList(Genre.COMEDY))
                        .setId("2").setReleaseYear(2021).setRating(7.5)
        );

        public TestableMovieRepository() {
            // Don't call super() to avoid database initialization issues in tests
        }

        public void setShouldFailOnRead(boolean shouldFail) {
            this.shouldFailOnRead = shouldFail;
        }

        public void setShouldFailOnSave(boolean shouldFail) {
            this.shouldFailOnSave = shouldFail;
        }

        public void setShouldFailOnRemoveAll(boolean shouldFail) {
            this.shouldFailOnRemoveAll = shouldFail;
        }

        @Override
        public List<Movie> getAllMovies() {
            if (shouldFailOnRead) {
                throw new DataBaseException("Simulated database failure during getAllMovies", new SQLException("Connection failed"));
            }
            return new ArrayList<>(testData);
        }

        @Override
        public int removeAll() {
            if (shouldFailOnRemoveAll) {
                throw new DataBaseException("Simulated database failure during removeAll", new SQLException("Delete failed"));
            }
            return testData.size(); // Simulate successful removal
        }

        @Override
        public void addAllMovies(List<Movie> movies) throws SQLException {
            if (shouldFailOnSave) {
                throw new DataBaseException("Simulated database failure during addAllMovies", new SQLException("Insert failed"));
            }
            // Simulate successful addition
        }
    }
}
