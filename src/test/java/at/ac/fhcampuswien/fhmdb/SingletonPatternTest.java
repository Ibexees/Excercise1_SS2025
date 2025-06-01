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


class SingletonPatternTest {

    @BeforeEach
    void setUp() {

        MovieRepository.resetInstance();
        WatchlistRepository.resetInstance();
    }

    @AfterEach
    void tearDown() {

        MovieRepository.resetInstance();
        WatchlistRepository.resetInstance();
    }

    @Test
    void testMovieRepositorySingleton_ShouldReturnSameInstance() {

        MovieRepository instance1 = MovieRepository.getInstance();
        MovieRepository instance2 = MovieRepository.getInstance();


        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertSame(instance1, instance2, "Both instances should be the same object");
    }

    @Test
    void testWatchlistRepositorySingleton_ShouldReturnSameInstance() {

        WatchlistRepository instance1 = WatchlistRepository.getInstance();
        WatchlistRepository instance2 = WatchlistRepository.getInstance();


        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertSame(instance1, instance2, "Both instances should be the same object");
    }

    @Test
    void testDifferentRepositoryTypes_ShouldBeDifferentObjects() {

        MovieRepository movieRepo = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo = WatchlistRepository.getInstance();


        assertNotSame(movieRepo, watchlistRepo, "Different repository types should be different objects");
    }

    @Test
    void testMovieRepositoryReset_ShouldCreateNewInstanceAfterReset() {
        MovieRepository instance1 = MovieRepository.getInstance();


        MovieRepository.resetInstance();
        MovieRepository instance2 = MovieRepository.getInstance();


        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertNotSame(instance1, instance2, "Instance after reset should be different");
    }

    @Test
    void testWatchlistRepositoryReset_ShouldCreateNewInstanceAfterReset() {

        WatchlistRepository instance1 = WatchlistRepository.getInstance();


        WatchlistRepository.resetInstance();
        WatchlistRepository instance2 = WatchlistRepository.getInstance();


        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertNotSame(instance1, instance2, "Instance after reset should be different");
    }

    @Test
    void testMovieRepositorySingleton_ThreadSafety() throws InterruptedException {

        final int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Future<MovieRepository>[] futures = new Future[threadCount];


        for (int i = 0; i < threadCount; i++) {
            futures[i] = executor.submit(MovieRepository::getInstance);
        }


        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));


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
        final int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Future<WatchlistRepository>[] futures = new Future[threadCount];


        for (int i = 0; i < threadCount; i++) {
            futures[i] = executor.submit(WatchlistRepository::getInstance);
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));


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

        MovieRepository movieRepo1 = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo1 = WatchlistRepository.getInstance();
        MovieRepository movieRepo2 = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo2 = WatchlistRepository.getInstance();
        MovieRepository movieRepo3 = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo3 = WatchlistRepository.getInstance();


        assertSame(movieRepo1, movieRepo2, "Movie repository instances should be identical");
        assertSame(movieRepo2, movieRepo3, "Movie repository instances should be identical");
        assertSame(watchlistRepo1, watchlistRepo2, "Watchlist repository instances should be identical");
        assertSame(watchlistRepo2, watchlistRepo3, "Watchlist repository instances should be identical");
    }

    @Test
    void testHomeControllerUsesSingletons() {

        MovieRepository directInstance = MovieRepository.getInstance();
        WatchlistRepository directWatchlistInstance = WatchlistRepository.getInstance();


        MovieRepository homeControllerInstance = MovieRepository.getInstance();
        WatchlistRepository homeControllerWatchlistInstance = WatchlistRepository.getInstance();


        assertSame(directInstance, homeControllerInstance, "HomeController should use same MovieRepository singleton");
        assertSame(directWatchlistInstance, homeControllerWatchlistInstance, "HomeController should use same WatchlistRepository singleton");
    }

    @Test
    void testSingletonInstancesAreNotNull() {

        MovieRepository movieRepo = MovieRepository.getInstance();
        WatchlistRepository watchlistRepo = WatchlistRepository.getInstance();


        assertNotNull(movieRepo, "MovieRepository singleton should not be null");
        assertNotNull(watchlistRepo, "WatchlistRepository singleton should not be null");
    }

    @Test
    void testMultipleResetAndRecreate() {

        for (int i = 0; i < 3; i++) {

            MovieRepository repo1 = MovieRepository.getInstance();
            WatchlistRepository watch1 = WatchlistRepository.getInstance();

            MovieRepository repo2 = MovieRepository.getInstance();
            WatchlistRepository watch2 = WatchlistRepository.getInstance();
            assertSame(repo1, repo2, "Should be singleton within cycle " + i);
            assertSame(watch1, watch2, "Should be singleton within cycle " + i);

            MovieRepository.resetInstance();
            WatchlistRepository.resetInstance();
        }
    }
}

