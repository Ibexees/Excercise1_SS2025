package at.ac.fhcampuswien.fhmdb.dataLayer;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseManager //Singleton Klasse kann nur einmal instanziert werden!
{
    public static final String DB_URL = "jdbc:h2:file: ./db/movieDB";
    public static final String user = "user";
    public static final String password = "pass";

    private static ConnectionSource connectionSource;
    private Dao<MovieEntity, Long> dao;

    private static DatabaseManager instance;

    public Dao<MovieEntity, Long> getDao()
    {
        return this.dao;
    }

    private DatabaseManager()
    {
        try
        {
            createConnectionSource();
            dao = DaoManager.createDao(connectionSource, MovieEntity.class);
            createTables();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }

    public  void testDB() throws SQLException
    {
        ArrayList<Genre> list = new ArrayList<>();
        list.add(Genre.ACTION);
        list.add(Genre.ROMANCE);
        MovieEntity movieEntity = new MovieEntity(new Movie("Your Name","Coming of Age romance", Arrays.asList(Genre.ROMANCE,Genre.DRAMA)).setReleaseYear(2016).setId("5522a"));
        //MovieEntity movie = new MovieEntity("ID","title","desc",list,10,null,9,5);
        dao.create(movieEntity);
    }

    public static DatabaseManager  getDatabase()
    {
        if(instance == null)
        {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private static void createTables() throws SQLException
    {
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
    }

    private static void createConnectionSource() throws SQLException
    {
         connectionSource = new JdbcConnectionSource(DB_URL,user,password);
    }
}
