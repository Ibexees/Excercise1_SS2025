package at.ac.fhcampuswien.fhmdb.dataLayer.database;

import at.ac.fhcampuswien.fhmdb.ui.Observable;
import at.ac.fhcampuswien.fhmdb.ui.Observer;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository extends AbstractObservable implements Observable
{
    private static WatchlistRepository instance;
    Dao<WatchlistMovieEntity, Long> dao;


    public WatchlistRepository() {
        try {
            this.dao = DatabaseManager.getDatabase().getDynamicDao(WatchlistMovieEntity.class);
        } catch (Exception e) {
            throw new DataBaseException("could not initialize watchlist repository");
        }
    }

    public List<WatchlistMovieEntity> getWatchlist() throws SQLException
    {
        //System.out.println(dao.queryForAll());
        return dao.queryForAll();
    }

    public void addToWatchlist (WatchlistMovieEntity movie) throws SQLException
    {
        List<WatchlistMovieEntity> existing = dao.queryForEq("apiID", movie.getApiId());
        if(existing.isEmpty())
        {
            dao.create(movie);
            this.onWatchlistAdd();
        }
        else
        {
            this.onWatchlistAddExisting();
        }


        return;
    }

    public int removeFromWatchlist(String apiID) throws SQLException
    {
        DeleteBuilder<WatchlistMovieEntity, Long> deleteBuilder = dao.deleteBuilder();
        deleteBuilder.where().eq("apiID", apiID);
        deleteBuilder.delete();
        this.onWatchlistRemove();
        return 1;
    }

    public void onWatchlistAdd()
    {
        WatchlistRepositoryEvent event = new WatchlistRepositoryEvent(WatchlistRepositoryEvent.Type.MOVIE_ADDED,"Movie Successfully added to Watchlist");
        notifyObserver(event);

    }

    public void onWatchlistRemove()
    {
        WatchlistRepositoryEvent event = new WatchlistRepositoryEvent(WatchlistRepositoryEvent.Type.MOVIE_REMOVED, "Movie Successfully removed from Watchlist");
        notifyObserver(event);
    }

    public void onWatchlistAddExisting()
    {
        WatchlistRepositoryEvent event = new WatchlistRepositoryEvent(WatchlistRepositoryEvent.Type.MOVIE_ALREADY_EXISTS, "Movie already in Watchlist");
        notifyObserver(event);
    }

    public static WatchlistRepository getInstance() {
        if (instance == null) {
            synchronized (WatchlistRepository.class) {
                if (instance == null) {
                    instance = new WatchlistRepository();
                }
            }
        }
        return instance;
    }

    /**
     * Resets the singleton instance (for testing purposes).
     */
    public static synchronized void resetInstance() {
        instance = null;
    }
}


