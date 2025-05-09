package at.ac.fhcampuswien.fhmdb.models;

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


    public static List<Movie> initializeMovies(Map<String,String> parameters){
        List<Movie> movies = new ArrayList<>();

        String apiResponse;
        Map<String, String> emptyParameters = new HashMap<>();

        try {
            apiResponse = MovieAPI.getMovies(parameters);
            //System.out.println(apiResponse);
            movies = Deserializer.deserializeJsonToMovieModel(apiResponse);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Stock Movies
        /*
        movies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        movies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));
        movies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)));
        movies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)));
        movies.add(new Movie ("Kung Fu Panda", "Wuxi-fingergriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)));
        movies.add(new Movie("Inception", "A dream within a dream", Arrays.asList(Genre.SCIENCE_FICTION, Genre.THRILLER)));
        movies.add(new Movie("The Dark Knight", "The rise of the Batman", Arrays.asList(Genre.ACTION, Genre.CRIME, Genre.DRAMA)));
        movies.add(new Movie("Interstellar", "Love and science beyond time", Arrays.asList(Genre.SCIENCE_FICTION, Genre.DRAMA, Genre.ADVENTURE)));
        movies.add(new Movie("The Grand Budapest Hotel", "A whimsical tale of adventure and crime", Arrays.asList(Genre.COMEDY, Genre.CRIME, Genre.DRAMA)));
        movies.add(new Movie("Whiplash", "The cost of greatness", Arrays.asList(Genre.DRAMA, Genre.DOCUMENTARY)));
        movies.add(new Movie("Gladiator", "A general becomes a gladiator", Arrays.asList(Genre.ACTION, Genre.DRAMA, Genre.HISTORY)));
        movies.add(new Movie("The Conjuring", "Based on true paranormal investigations", Arrays.asList(Genre.HORROR, Genre.THRILLER)));
        movies.add(new Movie("The Revenant", "Survival against all odds", Arrays.asList(Genre.ADVENTURE, Genre.DRAMA, Genre.WESTERN)));
        movies.add(new Movie("Coco", "A boy's journey through the Land of the Dead", Arrays.asList(Genre.ANIMATION, Genre.FAMILY, Genre.MUSICAL)));
        movies.add(new Movie("Mad Max: Fury Road", "A high-octane road to redemption", Arrays.asList(Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION)));*/

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
            if (this.getId().equals(movie.getId()))
            {
                return true;
            }
            else if (this.title.equals(movie.title))
            {
                return true;
            }
        }
        return false;

    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
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
