package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Deserializer {
    public static List<Movie> deserializeJson(String apiResponseJsonFile) {
        List<Movie> movies = new ArrayList<>();
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<Movie>>(){}.getType();
        movies = gson.fromJson(apiResponseJsonFile, listType);
        return movies;
    }
}
