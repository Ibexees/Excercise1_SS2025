package at.ac.fhcampuswien.fhmdb.dataLayer.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository
{
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

    public int addToWatchlist (WatchlistMovieEntity movie) throws SQLException
    {
        List<WatchlistMovieEntity> existing = dao.queryForEq("apiID", movie.getApiId());
        if(existing.isEmpty())
        {
            dao.create(movie);
        }
        return 1;
    }

    public int removeFromWatchlist(String apiID) throws SQLException
    {
        DeleteBuilder<WatchlistMovieEntity, Long> deleteBuilder = dao.deleteBuilder();
        deleteBuilder.where().eq("apiID", apiID);
        deleteBuilder.delete();
        return 1;
    }
}
