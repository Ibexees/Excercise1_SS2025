package at.ac.fhcampuswien.fhmdb.models;

import java.util.Comparator;

public class MovieComparator implements Comparator<Movie>
{
    @Override
    public int compare(Movie o1, Movie o2)
    {
        return Character.compare(o1.getTitle().charAt(0), o2.getTitle().charAt(0));
    }
}
