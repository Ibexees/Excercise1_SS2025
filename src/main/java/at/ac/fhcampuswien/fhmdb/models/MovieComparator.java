package at.ac.fhcampuswien.fhmdb.models;

import java.util.Comparator;

public class MovieComparator implements Comparator<Movie>
{
    @Override
    public int compare(Movie o1, Movie o2)
    {
        return o1.getTitle().compareToIgnoreCase(o2.getTitle()); //Lexographischervergleich "welcher Buchstabe vorher im Alphabet"
    }
}
