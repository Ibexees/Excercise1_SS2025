package at.ac.fhcampuswien.fhmdb.logic;

import at.ac.fhcampuswien.fhmdb.logic.models.Movie;

import java.util.Comparator;

public class MovieComparator implements Comparator<Movie>
{
    @Override
    public int compare(Movie o1, Movie o2)
    {
        return o1.getTitle().compareToIgnoreCase(o2.getTitle()); //Lexographischervergleich "welcher Buchstabe vorher im Alphabet"
    }
}
