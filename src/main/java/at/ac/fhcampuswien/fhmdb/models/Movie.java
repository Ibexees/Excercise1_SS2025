package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {
    private String title;
    private String description;
    private List<Genre> genres;
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

    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Your Name","Coming of Age romance",Arrays.asList(Genre.ROMANCE,Genre.DRAMA)));
        movies.add(new Movie ("Into the Spiderverse", "interdimensional spider people", Arrays.asList(Genre.ACTION,Genre.SCIENCE_FICTION)));
        movies.add(new Movie ("Shutter Island", "Believing doesn't equal the truth", Arrays.asList(Genre.THRILLER,Genre.MYSTERY)));
        movies.add(new Movie ("Southpaw", "Boxen", Arrays.asList(Genre.BIOGRAPHY,Genre.ACTION)));
        movies.add(new Movie ("Kung Fu Panda", "Wuxifingegriff", Arrays.asList(Genre.COMEDY,Genre.ACTION)));
        // TODO add some dummy data here

        return movies;
    }

    @Override
    public String toString()
    {
        return("Title:"+title+"desc:"+description+"Genre:"+genres);
    }
}
