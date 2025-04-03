package at.ac.fhcampuswien.fhmdb.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class MovieAPI {

    /**
     * This class contains methods to interact with the online movie API, specifically to send HTTP GET requests and receive movie data as a JSON response
     * Its Purpose is to build a URL with query parameters, send the HTTP GET request to the movie API, and return the JSON response as a String
     * @param params (key value pair map, used to add the query parameters to the URL)
     * @return String
     * @throws IOException (might throw one if the HTTP request fails or occurrence of connectivity problems
     */
    public static String getMovies(Map<String, String> params) throws IOException {
        StringBuilder response = new StringBuilder();
        String apiUrl = "https://prog2.fh-campuswien.ac.at/movies";
        StringBuilder urlBuilder = new StringBuilder(apiUrl);

        //Biuld URL with parameters
        if (params != null && !params.isEmpty()) {
            urlBuilder.append("?");
            /*
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                        .append("&");

            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
*/
            //mit Java Streams
            params.entrySet().stream()
                    .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .forEach(param -> urlBuilder.append(param).append("&"));
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
            //System.out.println(urlBuilder);
        }

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        //connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "http.agent");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            //System.out.println("HTTP Request succeeded: " + responseCode);
        }
        else {
            throw new IOException("HTTP Request failed with Errorcode: " + responseCode);
        }

        Scanner scanner = new Scanner(connection.getInputStream());
        while (scanner.hasNextLine()) {
            response.append(scanner.nextLine());
        }
        scanner.close();
        connection.disconnect();

        return response.toString();
    }
}
