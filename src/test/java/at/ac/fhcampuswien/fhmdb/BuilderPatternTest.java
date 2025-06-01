package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.dataLayer.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.dataLayer.api.MovieAPIRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

class BuilderPatternTest {

    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";

    @BeforeEach
    void setUp() {

    }


    @Test
    void testBuilderPattern_Example1FromAssignment() {


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
    void testBuilderPattern_Example2FromAssignment() {


        String id = "12345";
        String baseWithId = BASE_URL + "/" + id;


        String url = new MovieAPIRequestBuilder(baseWithId).build();


        assertNotNull(url, "URL should not be null");
        assertEquals(baseWithId, url, "URL should exactly match base + id format");
        assertTrue(url.contains(id), "URL should contain the movie ID");
        assertFalse(url.contains("?"), "URL should not contain query parameters when no filters added");
    }



    @Test
    void testBuilderPattern_FlexibleFilterCriteria() {

        String searchOnly = new MovieAPIRequestBuilder(BASE_URL)
                .query("matrix")
                .build();
        assertTrue(searchOnly.contains("query=matrix"), "Should handle single search parameter");
        assertEquals(BASE_URL + "?query=matrix", searchOnly, "Single parameter should be formatted correctly");


        String genreOnly = new MovieAPIRequestBuilder(BASE_URL)
                .genre("SCIFI")
                .build();
        assertTrue(genreOnly.contains("genre=SCIFI"), "Should handle single genre parameter");


        String genreAndRating = new MovieAPIRequestBuilder(BASE_URL)
                .genre("ACTION")
                .ratingFrom("7.0")
                .build();
        assertTrue(genreAndRating.contains("genre=ACTION"), "Should contain genre in combination");
        assertTrue(genreAndRating.contains("ratingFrom=7.0"), "Should contain rating in combination");
        assertTrue(genreAndRating.contains("&"), "Should separate parameters with ampersand");


        String allParams = new MovieAPIRequestBuilder(BASE_URL)
                .ratingFrom("8.0")
                .query("avengers")
                .releaseYear("2019")
                .genre("ACTION")
                .build();
        assertTrue(allParams.contains("query=avengers"), "Should contain all parameters regardless of order");
        assertTrue(allParams.contains("genre=ACTION"), "Should contain all parameters regardless of order");
        assertTrue(allParams.contains("releaseYear=2019"), "Should contain all parameters regardless of order");
        assertTrue(allParams.contains("ratingFrom=8.0"), "Should contain all parameters regardless of order");
    }

    @Test
    void testBuilderPattern_IgnoreEmptyParameters() {

        String url = new MovieAPIRequestBuilder(BASE_URL)
                .query("test")
                .genre(null)  // null parameter
                .releaseYear("")  // empty parameter
                .ratingFrom("7.5")
                .build();

        assertTrue(url.contains("query=test"), "Should include valid parameters");
        assertTrue(url.contains("ratingFrom=7.5"), "Should include valid parameters");
        assertFalse(url.contains("genre="), "Should ignore null parameters");
        assertFalse(url.contains("releaseYear="), "Should ignore empty parameters");


        long ampersandCount = url.chars().filter(ch -> ch == '&').count();
        assertEquals(1, ampersandCount, "Should have 1 ampersand for 2 valid parameters");
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
    void testBuilderPattern_IntegrationWithMovieAPIGetMovies() {


        // Arrange
        String url = new MovieAPIRequestBuilder(BASE_URL)
                .query("integration")
                .genre("DRAMA")
                .build();


        assertDoesNotThrow(() -> {

            assertNotNull(url, "URL should not be null for MovieAPI");
            assertTrue(url.startsWith("http"), "URL should be valid HTTP URL");
            assertTrue(url.contains("?"), "URL should contain query string");


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

    @Test
    void testBuilderPattern_BuildMethodBehavior() {
        // Test dass build() Methode korrekt funktioniert


        MovieAPIRequestBuilder builder = new MovieAPIRequestBuilder(BASE_URL)
                .query("test")
                .genre("ACTION");


        String url1 = builder.build();
        String url2 = builder.build();


        assertNotNull(url1, "First build() should return valid URL");
        assertNotNull(url2, "Second build() should return valid URL");
        assertEquals(url1, url2, "Multiple build() calls should return identical results");
    }

    @Test
    void testBuilderPattern_BuilderReusability() {

        MovieAPIRequestBuilder builder = new MovieAPIRequestBuilder(BASE_URL);


        String url1 = builder.query("first").build();
        String url2 = builder.genre("ACTION").build();
        String url3 = builder.releaseYear("2023").build();

        // Assert
        assertTrue(url1.contains("query=first"), "First build should contain first query");
        assertFalse(url1.contains("genre="), "First build should not contain genre");

        assertTrue(url2.contains("query=first"), "Second build should retain previous parameters");
        assertTrue(url2.contains("genre=ACTION"), "Second build should add new parameters");

        assertTrue(url3.contains("query=first"), "Third build should retain all parameters");
        assertTrue(url3.contains("genre=ACTION"), "Third build should retain all parameters");
        assertTrue(url3.contains("releaseYear=2023"), "Third build should add latest parameters");
    }



    @Test
    void testBuilderPattern_URLEncodingSpaces() {


        String url = new MovieAPIRequestBuilder(BASE_URL)
                .query("action movie")
                .build();


        assertTrue(url.contains("query="), "Should contain query parameter");
        // Spaces should be encoded as + or %20
        assertTrue(url.contains("action+movie") || url.contains("action%20movie"),
                "Should encode spaces in query parameter");
    }

    @Test
    void testBuilderPattern_URLEncodingSpecialCharacters() {


        String url = new MovieAPIRequestBuilder(BASE_URL)
                .query("action & adventure")
                .build();


        assertTrue(url.contains("query="), "Should contain query parameter");
        assertTrue(url.contains("%26"), "Should encode ampersand in parameter value");
    }

    @Test
    void testBuilderPattern_GermanCharacters() {



        String url = new MovieAPIRequestBuilder(BASE_URL)
                .query("Müller")
                .build();


        assertNotNull(url, "Should handle German characters");
        assertTrue(url.contains("query="), "Should contain query parameter");

        assertFalse(url.contains("ü"), "Umlaut should be URL encoded");
    }



    @Test
    void testBuilderPattern_EmptyBuilder() {



        String url = new MovieAPIRequestBuilder(BASE_URL).build();


        assertEquals(BASE_URL, url, "Empty builder should return base URL without query string");
    }

    @Test
    void testBuilderPattern_AllParametersNull() {

        String url = new MovieAPIRequestBuilder(BASE_URL)
                .query(null)
                .genre(null)
                .releaseYear(null)
                .ratingFrom(null)
                .build();


        assertEquals(BASE_URL, url, "Should return base URL when all parameters are null");
    }

    @Test
    void testBuilderPattern_AllParametersEmpty() {

        String url = new MovieAPIRequestBuilder(BASE_URL)
                .query("")
                .genre("")
                .releaseYear("")
                .ratingFrom("")
                .build();


        assertEquals(BASE_URL, url, "Should return base URL when all parameters are empty");
    }

    @Test
    void testBuilderPattern_WhitespaceParameters() {

        String url = new MovieAPIRequestBuilder(BASE_URL)
                .query("   ")
                .genre("\t")
                .releaseYear("  ")
                .ratingFrom("\n")
                .build();

        // Assert
        assertEquals(BASE_URL, url, "Should ignore whitespace-only parameters");
    }


    @RepeatedTest(3)
    void testBuilderPattern_ConsistentBehavior() {

        String url = new MovieAPIRequestBuilder(BASE_URL)
                .query("matrix")
                .genre("SCIFI")
                .releaseYear("1999")
                .ratingFrom("8.5")
                .build();


        assertTrue(url.contains("query=matrix"), "Should consistently include all parameters");
        assertTrue(url.contains("genre=SCIFI"), "Should consistently include all parameters");
        assertTrue(url.contains("releaseYear=1999"), "Should consistently include all parameters");
        assertTrue(url.contains("ratingFrom=8.5"), "Should consistently include all parameters");

        // URL should be well-formed
        assertTrue(url.startsWith(BASE_URL), "Should start with base URL");
        assertTrue(url.contains("?"), "Should contain query string");
    }

    @Test
    void testBuilderPattern_LongParameterValues() {

        String longQuery = "a".repeat(100);


        String url = new MovieAPIRequestBuilder(BASE_URL)
                .query(longQuery)
                .genre("ACTION")
                .build();


        assertNotNull(url, "Should handle long parameters");
        assertTrue(url.contains("query="), "Should contain query parameter");
        assertTrue(url.contains("genre=ACTION"), "Should contain other parameters");
        assertTrue(url.length() > BASE_URL.length() + 100, "Should include long parameter");
    }

    @Test
    void testBuilderPattern_SpecialURLCharacters() {



        String url = new MovieAPIRequestBuilder(BASE_URL)
                .query("test?movie=1&action=true")
                .build();


        assertNotNull(url, "Should handle URL special characters");
        assertTrue(url.contains("query="), "Should contain query parameter");

        assertFalse(url.substring(url.indexOf("query=")).contains("?"),
                "Query characters should be encoded");
    }
}
