package at.ac.fhcampuswien.fhmdb.dataLayer.database;

import at.ac.fhcampuswien.fhmdb.logic.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "WatchlistMovieEntity")
public class WatchlistMovieEntity
{
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField()
    private String apiId;

    public WatchlistMovieEntity(){}

    public WatchlistMovieEntity(Movie movie)
    {
        this.apiId = movie.getId();
    }

    public long getId()
    {
        return id;
    }

    public String getApiId()
    {
        return apiId;
    }
}


