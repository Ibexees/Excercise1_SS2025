package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.dataLayer.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.dataLayer.database.WatchlistRepositoryEvent;

import at.ac.fhcampuswien.fhmdb.ui.Observable;
import at.ac.fhcampuswien.fhmdb.ui.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WatchListRepoObserverableTest implements Observer {

    private WatchlistRepository watchlistRepository;
    private boolean wasNotified = false;
    private WatchlistRepositoryEvent lastEvent;

    @BeforeEach
    void setUp() {
            watchlistRepository = new WatchlistRepository();
            watchlistRepository.addSubscriber(this);
            wasNotified = false;
            lastEvent = null;
    }

    @Test
    void testObserverNotificationOnWatchlistAdd() {
        watchlistRepository.onWatchlistAdd();

        // Assert
        assertTrue(wasNotified);
        assertEquals(WatchlistRepositoryEvent.Type.MOVIE_ADDED, lastEvent.getType());
        assertEquals("Movie Successfully added to Watchlist", lastEvent.getMessage());
    }

    @Test
    void testObserverNotificationOnWatchlistRemove() {
        watchlistRepository.onWatchlistRemove();

        assertTrue(wasNotified);
        assertEquals(WatchlistRepositoryEvent.Type.MOVIE_REMOVED, lastEvent.getType());
        assertEquals("Movie Successfully removed from Watchlist", lastEvent.getMessage());
    }

    @Test
    void testObserverNotificationOnWatchlistAddExisting() {
        watchlistRepository.onWatchlistAddExisting();

        assertTrue(wasNotified);
        assertEquals(WatchlistRepositoryEvent.Type.MOVIE_ALREADY_EXISTS, lastEvent.getType());
        assertEquals("Movie already in Watchlist", lastEvent.getMessage());
    }

    @Override
    public void onRepositoryEvent(WatchlistRepositoryEvent event) {
        this.wasNotified = true;
        this.lastEvent = event;
    }
}
