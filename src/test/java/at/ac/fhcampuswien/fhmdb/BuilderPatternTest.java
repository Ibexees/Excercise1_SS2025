package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.dataLayer.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.dataLayer.api.MovieAPIRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

class BuilderPatternTest {

    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";


    @Test
    void testBuilderPattern_AllParameters() {


        String url = new MovieAPIRequestBuilder(BASE_URL)
                .query("suchwort")
                .genre("ACTION")
                .releaseYear("2012")
                .ratingFrom("8.3")
                .build();

        assertNotNull(url, "URL should not be null");
        assertTrue(url.startsWith(BASE_URL), "URL should start with base URL");
        assertTrue(url.contains("query=suchwort"), "URL should contain search term 'suchwort'");
        assertTrue(url.contains("genre=ACTION"), "URL should contain genre filter 'ACTION'");
        assertTrue(url.contains("releaseYear=2012"), "URL should contain release year '2012'");
        assertTrue(url.contains("ratingFrom=8.3"), "URL should contain rating filter '8.3'");


        assertTrue(url.contains("?"), "URL should contain query string separator");
        long ampersandCount = url.chars().filter(ch -> ch == '&').count();
        assertEquals(3, ampersandCount, "Should have 3 ampersands for 4 parameters");
    }


    @Test
    void testBuilderPattern_MovieAPIFactoryMethods() {

        assertDoesNotThrow(() -> {
            MovieAPIRequestBuilder builder = MovieAPI.builder();
            assertNotNull(builder, "MovieAPI should provide builder factory method");

            String url = builder.query("test").build();
            assertTrue(url.contains("prog2.fh-campuswien.ac.at"), "Should use correct default base URL");
        });


        assertDoesNotThrow(() -> {
            String customUrl = "https://custom.api.com/movies";
            MovieAPIRequestBuilder builder = MovieAPI.builder(customUrl);
            assertNotNull(builder, "MovieAPI should provide builder factory method with custom URL");

            String url = builder.query("test").build();
            assertTrue(url.startsWith(customUrl), "Should use custom base URL");
        });
    }


    @Test
    void testBuilderPattern_FluentInterface() {


        MovieAPIRequestBuilder builder = new MovieAPIRequestBuilder(BASE_URL);


        MovieAPIRequestBuilder result = builder
                .query("test")
                .genre("ACTION")
                .releaseYear("2023")
                .ratingFrom("8.0");

        assertSame(builder, result, "All builder methods should return same instance for chaining");

        String url = result.build();
        assertNotNull(url, "Chained builder should produce valid URL");
        assertTrue(url.contains("query=test"), "Chained parameters should be included");
        assertTrue(url.contains("genre=ACTION"), "Chained parameters should be included");
        assertTrue(url.contains("releaseYear=2023"), "Chained parameters should be included");
        assertTrue(url.contains("ratingFrom=8.0"), "Chained parameters should be included");
    }
}

