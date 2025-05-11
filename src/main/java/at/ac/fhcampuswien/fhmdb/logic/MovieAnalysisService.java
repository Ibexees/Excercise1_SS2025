package at.ac.fhcampuswien.fhmdb.logic;

import at.ac.fhcampuswien.fhmdb.logic.models.Movie;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovieAnalysisService {

    /**
     * This method identifies the actor(s) who appear in the most movies from a given list, and returns their name(s) as a comma-separated String
     * Its purpose is to find the actor(s) who are most frequently featured across the provided list of Movie objects and return their name(s) as a comma-separated String
     * Key concept used is "Stream", which processes list cleanly instead of loops
     * @param movies (list of movie objects to analyse)
     * @return String (names of the actor(s) with the most appearance
     */
    public String getMostPopularActor(List<Movie> movies) {
        Map<String, Long> actorMap = new HashMap<>();
        Long maxFeatures = (long) 0;
        String mostCastActor = "";
        Stream<Movie> movieStream = movies.stream();

        actorMap = movieStream
                .flatMap(movie -> Arrays.stream(movie.getMainCast())) //Actors aus Movie und Maincast array extrahieren
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting())); //nach actor gruppieren und vorkommen zählen

        maxFeatures = actorMap.values().stream()
                .max((cntActor1, cntActor2) -> Long.compare(cntActor1, cntActor2)) //häufigstes Vorkommen feststellen
                .orElse((long) (0));

        Long finalMaxFeatures = maxFeatures;
        mostCastActor = actorMap.entrySet().stream()
                .filter(features -> features.getValue() == finalMaxFeatures) //filtern auf nur maximales Vorkommen
                .map(entry -> entry.getKey()) // passenden Schlüssel zum höchsten Vorkommen finden
                .collect(Collectors.toList())
                .stream().collect(Collectors.joining(", ")); //Liste komma separiert weil Laut angabe String returned werden muss

        return mostCastActor;
    }

    /**
     * This method finds the length of the longest movie title from a given list of Movie objects
     * Key concept used is "Stream", for processing the list cleanly
     * map() is used to extract movie titles
     * mapToInt() is used to convert strings to their lengths
     * max() is used to find the longest length
     * orElse(0) is used to handle the case where the list is empty
     * @param movies (list of movie objects to examine)
     * @return int (length of longest title)
     */
    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream() // stream erstellt, wandelt liste von filmen in datenstrom um
                .map(Movie::getTitle) //holt aus movie object den titel raus, strom hat nur noch mehr die strings
                .mapToInt(String::length) // jeder titel wird in seine länge umgewandelt, stream enthält länge
                .max() // sucht größte länge raus
                .orElse(0); //falls liste leer = 0
    }

    /**
     * This method counts how many movies in the provided list were directed by a specific director
     * Key concept used is "Stream", for processing the list cleanly
     * filter() is used to only keep the movies that match the director
     * Arrays.asList() converts array of directors to a list for searching
     * contains() checks if the director is listed
     * count() returns the number of matching elements
     * @param movies (list of movie objects to examine)
     * @param director (the director's name to search for)
     * @return long (number of movies directed by the given name)
     */
    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors() != null &&
                        Arrays.asList(movie.getDirectors()).contains(director))
                .count();
    }

    /**
     * This method is used to return a list of movies that were released between two given years
     * Key concept used is "Stream", for processing the list cleanly
     * filter() to select only movies within the year range
     * toList() converts the result back to a list
     * @param movies (full list of movie objects to filter)
     * @param startYear (the earliest release year to include)
     * @param endYear (the latest release year to include)
     * @return List (filtered list of movies released in the specific range)
     */
    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        Stream<Movie> movieStream = movies.stream();

        List<Movie> filteredMovies = movieStream
                .filter(m -> m.getReleaseYear() >= startYear && m.getReleaseYear() <= endYear)
                .toList();

        return filteredMovies;
    }
}
