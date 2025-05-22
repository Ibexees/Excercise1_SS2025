package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.dataLayer.database.WatchlistRepositoryEvent;

public interface Observable
{

    void addSubscriber(Observer observer);
    void removeSubscriber(Observer observer);
    void onWatchlistAdd();
    void onWatchlistAddExisting();
    void onWatchlistRemove();
    void notifyObserver(WatchlistRepositoryEvent event);
}
