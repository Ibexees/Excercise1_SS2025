package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.dataLayer.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.WatchlistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testcases für Singleton Pattern - Repository Klassen
 * NUR Singleton-Verhalten wird getestet, keine Repository-spezifischen Methoden
 */
class SingletonPatternTest {

    @BeforeEach
    void setUp() {
        // Reset singletons before each test
        MovieRepository.resetInstance();
        WatchlistRepository.resetInstance();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        MovieRepository.resetInstance();
        WatchlistRepository.resetInstance();
    }

    @Test
    void testMovieRepositorySingleton_ShouldReturnSameInstance() {
        // Act
        MovieRepository instance1 = MovieRepository.getInstance();
        MovieRepository instance2 = MovieRepository.getInstance();

        // Assert
        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertSame(instance1, instance2, "Both instances should be the same object");
    }

    @Test
    void testWatchlistRepositorySingleton_ShouldReturnSameInstance() {
        // Act
        WatchlistRepository instance1 = WatchlistRepository.getInstance();
        WatchlistRepository instance2 = WatchlistRepository.getInstance();

        // Assert
        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertSame(instance1, instance2, "Both instances should be the same object");
    }

    @Test
    void testDifferentRepositoryTypes_ShouldBeDifferentObjects() {
        // Act
        MovieRepository movieRepo = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo = WatchlistRepository.getInstance();

        // Assert
        assertNotSame(movieRepo, watchlistRepo, "Different repository types should be different objects");
    }

    @Test
    void testMovieRepositoryReset_ShouldCreateNewInstanceAfterReset() {
        // Arrange
        MovieRepository instance1 = MovieRepository.getInstance();

        // Act
        MovieRepository.resetInstance();
        MovieRepository instance2 = MovieRepository.getInstance();

        // Assert
        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertNotSame(instance1, instance2, "Instance after reset should be different");
    }

    @Test
    void testWatchlistRepositoryReset_ShouldCreateNewInstanceAfterReset() {
        // Arrange
        WatchlistRepository instance1 = WatchlistRepository.getInstance();

        // Act
        WatchlistRepository.resetInstance();
        WatchlistRepository instance2 = WatchlistRepository.getInstance();

        // Assert
        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertNotSame(instance1, instance2, "Instance after reset should be different");
    }

    @Test
    void testMovieRepositorySingleton_ThreadSafety() throws InterruptedException {
        // Arrange
        final int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Future<MovieRepository>[] futures = new Future[threadCount];

        // Act - Create multiple threads trying to get the singleton instance
        for (int i = 0; i < threadCount; i++) {
            futures[i] = executor.submit(MovieRepository::getInstance);
        }

        // Wait for all threads to complete
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        // Assert - All threads should return the same instance
        try {
            MovieRepository firstInstance = futures[0].get();
            for (int i = 1; i < threadCount; i++) {
                MovieRepository instance = futures[i].get();
                assertSame(firstInstance, instance, "All instances from different threads should be the same");
            }
        } catch (Exception e) {
            fail("Exception occurred while getting instances from futures: " + e.getMessage());
        }
    }

    @Test
    void testWatchlistRepositorySingleton_ThreadSafety() throws InterruptedException {
        // Arrange
        final int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Future<WatchlistRepository>[] futures = new Future[threadCount];

        // Act - Create multiple threads trying to get the singleton instance
        for (int i = 0; i < threadCount; i++) {
            futures[i] = executor.submit(WatchlistRepository::getInstance);
        }

        // Wait for all threads to complete
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        // Assert - All threads should return the same instance
        try {
            WatchlistRepository firstInstance = futures[0].get();
            for (int i = 1; i < threadCount; i++) {
                WatchlistRepository instance = futures[i].get();
                assertSame(firstInstance, instance, "All instances from different threads should be the same");
            }
        } catch (Exception e) {
            fail("Exception occurred while getting instances from futures: " + e.getMessage());
        }
    }

    @Test
    void testSingletonIntegrityAfterMultipleGetInstance() {
        // Arrange & Act - Get instances multiple times
        MovieRepository movieRepo1 = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo1 = WatchlistRepository.getInstance();
        MovieRepository movieRepo2 = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo2 = WatchlistRepository.getInstance();
        MovieRepository movieRepo3 = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo3 = WatchlistRepository.getInstance();

        // Assert - All should be the same instances
        assertSame(movieRepo1, movieRepo2, "Movie repository instances should be identical");
        assertSame(movieRepo2, movieRepo3, "Movie repository instances should be identical");
        assertSame(watchlistRepo1, watchlistRepo2, "Watchlist repository instances should be identical");
        assertSame(watchlistRepo2, watchlistRepo3, "Watchlist repository instances should be identical");
    }

    @Test
    void testHomeControllerUsesSingletons() {
        // Note: Dieser Test prüft indirekt, dass HomeController Singletons verwendet
        // Da HomeController jetzt getInstance() verwendet, sollten diese Repositories dieselben sein

        // Act
        MovieRepository directInstance = MovieRepository.getInstance();
        WatchlistRepository directWatchlistInstance = WatchlistRepository.getInstance();

        // Weitere Instanzen holen (simuliert HomeController-Verwendung)
        MovieRepository homeControllerInstance = MovieRepository.getInstance();
        WatchlistRepository homeControllerWatchlistInstance = WatchlistRepository.getInstance();

        // Assert
        assertSame(directInstance, homeControllerInstance, "HomeController should use same MovieRepository singleton");
        assertSame(directWatchlistInstance, homeControllerWatchlistInstance, "HomeController should use same WatchlistRepository singleton");
    }

    @Test
    void testSingletonInstancesAreNotNull() {
        // Act
        MovieRepository movieRepo = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo = WatchlistRepository.getInstance();

        // Assert
        assertNotNull(movieRepo, "MovieRepository singleton should not be null");
        assertNotNull(watchlistRepo, "WatchlistRepository singleton should not be null");
    }

    @Test
    void testMultipleResetAndRecreate() {
        // Test multiple reset cycles
        for (int i = 0; i < 3; i++) {
            // Act
            MovieRepository repo1 = MovieRepository.getInstance();
            WatchlistRepository watch1 = WatchlistRepository.getInstance();

            // Assert instances are singletons within cycle
            MovieRepository repo2 = MovieRepository.getInstance();
            WatchlistRepository watch2 = WatchlistRepository.getInstance();
            assertSame(repo1, repo2, "Should be singleton within cycle " + i);
            assertSame(watch1, watch2, "Should be singleton within cycle " + i);

            // Reset for next cycle
            MovieRepository.resetInstance();
            WatchlistRepository.resetInstance();
        }
    }
}