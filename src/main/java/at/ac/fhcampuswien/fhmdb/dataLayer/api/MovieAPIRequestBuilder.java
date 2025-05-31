package at.ac.fhcampuswien.fhmdb.dataLayer.api;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Builder Pattern implementation for creating MovieAPI request URLs.
 * Allows flexible construction of API URLs with various filter criteria.
 */
public class MovieAPIRequestBuilder {
    private final StringBuilder urlBuilder;
    private boolean hasParams = false;

    /**
     * Constructor that takes the base URL.
     * @param baseUrl The base URL for the API
     */
    public MovieAPIRequestBuilder(String baseUrl) {
        this.urlBuilder = new StringBuilder(baseUrl);
    }

    /**
     * Adds a search query parameter to the URL.
     * @param searchQuery The search term
     * @return This builder instance for method chaining
     */
    public MovieAPIRequestBuilder query(String searchQuery) {
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            addParameter("query", encode(searchQuery));
        }
        return this;
    }

    /**
     * Adds a genre filter parameter to the URL.
     * @param genre The genre to filter by
     * @return This builder instance for method chaining
     */
    public MovieAPIRequestBuilder genre(String genre) {
        if (genre != null && !genre.trim().isEmpty()) {
            addParameter("genre", encode(genre));
        }
        return this;
    }

    /**
     * Adds a release year filter parameter to the URL.
     * @param releaseYear The release year to filter by
     * @return This builder instance for method chaining
     */
    public MovieAPIRequestBuilder releaseYear(String releaseYear) {
        if (releaseYear != null && !releaseYear.trim().isEmpty()) {
            addParameter("releaseYear", encode(releaseYear));
        }
        return this;
    }

    /**
     * Adds a minimum rating filter parameter to the URL.
     * @param ratingFrom The minimum rating
     * @return This builder instance for method chaining
     */
    public MovieAPIRequestBuilder ratingFrom(String ratingFrom) {
        if (ratingFrom != null && !ratingFrom.trim().isEmpty()) {
            addParameter("ratingFrom", encode(ratingFrom));
        }
        return this;
    }

    /**
     * Helper method to add parameters to the URL.
     * @param key The parameter key
     * @param value The parameter value
     */
    private void addParameter(String key, String value) {
        if (!hasParams) {
            urlBuilder.append("?");
            hasParams = true;
        } else {
            urlBuilder.append("&");
        }
        urlBuilder.append(key).append("=").append(value);
    }

    /**
     * Helper method to URL-encode parameter values.
     * @param value The value to encode
     * @return The URL-encoded value
     */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /**
     * Builds and returns the final URL string.
     * @return The constructed URL as a string
     */
    public String build() {
        return urlBuilder.toString();
    }
}
