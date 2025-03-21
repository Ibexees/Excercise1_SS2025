package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.api.Deserializer;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.*;

public class Movie {
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
        //Test Parameters - Funktioniert mit dem Beispiel



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

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public void setLengthInMinutes(int lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }

    public String[] getMainCast() {
        return mainCast;
    }

    public void setMainCast(String[] mainCast) {
        this.mainCast = mainCast;
    }

    public String[] getDirectors() {
        return directors;
    }

    public void setDirectors(String[] directors) {
        this.directors = directors;
    }

    public String[] getWriters() {
        return writers;
    }

    public void setWriters(String[] writers) {
        this.writers = writers;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
