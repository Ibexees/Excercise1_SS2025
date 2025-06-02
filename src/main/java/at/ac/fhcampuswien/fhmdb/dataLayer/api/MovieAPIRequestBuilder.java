package at.ac.fhcampuswien.fhmdb.dataLayer.api;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class MovieAPIRequestBuilder {
    private final StringBuilder urlBuilder;
    private boolean hasParams = false;


    public MovieAPIRequestBuilder(String baseUrl) {
        this.urlBuilder = new StringBuilder(baseUrl);
    }


    public MovieAPIRequestBuilder query(String searchQuery) {
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            addParameter("query", encode(searchQuery));
        }
        return this;
    }


    public MovieAPIRequestBuilder genre(String genre) {
        if (genre != null && !genre.trim().isEmpty()) {
            addParameter("genre", encode(genre));
        }
        return this;
    }


    public MovieAPIRequestBuilder releaseYear(String releaseYear) {
        if (releaseYear != null && !releaseYear.trim().isEmpty()) {
            addParameter("releaseYear", encode(releaseYear));
        }
        return this;
    }


    public MovieAPIRequestBuilder ratingFrom(String ratingFrom) {
        if (ratingFrom != null && !ratingFrom.trim().isEmpty()) {
            addParameter("ratingFrom", encode(ratingFrom));
        }
        return this;
    }


    private void addParameter(String key, String value) {
        if (!hasParams) {
            urlBuilder.append("?");
            hasParams = true;
        } else {
            urlBuilder.append("&");
        }
        urlBuilder.append(key).append("=").append(value);
    }


    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }


    public String build() {
        return urlBuilder.toString();
    }
}
