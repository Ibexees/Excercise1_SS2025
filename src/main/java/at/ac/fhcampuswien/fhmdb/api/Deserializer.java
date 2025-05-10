package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * This class is used to convert JSON data, in form of a String, into a list of Movie Objects
 * Its purpose is to take the JSON-formatted String (in our case the response from a web API) and turn it into a list of movies
 * GSON is a Google library used to convert Java objects to JSON and vice versa
 * TypeToken is used to capture generic info type at runtime
 * It then returns the final list of deserialized movies
 */
public class Deserializer {
    public static List<Movie> deserializeJsonToMovieModel(String apiResponseJsonFile) {
        List<Movie> movies = new ArrayList<>();
        Gson gson = new Gson();

        try {
            Type listType = new TypeToken<ArrayList<Movie>>() {
            }.getType();
            movies = gson.fromJson(apiResponseJsonFile, listType);
        }
        catch (JsonSyntaxException e) {
            throw new JsonSyntaxException("Failed to deserialize JSON response into movie model", e);
        }
        return movies;
    }
}
