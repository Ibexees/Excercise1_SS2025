package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.api.ApiException;
import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.api.Deserializer;
import at.ac.fhcampuswien.fhmdb.dataLayer.MovieEntity;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.*;

public class Movie {
    private long databaseID;
    private String id;
    private String title;
    private String description;
    @SerializedName("genres")
    private List<Genre> genres;
    private int releaseYear;
    private String imgUrl;
    private int lengthInMinutes;
    private String[] mainCast;
    private String[] directors;
    private String[] writers;
    private double rating;


    // TODO add more properties here

    public Movie(String title, String description,List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public Movie(MovieEntity movieEntity)
    {
        this.databaseID = movieEntity.getId();
        this.id = movieEntity.getApiId();
        this.title = movieEntity.getTitle();
        this.description = movieEntity.getDescription();
        this.releaseYear = movieEntity.getReleaseYear();
        this.genres = movieEntity.stringToGenres(movieEntity.getGenres());
        this.imgUrl = movieEntity.getImgUrl();
        this.lengthInMinutes = movieEntity.getLengthInMinutes();
        this.rating = movieEntity.getRating();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres()
    {
        return genres;
    }

    public String getGenresAsString()
    {
        return this.genres.toString();
    }


    public static List<Movie> initializeMovies(Map<String,String> parameters) throws RuntimeException {
        List<Movie> movies = new ArrayList<>();

        String apiResponse;
        Map<String, String> emptyParameters = new HashMap<>();

        try {
            apiResponse = MovieAPI.getMovies(parameters);
            //System.out.println(apiResponse);
            movies = Deserializer.deserializeJsonToMovieModel(apiResponse);

        } catch (IOException | ApiException e) {
            throw new RuntimeException("Failed to initialize movies: " + e.getMessage(), e);
        }
        return movies;
    }


    @Override
    public String toString()
    {
        return("Title:"+title+"desc:"+description+"Genre:"+genres);
    }

    @Override
    public boolean equals(Object obj)
    {

        if(obj instanceof  Movie)
        {
            Movie movie = (Movie) obj;
            if (this.title.equals(movie.title))
            {
                return true;
            }
        }
        return false;

    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public Movie setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public Movie setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Movie setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public Movie setLengthInMinutes(int lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
        return this;
    }

    public String[] getMainCast() {
        if(mainCast == null)
        {
            mainCast = new String[0];
        }
        return mainCast;
    }

    public Movie setMainCast(String[] mainCast) {
        this.mainCast = mainCast;
        return this;
    }

    public String[] getDirectors() {
        return directors;
    }

    public Movie setDirectors(String[] directors) {
        this.directors = directors;
        return this;
    }

    public String[] getWriters() {
        return writers;
    }

    public Movie setWriters(String[] writers) {
        this.writers = writers;
        return this;
    }

    public double getRating() {
        return rating;
    }

    public Movie setRating(double rating) {
        this.rating = rating;
        return this;
    }

    public String getId() {
        return id;
    }

    public Movie setId(String id) {
        this.id = id;
        return this;
    }
}
