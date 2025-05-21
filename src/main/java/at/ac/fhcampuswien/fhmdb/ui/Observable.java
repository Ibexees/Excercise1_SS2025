package at.ac.fhcampuswien.fhmdb.ui;

import java.util.ArrayList;
import java.util.List;

public interface Observable
{

    void addSubscriber(Observer observer);
    void removeSubscriber(Observer observer);
    void notifyAddedToWatchlist();
}
