package at.ac.fhcampuswien.fhmdb.dataLayer.database;

import at.ac.fhcampuswien.fhmdb.logic.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.Observable;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository
{
    private static MovieRepository instance;
    Dao<MovieEntity, Long> dao;


    private MovieRepository() {
    try {
        this.dao = DatabaseManager.getDatabase().getDynamicDao(MovieEntity.class);
    } catch (Exception e) {
        throw new DataBaseException( "could not initialize movie repository" );
    }


    }

    public void addAllMovies(List<Movie> movies) throws SQLException
    {
        for(Movie currentMovie : movies)
        {
            MovieEntity movieEntity = new MovieEntity(currentMovie);
            dao.create(movieEntity);
        }

    }

    /*public Movie getMovie(String name) throws SQLException //get by ID oder Name ?
    {
        List<MovieEntity> result = dao.queryBuilder()
                .where()
                .eq("title", name)
                .query();
        System.out.println(MovieEntity.toMovies(result).get(0));
        return MovieEntity.toMovies(result).get(0);
    }*/

    public Movie getMovie(String apiID) throws SQLException //get by ID oder Name ?
    {
        List<MovieEntity> result = dao.queryBuilder()
                .where()
                .eq("apiID", apiID)
                .query();
        //System.out.println(MovieEntity.toMovies(result).get(0));
        return MovieEntity.toMovies(result).get(0);
    }

    public List<Movie> getAllMovies() {
        try {
            return MovieEntity.toMovies(dao.queryForAll());
        } catch (SQLException e) {
            throw new DataBaseException("Failed to retrieve all movies", e);
        }
    }

    public int removeAll() {
        try {
            return dao.deleteBuilder().delete();
        } catch (SQLException e) {
            throw new DataBaseException("Failed to remove all movies", e);
        }
    }

    public List<MovieEntity> readAllMovies() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DataBaseException("Failed to read all movies", e);
        }
    }

    public static MovieRepository getInstance() {
        if (instance == null) {
            synchronized (MovieRepository.class) {
                if (instance == null) {
                    instance = new MovieRepository();
                }
            }
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        instance = null;
    }
}




