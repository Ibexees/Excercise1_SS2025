package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {


    @Test
    public void Movie_should_have_Attributes()
    {
        //Erwartete Attribute
        List<String> expectedFields = Arrays.asList("title","description","genres");

        //Tatsächliche Attribute aus Klasse
        Field[] fields = Movie.class.getDeclaredFields();
        List<String> actualFields = new ArrayList<>();
        for(Field field : fields)
        {
            actualFields.add(field.getName());
        }
        assertEquals(actualFields,expectedFields, "Unterschiede gefunden! Erwartet: " + expectedFields + ", aber war: " + actualFields);


    }
}