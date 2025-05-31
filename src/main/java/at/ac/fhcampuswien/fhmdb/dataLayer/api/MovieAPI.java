package at.ac.fhcampuswien.fhmdb.dataLayer.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class MovieAPI {



    public static String getMovies(Map<String, String> params) throws MovieAPIException, IOException {
        String apiUrl = "https://prog2.fh-campuswien.ac.at/movies";
        String fullUrl = buildUrlWithParams(apiUrl, params);
        HttpURLConnection connection = createHttpConnection(fullUrl);
        return readResponse(connection);
    }

    private static String buildUrlWithParams(String apiUrl, Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        if (params != null && !params.isEmpty()) {
            urlBuilder.append("?");
            params.entrySet().stream()
                    .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8)
                            + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .forEach(param -> urlBuilder.append(param).append("&"));
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        return urlBuilder.toString();
    }

    private static HttpURLConnection createHttpConnection(String urlString) throws MovieAPIException, IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "http.agent");

        int responseCode = connection.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new MovieAPIException("HTTP Request to Movie API failed: " + responseCode);
        }

        return connection;
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        Scanner scanner = null;
        try {
            scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        } catch (IOException e) {
            throw new IOException("Error beim auslesen des responses: " + e.getMessage(), e);
        }

        finally {
            if (scanner != null) {
                scanner.close();
            }
            connection.disconnect();
        }
        return response.toString();
    }



    /**
     * Fetches movies from the API using a pre-built URL.
     * NEW METHOD for Builder Pattern support.
     */
    public static String getMovies(String url) throws MovieAPIException, IOException {
        HttpURLConnection connection = createHttpConnection(url);
        return readResponse(connection);
    }

    /**
     * Creates a new MovieAPIRequestBuilder for URL construction.
     * NEW METHOD for Builder Pattern support.
     */
    public static MovieAPIRequestBuilder builder() {
        return new MovieAPIRequestBuilder("https://prog2.fh-campuswien.ac.at/movies");
    }

    /**
     * Creates a new MovieAPIRequestBuilder with a custom base URL.
     * NEW METHOD for Builder Pattern support.
     */
    public static MovieAPIRequestBuilder builder(String baseUrl) {
        return new MovieAPIRequestBuilder(baseUrl);
    }
}

