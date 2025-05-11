package at.ac.fhcampuswien.fhmdb.dataLayer.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class MovieAPI {

    /**
     * This class contains methods to interact with the online movie API,
     * specifically to send HTTP GET requests and receive movie data as a JSON response.
     * <p>
     * Its purpose is to build a URL with query parameters, send the HTTP GET request to the movie API,
     * and return the JSON response as a String.
     *
     * @param params a key-value pair map used to add query parameters to the URL
     * @return the JSON response as a String
     * @throws IOException   if a network or I/O error occurs during the request
     * @throws MovieAPIException  if the API responds with a non-successful HTTP status code
     */

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
}
