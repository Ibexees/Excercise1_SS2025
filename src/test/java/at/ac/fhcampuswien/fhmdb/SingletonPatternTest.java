package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.dataLayer.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.WatchlistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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
}

