package at.ac.fhcampuswien.fhmdb.dataLayer.database; //import libraries
import at.ac.fhcampuswien.fhmdb.logic.models.Genre;
import at.ac.fhcampuswien.fhmdb.logic.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DatabaseTable(tableName = "MovieEntity")
public class MovieEntity
{
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField()
    private String apiId;

    @DatabaseField()
    private String title;

    @DatabaseField()
    private String description;

    @DatabaseField()
    private String genres;

    @DatabaseField()
    private int releaseYear;

    @DatabaseField()
    private String imgUrl;

    @DatabaseField()
    private int lengthInMinutes;

    //private String[] mainCast;
    //private String[] directors;

    @DatabaseField()//private String[] writers;
    private double rating;




    public MovieEntity(){}


    public MovieEntity(String apiId, String title, String description, List<Genre> genres, int releaseYear, String imgUrl, int lengthInMinutes, double rating)
    {
        this.apiId = apiId;
        this.title = title;
        this.description = description;
        this.genres = this.generesToString(genres);
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.rating = rating;
    }

    public MovieEntity(Movie movie)
    {
        this.apiId = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.releaseYear = movie.getReleaseYear();
        this.genres = this.generesToString(movie.getGenres());
        this.imgUrl = movie.getImgUrl();
        this.lengthInMinutes = movie.getLengthInMinutes();
        this.rating = movie.getRating();
    }

    public String getGenres()
    {
        return genres;
    }

    public String generesToString(List<Genre> genres)
    {
        StringBuilder genreString = new StringBuilder();
        for(Genre genre : genres)
        {
            genreString.append(genre).append(", ");
        }
        genreString.delete(genreString.length()-2,genreString.length());
        return genreString.toString();
    }

    public List<Genre> stringToGenres(String genreString) {
        if (genreString == null || genreString.isBlank()) {
            return Collections.emptyList();
        }

        String[] parts = genreString.split(",\\s*"); // split bei Komma und optionalem Leerzeichen
        List<Genre> genres = new ArrayList<>();

        for (String part : parts) {
            try {
                genres.add(Genre.valueOf(part.toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Ungültiges Genre – optional ignorieren oder loggen
                System.err.println("Unbekanntes Genre: " + part);
            }
        }

        return genres;
    }

    public static List<MovieEntity> fromMovies(List<Movie> movies)
    {
        List<MovieEntity> movieEntityList = new ArrayList<>();

        for(Movie currentMovie : movies)
        {
            movieEntityList.add(new MovieEntity(currentMovie));
        }
        return movieEntityList;
    }

    public static List<Movie> toMovies(List<MovieEntity> movieEntities)
    {
        List<Movie> movieList = new ArrayList<>();
        for(MovieEntity currentMovieEntity : movieEntities)
        {
             movieList.add(new Movie(currentMovieEntity));
        }
        return movieList;
    }

    public String getApiId()
    {
        return apiId;
    }

    public int getReleaseYear()
    {
        return releaseYear;
    }

    public double getRating()
    {
        return rating;
    }

    public int getLengthInMinutes()
    {
        return lengthInMinutes;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public String getDescription()
    {
        return description;
    }

    public String getTitle()
    {
        return title;
    }

    public long getId()
    {
        return id;
    }
}
